package com.nati.zinab.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nati.zinab.R;
import com.nati.zinab.helpers.Constants;
import com.nati.zinab.helpers.StaticMethods;
import com.nati.zinab.models.WeatherResponse;

import org.joda.time.DateTime;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nati on 8/28/2015.
 * Current weather fragment. This is the class tha calls load weather when its created
 */
public class CurrentFragment extends Fragment {

    @Bind(R.id.weather_icon) TextView weatherIcon;
    @Bind(R.id.weather_description) TextView weatherDescription;
    @Bind(R.id.current_temp) TextView currentTemp;
    @Bind(R.id.low_temp) TextView lowTemp;
    @Bind(R.id.high_temp) TextView highTemp;
    @Bind(R.id.wind) TextView wind;
    @Bind(R.id.sunset) TextView sunset;
    @Bind(R.id.chance) TextView chance;
    @Bind(R.id.humidity) TextView humidity;
    @Bind(R.id.content) ScrollView content;

    private OnViewCreatedListener listener;

    public static CurrentFragment newInstance() {
        CurrentFragment fragment = new CurrentFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.current_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener.loadWeather();
    }

    public void setupUI(WeatherResponse weatherResponse) {
        content.setVisibility(View.VISIBLE);
        weatherDescription.setText(StaticMethods.getWeatherDescription(getActivity(), weatherResponse.getCurrently()));
        currentTemp.setText(String.format("%.0f" + Constants.DEGREE, weatherResponse.getCurrently().getTemperature()));
        lowTemp.setText(String.format(getString(R.string.low) + " %.0f" + Constants.DEGREE, weatherResponse.getDaily().getData().get(0).getTemperatureMin()));
        highTemp.setText(String.format(getString(R.string.high) + " %.0f" + Constants.DEGREE, weatherResponse.getDaily().getData().get(0).getTemperatureMax()));
        wind.setText(String.format("%.1fmps", weatherResponse.getCurrently().getWindSpeed()));
        chance.setText(String.format("%.0f%%", weatherResponse.getCurrently().getPrecipProbability() * 100));
        humidity.setText(String.format("%.0f%%", weatherResponse.getCurrently().getHumidity() * 100));
        StaticMethods.setupWeatherIcon(weatherResponse.getCurrently().getIcon(), weatherIcon);

        DateTime dateTime = new DateTime(weatherResponse.getDaily().getData().get(0).getSunsetTime() * 1000L);
        sunset.setText(StaticMethods.formattedTime(getActivity(), dateTime, true));
    }

    public void hideUI(){
        content.setVisibility(View.GONE);
    }

}

