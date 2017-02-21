package com.nati.zinab.helpers;

import com.nati.zinab.models.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nati on 2/21/17.
 */
public interface WeatherService {
    @GET("forecast/{api-key}/{lat},{lng}")
    Call<WeatherResponse> search(@Path("api-key") String apiKey,
                                 @Path("lat") String lat,
                                 @Path("lng") String lng,
                                 @Query("units") String unit,
                                 @Query("exclude") String flag);
}