package com.example.hospitalwaka.Util;

import android.app.Application;

import com.example.hospitalwaka.Models.myModel;

import java.util.List;

public class WakaApi extends Application {
    public  static WakaApi instance ;
    private String username;
    private  String userId;
    private List<myModel>  myList ;
    private List<Double> lat;
    private List<Double> lons;


// creating a singleton .. i.e create on unique object for entire app


    public static WakaApi getInstance() {
        if(instance == null)
        {
            instance  = new WakaApi();

        }
        return instance;
    }
    public List<myModel> getMyList() {
        return myList;
    }

    public void setMyList(List<myModel> myList) {
        this.myList = myList;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Double> getLat() {
        return lat;
    }

    public void setLat(List<Double> lat) {
        this.lat = lat;
    }

    public List<Double> getLons() {
        return lons;
    }

    public void setLons(List<Double> lons) {
        this.lons = lons;
    }
}
