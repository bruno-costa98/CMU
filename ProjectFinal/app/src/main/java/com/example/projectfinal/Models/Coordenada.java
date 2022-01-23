package com.example.projectfinal.Models;

public class Coordenada {

    double latitude;
    double longitude;

    public Coordenada(){

    }

    public Coordenada(double lat, double lon){
        this.latitude = lat;
        this.longitude = lon;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLatString(){
        String string = String.valueOf(latitude);
        return string;
    }
}
