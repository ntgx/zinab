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
import com.nati.zinab.models.DataBlock;
import com.nati.zinab.models.DataPoint;

import java.util.List;

/**
 * Created by Nati on 8/29/2015.
 */
public class DailyAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private Context context;
    private List<DataPoint> items;

    public DailyAdapter(Context context, List<DataPoint> items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        public TextView icon, day, summary, temp;

        public ViewHolder(View view) {
            icon = (TextView) view.findViewById(R.id.weather_icon);
            day = (TextView) view.findViewById(R.id.day);
            summary = (TextView) view.findViewById(R.id.summary);
            temp = (TextView) view.findViewById(R.id.temp);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;
        if (view == null) {

            view = inflater.inflate(R.layout.daily_list_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        DataPoint item = items.get(position);

        holder.day.setText("Mon");
        holder.icon.setText("{wi_day_cloudy}");
        holder.summary.setText("Partly Bla");
        //holder.temp.setText("");

        return view;
    }

    public DataPoint getLeaderboardItem(int position) {
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
