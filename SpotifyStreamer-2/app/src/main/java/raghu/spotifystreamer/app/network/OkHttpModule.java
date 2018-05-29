package raghu.spotifystreamer.app.network;

import android.util.Log;



import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created by Raghunandan on 08-12-2015.
 */

@Module
public class OkHttpModule {

    @Provides
    @Singleton
    OkHttpClient getOkHttpClient() {

       return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        Request originalRequest = chain.request(); //Current Request

                        Response response = chain.proceed(originalRequest); //Get response of the request

                        //I am logging the response body in debug mode. When I do this I consume the response (OKHttp only lets you do this once) so i have re-build a new one using the cached body
                        String bodyString = response.body().string();
                        Log.i("...", bodyString);

                        Log.i("NetworkModule", String.format("Sending request %s with headers %s ", originalRequest.url(), originalRequest.headers()));
                        Log.i("", (String.format("Got response HTTP %s %s \n\n with body %s \n\n with headers %s ", response.code(), response.message(), bodyString, response.headers())));
                        response = response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();

                        return response;
                    }
                })
                .readTimeout(5,TimeUnit.SECONDS)
                .connectTimeout(5,TimeUnit.SECONDS)
                .build();


    }
}
