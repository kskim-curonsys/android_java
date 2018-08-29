package com.curonsys.android_java.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Map;

public class MarkerModel {
    private String mMarkerId;
    private String mUserId;
    private String mFile;
    private float mRating;
    private double mLatitude;
    private double mLongitude;
    private String mContentId;
    private ArrayList<Float> mContentRotation;
    private float mContentScale;
    private ArrayList<String> mAdditionalMediaId;

    public MarkerModel() {
        mMarkerId = "";
        mUserId = "";
        mFile = "";
        mRating = 0;
        mLatitude = 0;
        mLongitude = 0;
        mContentId = "";
        mContentRotation = new ArrayList<Float>();
        mContentScale = 0;
        mAdditionalMediaId = new ArrayList<String>();
    }

    public MarkerModel(Map<String, Object> data) {
        mMarkerId = (String) data.get("marker_id");
        mUserId = (String) data.get("user_id");
        mFile = (String) data.get("file");
        mRating = (float) data.get("rating");
        GeoPoint gp = (GeoPoint) data.get("location");
        mLatitude = gp.getLatitude();
        mLongitude = gp.getLongitude();
        mContentId = (String) data.get("content_id");
        mContentRotation = (ArrayList<Float>) data.get("content_rotation");
        mContentScale = (float) data.get("content_scale");
        mAdditionalMediaId = (ArrayList<String>) data.get("additional_media_id");
    }

    public String getMarkerId() {
        return mMarkerId;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getFile() {
        return mFile;
    }

    public float getRating() {
        return mRating;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getContentId() {
        return mContentId;
    }

    public ArrayList<Float> getContentRotation() {
        return mContentRotation;
    }

    public float getScale() {
        return mContentScale;
    }

    public ArrayList<String> getMediaId() {
        return mAdditionalMediaId;
    }
}
