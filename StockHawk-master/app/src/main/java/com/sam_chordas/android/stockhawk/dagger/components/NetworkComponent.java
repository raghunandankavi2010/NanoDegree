package com.sam_chordas.android.stockhawk.dagger.components;


import com.sam_chordas.android.stockhawk.APIS.LineGraphApi;
import com.sam_chordas.android.stockhawk.dagger.UserScope;
import com.sam_chordas.android.stockhawk.dagger.modules.NetworkModule;

import dagger.Component;

/**
 * Created by Raghunandan on 16-12-2015.
 */
@UserScope
@Component(dependencies = ApplicationComponent.class,modules =NetworkModule.class)
public interface NetworkComponent  {
   void inject(LineGraphApi lineGraphApi);

}
