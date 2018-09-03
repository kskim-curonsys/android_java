package com.curonsys.android_java.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MarkerModel {
    private String mMarkerId;
    private String mUserId;
    private String mFile;
    private float mRating;
    private GeoPoint mGeoPoint;
    private double mLatitude;
    private double mLongitude;
    private String mContentId;
    private ArrayList<Float> mContentRotation;
    private float mContentScale;
    private ArrayList<String> mAdditionalMediaId;
    private String mDownloadedPath;

    public MarkerModel() {
        mMarkerId = "";
        mUserId = "";
        mFile = "";
        mRating = 0;
        mGeoPoint = new GeoPoint(0, 0);
        mContentId = "";
        mContentRotation = new ArrayList<Float>();
        mContentScale = 0;
        mAdditionalMediaId = new ArrayList<String>();
        mDownloadedPath = "";
    }

    public MarkerModel(Map<String, Object> data) {
        mMarkerId = (String) data.get("marker_id");
        mUserId = (String) data.get("user_id");
        mFile = (String) data.get("file");
        mRating = (float) data.get("rating");
        mGeoPoint = (GeoPoint) data.get("location");
        mContentId = (String) data.get("content_id");
        mContentRotation = (ArrayList<Float>) data.get("content_rotation");
        mContentScale = (float) data.get("content_scale");
        mAdditionalMediaId = (ArrayList<String>) data.get("additional_media_id");
        mDownloadedPath = "";
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
        return mGeoPoint.getLatitude();
    }

    public double getLongitude() {
        return mGeoPoint.getLongitude();
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

    public String getLocalPath() {
        return mDownloadedPath;
    }

    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();

        data.put("marker_id", mMarkerId);
        data.put("user_id", mUserId);
        data.put("file", mFile);
        data.put("rating", mRating);
        data.put("location", mGeoPoint);
        data.put("content_id", mContentId);
        data.put("content_rotation", mContentRotation);
        data.put("content_scale", mContentScale);
        data.put("additional_media_id", mAdditionalMediaId);

        return data;
    }
}
