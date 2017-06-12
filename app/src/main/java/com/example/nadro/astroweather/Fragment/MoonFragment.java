package com.example.nadro.astroweather.Fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.nadro.astroweather.AstroInfo;
import com.example.nadro.astroweather.AstroInfoCalculator;
import com.example.nadro.astroweather.MainActivity;
import com.example.nadro.astroweather.R;
import com.example.nadro.astroweather.Fragment.dummy.DummyContent;
import com.example.nadro.astroweather.Fragment.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MoonFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    //shit got crazy xxxxxxxxxxxxxx
    MainActivity mainActivity;

    //location and datetime needed for astro info calculation
    AstroCalculator.Location location;
    AstroDateTime astroDateTime;

    //this we want to pass to adapter
    public List<AstroInfo> moonInfoList;
    //resources to get labels
    private Resources moonResources;
    private String[] moonLabels;
    //moonInfo values
    private List<String> moonValues;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MoonFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MoonFragment newInstance(int columnCount) {
        MoonFragment fragment = new MoonFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moon_list, container, false);

        //get labels
        moonResources = getResources();
        moonLabels = moonResources.getStringArray(R.array.moon_items);
        moonValues = getMoonValues();
        moonInfoList = setMoonList(moonLabels);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
            recyclerView.setAdapter(new MyMoonRecyclerViewAdapter(moonInfoList, mListener));
        }
        return view;
    }

    //sets labels from resources and calculated values
    public List<AstroInfo> setMoonList (String[] moonLabels) {
        AstroInfo astroInfo;
        List<AstroInfo> moonInfoList = new ArrayList<>();
        for (int i=0; i < 6; i++) {
            astroInfo = new AstroInfo();
            astroInfo.setLabel(moonLabels[i]);
            astroInfo.setValue(moonValues.get(i).toString());
            moonInfoList.add(astroInfo);
        }

        return moonInfoList;
    }

    public List<String> getMoonValues() {
        location = mainActivity.getLocation();
        astroDateTime = mainActivity.getAstroDateTime();
        mainActivity.setUpAstroDateTime(astroDateTime);
        return new AstroInfoCalculator(location, astroDateTime).getMoonInfoList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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