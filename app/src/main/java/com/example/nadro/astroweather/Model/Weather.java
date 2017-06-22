package com.example.nadro.astroweather.Model;

import java.io.Serializable;
import java.util.ArrayList;


public class Weather implements Serializable{
    public String imageUrl;

    public Condition condition = new Condition();
    public Wind wind = new Wind();
    public Atmosphere atmosphere = new Atmosphere();
    public Forecast forecast = new Forecast();
    public Location location = new Location();
    public Astronomy astronomy = new Astronomy();
    public Units units = new Units();
    public NextDays nextDays = new NextDays();
    public ArrayList<NextDays> nextDaysList = new ArrayList<>();

    public long lastUpdate;

    public class Condition implements Serializable{
        public String description;
        public int code;
        public String date;
        public int temp;
    }

    public class Forecast implements Serializable{
        public int tempMin;
        public int tempMax;
        public String description;
        public int code;
    }

    public static class Atmosphere implements Serializable{
        public int humidity;
        public float visibility;
        public float pressure;
        public int rising;
    }

    public class Wind implements Serializable{
        public int chill;
        public int direction;
        public float speed;
    }

    public class Units implements Serializable{
        public String speed;
        public String distance;
        public String pressure;
        public String temperature;
    }

    public class Location implements Serializable{
        public String name;
        public String region;
        public String country;
    }

    public class Astronomy implements Serializable{
        public String sunRise;
        public String sunSet;
    }

    public class NextDays implements Serializable {
        public int code;
        public String date;
        public String day;
        public Integer low;
        public Integer high;
        public String description;

    }
}
