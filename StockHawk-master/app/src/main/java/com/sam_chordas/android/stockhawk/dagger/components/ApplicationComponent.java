package com.sam_chordas.android.stockhawk.dagger.components;

import android.app.Application;


import com.sam_chordas.android.stockhawk.dagger.modules.ApplicationModule;
import com.sam_chordas.android.stockhawk.ui.LineGraphActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Raghunandan on 14-12-2015.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(LineGraphActivity lineGraphActivity);

    Application provideApplication();


}