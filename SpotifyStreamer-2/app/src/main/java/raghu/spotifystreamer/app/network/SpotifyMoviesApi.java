package raghu.spotifystreamer.app.network;

import raghu.spotifystreamer.app.model.MoviesList;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface SpotifyMoviesApi {

    @GET("discover/movie?api_key=6d32f2a6596004bb66069187b4c9b933")
    Call<MoviesList> getMoviesList(@Query("sort_by") String sort,
                                   @Query("page") int page);

}
