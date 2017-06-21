package com.example.nadro.astroweather.Fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nadro.astroweather.MainActivity;
import com.example.nadro.astroweather.Model.Weather;
import com.example.nadro.astroweather.R;

import java.util.List;


public class NextDaysListAdapter extends ArrayAdapter<Weather.NextDays> {
    TextView dayTextView;
    TextView dateTextView;
    TextView descTextView;
    TextView tempTextView;
    ImageView imageView;

    public NextDaysListAdapter(Context context, List<Weather.NextDays> values) {
        super(context, R.layout.days_list_item, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.days_list_item, parent, false);
        }

        dayTextView = (TextView) convertView.findViewById(R.id.list_item_day);
        dateTextView = (TextView) convertView.findViewById(R.id.list_item_date);
        descTextView = (TextView) convertView.findViewById(R.id.list_item_desc);
        tempTextView = (TextView) convertView.findViewById(R.id.list_item_temp);
        imageView = (ImageView) convertView.findViewById(R.id.temp_imageView);

        final Weather.NextDays item = getItem(position);
        setTextItem(item);

        return convertView;
    }

    public void setTextItem(Weather.NextDays item) {
        dayTextView.setText(String.valueOf(item.day));
        dateTextView.setText(String.valueOf(item.date));
        descTextView.setText(String.valueOf(item.description));
        tempTextView.setText(String.valueOf(item.high) + (char) 0x00B0 + MainActivity.unitSetting);
        if (imageView != null) {
//            imageView.setBackgroundResource(item.getIconId());
        }
    }
}
