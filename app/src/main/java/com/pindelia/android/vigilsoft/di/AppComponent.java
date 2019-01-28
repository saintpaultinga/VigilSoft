package com.pindelia.android.vigilsoft.di;

import android.app.Application;

import com.pindelia.android.vigilsoft.App;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent extends AndroidInjector<App> {
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App> {
    }

}
