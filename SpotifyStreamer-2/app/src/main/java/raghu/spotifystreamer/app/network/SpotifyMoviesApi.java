package raghu.spotifystreamer.app.network;

import raghu.spotifystreamer.app.model.MoviesList;

import raghu.spotifystreamer.app.model.ReviewsList;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface SpotifyMoviesApi {

    @GET("discover/movie?api_key=your spotify key")
    Call<MoviesList> getMoviesList(@Query("sort_by") String sort,
                                   @Query("page") int page);

}
