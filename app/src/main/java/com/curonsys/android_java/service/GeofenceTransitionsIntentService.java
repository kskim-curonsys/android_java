package com.curonsys.android_java.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import com.curonsys.android_java.R;

public class GeofenceTransitionsIntentService extends IntentService {
    String mName;

    public GeofenceTransitionsIntentService() {
        super("testname");
        mName = "testname";
    }

    public GeofenceTransitionsIntentService(String name) {
        super(name);
        mName = name;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /*

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.e("location", errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );

            // Send notification and log the transition details.
            sendNotification(geofenceTransitionDetails);
            Log.i("location", geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.e("locationi", getString(R.string.geofence_transition_invalid_type,
                    geofenceTransition));
        }
        */
    }
}
