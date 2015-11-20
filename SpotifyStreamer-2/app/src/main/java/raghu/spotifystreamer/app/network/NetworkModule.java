package raghu.spotifystreamer.app.network;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


import static java.lang.String.format;

@Module
public class NetworkModule {
    public static final String MOVIE_DB_API_URL = "http://api.themoviedb.org/3/";


 /*   @Provides
    GitHubApi provideGitHubApi() {
        final String GITHUB_ENDPOINT = "https://api.github.com/";
        OkHttpClient okHttpClient = new OkHttpClient();

        final String githubToken = RxApp.get().getResources().getString(R.string
                .github_oauth_token);

        if (!isNullOrEmpty(githubToken)) {

            okHttpClient.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request authorizedRequest = originalRequest.newBuilder()
                            .header("Authorization", format("token %s", githubToken))
                            .build();

                    return chain.proceed(authorizedRequest);
                }
            });

            okHttpClient.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Timber.d("Sending request: " + request.url() + " with headers: " + request.headers());
                    return chain.proceed(request);
                }
            });
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GITHUB_ENDPOINT)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(GitHubApi.class);
    }*/

    @Provides
    SpotifyMoviesApi getApi() {

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(5, TimeUnit.SECONDS);

        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request originalRequest = chain.request(); //Current Request

                Response response = chain.proceed(originalRequest); //Get response of the request


                //I am logging the response body in debug mode. When I do this I consume the response (OKHttp only lets you do this once) so i have re-build a new one using the cached body
                String bodyString = response.body().string();
                Log.i("...", bodyString);

                Log.i("NetworkModule", String.format("Sending request %s with headers %s ", originalRequest.url(), originalRequest.headers()));
                Log.i("", (String.format("Got response HTTP %s %s \n\n with body %s \n\n with headers %s ", response.code(), response.message(), bodyString, response.headers())));
                response = response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();


                return response;
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_DB_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit.create(SpotifyMoviesApi.class);

    }

    @Provides
    ReviewsApi getReviewsApi() {

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(5, TimeUnit.SECONDS);

        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request originalRequest = chain.request(); //Current Request

                Response response = chain.proceed(originalRequest); //Get response of the request


                //I am logging the response body in debug mode. When I do this I consume the response (OKHttp only lets you do this once) so i have re-build a new one using the cached body
                String bodyString = response.body().string();
                Log.i("...", bodyString);

                Log.i("NetworkModule", String.format("Sending request %s with headers %s ", originalRequest.url(), originalRequest.headers()));
                Log.i("", (String.format("Got response HTTP %s %s \n\n with body %s \n\n with headers %s ", response.code(), response.message(), bodyString, response.headers())));
                response = response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();


                return response;
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_DB_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit.create(ReviewsApi.class);

    }

    @Provides
    VideosApi getVideosApi() {

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(5, TimeUnit.SECONDS);

        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request originalRequest = chain.request(); //Current Request

                Response response = chain.proceed(originalRequest); //Get response of the request


                //I am logging the response body in debug mode. When I do this I consume the response (OKHttp only lets you do this once) so i have re-build a new one using the cached body
                String bodyString = response.body().string();
                Log.i("...", bodyString);

                Log.i("NetworkModule", String.format("Sending request %s with headers %s ", originalRequest.url(), originalRequest.headers()));
                Log.i("", (String.format("Got response HTTP %s %s \n\n with body %s \n\n with headers %s ", response.code(), response.message(), bodyString, response.headers())));
                response = response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();


                return response;
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_DB_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit.create(VideosApi.class);

    }

}
