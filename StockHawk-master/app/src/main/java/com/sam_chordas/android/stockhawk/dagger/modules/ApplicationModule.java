package com.sam_chordas.android.stockhawk.dagger.modules;

import android.app.Application;


import com.sam_chordas.android.stockhawk.APIS.LineGraphApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Raghunandan on 14-12-2015.
 */
@Module
public class ApplicationModule {

    private Application app;
    private String PREF_NAME = "prefs";



    public ApplicationModule(Application app) {
        this.app = app;
    }



    @Singleton
    @Provides
    public Application provideApplication() {
        return app;
    }


    @Singleton
    @Provides
    public LineGraphApi provideLineGrpahApi()
    {
        return new LineGraphApi();
    }
}