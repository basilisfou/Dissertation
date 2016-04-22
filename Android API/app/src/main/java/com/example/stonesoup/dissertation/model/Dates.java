package com.example.stonesoup.dissertation.model;

import java.io.Serializable;

/**
 * Created by vfour_000 on 14/2/2016.
 */
public class Dates implements Serializable {
    private String date;
    private String place;
    private Double longitude , latitude;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
