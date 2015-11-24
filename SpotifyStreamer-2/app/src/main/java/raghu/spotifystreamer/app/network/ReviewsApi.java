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

    @GET("movie/{id}/reviews?api_key=yourapikey")
    Call<ReviewsList> reviews(
            @Path("id") long movieId,
            @Query("page") int page);

}
