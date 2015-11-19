package raghu.spotifystreamer.app.model;

import android.util.Log;

import raghu.spotifystreamer.app.network.ReviewsApi;
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
    private final ReviewsApi mRApi;
    private static int total_pages;

    private Observable<ArrayList<Movies>> mList;
    private Observable<ArrayList<Reviews>> mReviews;

    @Inject
    public SpotifyMoviesModel(SpotifyMoviesApi api,ReviewsApi mRapi) {
        mApi = api;
        mRApi = mRapi;
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

                            Log.i("SpotifyMoviesModel", "................");
                           for (Movies movie : list) {
                                Log.i("SpotifyMoviesModel", "" + movie.getTitle());
                            }
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


                            subscriber.onNext(reviewslist);

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

   /* public Observable<ArrayList<Reviews>> getVideoRequest() {
        return mReviews;
    }

    public Observable<ArrayList<Reviews>> getVideoList(final int id, final int pagecount) {


        mReviews = Observable.create(new Observable.OnSubscribe<ArrayList<Reviews>>() {
            @Override
            public void call(final Subscriber<? super ArrayList<Reviews>> subscriber) {


                Call<ReviewsList> response = mApi.reviews(id, pagecount);
                response.enqueue(new Callback<ReviewsList>() {
                    @Override
                    public void onResponse(Response<ReviewsList> resp) {
                        // Get result Repo from response.body()

                        try {
                            total_pages = resp.body().getTotal_pages();
                            ArrayList<Reviews> reviewslist = resp.body().getReviewsResults();

                           *//* for (Movies movie : list) {
                                Log.i("SpotifyMoviesModel", "" + movie.getTitle());
                            }*//*
                            subscriber.onNext(reviewslist);

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
    }*/

    public int getTotal_pages() {
        return total_pages;
    }
}