package com.example.nadro.astroweather;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.nadro.astroweather.Model.CityResult;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class Helpers {

    public static boolean containsCity(ArrayList<CityResult> list, String woeid){
        for(CityResult c: list){
            if(c.getWoeid().equals(woeid))
                return true;
        }
        return false;
    }

    public static void saveToFile(String fileName, Object object, Activity activity) throws IOException {
        Log.d("Helper:saveToFile", "calling");
        FileOutputStream fos = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(object);
        os.close();
        fos.close();
    }

    public static Object loadFromFile(String fileName, Activity activity) throws IOException, ClassNotFoundException {
        Log.d("Helper:loadFromFile", "calling");
        FileInputStream fis = activity.openFileInput(fileName);
        ObjectInputStream is = new ObjectInputStream(fis);
        Object result = is.readObject();
        is.close();
        fis.close();
        return result;
    }

    public static boolean isNetworkAvailable(Activity activity) {
        Log.d("Helper:isNetAvailable", "calling");
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
