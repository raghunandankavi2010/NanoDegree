package raghu.spotifystreamer.app.network;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Raghunandan on 16-12-2015.
 */


@Singleton
@Component(modules = {OkHttpModule.class})
public interface OkHttpComponent {

    //OkHttpModule prOkHttpModule();
    void inject(NetworkModule networkModule);
}




