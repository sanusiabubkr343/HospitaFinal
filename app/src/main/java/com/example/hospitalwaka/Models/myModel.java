package com.example.hospitalwaka.Models;

import java.io.Serializable;

public class myModel implements Serializable {
    String hospitalName;
    String Vicinity;
    String geographicalLocation ;
     int photoUrl;

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getVicinity() {
        return Vicinity;
    }

    public void setVicinity(String vicinity) {
        Vicinity = vicinity;
    }

    public String getGeographicalLocation() {
        return geographicalLocation;
    }

    public void setGeographicalLocation(String geographicalLocation) {
        this.geographicalLocation = geographicalLocation;
    }

    public int getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(int photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return "myModel{" +
                "hospitalName='" + hospitalName + '\'' +
                ", Vicinity='" + Vicinity + '\'' +
                ", geographicalLocation='" + geographicalLocation + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
