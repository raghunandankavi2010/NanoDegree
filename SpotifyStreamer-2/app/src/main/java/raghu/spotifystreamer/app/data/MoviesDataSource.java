package raghu.spotifystreamer.app.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.ItemKeyedDataSource;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import raghu.spotifystreamer.app.model.Movies;
import raghu.spotifystreamer.app.model.MoviesList;
import raghu.spotifystreamer.app.network.Api;
import raghu.spotifystreamer.app.network.NetworkState;
import raghu.spotifystreamer.app.network.Status;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesDataSource  extends PageKeyedDataSource<Integer,Movies>{

    public static final String TAG = MoviesDataSource.class.getSimpleName();
    private Api api;
    private MutableLiveData<NetworkState> networkState;
    private MutableLiveData<NetworkState> initialLoading;
    private Executor retryExecutor;

    public static final String MOVIE_DB_API_URL = "http://api.themoviedb.org/3/";


    public MoviesDataSource(Executor retryExecutor) {
        api = createApi();
        networkState = new MutableLiveData<NetworkState>();
        initialLoading = new MutableLiveData<NetworkState>();
        this.retryExecutor = retryExecutor;
    }
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final  LoadInitialCallback<Integer, Movies> callback) {
        Log.i(TAG, "Loading Rang " + 1 + " Count " + params.requestedLoadSize);
        final List<Movies> moviesList = new ArrayList();
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);
        api.getMoviesList("popularity.desc",1).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if (response.isSuccessful() && response.code() == 200) {

                    MoviesList movies = response.body();
                    moviesList.addAll(movies.getResults());

                    callback.onResult(moviesList,movies.getPageCount(), movies.getPageCount()+1);

                    initialLoading.postValue(NetworkState.LOADED);
                    networkState.postValue(NetworkState.LOADED);

                } else {
                    Log.e("API CALL", response.message());
                    initialLoading.postValue(new NetworkState(Status.FAILED, response.message()));
                    networkState.postValue(new NetworkState(Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {

                String errorMessage;
                errorMessage = t.getMessage();
                if (t == null) {
                    errorMessage = "unknown error";
                }
                networkState.postValue(new NetworkState(Status.FAILED, errorMessage));
            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movies> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movies> callback) {

        Log.i(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);
        final List<Movies> moviesList = new ArrayList();
        LoadParams<Integer> afterParams = params;

        networkState.postValue(NetworkState.LOADING);

        api.getMoviesList("popularity.desc",params.key).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if (response.isSuccessful() && response.code() == 200) {

                    MoviesList movies = response.body();
                    moviesList.addAll(movies.getResults());

                    callback.onResult(moviesList,movies.getPageCount()+1);
                    initialLoading.postValue(NetworkState.LOADED);
                    networkState.postValue(NetworkState.LOADED);
                } else {
                    Log.e("API CALL", response.message());
                    initialLoading.postValue(new NetworkState(Status.FAILED, response.message()));
                    networkState.postValue(new NetworkState(Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {

                String errorMessage;
                errorMessage = t.getMessage();
                if (t == null) {
                    errorMessage = "unknown error";
                }
                networkState.postValue(new NetworkState(Status.FAILED, errorMessage));
            }
        });

    }


    public Api createApi(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_DB_API_URL)
                .client(getOkhttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit.create(Api.class);
    }

    private OkHttpClient getOkhttpClient() {

            return new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {

                            Request originalRequest = chain.request(); //Current Request

                            okhttp3.Response response = chain.proceed(originalRequest); //Get response of the request

                            //I am logging the response body in debug mode. When I do this I consume the response (OKHttp only lets you do this once) so i have re-build a new one using the cached body
                            String bodyString = response.body().string();
                            Log.i("...", bodyString);

                            Log.i("NetworkModule", String.format("Sending request %s with headers %s ", originalRequest.url(), originalRequest.headers()));
                            Log.i("", (String.format("Got response HTTP %s %s \n\n with body %s \n\n with headers %s ", response.code(), response.message(), bodyString, response.headers())));
                            response = response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();

                            return response;
                        }
                    })
                    .readTimeout(5,TimeUnit.SECONDS)
                    .connectTimeout(5,TimeUnit.SECONDS)
                    .build();


        }


    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

}
