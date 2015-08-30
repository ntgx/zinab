package com.nati.zinab.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nati.zinab.R;
import com.nati.zinab.activities.MainActivity;
import com.nati.zinab.helpers.Constants;
import com.nati.zinab.models.WeatherResponse;

import org.joda.time.DateTime;

import java.text.DateFormatSymbols;

/**
 * Created by Nati on 8/28/2015.
 */
public class CurrentlyFragment extends Fragment {

    private TextView weatherIcon, weatherDescription, currentTemp, lowTemp, highTemp, wind, sunset, chance, humidity;
    private RelativeLayout content;
    private String[] amPmTexts;
    private OnViewCreatedListener listener;

    public static CurrentlyFragment newInstance() {
        CurrentlyFragment fragment = new CurrentlyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public interface OnViewCreatedListener {
        void loadWeather();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnViewCreatedListener) {
            listener = (OnViewCreatedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        amPmTexts = new DateFormatSymbols().getAmPmStrings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_fragment, container, false);
        content = (RelativeLayout) view.findViewById(R.id.content);
        weatherIcon = (TextView) view.findViewById(R.id.weather_icon);
        currentTemp = (TextView) view.findViewById(R.id.current_temp);
        weatherDescription = (TextView) view.findViewById(R.id.weather_description);
        lowTemp = (TextView) view.findViewById(R.id.low_temp);
        highTemp = (TextView) view.findViewById(R.id.high_temp);
        wind = (TextView) view.findViewById(R.id.wind);
        sunset = (TextView) view.findViewById(R.id.sunset);
        chance = (TextView) view.findViewById(R.id.chance);
        humidity = (TextView) view.findViewById(R.id.humidity);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener.loadWeather();
    }

    public void setupUI(WeatherResponse weatherResponse) {
        content.setVisibility(View.VISIBLE);
        weatherDescription.setText(weatherResponse.getCurrently().getSummary());
        currentTemp.setText(String.format("%.0f" + Constants.DEGREE, weatherResponse.getCurrently().getTemperature()));
        lowTemp.setText(String.format("LOW %.0f" + Constants.DEGREE, weatherResponse.getDaily().getData().get(0).getTemperatureMin()));
        highTemp.setText(String.format("HIGH %.0f" + Constants.DEGREE, weatherResponse.getDaily().getData().get(0).getTemperatureMax()));
        wind.setText(String.format("%.1fmps", weatherResponse.getCurrently().getWindSpeed()));
        chance.setText(String.format("%.0f%%", weatherResponse.getCurrently().getPrecipProbability() * 100));
        humidity.setText(String.format("%.0f%%", weatherResponse.getCurrently().getHumidity() * 100));
        setupWeatherIcon(weatherResponse);

        DateTime dateTime = new DateTime(weatherResponse.getDaily().getData().get(0).getSunsetTime() * 1000L);
        sunset.setText(formattedReminderTime(dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), false));
    }

    public void hideUI(){
        content.setVisibility(View.GONE);
    }

    private void setupWeatherIcon(WeatherResponse weatherResponse) {
        //TODO: set main bg here as well
        switch(weatherResponse.getCurrently().getIcon()){
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

    private String formattedReminderTime(int hour, int minute, boolean is24HourFormat) {
        String reminderTime;
        if(is24HourFormat) {
            reminderTime = String.format("%02d:%02d", hour, minute);
        }
        else{
            if(hour == 12){
                reminderTime = String.format("%02d:%02d %s", hour, minute, amPmTexts[1]);
            }

            else if(hour > 12) {
                hour -= 12;
                reminderTime = String.format("%02d:%02d %s", hour, minute, amPmTexts[1]);
            }

            else
                reminderTime = String.format("%02d:%02d %s", hour, minute, amPmTexts[0]);
        }

        return reminderTime;
    }

}

