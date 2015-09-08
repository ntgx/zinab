package com.nati.zinab.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.nati.zinab.R;
import com.nati.zinab.adapters.CitiesAdapter;
import com.nati.zinab.adapters.HomeTabsAdapter;
import com.nati.zinab.fragments.CurrentFragment;
import com.nati.zinab.fragments.DailyFragment;
import com.nati.zinab.fragments.HourlyFragment;
import com.nati.zinab.helpers.Constants;
import com.nati.zinab.helpers.StaticMethods;
import com.nati.zinab.models.City;
import com.nati.zinab.models.WeatherResponse;

import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements CurrentFragment.OnViewCreatedListener {

    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tabLayout) TabLayout tabs;
    @Bind(R.id.pager) ViewPager viewPager;
    @Bind(R.id.selected_city) TextView selectedCityTv;

    private HomeTabsAdapter adapter;
    private CurrentFragment currentFragment;
    private HourlyFragment hourlyFragment;
    private DailyFragment dailyFragment;
    private AlertDialog citiesDialog;
    private City[] cities;
    private City selectedCity;
    private static final String CITY_PREF_KEY = "selectedCityIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StaticMethods.loadLocale(getBaseContext());
        cities = City.getCities(this);
        SharedPreferences prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        selectedCity = cities[prefs.getInt(CITY_PREF_KEY, 0)];

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        selectedCityTv.setText(selectedCity.getName());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        setupTabs();
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
        currentFragment = (CurrentFragment) adapter.getItem(0);
        hourlyFragment = (HourlyFragment) adapter.getItem(1);
        dailyFragment = (DailyFragment) adapter.getItem(2);
        currentFragment.setupUI(weatherResponse);
        hourlyFragment.setupUI(weatherResponse);
        dailyFragment.setupUI(weatherResponse);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_settings).setIcon(
                new IconDrawable(this, MaterialIcons.md_settings)
                        .colorRes(R.color.white)
                        .actionBarSize());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, Settings.class);
                startActivity(i);
                break;
        }
        return true;
    }

    private void setupTabs() {
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setOffscreenPageLimit(3);
        adapter = new HomeTabsAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

    }

    @OnClick(R.id.selected_city)
    public void showCitiesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getLayoutInflater().inflate(R.layout.cities_dialog, null));
        citiesDialog = builder.create();
        citiesDialog.show();

        final ListView list = (ListView) citiesDialog.findViewById(R.id.list);
        list.setDivider(null);

        final CitiesAdapter citiesAdapter = new CitiesAdapter(this, cities);
        list.setAdapter(citiesAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                citiesDialog.dismiss();
                selectedCity = (City) list.getAdapter().getItem(i);
                SharedPreferences prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(CITY_PREF_KEY, i);
                editor.apply();
                selectedCityTv.setText(selectedCity.getName());
                currentFragment.hideUI();
                loadWeather();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
