package raghu.spotifystreamer.app.network;

import android.util.Log;



import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import raghu.spotifystreamer.app.DaggerInjector;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
    Api getApi() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_DB_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                 .build();


        return retrofit.create(Api.class);

    }

    @Provides
    Retrofit getReviewsApi() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_DB_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit;

    }


}
