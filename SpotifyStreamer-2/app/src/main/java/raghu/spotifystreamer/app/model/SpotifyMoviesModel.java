package raghu.spotifystreamer.app.model;

import android.util.Log;

import raghu.spotifystreamer.app.network.ReviewsApi;
import raghu.spotifystreamer.app.network.SpotifyMoviesApi;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import raghu.spotifystreamer.app.network.SpotifyMoviesApi;
import raghu.spotifystreamer.app.network.VideosApi;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import rx.Observable;
import rx.Subscriber;

@Singleton
public class SpotifyMoviesModel {

    private final SpotifyMoviesApi mApi;
    private final ReviewsApi mRApi;
    private final VideosApi mVApi;
    private static int total_pages;

    private Observable<ArrayList<Movies>> mList;
    private Observable<ArrayList<Reviews>> mReviews;
    private Observable<ArrayList<Videos>> mVidoes;


    @Inject
    public SpotifyMoviesModel(SpotifyMoviesApi api,ReviewsApi mRapi,VideosApi mVApi) {
        this.mApi = api;
        this.mRApi = mRapi;
       this.mVApi = mVApi;
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
                            /*Log.i("SpotifyMoviesModel", "................");
                           for (Movies movie : list) {
                                Log.i("SpotifyMoviesModel", "" + movie.getTitle());
                            }*/
                            if(list.size()>0)
                            subscriber.onNext(list);
                            else
                            subscriber.onError(new Throwable("Empty List"));

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

    public Observable<ArrayList<Reviews>> getReviewsRequest() {
        return mReviews;
    }

    public Observable<ArrayList<Reviews>> getReviewsList(final int id, final int pagecount) {


        mReviews = Observable.create(new Observable.OnSubscribe<ArrayList<Reviews>>() {
            @Override
            public void call(final Subscriber<? super ArrayList<Reviews>> subscriber) {


                Call<ReviewsList> response = mRApi.reviews(id, pagecount);
                response.enqueue(new Callback<ReviewsList>() {
                    @Override
                    public void onResponse(Response<ReviewsList> resp) {
                        // Get result Repo from response.body()

                        try {
                            //total_pages = resp.body().getTotal_pages();
                            ArrayList<Reviews> reviewslist = resp.body().getReviewsResults();
                            if(reviewslist.size()>0) {
                                subscriber.onNext(reviewslist);
                            }else {
                                subscriber.onError(new Throwable("Empty List"));
                            }

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

        return mReviews;
    }

    public Observable<ArrayList<Videos>> getVideoRequest() {
        return mVidoes;
    }

    public Observable<ArrayList<Videos>> getVideoList(final int movieId) {


        mVidoes = Observable.create(new Observable.OnSubscribe<ArrayList<Videos>>() {
            @Override
            public void call(final Subscriber<? super ArrayList<Videos>> subscriber) {


                Call<VideoList> response = mVApi.videos(movieId);
                response.enqueue(new Callback<VideoList>() {
                    @Override
                    public void onResponse(Response<VideoList> resp) {
                        // Get result Repo from response.body()

                        try {

                            ArrayList<Videos> videolist = resp.body().getResults();
                            if(videolist.size()>0) {
                             subscriber.onNext(videolist);
                            }
                            else
                            {
                                subscriber.onError(new Throwable("Empty List"));
                            }

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

        return mVidoes;
    }

    public int getTotal_pages() {
        return total_pages;
    }
}