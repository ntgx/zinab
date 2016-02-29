package com.nati.zinab.models;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.InputStream;

/**
 * Created by Nati on 8/23/2015.
 */
public class City {
    private String name;
    @SerializedName("name_am")
    private String amharicName;
    private float lat, lng;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public static String loadJsonFromAsset(Context context){
        String json = null;
        try {
            InputStream is = context.getAssets().open("cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer,"UTF-8");
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.d("City", "json:" + json);
        return json;
    }

    public static City[] getCities(Context context) {
        String json = loadJsonFromAsset(context);
        return new Gson().fromJson(json, City[].class);
    }

    public String getAmharicName() {
        return amharicName;
    }

    public void setAmharicName(String amharicName) {
        this.amharicName = amharicName;
    }
}
