package com.example.android.quakereport;

/**
 * Created by tusharsharma on 30/07/16.
 */
public class ListItemClass {

    private double magnitude;
    private String place;
    private long timeInMillisecs;
    private String url;

    public ListItemClass(double mag,String place,long time,String url){
        magnitude = mag;
        this.place = place;
        this.timeInMillisecs = time;
        this.url = url;
    }

    public double getMagnitude(){
        return magnitude;
    }

    public String getPlace(){
        return place;
    }

    public long getTimeInMillisecs(){
        return timeInMillisecs;
    }

    public String getUrl(){
        return url;
    }
}
