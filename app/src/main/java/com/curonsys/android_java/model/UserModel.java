package com.curonsys.android_java.model;

import java.util.ArrayList;
import java.util.Map;

public class UserModel {
    private String mUserId;
    private String mName;
    private boolean mNotification;
    private String mImageUrl;
    private ArrayList<String> mContents;
    private ArrayList<String> mPurchases;
    private ArrayList<String> mLocations;

    public UserModel() {
        mUserId = "";
        mName = "";
        mNotification = true;
        mImageUrl = "";
        mContents = new ArrayList<String>();
        mPurchases = new ArrayList<String>();
        mLocations = new ArrayList<String>();
    }

    public UserModel(Map<String, Object> data) {
        mUserId = (String) data.get("user_id");
        mName = (String) data.get("name");
        mNotification = (boolean) data.get("notification");
        mImageUrl = (String) data.get("image_url");
        mContents = (ArrayList<String>) data.get("contents");
        mPurchases = (ArrayList<String>) data.get("purchase");
        mLocations = (ArrayList<String>) data.get("location");

    }

    public UserModel(String userid, String name, boolean noti, String imageurl, ArrayList<String> contents,
                     ArrayList<String> purchases, ArrayList<String> locations) {

        mUserId = userid;
        mName = name;
        mNotification = noti;
        mImageUrl = imageurl;
        mContents = contents;
        mPurchases = purchases;
        mLocations = locations;
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
        return mPurchases;
    }

    public ArrayList<String> getLocations() {
        return mLocations;
    }
}
