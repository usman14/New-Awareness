package com.example.usman.newawareness.Objects;

import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmObject;

/**
 * Created by usman on 3/28/2017.
 */

public class Realm_Situation extends RealmObject {


    public String getSituationname() {
        return situationname;
    }

    public void setSituationname(String situationname) {
        this.situationname = situationname;
    }


    public String getLocationname() {
        return locationname;
    }

    public void setLocationname(String locationname) {
        this.locationname = locationname;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }


    int headphone;

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    String  weather;
    int activity;
    String situationname,locationname,time,date,action,notification;
    Long lat;
    Long longi;
    Double radius;

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }


    public int getHeadphone() {
        return headphone;
    }

    public void setHeadphone(int headphone) {
        this.headphone = headphone;
    }



    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }




    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    int key;

    public Long getLat() {
        return lat;
    }

    public void setLat(Long lat) {
        this.lat = lat;
    }

    public Long getLongi() {
        return longi;
    }

    public void setLongi(Long longi) {
        this.longi = longi;
    }


}
