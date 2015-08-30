package com.nati.zinab.helpers;

import android.graphics.Typeface;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.fonts.WeathericonsModule;
import com.orm.SugarApp;

import net.danlew.android.joda.JodaTimeAndroid;

public class ZinabApp extends SugarApp {

    public static Typeface primaryFont, weatherFont, forecastFont;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);

        Iconify.with(new MaterialModule())
                .with(new WeathericonsModule());

    }
}
