package com.nati.zinab.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nati.zinab.R;
import com.nati.zinab.models.City;

public class CitiesAdapter extends BaseAdapter {

    private final Context context;
    City[] items;
    LayoutInflater inflater;
    City city;

    public CitiesAdapter(Context context, City[] items) {

        this.context = context;
        this.items = items;
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
        cityName.setText(city.getName());

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