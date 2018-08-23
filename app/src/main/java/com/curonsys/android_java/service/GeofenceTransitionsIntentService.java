package com.curonsys.android_java.service;

import android.app.IntentService;
import android.content.Intent;

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

    }
}
