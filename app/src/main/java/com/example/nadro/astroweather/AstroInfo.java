package com.example.nadro.astroweather;

/**
 * Created by nadro on 11.06.2017.
 */

public class AstroInfo {
    private String label;
    private String value;

    public AstroInfo(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public AstroInfo() {

    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {

        return label;
    }

    public String getValue() {
        return value;
    }

}