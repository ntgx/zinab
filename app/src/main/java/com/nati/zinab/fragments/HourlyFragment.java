package com.nati.zinab.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nati.zinab.R;
import com.nati.zinab.adapters.DailyAdapter;
import com.nati.zinab.adapters.HourlyAdapter;
import com.nati.zinab.models.WeatherResponse;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nati on 8/28/2015.
 */
public class HourlyFragment extends Fragment {

    @Bind(R.id.listView) ListView listView;
    @Bind(R.id.content) RelativeLayout content;

    public static HourlyFragment newInstance() {
        HourlyFragment fragment = new HourlyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hourly_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    public void setupUI(WeatherResponse weatherResponse) {
        content.setVisibility(View.VISIBLE);
        HourlyAdapter adapter = new HourlyAdapter(getActivity(), weatherResponse.getHourly().getData());
        listView.setAdapter(adapter);
    }
}
