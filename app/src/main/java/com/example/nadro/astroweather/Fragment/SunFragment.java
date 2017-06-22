package com.example.nadro.astroweather.Fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.nadro.astroweather.AstroInfo;
import com.example.nadro.astroweather.AstroInfoCalculator;
import com.example.nadro.astroweather.MainActivity;
import com.example.nadro.astroweather.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SunFragment extends Fragment {

//    // TODO: Customize parameter argument names
//    private static final String ARG_COLUMN_COUNT = "column-count";
//    // TODO: Customize parameters
//    private int mColumnCount = 1;

    private OnListFragmentInteractionListener mListener;

    //shit got crazy xxxxxxxxxxxxxx
    MainActivity mainActivity;

    //location and datetime needed for astro info calculation
    AstroCalculator.Location location;
    AstroDateTime astroDateTime;

    //this we want to pass to adapter
    public List<AstroInfo> sunInfoList;
    //resources to get labels
    private Resources sunResources;
    private String[] sunLabels;
    //sunInfo values
    private List<String> sunValues;

    Timer updateListTimer = null;
    TimerTask updateValuesTask;

    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SunFragment() {
    }

//     TODO: Customize parameter initialization
//    @SuppressWarnings("unused")
//    public static SunFragment newInstance(int columnCount) {
//        SunFragment fragment = new SunFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SunFragment", "onCreate");

//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
        sunResources = getResources();
        sunLabels = sunResources.getStringArray(R.array.sun_items);
        sunValues = getSunValues();
        sunInfoList = setSunList(sunLabels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("SunFragment", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_sun_list, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
            recyclerView.setAdapter(new MySunRecyclerViewAdapter(sunInfoList, mListener));
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("SunFragment", "onPause");
//        stopTimerTask();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("SunFragment", "onResume");
        mainActivity.updateLocation();
        sunValues = getSunValues();
        sunInfoList= setSunList(sunLabels);
        recyclerView.setAdapter(new MySunRecyclerViewAdapter(sunInfoList, mListener));

//        if (updateListTimer == null) {
//            startTimer();
//        }
    }

    public void startTimer() {
        Log.d("start timer","calling");

        updateListTimer = new Timer("sunListTimer");

        updateSunMoonValues();

        Log.d("refresh time:", MainActivity.refreshTimeSetting);
        updateListTimer.schedule(updateValuesTask,0, Long.parseLong(MainActivity.refreshTimeSetting)*1000);
    }

    public void stopTimerTask() {
        if (updateListTimer != null) {
            Log.d("Sun onPause del timer", "calling");
            updateListTimer.cancel();
            updateListTimer.purge();
            updateListTimer = null;
            updateValuesTask.cancel();
        }
    }

    //sets labels from resources and calculated values
    public List<AstroInfo> setSunList (String[] sunLabels) {
        Log.d("SunFragment", "setSunInfoList");
        AstroInfo astroInfo;
        List<AstroInfo> sunInfoList = new ArrayList<>();
        for (int i=0; i < 6; i++) {
            astroInfo = new AstroInfo();
            astroInfo.setLabel(sunLabels[i]);
            astroInfo.setValue(sunValues.get(i).toString());
            sunInfoList.add(astroInfo);
        }

        return sunInfoList;
    }

    public List<String> getSunValues() {
        Log.d("SunFragment", "getSunValues");
        location = mainActivity.getLocation();
        astroDateTime = mainActivity.getAstroDateTime();
        mainActivity.setUpAstroDateTime(astroDateTime);
        return new AstroInfoCalculator(location, astroDateTime).getSunInfoList();
    }

    public void updateSunMoonValues() {
        Log.d("SunFragment", "updateValues");
        updateValuesTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("moon timer", "start");
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i=0; i < 6; i++) {
                            sunValues = getSunValues();
                            sunInfoList.get(i).setValue(sunValues.get(i).toString());
                        }
//                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("SunFragment", "onAttach");
        mainActivity = (MainActivity) context;
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("SunFragment", "onDetach");
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(AstroInfo item);
    }
}
