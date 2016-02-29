package com.nati.zinab.helpers;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.fonts.WeathericonsModule;
import com.nati.zinab.R;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.HashMap;

public class ZinabApp extends Application {

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);

        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Bariol_Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());*/

                Iconify
                .with(new MaterialModule())
                .with(new WeathericonsModule());

    }

    public ZinabApp() {

    }

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg:
        // roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a
        // company.
    }

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(R.xml.app_tracker);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

}
