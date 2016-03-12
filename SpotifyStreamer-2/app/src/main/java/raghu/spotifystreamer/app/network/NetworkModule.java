package raghu.spotifystreamer.app.network;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import raghu.spotifystreamer.app.DaggerInjector;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


import static java.lang.String.format;

@Module
        (
                includes = OkHttpModule.class
        )
public class NetworkModule {

    public static final String MOVIE_DB_API_URL = "http://api.themoviedb.org/3/";

    @Inject
    OkHttpClient okHttpClient;


    public NetworkModule() {
        DaggerInjector.getOkHttpComponent().inject(this);

    }


    @Provides
    SpotifyMoviesApi getApi() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_DB_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                 .build();


        return retrofit.create(SpotifyMoviesApi.class);

    }

    @Provides
    ReviewsApi getReviewsApi() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_DB_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit.create(ReviewsApi.class);

    }

    @Provides
    VideosApi getVideosApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_DB_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit.create(VideosApi.class);

    }


}
