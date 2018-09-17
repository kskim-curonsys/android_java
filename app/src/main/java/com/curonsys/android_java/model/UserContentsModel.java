package com.curonsys.android_java.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserContentsModel {
    private String mUserId;
    private String mName;
    private boolean mNotification;
    private String mImageUrl;
    private ArrayList<String> mContents;
    private ArrayList<String> mPurchase;
    private GeoPoint mLocation;

    public UserContentsModel() {
        mUserId = "";
        mName = "";
        mNotification = true;
        mImageUrl = "";
        mContents = new ArrayList<String>();
        mPurchase = new ArrayList<String>();
        mLocation = new GeoPoint(0, 0);

    }

    public UserContentsModel(Map<String, Object> data) {
        if (data.containsKey("user_id")) {
            mUserId = (String) data.get("user_id");
        } else {
            mUserId = "";
        }

        if (data.containsKey("name")) {
            mName = (String) data.get("name");
        } else {
            mName = "";
        }

        if (data.containsKey("notification")) {
            mNotification = (boolean) data.get("notification");
        } else {
            mNotification = true;
        }

        if (data.containsKey("image_url")) {
            mImageUrl = (String) data.get("image_url");
        } else {
            mImageUrl = "";
        }

        if (data.containsKey("contents")) {
            mContents = (ArrayList<String>) data.get("contents");
        } else {
            mContents = new ArrayList<String>();
        }

        if (data.containsKey("purchase")) {
            mPurchase = (ArrayList<String>) data.get("purchase");
        } else {
            mPurchase = new ArrayList<String>();
        }

        if (data.containsKey("location")) {
            mLocation = (GeoPoint) data.get("location");
        } else {
            mLocation = new GeoPoint(0, 0);
        }
    }

    public String getUserId() {
        return mUserId;
    }

    public String getName() {
        return mName;
    }

    public boolean getNotification() {
        return mNotification;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public ArrayList<String> getContents() {
        return mContents;
    }

    public ArrayList<String> getPurchases() {
        return mPurchase;
    }

    public GeoPoint getLocations() {
        return mLocation;
    }

    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();

        data.put("user_id", mUserId);
        data.put("name", mName);
        data.put("notification", mNotification);
        data.put("image_url", mImageUrl);
        data.put("contents", mContents);
        data.put("purchase", mPurchase);
        data.put("location", mLocation);

        return data;
    }
}
