package com.example.nadro.astroweather;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by nadro on 11.06.2017.
 */

public class AstroInfoCalculator {
    private final float SYNODIC_MONTH_LENGTH = 29.531f;
    AstroCalculator.Location location;
    AstroDateTime astroDateTime;
    AstroCalculator astroCalculator;


    static DecimalFormat REAL_FORMATTER = new DecimalFormat("0.######",
            new DecimalFormatSymbols(Locale.getDefault()));

    public AstroInfoCalculator(com.astrocalculator.AstroCalculator.Location location, AstroDateTime dateTime) {
        this.location = location;
        this.astroDateTime = dateTime;
        this.astroCalculator = new com.astrocalculator.AstroCalculator(astroDateTime, location);
    }

    public String calculateSunrise() {
        return astroCalculator.getSunInfo().getSunrise().toString();
    }

    public String calculateSunset() {
        return astroCalculator.getSunInfo().getSunset().toString();
    }

    public String calculateAzimuthRise() {
        return REAL_FORMATTER.format(astroCalculator.getSunInfo().getAzimuthRise());

    }

    public String calculateAzimuthSet() {
        return REAL_FORMATTER.format(astroCalculator.getSunInfo().getAzimuthSet());
    }

    public AstroDateTime calculateTwilightMorning() {
        return astroCalculator.getSunInfo().getTwilightMorning();
    }

    public AstroDateTime calculateTwilightEvening() {
        return  astroCalculator.getSunInfo().getTwilightEvening();
    }

    public List<String> getSunInfoList() {
        List<String> sunList = new ArrayList<>();
        sunList.add(calculateSunrise());
        sunList.add(calculateSunset());
        sunList.add(calculateAzimuthRise());
        sunList.add(calculateAzimuthSet());
        sunList.add(calculateTwilightMorning().toString());
        sunList.add(calculateTwilightEvening().toString());
        return sunList;
    }

    public String calculateMoonRise() {
        return astroCalculator.getMoonInfo().getMoonrise().toString();
    }

    public String calculateMoonSet() {
        return astroCalculator.getMoonInfo().getMoonset().toString();
    }

    public String calculateNextFullMoon() {
        return astroCalculator.getMoonInfo().getNextFullMoon().toString();
    }

    public String calculateNextNewMoon() {
        return astroCalculator.getMoonInfo().getNextNewMoon().toString();
    }

    public String calculateIllumination() {
        return String.valueOf(astroCalculator.getMoonInfo().getIllumination()*100).substring(0,5) + "%";
        //return (REAL_FORMATTER.format(astroCalculator.getMoonInfo().getIllumination()));
//        return astroCalculator.getMoonInfo().getIllumination();
    }

    public String calculateMoonAge() {
        return String.valueOf((int)(astroCalculator.getMoonInfo().getAge() /*% SYNODIC_MONTH_LENGTH*/));
    }

    public List<String> getMoonInfoList() {
        List<String> moonList = new ArrayList<>();
        moonList.add(calculateMoonRise());
        moonList.add(calculateMoonSet());
        moonList.add(calculateNextFullMoon());
        moonList.add(calculateNextNewMoon());
        moonList.add(String.valueOf(calculateIllumination()));
        moonList.add(String.valueOf(calculateMoonAge()));
        return moonList;
    }
}