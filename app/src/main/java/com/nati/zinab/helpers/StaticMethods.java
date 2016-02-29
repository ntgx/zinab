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

import com.nati.zinab.R;
import com.nati.zinab.models.DataPoint;
import com.nati.zinab.models.WeatherResponse;

import org.jasypt.util.text.BasicTextEncryptor;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormatSymbols;
import java.util.Locale;

public class StaticMethods {
	
	public static void setupActionbar(Context context, String title, ActionBar actionBar, boolean enableBackButton){
		SpannableString tit = new SpannableString(title);
		Typeface t = Typeface.createFromAsset(context.getAssets(), "fonts/" + Constants.BARIOL_FONT);
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
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String language = sharedPref.getString("langpref", "am");
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

	public static String getLanguage(Context context){
		SharedPreferences langPref = PreferenceManager.getDefaultSharedPreferences(context);
		return langPref.getString("langpref", "");
	}

	public static void setLanguage(Context context, String lang){
		SharedPreferences langPref = PreferenceManager.getDefaultSharedPreferences(context);
		langPref.edit().putString("langpref", lang).apply();
	}

	public static void setupWeatherIcon(String icon, TextView weatherIcon) {
		//TODO: set main bg here as well
		switch(icon){
			case "clear-day":
				weatherIcon.setText("{wi_forecast_io_clear_day}");
				break;
			case "clear-night":
				weatherIcon.setText("{wi_stars}");
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

    public static String getWeatherDescription(Context context, DataPoint item) {
        String summary = item.getSummary();
        if(summary.equalsIgnoreCase("clear")){
            String icon = item.getIcon();
            if(icon.equalsIgnoreCase("clear-night")){
                return context.getString(R.string.clear_night);
            }
            else if(icon.equalsIgnoreCase("clear-day")){
                return context.getString(R.string.clear_day);
            }
            else{
                return summary;
            }
        }
        else if(summary.equalsIgnoreCase("partly cloudy")){
            return context.getString(R.string.partly_cloudy);
        }
        else if(summary.equalsIgnoreCase("cloudy")){
            return context.getString(R.string.cloudy);
        }
        else if(summary.equalsIgnoreCase("mostly cloudy")){
            return context.getString(R.string.mostly_cloudy);
        }
        else if(summary.equalsIgnoreCase("rain")){
            return context.getString(R.string.rain);
        }
        else if(summary.equalsIgnoreCase("light rain")){
            return context.getString(R.string.light_rain);
        }
        else if(summary.equalsIgnoreCase("heavy rain")){
            return context.getString(R.string.heavy_rain);
        }
        else if(summary.equalsIgnoreCase("drizzle")){
            return context.getString(R.string.very_light_rain);
        }
        else{
            return summary;
        }
    }

    public static String formattedTime(Context context, DateTime dateTime, boolean showMinutes) {

        String formattedTime;
        String language = StaticMethods.getLanguage(context);

        if(language.equals(Constants.LANGUAGE_AMHARIC)){
            dateTime = dateTime.minusHours(6);
            DateTimeFormatter fmt;
            if(showMinutes){
                fmt = DateTimeFormat.forPattern("h:m");
            }
            else {
                fmt = DateTimeFormat.forPattern("E h");
            }
            String date = fmt.print(dateTime);
            if(dateTime.getHourOfDay() > 12) {
                formattedTime = date + "ማታ";
            }
            else{
                if(dateTime.getHourOfDay() < 6)
                    formattedTime = date + "ጠዋት";
                else
                    formattedTime = date + "ከሰአት";
            }
        }
        else{
            DateTimeFormatter fmt;
            if(showMinutes){
                fmt = DateTimeFormat.forPattern("h:ma");
            }
            else{
                fmt = DateTimeFormat.forPattern("E ha");
            }
            formattedTime = fmt.print(dateTime);
        }

        return formattedTime;
    }

	public static String decrypt(String string, Context context) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(context.getPackageName());
        return textEncryptor.decrypt(string);
    }

    public static String encrypt(String string, Context context) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(context.getPackageName());
        return textEncryptor.encrypt(string);
    }

}