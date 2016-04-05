package com.nati.zinab.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.ads.AdRequest;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Application application;

    public AppModule(Application application){
        this.application = application;
    }

    @Provides
    @Singleton
    public SharedPreferences sharedPreferencesProvider(){
        return application.getSharedPreferences(application.getPackageName(), Context.MODE_PRIVATE);
    }

    @Provides
    public AdRequest adRequestProvider(){
        return new AdRequest.Builder().build();
    }
}
