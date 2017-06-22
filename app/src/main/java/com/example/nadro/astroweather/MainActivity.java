package com.example.nadro.astroweather;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.astrocalculator.*;
import com.example.nadro.astroweather.Fragment.DetailsFragment;
import com.example.nadro.astroweather.Fragment.MoonFragment;
import com.example.nadro.astroweather.Fragment.SunFragment;
import com.example.nadro.astroweather.Fragment.NextDaysFragment;
import com.example.nadro.astroweather.Fragment.WeatherFragment;
import com.example.nadro.astroweather.Model.CityResult;
import com.example.nadro.astroweather.Model.Weather;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements DetailsFragment.OnFragmentInteractionListener, WeatherFragment.OnFragmentInteractionListener, WeatherFragment.OnCitySelectedListener {


    SharedPreferences SP;

    private static String newCitySetting;
    private String selectedCitySetting;

    public static String refreshTimeSetting;
    public static String unitSetting;
    public static String latitudeSetting;
    public static String longitudeSetting;


    // ASTRO STUFF -------------------------------
    AstroDateTime astroDateTime;
    AstroCalculator.Location location;

    // STATE -------------------------------------
    public static CityResult currentCity;
    public static CityResult newCity;
    private ArrayList<CityResult> favCityList = new ArrayList<>();
    RequestQueue requestQueue;


    // VIEW --------------------------------------
    //The pager adapter, which provides the pages to the view pager widget.
    SectionsPagerAdapter pagerAdapter;
    //number of pages
    private static final int NUM_PAGES = 5;
    //The pager widget, which handles animation and allows swiping horizontally to access previous and next wizard steps.
    private ViewPager viewPager;

    private SunFragment sunFragment;
    private MoonFragment moonFragment;
    private NextDaysFragment nextDaysFragment;
    private WeatherFragment weatherFragment;
    private DetailsFragment detailsFragment;

    private TextView longitudeTextView;
    private TextView latitudeTextView;
    private TextView clockTextView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate");
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        refreshTimeSetting = SP.getString("refreshTimeSetting", "60");
        latitudeSetting = SP.getString("latitudeSetting", "0"); //szerokosc
        longitudeSetting = SP.getString("longitudeSetting", "0"); //dlugosc
//        newCitySetting = SP.getString("newCitySetting", "");
        unitSetting = SP.getString("unitSetting", "c");
        selectedCitySetting = SP.getString("selectedCitySetting","");
        location = this.getLocation();
        astroDateTime = this.getAstroDateTime();

        longitudeTextView = (TextView) findViewById(R.id.lon_text_view);
        latitudeTextView = (TextView) findViewById(R.id.lat_text_view);
        clockTextView = (TextView) findViewById(R.id.clock_text_view);

//        ??
//        Toolbar toolbar = (Toolbar) findViewById(nadro.weather.R.id.toolbar);
//        setSupportActionBar(toolbar);

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

//            !!!!!
//            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//            if (tabLayout != null) {
//                tabLayout.setupWithViewPager(viewPager);
//            }
        }

        if(!selectedCitySetting.equals("")) {
            if (getObjectsFromFiles()) {
                for (CityResult c : favCityList) {

                    if (c.getCityName().equals(selectedCitySetting)) {
                        currentCity = c;
                    }
                }
                Calendar calendar = Calendar.getInstance();
                if((calendar.getTimeInMillis() - currentCity.getWeather().lastUpdate) > 3600000){
                    Log.d("Main", "weather out of date - refresh");
                    refreshWeather();
                }

//                refreshWeather();
//                updateWeatherViews(currentCity);
            }
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        Log.d("MainActivity", "setupViewPager: calling");
        if (moonFragment == null || sunFragment == null || weatherFragment == null
                || detailsFragment == null || nextDaysFragment == null) {
            Log.d("MainActivity", "setupViewPager: setupFragments");
            sunFragment = new SunFragment();
            moonFragment = new MoonFragment();
            weatherFragment = new WeatherFragment();
            detailsFragment = new DetailsFragment();
            nextDaysFragment = new NextDaysFragment();

            pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            pagerAdapter.addFragment(sunFragment, "SUN");
            pagerAdapter.addFragment(moonFragment, "MOON");
            pagerAdapter.addFragment(weatherFragment, "WEATHER");
            pagerAdapter.addFragment(detailsFragment, "DETAILS");
            pagerAdapter.addFragment(nextDaysFragment, "TEST");

            viewPager.setAdapter(pagerAdapter);
            sunFragment = (SunFragment) pagerAdapter.getItem(0);
            moonFragment = (MoonFragment) pagerAdapter.getItem(1);
            weatherFragment = (WeatherFragment) pagerAdapter.getItem(2);
            detailsFragment = (DetailsFragment) pagerAdapter.getItem(3);
            nextDaysFragment = (NextDaysFragment) pagerAdapter.getItem(4);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");

        latitudeSetting = SP.getString("latitudeSetting", "0"); //szerokosc
        longitudeSetting = SP.getString("longitudeSetting", "0"); //dlugosc
        refreshTimeSetting = SP.getString("refreshTimeSetting", "10000");
        unitSetting = SP.getString("unitSetting", "c");


        latitudeTextView.setText(latitudeSetting);
        longitudeTextView.setText(longitudeSetting);

//        if (!newCitySetting.equals(selectedCitySetting)) {
//            getJsonCity(newCitySetting);
//        }

        setUpAstroDateTime(astroDateTime);


//        if(currentCity != null) {
//            updateWeatherViews(currentCity);
//        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onPause");
        if (currentCity != null) {
            SharedPreferences.Editor editor = SP.edit();
            editor.putString("selectedCitySetting", String.valueOf(currentCity.getCityName()));
            editor.apply();
        }

    }

    public com.astrocalculator.AstroCalculator.Location getLocation() {
        return new AstroCalculator.Location(Double.valueOf(this.getLatitudeSetting()),
                Double.valueOf(this.getLongitudeSetting()));
    }

    public Weather getWeather(){
        return this.currentCity.getWeather();
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
        Log.d("MainActivity", "getCity");
        YahooWeather.getJsonCity(cityName, requestQueue, new YahooWeather.WeatherClientListener() {
            @Override
            public void onCityResponse(CityResult city) {
                if (city.getCityName() != null) {

                  if(!Helpers.containsCity(favCityList, city.getWoeid())){
                      currentCity = city;
                      getJsonWeather(currentCity, true);

                  }else{
                      updateWeatherViews(currentCity);
                  }


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

    private void getJsonWeather(final CityResult city, final boolean refreshViews) {
        Log.d("MainActivity", "getWeather");
        String woeid = city.getWoeid();
        if (woeid != null) {
            YahooWeather.getJsonWeather(woeid, unitSetting, requestQueue, new YahooWeather.WeatherClientListener() {
                @Override
                public void onCityResponse(CityResult city) {
                }

                @Override
                public void onWeatherResponse(Weather weather) {

//                    city.setWeather(weather);
//                    favCityList.add(city);

                    currentCity = city;
                    currentCity.setWeather(weather);
                    favCityList.add(currentCity);

                    getImage();
                    if(refreshViews) {
                        updateWeatherViews(currentCity);
                    }

//                    String s1 = currentCity.getWeather().condition.date;
//                    String s2 = String.valueOf(currentCity.getWeather().atmosphere.humidity);
//                    String s3 = String.valueOf(currentCity.getWeather().wind.speed);
//
//                    Log.d("Can we display this?", s1 + " " + s2 + " " + s3);

                    try {
                        Helpers.saveToFile("city_" + city.getCityName()+ ".txt", city, MainActivity.this);
                        Log.d("getWeather", "City saved to txt file: " + currentCity.getCityName());

                    } catch (IOException e) {
                        Log.d("getWeather", "City save failed");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onImageReady(Bitmap image) {
                }
            });
        }else{
            Log.d("getWeather", "woeid is null bro");
        }
    }

    public void refreshWeather() {
        Log.d("MainActivity", "refreshWeather");
        if (Helpers.isNetworkAvailable(this)){
            Log.d("reading from web", "OK");

            String tempWoeid = currentCity.getWoeid();
            ArrayList<CityResult> temp = favCityList;
            favCityList = new ArrayList<>();

            for (CityResult c : temp) {
                getJsonWeather(c, false);
                if(c.getWoeid().equals(tempWoeid)){
                    newCity = c;
                }
            }

            updateWeatherViews(newCity);

            Toast.makeText(this, "Weather updated :>",
                    Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "No internet connection :<",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void getImage() {
        Log.d("MainActivity", "getImage");
        YahooWeather.getImage(currentCity.getWeather().condition.code, requestQueue, new YahooWeather.WeatherClientListener() {
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
        Log.d("MainActivity", "setClockTextView");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("MainActivity", "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MainActivity", "optionItemSelected" + " : " + item.getItemId());
        switch (item.getItemId()) {

            case R.id.refresh_info_button:
                if (favCityList.size() > 0) {
                    refreshWeather();
                    updateWeatherViews(currentCity);
                } else {
                    Toast.makeText(this, "There's nothing to reload ;>", Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.favourite_cities_button:
                initCityDialog();
                return true;

            case R.id.settings_button:
                Intent settingsIntent = new Intent(this, Preferences.class);
                startActivity(settingsIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCitySelected(String city) {
        Log.d("MainActivity", "onCitySelected");
        if(!city.isEmpty()){
            if (Helpers.isNetworkAvailable(this)) {
                getJsonCity(city);
            }
            else{
                Toast.makeText(this, "No internet connection",
                        Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Enter valid city name >:[",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void initCityDialog() {
        final String[] cityNames = new String[favCityList.size()];

        for (int i = 0; i < favCityList.size(); i++) {
            cityNames[i] = favCityList.get(i).getCityName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose city");

        builder.setItems(cityNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                currentCity = favCityList.get(position);
                getImage();
                updateWeatherViews(currentCity);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void updateLocation() {
        Log.d("MainActivity", "updateLocation");
        location.setLatitude(Double.parseDouble(this.getLatitudeSetting()));
        location.setLongitude(Double.parseDouble(this.getLongitudeSetting()));
    }

    public void updateWeatherViews(CityResult selectedCity){
        Log.d("MainActivity", "updateWeatherViews");
        weatherFragment.fillWeatherView(selectedCity);
        detailsFragment.fillDetailsView(selectedCity);
        nextDaysFragment.updateListValues();
    }

    private boolean getObjectsFromFiles() {
        Log.d("MainActivity", "getObjectFromFiles");

        String[] savedFiles = fileList();
        Log.d("getObjectFromFiles", "saved files: " + String.valueOf(savedFiles.length));

        if (savedFiles.length == 0)
            return false;
        for (String savedFile : savedFiles) {
            Log.d("Zapisany plik", savedFile);
            try {
                if (savedFile.contains("city")) {
                    favCityList.add((CityResult) Helpers.loadFromFile(savedFile, MainActivity.this));
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        Log.d("favCity length", String.valueOf(favCityList.size()));
        Toast.makeText(this, "Weather info may be out of date",
                Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d("MainActivity", "onBackPressed");
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

