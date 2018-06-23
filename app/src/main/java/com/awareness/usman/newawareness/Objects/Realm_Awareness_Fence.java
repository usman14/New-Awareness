package com.awareness.usman.newawareness.Objects;

import io.realm.RealmObject;

/**
 * Created by usman on 4/3/2017.
 */

public class Realm_Awareness_Fence extends RealmObject {

    String situationname;
    String weather;
    String appopen;
    String notification;
    int headphone_txt;
    String appname;
    int activity_txt;
    Boolean active;
    String date;
    int weather_txt;
    Double longi;
    Double Lat;
    String headphone;
    String Location;
    String time;
    String activity;
    String minutes;
    String hours;
    int radius;
    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }


    public String getSituationname() {
        return situationname;
    }

    public void setSituationname(String situationname) {
        this.situationname = situationname;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getAppopen() {
        return appopen;
    }

    public void setAppopen(String appopen) {
        this.appopen = appopen;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }


    public String getHeadphone() {
        return headphone;
    }

    public void setHeadphone(String headphone) {
        this.headphone = headphone;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }



    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }


    public int getWeather_txt() {
        return weather_txt;
    }

    public void setWeather_txt(int weather_txt) {
        this.weather_txt = weather_txt;
    }


    public int getHeadphone_txt() {
        return headphone_txt;
    }

    public void setHeadphone_txt(int headphone_txt) {
        this.headphone_txt = headphone_txt;
    }

    public int getActivity_txt() {
        return activity_txt;
    }

    public void setActivity_txt(int activity_txt) {
        this.activity_txt = activity_txt;
    }


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active=active;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
