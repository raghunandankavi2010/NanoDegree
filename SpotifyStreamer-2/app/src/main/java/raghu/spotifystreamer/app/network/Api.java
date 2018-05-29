package raghu.spotifystreamer.app.network;

import raghu.spotifystreamer.app.model.MoviesList;
import raghu.spotifystreamer.app.model.ReviewsList;
import raghu.spotifystreamer.app.model.VideoList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by raghu on 18/3/18.
 */

public interface Api {

    @GET("movie/{id}/reviews?api_key=6d32f2a6596004bb66069187b4c9b933")
    Call<ReviewsList> reviews(
            @Path("id") long movieId,
            @Query("page") int page);

    @GET("discover/movie?api_key=6d32f2a6596004bb66069187b4c9b933")
    Call<MoviesList> getMoviesList(@Query("sort_by") String sort,
                                   @Query("page") int page);

    @GET("movie/{id}/videos?api_key=6d32f2a6596004bb66069187b4c9b933")
    Call<VideoList> videos(
            @Path("id") int movieId);

}
