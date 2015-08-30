package com.nati.zinab.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.nati.zinab.R;
import com.nati.zinab.adapters.CitiesAdapter;
import com.nati.zinab.adapters.HomeTabsAdapter;
import com.nati.zinab.fragments.CurrentlyFragment;
import com.nati.zinab.fragments.DailyFragment;
import com.nati.zinab.fragments.HourlyFragment;
import com.nati.zinab.helpers.Constants;
import com.nati.zinab.helpers.StaticMethods;
import com.nati.zinab.models.City;
import com.nati.zinab.models.WeatherResponse;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements CurrentlyFragment.OnViewCreatedListener {

    private ProgressBar progressBar;
    private City selectedCity;
    private Toolbar toolbar;
    private TabLayout tabs;

    private ViewPager viewPager;
    private HomeTabsAdapter adapter;
    private CurrentlyFragment currentlyFragment;
    private HourlyFragment hourlyFragment;
    private DailyFragment dailyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StaticMethods.loadLocale(getBaseContext());

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Spinner citiesSpinner = (Spinner) findViewById(R.id.cities_spinner);

        final CitiesAdapter citiesAdapter = new CitiesAdapter(this, City.getCities(this));
        citiesSpinner.setAdapter(citiesAdapter);

        citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    selectedCity = (City) citiesSpinner.getSelectedItem();
                    currentlyFragment.hideUI();
                    loadWeather();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        setupTabs();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        selectedCity = (City) citiesSpinner.getSelectedItem();
    }

    /*private void blurBackground() {
        Blurry.with(MainActivity.this)
                .radius(25)
                .sampling(2)
                .async()
                .animate(500)
                .onto((ViewGroup) findViewById(R.id.content));
    }*/

    private void load() {
        progressBar.setVisibility(View.GONE);

        String json = null;
        try {
            InputStream is = getAssets().open("response.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        WeatherResponse weatherResponse = new Gson().fromJson(json, WeatherResponse.class);
        setupUI(weatherResponse);
    }

    public void loadWeather() {
        if (StaticMethods.checkConnection(this)) {
            progressBar.setVisibility(View.VISIBLE);
            Ion.with(getBaseContext())
                    .load(Constants.BASE_URL + Constants.API_KEY
                            + "/" + selectedCity.getLat() + "," + selectedCity.getLng() + Constants.OPTIONS)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressBar.setVisibility(View.GONE);
                            if (e != null) {
                                e.printStackTrace();
                            }

                            if (result != null) {
                                Log.d("hey", result.toString());
                                WeatherResponse weatherResponse = new Gson().fromJson(result, WeatherResponse.class);
                                setupUI(weatherResponse);
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Please connect to the internet and try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupUI(WeatherResponse weatherResponse) {
        currentlyFragment = (CurrentlyFragment) adapter.getItem(0);
        hourlyFragment = (HourlyFragment) adapter.getItem(1);
        dailyFragment = (DailyFragment) adapter.getItem(2);
        currentlyFragment.setupUI(weatherResponse);
        hourlyFragment.setupUI(weatherResponse);
        dailyFragment.setupUI(weatherResponse);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setupTabs() {
        tabs = (TabLayout) findViewById(R.id.tabLayout);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.addTab(tabs.newTab().setText("አሁን"));
        tabs.addTab(tabs.newTab().setText("Hourly"));
        tabs.addTab(tabs.newTab().setText("Daily"));

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(3);
        adapter = new HomeTabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

    }

}
