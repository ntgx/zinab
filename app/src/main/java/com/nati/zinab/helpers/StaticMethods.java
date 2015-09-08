package com.nati.zinab.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Locale;

public class StaticMethods {
	
	public static void setupActionbar(Context context, String title, ActionBar actionBar, boolean enableBackButton){
		SpannableString tit = new SpannableString(title);
		Typeface t = Typeface.createFromAsset(context.getAssets(), "fonts/" + Constants.GOTHAM_FONT);
		CustomTypefaceSpan type = new CustomTypefaceSpan(t);
	    tit.setSpan(type, 0, tit.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    actionBar.setTitle(tit);
	    if (enableBackButton)
	    	actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	public static boolean checkConnection(Context context){
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    	if (networkInfo != null && networkInfo.isConnected()) {
    		return true;
    	}
		return false;
	}

	public static void loadLocale(Context context)
	{
		String langPref = "langpref";
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String language = sharedPref.getString(langPref, "");
		Log.d("hey", "language:" + language);
		changeLang(language, context);
	}

	private static void changeLang(String lang, Context context)
	{
		if (lang.equalsIgnoreCase(""))
			return;
		Locale myLocale = new Locale(lang);
		Locale.setDefault(myLocale);
		android.content.res.Configuration config = new android.content.res.Configuration();
		config.locale = myLocale;
		context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
	}

	public static void setupWeatherIcon(String icon, TextView weatherIcon) {
		//TODO: set main bg here as well
		switch(icon){
			case "clear-day":
				weatherIcon.setText("{wi_forecast_io_clear_day}");
				break;
			case "clear-night":
				weatherIcon.setText("{wi_forecast_io_clear_night}");
				break;
			case "rain":
				weatherIcon.setText("{wi_forecast_io_rain}");
				break;
			case "snow":
				weatherIcon.setText("{wi_forecast_io_snow}");
				break;
			case "sleet":
				weatherIcon.setText("{wi_forecast_io_sleet}");
				break;
			case "wind":
				weatherIcon.setText("{wi_forecast_io_wind}");
				break;
			case "fog":
				weatherIcon.setText("{wi_forecast_io_fog}");
				break;
			case "cloudy":
				weatherIcon.setText("{wi_forecast_io_cloudy}");
				break;
			case "partly-cloudy-day":
				weatherIcon.setText("{wi_forecast_io_partly_cloudy_day}");
				break;
			case "partly-cloudy-night":
				weatherIcon.setText("{wi_forecast_io_partly_cloudy_night}");
				break;
		}
	}
}