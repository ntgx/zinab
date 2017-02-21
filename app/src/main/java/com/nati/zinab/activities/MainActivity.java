package com.nati.zinab.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.nati.zinab.R;
import com.nati.zinab.adapters.CitiesAdapter;
import com.nati.zinab.adapters.HomeTabsAdapter;
import com.nati.zinab.fragments.CurrentFragment;
import com.nati.zinab.fragments.DailyFragment;
import com.nati.zinab.fragments.HourlyFragment;
import com.nati.zinab.helpers.Constants;
import com.nati.zinab.helpers.StaticMethods;
import com.nati.zinab.helpers.WeatherService;
import com.nati.zinab.helpers.ZinabApp;
import com.nati.zinab.models.City;
import com.nati.zinab.models.WeatherResponse;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements CurrentFragment.OnViewCreatedListener, Callback<WeatherResponse> {

    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tabLayout) TabLayout tabs;
    @Bind(R.id.pager) ViewPager viewPager;
    @Bind(R.id.selected_city) TextView selectedCityTv;
    @Bind(R.id.errorLayout) RelativeLayout errorLayout;
    @Bind(R.id.try_again) Button tryAgain;
    @Bind(R.id.powered_by) TextView poweredBy;
    @Bind(R.id.adView) AdView adView;

    private HomeTabsAdapter adapter;
    private CurrentFragment currentFragment;
    private HourlyFragment hourlyFragment;
    private DailyFragment dailyFragment;
    private AlertDialog citiesDialog;
    private City[] cities;
    private City selectedCity;
    private String language;
    private static final String CITY_PREF_KEY = "selectedCityIndex";
    private AlertDialog languagePickerDialog;

    @Inject SharedPreferences prefs;
    @Inject AdRequest adRequest;
    @Inject Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((ZinabApp) getApplication()).getComponent().inject(this);

        super.onCreate(savedInstanceState);

        Tracker t = ((ZinabApp) getApplication()).getTracker(ZinabApp.TrackerName.APP_TRACKER);
        t.enableAdvertisingIdCollection(true);// to enable demographics tracking stuff

        StaticMethods.loadLocale(this);
        language = StaticMethods.getLanguage(this);
        cities = City.getCities(this);
        selectedCity = cities[prefs.getInt(CITY_PREF_KEY, 0)];

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        checkFirstTime();

        setSupportActionBar(toolbar);
        setSelectedCityTvText();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        setupTabs();
        setupAds();
    }

    private void setupAds() {
        try {
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    adView.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.try_again)
    public void loadWeather() {
        progressBar.setVisibility(View.VISIBLE);
        hideErrorLayout();

        WeatherService weatherService = retrofit.create(WeatherService.class);

        Call<WeatherResponse> call = weatherService
                .search(StaticMethods.decrypt(Constants.API_KEY, getBaseContext()),
                        String.valueOf(selectedCity.getLat()),
                        String.valueOf(selectedCity.getLng()),
                        "si",
                        "flags");
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
        progressBar.setVisibility(View.GONE);
        if (response.body() != null) {
            Log.d("hey", response.body().toString());
            setupUI(response.body());
        }
    }

    @Override
    public void onFailure(Call<WeatherResponse> call, Throwable t) {
        progressBar.setVisibility(View.GONE);
        if (!StaticMethods.checkConnection(getBaseContext())) {
            Toast.makeText(getBaseContext(), "Please connect to the internet and try again!",
                    Toast.LENGTH_SHORT).show();
        }
        t.printStackTrace();
        showErrorLayout();
    }

    private void showErrorLayout() {
        errorLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
    }

    private void hideErrorLayout() {
        errorLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
    }

    private void checkFirstTime() {
        if(prefs.getBoolean("firstRun", true)){
            showLanguageDialog();
            prefs.edit().putBoolean("firstRun", false).apply();
        }
    }

    private void showLanguageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Language")
                .setSingleChoiceItems(R.array.languages, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {//default lang is amharic and if language is set to english restart activity
                            setLanguageAndRestart(Constants.LANGUAGE_ENGLISH);
                        } else {
                            languagePickerDialog.dismiss();
                        }
                    }
                });
        languagePickerDialog = builder.create();
        languagePickerDialog.show();
    }

    private void setLanguageAndRestart(String lang) {
        StaticMethods.setLanguage(this, lang);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        languagePickerDialog.dismiss();
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

    public void setSelectedCityTvText() {
        if(language.equals(Constants.LANGUAGE_ENGLISH)) {
            selectedCityTv.setText(selectedCity.getName());
        }else{
            selectedCityTv.setText(selectedCity.getAmharicName());
        }
    }

    @OnClick(R.id.selected_city)
    public void showCitiesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getLayoutInflater().inflate(R.layout.cities_dialog, null));
        citiesDialog = builder.create();
        citiesDialog.show();

        final ListView list = (ListView) citiesDialog.findViewById(R.id.list);
        list.setDivider(null);

        final CitiesAdapter citiesAdapter = new CitiesAdapter(this, cities, language);
        list.setAdapter(citiesAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                citiesDialog.dismiss();
                selectedCity = (City) list.getAdapter().getItem(i);
                prefs.edit().putInt(CITY_PREF_KEY, i).apply();
                setSelectedCityTvText();
                if (currentFragment != null) {
                    currentFragment.hideUI();
                    hourlyFragment.hideUI();
                    dailyFragment.hideUI();
                }
                loadWeather();
            }
        });
    }

    @OnClick(R.id.powered_by)
    public void openForcastIo(){
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://forecast.io"));
            startActivity(i);
        }catch(Exception e){
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Stop the analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
