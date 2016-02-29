package com.nati.zinab.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nati.zinab.R;
import com.nati.zinab.helpers.Constants;
import com.nati.zinab.models.City;

public class CitiesAdapter extends BaseAdapter {

    private final Context context;
    private City[] items;
    private LayoutInflater inflater;
    private City city;
    private String language;

    public CitiesAdapter(Context context, City[] items, String language) {

        this.context = context;
        this.items = items;
        this.language = language;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.city_list_item, parent, false);
        }

        city = items[position];

        TextView cityName = (TextView) view.findViewById(R.id.city_name);

        if(language.equals(Constants.LANGUAGE_ENGLISH)) {
            cityName.setText(city.getName());
        }else{
            cityName.setText(city.getAmharicName());
        }

        return view;
    }

    public City getCity(int position) {
        return ((City) getItem(position));
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    public City[] getItems() {
        return items;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}