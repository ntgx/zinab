package com.nati.zinab.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.ads.AdRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nati.zinab.helpers.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    @Singleton
    public Gson gsonProvider(){
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    @Provides
    @Singleton
    public Retrofit retrofitProvider(Gson gson){
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    public AdRequest adRequestProvider(){
        return new AdRequest.Builder()
                .addTestDevice("30C07DF31F1212B2E7A6D7BEFCD2CAE1")
                .build();
    }
}
