package com.example.nadro.astroweather.Model;

import com.astrocalculator.AstroCalculator;

import java.io.Serializable;

import static java.lang.Math.abs;


public class CityResult implements Serializable {

    private String woeid;
    private String cityName;
    private String country;
    private Weather weather;
    private AstroCalculator.Location coordinates;

    public CityResult() {}

    public CityResult(String woeid, String cityName, String country) {
        this.woeid = woeid;
        this.cityName = cityName;
        this.country = country;
    }

    public void setCoordinates(double lat, double lon){
        this.coordinates = new AstroCalculator.Location(lat, lon);
    }

    public String getCoordinates(){
        String fLat, fLon;
        double lat = coordinates.getLatitude();
        double lon = coordinates.getLongitude();
        if(lat < 0){
            fLat = abs(lat) + " S";
        }else{
            fLat = abs(lat) + " N";
        }
        if(lon < 0){
            fLon = abs(lon) + " W";
        }else{
            fLon = abs(lon) + " E";
        }
        return fLat + ", " + fLon;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return cityName + "," + country;
    }
}
