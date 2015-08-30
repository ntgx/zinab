package com.nati.zinab.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.nati.zinab.R;
import com.nati.zinab.adapters.CitiesAdapter;
import com.nati.zinab.models.City;

public class PickCityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_city_activity);

        final Spinner citiesSpinner = (Spinner) findViewById(R.id.cities_spinner);
        Button pickButton = (Button) findViewById(R.id.pick_button);

        final CitiesAdapter citiesAdapter = new CitiesAdapter(this, City.getCities(this));
        citiesSpinner.setAdapter(citiesAdapter);

        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                City selectedCity = (City)citiesSpinner.getSelectedItem();
                Intent intent = new Intent(PickCityActivity.this, MainActivity.class);
                intent.putExtra("selectedCity", new Gson().toJson(selectedCity, City.class));
                startActivity(intent);
            }
        });
    }
}
