package com.example.nadro.astroweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Created by Damian on 21.06.2017.
 */

public class Preferences extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    static final String LAT = "latitudeSetting";
    static final String LON = "longitudeSetting";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getPreferenceScreen().getSharedPreferences()
//                .registerOnSharedPreferenceChangeListener(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        getPreferenceScreen().getSharedPreferences()
//                .unregisterOnSharedPreferenceChangeListener(this);
//    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//
//        if (LAT.equals(key)) {
//            String value = sharedPreferences.getString(key, "");
////            try {
//                double v = Double.parseDouble(value);
//                if(!isGoodLat(v)){
//                    sharedPreferences.edit().putString(LAT, "0");
//                }else{
//                    sharedPreferences.edit().putString(LAT, v+"");
//                }
////            } catch (RuntimeException e) {
////
////                    EditTextPreference p = (EditTextPreference) findPreference(key);
////                    p.setText(def);
////            }
//
//        }else if(LON.equals(key)){
//            String value = sharedPreferences.getString(key, "");
//            double v = Double.parseDouble(value);
//            if(!isGoodLon(v)){
//                sharedPreferences.edit().putString(LON, "0");
//            }else{
//                sharedPreferences.edit().putString(LON, v+"");
//            }
//        }
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);


//            findPreference("latitudeSetting").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object newValue) {
//                    if(!newValue.toString().isEmpty()){
//                        if(Double.parseDouble(newValue.toString()) < -90 || Double.parseDouble(newValue.toString()) > 90){
//                            return false;
//                        }else {
//                            return true;
//                        }
//                    }else{
//                        return false;
//                    }
//                }
//            });
//
//            findPreference("longitudeSetting").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object newValue) {
//                    if(!newValue.toString().isEmpty()){
//                        if(Double.parseDouble(newValue.toString()) < -180 || Double.parseDouble(newValue.toString()) > 180){
//                            return false;
//                        }else {
//                            return true;
//                        }
//                    }else{
//                        return false;
//                    }
//                }
//            });
        }

    }

//    private boolean isGoodLat(double lat) {
//        if (lat < -90 || lat > 90)
//            return false;
//        return true;
//    }
//
//    private boolean isGoodLon(double lon) {
//        if (lon < -180 || lon > 180)
//            return false;
//        return true;
//    }



}
