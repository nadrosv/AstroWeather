package com.example.nadro.astroweather;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.nadro.astroweather.Model.CityResult;
import com.example.nadro.astroweather.Model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class YahooWeather {

//    public static String YAHOO_URL = "https://query.yahooapis.com/v1/public/yql?q=";


    public static void getJsonCity(String cityName, RequestQueue rq, final WeatherClientListener listener) {
        String cityQuery = makeQueryForJsonCity(cityName);
        Log.d("getJsonCity", "getCity: Weather URL ["+cityQuery+"]");
//        final CityResult result = new CityResult();
        StringRequest req = new StringRequest(Request.Method.GET, cityQuery, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("JsonCityResponse", response);
                CityResult result = parseJsonCityResponse(response);

                Log.d("JsonCityResult", result.toString());
                listener.onCityResponse(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("getJsonCity", "error");
            }
        });
        rq.add(req);
    }

    public static void getJsonWeather(String woeid, String unit, RequestQueue rq, final WeatherClientListener listener) {
        String weatherQuery = makeQueryForJsonWeather(woeid, unit);
        Log.d("getJsonWeather", " getWeather: Weather URL ["+weatherQuery+"]");
//        final Weather result = new Weather();
        StringRequest req = new StringRequest(Request.Method.GET, weatherQuery, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Weather result = parseJsonWeatherResponse(response);

                listener.onWeatherResponse(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("getJsonWeather", "error");
            }
        });

        rq.add(req);
    }

    private static CityResult parseJsonCityResponse(String response){
        CityResult result = new CityResult();

        try {
            JSONObject resp = new JSONObject(response);
            JSONObject query = resp.getJSONObject("query").getJSONObject("results").getJSONObject("place");

            result.setWoeid(query.getString("woeid"));
            result.setCityName(query.getString("name"));
            result.setCountry(query.getJSONObject("country").getString("content"));

            double lat = Double.parseDouble(query.getJSONObject("centroid").getString("latitude"));
            double lon = Double.parseDouble(query.getJSONObject("centroid").getString("longitude"));
            result.setCoordinates(lat, lon);


            Log.d("parseJsonCity", "woeid: " + result.getWoeid() + " city: " + result.getCityName());

        }catch(JSONException e){
            Log.d("parseCityJson", "JSON Error");
            e.printStackTrace();
        }

        return result;
    }

    private static Weather parseJsonWeatherResponse(String response){
        Weather weather = new Weather();

        try {
            JSONObject resp = new JSONObject(response);
            JSONObject query = resp.getJSONObject("query").getJSONObject("results").getJSONObject("channel");

            JSONObject units = query.getJSONObject("units");
            weather.units.speed = units.getString("speed");
            weather.units.temperature = units.getString("temperature");
            weather.units.pressure = units.getString("pressure");
            weather.units.distance = units.getString("distance");

            JSONObject location = query.getJSONObject("location");
            weather.location.name = location.getString("city");
            weather.location.country = location.getString("country");
            weather.location.region = location.getString("region");

            JSONObject wind = query.getJSONObject("wind");
            weather.wind.speed = Float.parseFloat(wind.getString("speed"));
            weather.wind.direction = Integer.parseInt(wind.getString("direction"));

            JSONObject atmosphere = query.getJSONObject("atmosphere");
            weather.atmosphere.humidity = Integer.parseInt(atmosphere.getString("humidity"));
            weather.atmosphere.pressure = Float.parseFloat(atmosphere.getString("pressure"))/33.7685f;
            weather.atmosphere.visibility = Float.parseFloat(atmosphere.getString("visibility"));

            JSONObject condition = query.getJSONObject("item").getJSONObject("condition");
            weather.condition.temp = Integer.parseInt(condition.getString("temp"));
            weather.condition.date = condition.getString("date");
            weather.condition.code = Integer.parseInt(condition.getString("code"));

            JSONArray forecast = query.getJSONObject("item").getJSONArray("forecast");

            for (int i = 0; i < forecast.length(); i++) {
                JSONObject day = forecast.getJSONObject(i);
                Weather.NextDays nextDay = new Weather().new NextDays();
                nextDay.code = Integer.parseInt(day.getString("code"));
                nextDay.date = day.getString("date");
                nextDay.day = day.getString("day");
                nextDay.high = Integer.parseInt(day.getString("high"));
                nextDay.low = Integer.parseInt(day.getString("low"));
                nextDay.description = day.getString("text");

                weather.nextDaysList.add(i, nextDay);
            }

            Calendar calendar = Calendar.getInstance();
            weather.lastUpdate = calendar.getTimeInMillis();

//            for (int i = 0; i < weather.nextDaysList.size(); i++) {
//                Log.d("Forecast", weather.nextDaysList.get(i).day);
//            }

            Log.d("YahooWeather", "Temp" + String.valueOf(weather.condition.temp));


        }catch(JSONException e){
            Log.d("parseCityJson", "JSON Error");
        }

        return weather;
    }

    private static String makeQueryForJsonCity(String cityName){
        cityName = cityName.replaceAll(" ", "%20");
        return "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.places(1)%20where%20text=\""+cityName+"\"&format=json";
    }

    private static String makeQueryForJsonWeather(String woeid, String unit) {
        return "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid=" + woeid + "%20and%20u=\"" + unit + "\"&format=json";
    }


    /* Pubblic listener interface */

    public interface WeatherClientListener {
        void onCityResponse(CityResult city);

        void onWeatherResponse(Weather weather);

        void onImageReady(Bitmap image);
    }

    private static final String YAHOO_IMG_URL = "http://l.yimg.com/a/i/us/we/52/";

    public static Bitmap getImage(int code, RequestQueue requestQueue, final WeatherClientListener listener) {
        String imageURL = YAHOO_IMG_URL + code + ".gif";
        ImageRequest ir = new ImageRequest(imageURL, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap response) {
                if (listener != null)
                    listener.onImageReady(response);
            }
        }, 60, 60, ImageView.ScaleType.CENTER,null, null);

        requestQueue.add(ir);
        return null;
    }


}
