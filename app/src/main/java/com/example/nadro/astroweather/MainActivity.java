package com.example.nadro.astroweather;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.astrocalculator.*;
import com.example.nadro.astroweather.Fragment.DetailsFragment;
import com.example.nadro.astroweather.Fragment.MoonFragment;
import com.example.nadro.astroweather.Fragment.SunFragment;
import com.example.nadro.astroweather.Fragment.ForecastFragment;
import com.example.nadro.astroweather.Fragment.NextDaysFragment;
import com.example.nadro.astroweather.Fragment.WeatherFragment;
import com.example.nadro.astroweather.Model.CityResult;
import com.example.nadro.astroweather.Model.Weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements DetailsFragment.OnFragmentInteractionListener, WeatherFragment.OnFragmentInteractionListener {

    // instantiate stuff!!!----------------------------------
    private SunFragment sunFragment;
    private MoonFragment moonFragment;
    private NextDaysFragment nextDaysFragment;
    private WeatherFragment weatherFragment;
    private DetailsFragment detailsFragment;

    private TextView longitudeTextView;
    private TextView latitudeTextView;
    private TextView clockTextView;

    //    astro shit
    AstroDateTime astroDateTime;
    AstroCalculator.Location location;
//    --------------------------------------------------------

//    settings stuff & stored preferences
    String longitudeSetting = "19.5";
    String latitudeSetting = "51.5";



    public static String refreshTimeSetting;
    private static String newCitySetting;
    private String selectedCitySetting;
    public static String unitSetting;
    SharedPreferences SP;


    //state
    public static CityResult selectedCity;
    private ArrayList<CityResult> favCityList = new ArrayList<>();
    //network shit
    RequestQueue requestQueue;


//SUPER ADAPTERS--------------------------------------------------------------------------
    //The pager adapter, which provides the pages to the view pager widget.
    SectionsPagerAdapter pagerAdapter;
    //number of pages
    private static final int NUM_PAGES = 5;
    //The pager widget, which handles animation and allows swiping horizontally to access previous and next wizard steps.
    private ViewPager viewPager;

    TextView test;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DO STUFF WITH STUFF XD

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        longitudeTextView = (TextView) findViewById(R.id.lon_text_view);
        latitudeTextView = (TextView) findViewById(R.id.lat_text_view);
        clockTextView = (TextView) findViewById(R.id.clock_text_view);

        test = (TextView) findViewById(R.id.test_text_view);

        latitudeTextView.setText(latitudeSetting);
        longitudeTextView.setText(longitudeSetting);
        setClockTextView();

        astroDateTime = new AstroDateTime();


        // Instantiate a ViewPager and a PagerAdapter.
        viewPager = (ViewPager) findViewById(R.id.portraitViewPager);
        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        if (viewPager != null) {
            setupViewPager(viewPager);
            viewPager.setOffscreenPageLimit(5);
        }

        unitSetting = "c";
        selectedCitySetting = "new york";

        getJsonCity(selectedCitySetting);
    }

    private void setupViewPager(ViewPager viewPager) {
        if (moonFragment == null && sunFragment == null) {

            sunFragment = new SunFragment();
            moonFragment = new MoonFragment();
            weatherFragment = new WeatherFragment();
            detailsFragment = new DetailsFragment();
            nextDaysFragment = new NextDaysFragment();
//            fragment5 = new NextDaysFragment();
            pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            pagerAdapter.addFragment(sunFragment, "SUN");
            pagerAdapter.addFragment(moonFragment, "MOON");
            pagerAdapter.addFragment(weatherFragment, "WEATHER");
            pagerAdapter.addFragment(detailsFragment, "DETAILS");
            pagerAdapter.addFragment(nextDaysFragment, "TEST");
//            pagerAdapter.addFragment(fragment3, getString(nadro.weather.R.string.basic_temp_label));
//            pagerAdapter.addFragment(fragment4, getString(nadro.weather.R.string.additional_temp_label));
//            pagerAdapter.addFragment(fragment5, getString(nadro.weather.R.string.days_temp_label));

            viewPager.setAdapter(pagerAdapter);
            sunFragment = (SunFragment) pagerAdapter.getItem(0);
            moonFragment = (MoonFragment) pagerAdapter.getItem(1);
            weatherFragment = (WeatherFragment) pagerAdapter.getItem(2);
            detailsFragment = (DetailsFragment) pagerAdapter.getItem(3);
            nextDaysFragment = (NextDaysFragment) pagerAdapter.getItem(4);
//            fragment3 = (WeatherFragment) pagerAdapter.getItem(2);
//            Log.d("fragment 3 adapter", pagerAdapter.getItem(2).toString());
//            fragment4 = (MoreWeatherFragment) pagerAdapter.getItem(3);
//            fragment5 = (NextDaysFragment) pagerAdapter.getItem(4);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mainActivity onResume", "calling");

        latitudeSetting = "51.5";
        longitudeSetting = "19.5";
//        latitudeSetting = SP.getString("latitudeSetting", "50"); //szerokosc
//        longitudeSetting = SP.getString("longitudeSetting", "20"); //dlugosc
//        refreshTimeSetting = SP.getString("refreshTimeSetting", "10");
//        newCitySetting = SP.getString("newCitySetting", "");
//        Log.d("newCity", newCitySetting);

//        if (!newCitySetting.equals(selectedCitySetting)) {
//            getCity(newCitySetting);
//        }
//        unitSetting = SP.getString("unitSetting", "c");
//        latitudeTextView.setText(getResources().getString(R.string.la, latitudeSetting));
//        longitudeTextView.setText(getResources().getString(R.string.long_textField, longitudeSetting));
        latitudeTextView.setText(latitudeSetting);
        longitudeTextView.setText(/*String.valueOf(longitudeSetting)*/longitudeSetting);

        setUpAstroDateTime(astroDateTime);

//        Log.d("New city setting", newCitySetting);
//        if(selectedCity != null)
//            handleCitySelection(selectedCity);
    }

    public com.astrocalculator.AstroCalculator.Location getLocation() {
        return new AstroCalculator.Location(Double.valueOf(this.getLatitudeSetting()),
                Double.valueOf(this.getLongitudeSetting()));
    }

    public Weather getWeather(){
        return this.selectedCity.getWeather();
    }

    public String getLatitudeSetting() {
        return latitudeSetting;
    }

    public String getLongitudeSetting() {
        return longitudeSetting;
    }

    public AstroDateTime getAstroDateTime() {
        return new AstroDateTime();
    }

    public void setUpAstroDateTime(AstroDateTime astroDateTime) {
        Calendar calendar = Calendar.getInstance();
        astroDateTime.setYear(calendar.get(Calendar.YEAR));
        astroDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        astroDateTime.setMonth(calendar.get(Calendar.MONTH) + 1);  // indexed from 0!!!
        astroDateTime.setHour(calendar.get(Calendar.HOUR));
        astroDateTime.setMinute(calendar.get(Calendar.MINUTE));
        astroDateTime.setSecond(calendar.get(Calendar.SECOND));

        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        boolean dlt = tz.inDaylightTime(now);
        if (dlt) {
            astroDateTime.setDaylightSaving(true);
            astroDateTime.setTimezoneOffset(calendar.get(Calendar.ZONE_OFFSET) / (60 * 60 * 1000));
        } else {
            astroDateTime.setDaylightSaving(false);
            astroDateTime.setTimezoneOffset(calendar.get(Calendar.ZONE_OFFSET) / (60 * 60 * 1000));
        }
    }

    private void getJsonCity(final String cityName){
        YahooWeather.getJsonCity(cityName, requestQueue, new YahooWeather.WeatherClientListener() {
            @Override
            public void onCityResponse(CityResult city) {
                if (city.getCityName() != null) {
                    selectedCity = city;
                    getJsonWeather(city);
                    Log.d("c", city.toString());
                } else {
                    Toast.makeText(MainActivity.this, "Bledne miasto",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onWeatherResponse(Weather weather) {
            }

            @Override
            public void onImageReady(Bitmap image) {
            }
        });
    }

    private void getJsonWeather(final CityResult city) {
        String woeid = city.getWoeid();
        if (woeid != null) {
            YahooWeather.getJsonWeather(woeid, unitSetting, requestQueue, new YahooWeather.WeatherClientListener() {
                @Override
                public void onCityResponse(CityResult city) {
                }

                @Override
                public void onWeatherResponse(Weather weather) {
                    selectedCity = city;
                    selectedCity.setWeather(weather);

                    String s1 = selectedCity.getWeather().condition.date;
                    String s2 = String.valueOf(selectedCity.getWeather().atmosphere.humidity);
                    String s3 = String.valueOf(selectedCity.getWeather().wind.speed);


                    Log.d("Can we display this?", s1 + " " + s2 + " " + s3);
                    test.setText(selectedCity.getWeather().condition.temp + " " + " " + selectedCity.getCityName());

                    weatherFragment.fillWeatherView(selectedCity);
                    detailsFragment.fillDetailsView(selectedCity);
                    nextDaysFragment.updateListValues();

                    getImage();

                }

                @Override
                public void onImageReady(Bitmap image) {
                }
            });
        }else{
            Log.d("getWeather", "woeid is null bro");
        }
    }

    private void getImage() {
        YahooWeather.getImage(selectedCity.getWeather().condition.code, requestQueue, new YahooWeather.WeatherClientListener() {
            @Override
            public void onCityResponse(CityResult city) {
            }

            @Override
            public void onWeatherResponse(Weather weather) {
            }

            @Override
            public void onImageReady(Bitmap image) {
                weatherFragment.setImage(image);
            }
        });
    }

    private void setClockTextView() {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        Timer timer = new Timer("Clock timer");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                final String currTime = sdf.format(new Date());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clockTextView.setText(currTime);
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onWeatherFragmentInteraction(String string){
        //do something someday
        Log.d("MainActivity","onWeatherFragmentInteraction");
    }

    @Override
    public void onDetailsFragmentInteraction(String string){
        //do something someday
        Log.d("MainActivity","onDetailsFragmentInteraction");
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}





