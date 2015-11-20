package raghu.spotifystreamer.app.network;

import raghu.spotifystreamer.app.model.MoviesList;
import raghu.spotifystreamer.app.model.ReviewsList;
import raghu.spotifystreamer.app.model.VideoList;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Raghunandan on 20-11-2015.
 */
public interface VideosApi {

    @GET("movie/{id}/videos?api_key=6d32f2a6596004bb66069187b4c9b933")
    Call<VideoList> videos(
            @Path("id") int movieId);
}
