package com.nati.zinab.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nati.zinab.R;
import com.nati.zinab.adapters.DailyAdapter;
import com.nati.zinab.adapters.HourlyAdapter;
import com.nati.zinab.helpers.Constants;
import com.nati.zinab.helpers.StaticMethods;
import com.nati.zinab.models.DataPoint;
import com.nati.zinab.models.WeatherResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nati on 8/28/2015.
 */
public class HourlyFragment extends Fragment {

    @Bind(R.id.listView) ListView listView;
    @Bind(R.id.content) RelativeLayout content;
    private AlertDialog detailsDialog;

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
        final HourlyAdapter adapter = new HourlyAdapter(getActivity(),
                weatherResponse.getHourly().getData());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDetailsDialog((DataPoint) adapter.getItem(i));
            }
        });
    }

    public void showDetailsDialog(DataPoint item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.hourly_detail_dialog, null));
        DateTime dateTime = new DateTime(item.getTime() * 1000L);
        builder.setTitle(StaticMethods.formattedTime(getActivity(), dateTime, false));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        detailsDialog = builder.create();
        detailsDialog.show();

        TextView summary = (TextView) detailsDialog.findViewById(R.id.summary);
        TextView wind = (TextView) detailsDialog.findViewById(R.id.wind);
        TextView humidity = (TextView) detailsDialog.findViewById(R.id.humidity);
        TextView cloudCover = (TextView) detailsDialog.findViewById(R.id.cloud_cover);
        TextView chance = (TextView) detailsDialog.findViewById(R.id.chance);

        summary.setText(StaticMethods.getWeatherDescription(getActivity(), item));
        wind.setText(String.format(getString(R.string.wind) + " %.1fmps", item.getWindSpeed()));
        chance.setText(String.format(getString(R.string.chance) + " %.0f%%", item.getPrecipProbability() * 100));
        humidity.setText(String.format(getString(R.string.humidity) + " %.0f%%", item.getHumidity() * 100));
        cloudCover.setText(String.format(getString(R.string.cloud_cover) + " %.0f%%", item.getCloudCover() * 100));
    }

    public void hideUI(){
        content.setVisibility(View.GONE);
    }
}
