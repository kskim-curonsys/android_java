package com.curonsys.android_java.utils;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public final class Constants {
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    public static final String PACKAGE_NAME = "com.curonsys.android_java";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static final long GEOFENCE_EXPIRATION_IN_HOUR = 1;
    public static final float GEOFENCE_USER_RADIUS_IN_METERS = 100;
    public static final float GEOFENCE_SERVICE_RADIUS_IN_METERS = 200;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 60 * 60 * 1000 * GEOFENCE_EXPIRATION_IN_HOUR;

    public static final HashMap<String, LatLng> BAY_AREA_LANDMARKS = new HashMap<>();

    static {
        // Home_APT
        BAY_AREA_LANDMARKS.put("HOME_APT", new LatLng(34.951302, 127.689964));

        // San Francisco International Airport.
        BAY_AREA_LANDMARKS.put("SFO", new LatLng(37.621313, -122.378955));

        // Googleplex.
        BAY_AREA_LANDMARKS.put("GOOGLE", new LatLng(37.422611,-122.0840577));

    }
}

