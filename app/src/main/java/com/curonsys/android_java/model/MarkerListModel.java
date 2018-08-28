package com.curonsys.android_java.model;

public class MarkerListModel {
    String marker_id;
    double marker_latitude;
    double marker_longitude;

    public MarkerListModel() {
        marker_id = "";
        marker_latitude = 0;
        marker_longitude = 0;
    }

    public MarkerListModel(String id, double lat, double lon) {
        marker_id = id;
        marker_latitude = lat;
        marker_longitude = lon;
    }

    String getID() {
        return marker_id;
    }

    double getLatitude() {
        return marker_latitude;
    }

    double getLongitude() {
        return marker_longitude;
    }
}

