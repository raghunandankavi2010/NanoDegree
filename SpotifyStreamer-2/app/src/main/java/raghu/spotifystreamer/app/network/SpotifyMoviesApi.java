package raghu.spotifystreamer.app.network;

import raghu.spotifystreamer.app.model.MoviesList;

import raghu.spotifystreamer.app.model.ReviewsList;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface SpotifyMoviesApi {

    @GET("discover/movie?api_key=6d32f2a6596004bb66069187b4c9b933")
    Call<MoviesList> getMoviesList(@Query("sort_by") String sort,
                                   @Query("page") int page);

    @GET("/movie/{id}/videos?api_key=6d32f2a6596004bb66069187b4c9b933")
    Call<MoviesList>  videos(
            @Path("id") long movieId);

    @GET("/movie/{id}/reviews?api_key=6d32f2a6596004bb66069187b4c9b933") Call<ReviewsList> reviews(
            @Path("id") long movieId,
            @Query("page") int page);

}
