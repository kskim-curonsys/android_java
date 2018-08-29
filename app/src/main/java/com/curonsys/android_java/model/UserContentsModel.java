package com.curonsys.android_java.model;

import java.util.ArrayList;

public class UserContentsModel {
    private String user_id;
    private String name;
    private boolean notification;
    private String image_url;
    private ArrayList<String> contents;
    private ArrayList<String> purchase;
    private ArrayList<String> location;

    public UserContentsModel() {
        user_id = "";
        name = "";
        notification = true;
        image_url = "";
        contents = new ArrayList<String>();
        purchase = new ArrayList<String>();
        location = new ArrayList<String>();

    }

    public UserContentsModel(String id, String n, boolean noti, String img,
                             ArrayList<String> co, ArrayList<String> pu, ArrayList<String> lo) {

        user_id = id;
        name = n;
        notification = noti;
        image_url = img;
        contents = co;
        purchase = pu;
        location = lo;
    }

    public String getUserId() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public boolean getNotification() {
        return notification;
    }

    public String getImageUrl() {
        return image_url;
    }

    public ArrayList<String> getContents() {
        return contents;
    }

    public ArrayList<String> getPurchases() {
        return purchase;
    }

    public ArrayList<String> getLocations() {
        return location;
    }
}
