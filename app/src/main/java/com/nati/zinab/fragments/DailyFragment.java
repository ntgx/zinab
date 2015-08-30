package com.nati.zinab.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nati.zinab.R;
import com.nati.zinab.activities.MainActivity;
import com.nati.zinab.adapters.DailyAdapter;
import com.nati.zinab.models.WeatherResponse;

/**
 * Created by Nati on 8/28/2015.
 */
public class DailyFragment extends Fragment {

    private ListView listView;
    private RelativeLayout content;

    public static DailyFragment newInstance() {
        DailyFragment fragment = new DailyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_fragment, container, false);
        content = (RelativeLayout) view.findViewById(R.id.content);
        listView = (ListView) view.findViewById(R.id.listView);

        return view;
    }

    /*@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener.loadWeather();
        setupUI(((MainActivity)getActivity()).getWeatherResponse());
    }*/

    public void setupUI(WeatherResponse weatherResponse) {
        content.setVisibility(View.VISIBLE);
        DailyAdapter adapter = new DailyAdapter(getActivity(), weatherResponse.getDaily().getData());
        listView.setAdapter(adapter);
    }
}
