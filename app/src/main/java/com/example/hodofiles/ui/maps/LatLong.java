package com.example.hodofiles.ui.maps;

public class LatLong {
    private static LatLong instance;
    private double latitude;
    private double longitude;

    private LatLong () {}

    public static synchronized LatLong getInstance() {
        if (instance == null) {
            instance = new LatLong ();
        }
        return instance;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
