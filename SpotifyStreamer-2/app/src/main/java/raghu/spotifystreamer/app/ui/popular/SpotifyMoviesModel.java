package raghu.spotifystreamer.app.ui.popular;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import raghu.spotifystreamer.app.model.Movies;
import raghu.spotifystreamer.app.model.MoviesList;
import raghu.spotifystreamer.app.model.Reviews;
import raghu.spotifystreamer.app.model.ReviewsList;
import raghu.spotifystreamer.app.model.VideoList;
import raghu.spotifystreamer.app.model.Videos;
import raghu.spotifystreamer.app.network.Api;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class SpotifyMoviesModel {


    private final Api api;
    private static int total_pages;
    private Observable<ArrayList<Movies>> mList;
    private Observable<ArrayList<Reviews>> mReviews;
    private Observable<ArrayList<Videos>> mVidoes;

    private ObservableEmitter<ArrayList<Movies>> mEmitter;
    private ObservableEmitter<ArrayList<Reviews>> mR_Emitter;
    private ObservableEmitter<ArrayList<Videos>> mV_Emitter;

    @Inject
    public SpotifyMoviesModel(Api api) {
     this .api = api;

    }


    public Observable<ArrayList<Movies>> getRequest() {
        return mList;
    }

    public Observable<ArrayList<Movies>> getMoviesList(final String sort_by, final int pagecount) {


     mList = Observable.create(new ObservableOnSubscribe<ArrayList<Movies>>() {
           @Override
           public void subscribe(ObservableEmitter<ArrayList<Movies>> emitter) throws Exception {

               mEmitter = emitter;
               Call<MoviesList> response = api.getMoviesList(sort_by, pagecount);
               response.enqueue(new Callback<MoviesList>() {

                   @Override
                   public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                       try {
                           total_pages = response.body().getTotal_pages();
                           ArrayList<Movies> list = response.body().getResults();

                           if(list.size()>0)
                               mEmitter.onNext(list);
                           else
                               mEmitter.onError(new Throwable("Empty List"));

                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   }

                   @Override
                   public void onFailure(Call<MoviesList> call, Throwable t) {

                       mEmitter.onError(t);
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


        mReviews = Observable.create(new ObservableOnSubscribe<ArrayList<Reviews>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<Reviews>> emitter) throws Exception {

                mR_Emitter = emitter;

                Call<ReviewsList> response = api.reviews(id, pagecount);
                response.enqueue(new Callback<ReviewsList>() {
                    @Override
                    public void onResponse(Call<ReviewsList> call, Response<ReviewsList> response) {
                        try {
                            ArrayList<Reviews> reviewslist = response.body().getReviewsResults();
                            if(reviewslist.size()>0) {
                                mR_Emitter.onNext(reviewslist);
                            }else {
                                mR_Emitter.onError(new Throwable("Empty List"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewsList> call, Throwable t) {
                        mR_Emitter.onError(t);
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


        mVidoes =  Observable.create(new ObservableOnSubscribe<ArrayList<Videos>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<Videos>> emitter) throws Exception {

                mV_Emitter = emitter;

                Call<VideoList> response = api.videos(movieId);
                response.enqueue(new Callback<VideoList>() {
                    @Override
                    public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                        try {

                            ArrayList<Videos> videolist = response.body().getResults();
                            if(videolist.size()>0) {
                                mV_Emitter.onNext(videolist);
                            }
                            else
                            {
                                mV_Emitter.onError(new Throwable("Empty List"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoList> call, Throwable t) {
                        mV_Emitter.onError(t);
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