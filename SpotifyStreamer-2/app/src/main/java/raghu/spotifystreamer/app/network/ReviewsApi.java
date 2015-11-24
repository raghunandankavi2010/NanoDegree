package raghu.spotifystreamer.app.network;

import raghu.spotifystreamer.app.model.ReviewsList;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Raghunandan on 18-11-2015.
 */
public interface ReviewsApi {

    @GET("movie/{id}/reviews?api_key=6d32f2a6596004bb66069187b4c9b933")
    Call<ReviewsList> reviews(
            @Path("id") long movieId,
            @Query("page") int page);

}
