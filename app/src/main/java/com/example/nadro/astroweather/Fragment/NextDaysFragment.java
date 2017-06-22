package com.example.nadro.astroweather.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.nadro.astroweather.MainActivity;
import com.example.nadro.astroweather.R;


public class NextDaysFragment extends Fragment {
    MainActivity mainActivity;
    private ListView daysListView;
    private NextDaysListAdapter adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (MainActivity.currentCity != null) {
            adapter = new NextDaysListAdapter(mainActivity, MainActivity.currentCity.getWeather().nextDaysList);
            daysListView.setAdapter(adapter);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("NextDaysFragment", "onAttach");

        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_days, container, false);
        daysListView = (ListView) rootView.findViewById(R.id.days_listView);
        Log.d("NextDaysFragment", "onCreateView");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("NextDaysFragment", "onResume");
    }

    public void updateListValues() {

        if (MainActivity.currentCity != null) {
            adapter = new NextDaysListAdapter(mainActivity, MainActivity.currentCity.getWeather().nextDaysList);
            daysListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

}
