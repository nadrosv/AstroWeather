package com.example.nadro.astroweather.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nadro.astroweather.MainActivity;
import com.example.nadro.astroweather.Model.CityResult;
import com.example.nadro.astroweather.Model.Weather;
import com.example.nadro.astroweather.R;
import com.example.nadro.astroweather.YahooWeather;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeatherFragment} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    final String DEGREE  = "\u00b0";

    MainActivity mainActivity;


//    CityResult cRes;

//    private String cityName;
//    private String cityCords;
//    private Integer cityTemperature;
//    private Float cityPressure;


    private TextView cityNameTextView;
    private TextView cityCordsTextView;
    private TextView cityDateTextView;
    private TextView cityTemperatureTextView;
    private TextView cityPressureTextView;
    private ImageView cityWeatherIcon;
    private Button newCityButton;
    private EditText newCityText;

    OnCitySelectedListener csListener;

//    private BitmapDrawable testImage;



    private OnFragmentInteractionListener mListener;

    public WeatherFragment() {
        // Required empty public constructor
    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment WeatherFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static WeatherFragment newInstance(String param1, String param2) {
//        WeatherFragment fragment = new WeatherFragment();
//        Bundle args = new Bundle();
////        args.putString(ARG_PARAM1, param1);
////        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("WeatherFragment", "onCreate");
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("WeatherFragment", "onCreateView");
        final View view = inflater.inflate(R.layout.fragment_weather, container, false);

        cityNameTextView = (TextView) view.findViewById(R.id.city_name);
        cityCordsTextView = (TextView) view.findViewById(R.id.city_cord);
        cityDateTextView = (TextView) view.findViewById(R.id.city_date);
        cityTemperatureTextView = (TextView) view.findViewById(R.id.city_temperature);
        cityPressureTextView = (TextView) view.findViewById(R.id.city_pressure);
        cityWeatherIcon = (ImageView) view.findViewById(R.id.city_weather);

        newCityButton = (Button) view.findViewById(R.id.new_city_button);
        newCityText = (EditText) view.findViewById(R.id.new_city_text);

        newCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("WeatherFragment", newCityText.getText().toString());
                csListener.onCitySelected(newCityText.getText().toString());
                newCityText.setText("");
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (MainActivity.currentCity != null) {
            mainActivity.getImage();
            fillWeatherView(MainActivity.currentCity);
        }


        
        Log.d("WeatherFragment", "onResume");

    }

//    public void populateTempTextViews(CityResult city, Weather weather) {
//        Log.d("cond", String.valueOf(weather.condition.temp + (char) 0x00B0) + MainActivity.unitSetting);
//        tempTextView.setText(String.valueOf(weather.condition.temp) + (char) 0x00B0 + MainActivity.unitSetting);
//        descTextView.setText(String.valueOf(weather.condition.description));
//        if (weather.atmosphere.pressure > 10000)
//            weather.atmosphere.pressure /= 33.8639;
//        if (MainActivity.unitSetting.equals("c"))
//            pressureTextView.setText("Pressure   " + String.valueOf(weather.atmosphere.pressure) + "mb");
//        else
//            pressureTextView.setText(String.valueOf(weather.atmosphere.pressure) + "in");
//
//        cityTextView.setText(String.valueOf(city.getCityName()));
//        timeTextView.setText(String.valueOf(weather.lastUpdate));
//
//    }


    public void fillWeatherView(CityResult city){
        if(city.getWoeid() != null) {
            Weather weather = city.getWeather();
            cityNameTextView.setText(city.getCityName() + ", " + city.getCountry());
            cityCordsTextView.setText(city.getCoordinates());
            cityDateTextView.setText(weather.condition.date);
            cityTemperatureTextView.setText(weather.condition.temp + DEGREE + weather.units.temperature);
            cityPressureTextView.setText(String.format("%.1f", weather.atmosphere.pressure) + " hPa");
        } else{
            Log.d("WeatherFragment", "fillWeatherView: woeid is null");
        }




//    public CityResult getWeatherInfo(String cName){
//        YahooWeather.makeJsonObjectRequest(cName, new YahooWeather.VolleyCallback() {
//            @Override
//            public void onSuccess(CityResult result) {
//                cRes = result;
//            }
//        });
//        return cRes;
////        cityName = cRes.getCityName(); //"Warszawa";
////        cityCords = cRes.getCountry(); //"fake 51N19E";
////        cityTemperature = cRes.getWeather().condition.temp;//20f;
////        cityPressure = cRes.getWeather().atmosphere.pressure; //1000f;
//    }
    }


//    public CityResult getWeatherInfo(String cName){
//        YahooWeather.makeJsonObjectRequest(cName, new YahooWeather.VolleyCallback() {
//            @Override
//            public void onSuccess(CityResult result) {
//                cRes = result;
//            }
//        });
//        return cRes;
////        cityName = cRes.getCityName(); //"Warszawa";
////        cityCords = cRes.getCountry(); //"fake 51N19E";
////        cityTemperature = cRes.getWeather().condition.temp;//20f;
////        cityPressure = cRes.getWeather().atmosphere.pressure; //1000f;
//    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
//        if (context instanceof OnFragmentInteractionListener) {
        try {
            csListener = (WeatherFragment.OnCitySelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
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
        void onWeatherFragmentInteraction(String string);
    }

    public interface OnCitySelectedListener {
        void onCitySelected(String city);
    }

    public void setImage(Bitmap image) {
        cityWeatherIcon.setImageBitmap(image);
    }
}
