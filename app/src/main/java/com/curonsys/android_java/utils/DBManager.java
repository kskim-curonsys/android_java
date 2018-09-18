package com.curonsys.android_java.utils;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Leejuhwan on 2018-05-22.
 */

public class DBManager {

    //frag1
    public Uri imageURI = null;
    public double markerRating = 0;
    public String generatorId = null;

    //frag2
    public double markerLongtitude = 0;
    public double markerLatitude = 0;
    public String markerCountryCode = "";
    public String markerLocality = "";
    public String markerThoroughfare = "";

    //frag3
    public String contentId = "";
    public String contentName = "";
    public String contentFileName = "";
    public int textureCount = 0;
    public String[] contentTextureNames;
    public String[] contentTextureFiles;
    public Boolean contentHasAnimation = false;

    //frag4
    public float contentScale = 0;
    public ArrayList<Float> contentRotation = new ArrayList<Float>();


    public  DBManager(){
    }

    private static DBManager instance;

    public static DBManager getInstance () {
        if ( instance == null )
            instance = new DBManager();
        return instance;
    }
}
