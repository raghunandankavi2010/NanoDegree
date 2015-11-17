package raghu.spotifystreamer.app.model;

import raghu.spotifystreamer.app.network.SpotifyMoviesApi;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import raghu.spotifystreamer.app.network.SpotifyMoviesApi;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import rx.Observable;
import rx.Subscriber;

@Singleton
public class SpotifyMoviesModel {

    private final SpotifyMoviesApi mApi;
    private int total_pages;


    private Observable<ArrayList<Movies>> mList;

    @Inject
    public SpotifyMoviesModel(SpotifyMoviesApi api) {
        mApi = api;
    }


    public Observable<ArrayList<Movies>> getRequest() {
        return mList;
    }

    public Observable<ArrayList<Movies>> getMoviesList(final String sort_by, final int pagecount) {


       mList = Observable.create(new Observable.OnSubscribe<ArrayList<Movies>>() {
            @Override
            public void call(final Subscriber<? super ArrayList<Movies>> subscriber) {


                Call<MoviesList> response = mApi.getMoviesList(sort_by, pagecount);
                response.enqueue(new Callback<MoviesList>() {
                    @Override
                    public void onResponse(Response<MoviesList> resp) {
                        // Get result Repo from response.body()

                        try {
                            total_pages = resp.body().getTotal_pages();
                            ArrayList<Movies> list = resp.body().getResults();

                           /* for (Movies movie : list) {
                                Log.i("SpotifyMoviesModel", "" + movie.getTitle());
                            }*/
                            subscriber.onNext(list);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        subscriber.onError(t);
                    }
                });
            }
        });

        return mList;
    }

    public int getTotal_pages() {
        return total_pages;
    }
}