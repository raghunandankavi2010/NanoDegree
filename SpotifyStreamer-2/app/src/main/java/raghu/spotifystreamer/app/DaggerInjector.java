package raghu.spotifystreamer.app;

import javax.inject.Singleton;

import dagger.Component;
import raghu.spotifystreamer.app.model.SpotifyMoviesModel;
import raghu.spotifystreamer.app.network.DaggerOkHttpComponent;
import raghu.spotifystreamer.app.network.NetworkModule;
import raghu.spotifystreamer.app.network.OkHttpComponent;
import raghu.spotifystreamer.app.network.OkHttpModule;

/**
 * Created by Raghunandan on 16-12-2015.
 */
public class DaggerInjector {


    public static OkHttpComponent okHttpComponent = DaggerOkHttpComponent.builder()
            .okHttpModule(new OkHttpModule())
            .build();

    public static OkHttpComponent getOkHttpComponent() {
        return okHttpComponent;
    }
}
