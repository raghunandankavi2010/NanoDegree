package raghu.spotifystreamer.app;

import android.app.Application;

import raghu.spotifystreamer.app.model.SpotifyMoviesModel;
import raghu.spotifystreamer.app.network.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;
import raghu.spotifystreamer.app.network.OkHttpModule;


public class RxApp extends Application {

    @Singleton
    @Component(modules = {NetworkModule.class,OkHttpModule.class})
    public interface NetworkComponent {
        SpotifyMoviesModel spotifyMoviesModel();
    }



    private NetworkComponent mComponent = null;

    private static RxApp sInstance;
    public static RxApp get() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (mComponent == null) {
            mComponent = DaggerRxApp_NetworkComponent.builder()
                    .networkModule(new NetworkModule(new OkHttpModule()))
                    .build();
           // mComponent = DaggerRxApp_NetworkComponent.create();
        }

        sInstance = (RxApp) getApplicationContext();

    }

    public NetworkComponent component() {
        return mComponent;
    }

    // This allows providing mock NetworkComponent from test
    public void setComponent(NetworkComponent component) {
        mComponent = component;
    }
}
