package com.sam_chordas.android.stockhawk;

import android.app.Application;
import android.os.StrictMode;
import android.support.v7.appcompat.BuildConfig;

import com.sam_chordas.android.stockhawk.dagger.components.ApplicationComponent;
import com.sam_chordas.android.stockhawk.dagger.components.DaggerApplicationComponent;
import com.sam_chordas.android.stockhawk.dagger.components.DaggerNetworkComponent;
import com.sam_chordas.android.stockhawk.dagger.components.NetworkComponent;
import com.sam_chordas.android.stockhawk.dagger.modules.ApplicationModule;
import com.sam_chordas.android.stockhawk.dagger.modules.NetworkModule;

/**
 * Created by Raghunandan on 14-12-2015.
 */
public class SotckHawkApplication extends Application {




    private static ApplicationComponent applicationComponent;

    private static NetworkComponent networkComponent;


    @Override
    public void onCreate() {

        if (BuildConfig.DEBUG) {
            // do something for a debug build
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectAll() //for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        super.onCreate();

        if (applicationComponent == null && networkComponent == null) {

           applicationComponent = DaggerApplicationComponent.builder()
                    // list of modules that are part of this component need to be created here too
                    .applicationModule(new ApplicationModule(this))
                    .build();

            networkComponent = DaggerNetworkComponent.builder()
                    .applicationComponent(applicationComponent)
                    .networkModule(new NetworkModule())
                    .build();
        }


    }

    public static ApplicationComponent component() {
        return applicationComponent;
    }

    public static NetworkComponent netcomponent() {
        return networkComponent;
    }

}
