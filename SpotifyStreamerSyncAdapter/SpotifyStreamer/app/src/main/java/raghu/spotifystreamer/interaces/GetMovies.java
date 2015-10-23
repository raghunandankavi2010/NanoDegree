package raghu.spotifystreamer.interaces;

import java.util.List;

import raghu.spotifystreamer.Models.Movies;
import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by Raghunandan on 23-09-2015.
 */
public interface GetMovies {
    @GET("/users/{user}/repos")
    Call<List<Movies>> listMovies();
}