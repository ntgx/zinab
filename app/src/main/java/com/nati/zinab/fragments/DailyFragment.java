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

import com.nati.zinab.R;
import com.nati.zinab.activities.MainActivity;
import com.nati.zinab.adapters.DailyAdapter;
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
public class DailyFragment extends Fragment {

    @Bind(R.id.listView) ListView listView;
    @Bind(R.id.content) RelativeLayout content;
    private AlertDialog detailsDialog;

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
        ButterKnife.bind(this, view);

        return view;
    }

    public void setupUI(WeatherResponse weatherResponse) {
        content.setVisibility(View.VISIBLE);
        final DailyAdapter adapter = new DailyAdapter(getActivity(), weatherResponse.getDaily().getData());
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
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.day_detail_dialog, null));
        DateTime dateTime = new DateTime(item.getTime() * 1000L);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("EEEE");
        builder.setTitle(fmt.print(dateTime));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        detailsDialog = builder.create();
        detailsDialog.show();

        TextView summary = (TextView) detailsDialog.findViewById(R.id.summary);
        TextView sunrise = (TextView) detailsDialog.findViewById(R.id.sunrise);
        TextView sunset = (TextView) detailsDialog.findViewById(R.id.sunset);
        TextView low = (TextView) detailsDialog.findViewById(R.id.low_temp);
        TextView high = (TextView) detailsDialog.findViewById(R.id.high_temp);
        TextView chance = (TextView) detailsDialog.findViewById(R.id.chance);

        summary.setText(item.getSummary());
        chance.setText(String.format(getString(R.string.chance) + " %.0f%%", item.getPrecipProbability() * 100));
        low.setText(String.format(getString(R.string.low) + " %.0f" + Constants.DEGREE, item.getTemperatureMin()));
        high.setText(String.format(getString(R.string.high) + " %.0f" + Constants.DEGREE, item.getTemperatureMax()));

        DateTime sunsetDateTime = new DateTime(item.getSunsetTime() * 1000L);
        sunset.setText(getString(R.string.sunset) + " " + StaticMethods.formattedTime(getActivity(), sunsetDateTime, true));

        DateTime sunriseDateTime = new DateTime(item.getSunriseTime() * 1000L);
        sunrise.setText(getString(R.string.sunrise) + " " + StaticMethods.formattedTime(getActivity(), sunriseDateTime, true));
    }

    public void hideUI(){
        content.setVisibility(View.GONE);
    }
}
