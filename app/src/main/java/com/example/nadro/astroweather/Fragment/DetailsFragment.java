package com.example.nadro.astroweather.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nadro.astroweather.MainActivity;
import com.example.nadro.astroweather.Model.CityResult;
import com.example.nadro.astroweather.Model.Weather;
import com.example.nadro.astroweather.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {


//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    MainActivity mainActivity;

    private TextView windSpeedTextView;
    private TextView windDirectionTextView;
    private TextView humidityTextView;
    private TextView visibilityTextView;

    private OnFragmentInteractionListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment DetailsFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static DetailsFragment newInstance(String param1, String param2) {
//        DetailsFragment fragment = new DetailsFragment();
//        Bundle args = new Bundle();
////        args.putString(ARG_PARAM1, param1);
////        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        windSpeedTextView = (TextView) view.findViewById(R.id.wind_speed);
        windDirectionTextView = (TextView) view.findViewById(R.id.wind_direction);
        humidityTextView = (TextView) view.findViewById(R.id.humidity_value);
        visibilityTextView = (TextView) view.findViewById(R.id.visibility_value);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (MainActivity.currentCity != null) {
            fillDetailsView(MainActivity.currentCity);
        }

        Log.d("DetailsFragment", "onResume");

    }

    public void fillDetailsView(CityResult city){
        if(city.getWoeid() != null) {
            Weather weather = city.getWeather();
            windSpeedTextView.setText(weather.wind.speed + " " + weather.units.speed);
            windDirectionTextView.setText(interpretWindDirection(weather.wind.direction));
            humidityTextView.setText(weather.atmosphere.humidity + " %");
            visibilityTextView.setText(weather.atmosphere.visibility + " " + weather.units.distance);
        } else{
            Log.d("DetailsFragment", "fillDetailsView: woeid is null");
        }

    }

    private String interpretWindDirection(int direction){
        int val = (int)(direction / 22.5);
        String dirString;
        switch(val){
            case 0:
                dirString = "N"; break;
            case 1:
            case 2:
                dirString = "NE"; break;
            case 3:
            case 4:
                dirString = "E"; break;
            case 5:
            case 6:
                dirString = "SE"; break;
            case 7:
            case 8:
                dirString = "S"; break;
            case 9:
            case 10:
                dirString = "SW"; break;
            case 11:
            case 12:
                dirString = "W"; break;
            case 13:
            case 14:
                dirString = "NW"; break;
            case 15:
                dirString = "N"; break;
            default:
                dirString = "IDK"; break;
        }
        return dirString;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDetailsFragmentInteraction(String string);
    }
}
