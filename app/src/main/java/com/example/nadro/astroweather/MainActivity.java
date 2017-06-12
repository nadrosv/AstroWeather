package com.example.nadro.astroweather;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.astrocalculator.*;
import com.example.nadro.astroweather.Fragment.DetailsFragment;
import com.example.nadro.astroweather.Fragment.MoonFragment;
import com.example.nadro.astroweather.Fragment.SunFragment;
import com.example.nadro.astroweather.Fragment.ForecastFragment;
import com.example.nadro.astroweather.Fragment.WeatherFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements DetailsFragment.OnFragmentInteractionListener, WeatherFragment.OnFragmentInteractionListener {

    // instantiate stuff!!!
    private SunFragment sunFragment;
    private MoonFragment moonFragment;
    private ForecastFragment forecastFragment;
    private WeatherFragment weatherFragment;
    private DetailsFragment detailsFragment;

    private TextView longitudeTextView;
    private TextView latitudeTextView;
    private TextView clockTextView;

//    settings stuff & stored preferences
    String longitudeSetting = "19.5";
    String latitudeSetting = "51.5";

//    astro shit
    AstroDateTime astroDateTimeTime;
    AstroCalculator.Location location;

    //The pager adapter, which provides the pages to the view pager widget.
    SectionsPagerAdapter pagerAdapter;

    //number of pages
    private static final int NUM_PAGES = 5;

    //The pager widget, which handles animation and allows swiping horizontally to access previous and next wizard steps.
    private ViewPager viewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DO STUFF WITH STUFF XD

        longitudeTextView = (TextView) findViewById(R.id.lonTextView);
        latitudeTextView = (TextView) findViewById(R.id.latTextView);
        clockTextView = (TextView) findViewById(R.id.clockTextView);

        latitudeTextView.setText(latitudeSetting);
        longitudeTextView.setText(/*String.valueOf(longitudeSetting)*/longitudeSetting);
        setClockTextView();


        // Instantiate a ViewPager and a PagerAdapter.
        viewPager = (ViewPager) findViewById(R.id.portraitViewPager);
        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        if (viewPager != null) {
            setupViewPager(viewPager);
            viewPager.setOffscreenPageLimit(5);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        if (moonFragment == null && sunFragment == null) {

            sunFragment = new SunFragment();
            moonFragment = new MoonFragment();
            weatherFragment = new WeatherFragment();
            detailsFragment = new DetailsFragment();
            forecastFragment = new ForecastFragment();
//            fragment5 = new NextDaysFragment();
            pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            pagerAdapter.addFragment(sunFragment, "SUN");
            pagerAdapter.addFragment(moonFragment, "MOON");
            pagerAdapter.addFragment(weatherFragment, "WEATHER");
            pagerAdapter.addFragment(detailsFragment, "DETAILS");
            pagerAdapter.addFragment(forecastFragment, "TEST");
//            pagerAdapter.addFragment(fragment3, getString(nadro.weather.R.string.basic_temp_label));
//            pagerAdapter.addFragment(fragment4, getString(nadro.weather.R.string.additional_temp_label));
//            pagerAdapter.addFragment(fragment5, getString(nadro.weather.R.string.days_temp_label));

            viewPager.setAdapter(pagerAdapter);
            sunFragment = (SunFragment) pagerAdapter.getItem(0);
            moonFragment = (MoonFragment) pagerAdapter.getItem(1);
            weatherFragment = (WeatherFragment) pagerAdapter.getItem(2);
            detailsFragment = (DetailsFragment) pagerAdapter.getItem(3);
            forecastFragment = (ForecastFragment) pagerAdapter.getItem(4);
//            fragment3 = (WeatherFragment) pagerAdapter.getItem(2);
//            Log.d("fragment 3 adapter", pagerAdapter.getItem(2).toString());
//            fragment4 = (MoreWeatherFragment) pagerAdapter.getItem(3);
//            fragment5 = (NextDaysFragment) pagerAdapter.getItem(4);
        }
    }

    public com.astrocalculator.AstroCalculator.Location getLocation() {
        return new AstroCalculator.Location(Double.valueOf(this.getLatitudeSetting()),
                Double.valueOf(this.getLongitudeSetting()));
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





