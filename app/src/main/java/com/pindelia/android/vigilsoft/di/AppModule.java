package com.pindelia.android.vigilsoft.di;


import android.app.Application;

import com.pindelia.android.vigilsoft.App;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjectionModule;

@Module(includes = {AndroidInjectionModule.class,
                    ApplicationProviderModule.class})
public abstract class AppModule {
    @Binds
    @Singleton
    // Singleton annotation isn't necessary (in this case since Application instance is unique)
    // but is here for convention.
    abstract Application application(App app);
}
