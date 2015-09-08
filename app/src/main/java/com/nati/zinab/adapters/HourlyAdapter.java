package com.nati.zinab.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nati.zinab.R;
import com.nati.zinab.fragments.DailyFragment;
import com.nati.zinab.helpers.StaticMethods;
import com.nati.zinab.models.DataBlock;
import com.nati.zinab.models.DataPoint;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nati on 8/29/2015.
 */
public class HourlyAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private Context context;
    private List<DataPoint> items;

    public HourlyAdapter(Context context, List<DataPoint> items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        @Bind(R.id.icon) public TextView icon;
        @Bind(R.id.day) public TextView day;
        @Bind(R.id.temp) public TextView temp;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;
        if (view == null) {

            view = inflater.inflate(R.layout.hourly_list_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        DataPoint item = items.get(position);
        DateTime dateTime = new DateTime(item.getTime() * 1000L);

        DateTimeFormatter fmt = DateTimeFormat.forPattern("E ha");
        String date = fmt.print(dateTime);
        holder.day.setText(date);

        StaticMethods.setupWeatherIcon(item.getIcon(), holder.icon);
        holder.temp.setText(String.format("%.0f°", item.getTemperature()));

        return view;
    }

    public DataPoint getDataPoint(int position) {
        return ((DataPoint) getItem(position));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    public List<DataPoint> getItems() {
        return items;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
