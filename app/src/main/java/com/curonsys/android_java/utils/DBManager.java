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
    public double markerRating;
    public String generatorId = null;

    //frag2
    public double markerLongtitude;
    public double markerLatitude;

    //frag3
    public int contentId;
    public String contentName;
    public String contentFileName;
    public int textureCount;
    public String[] contentTextureNames;
    public String[] contentTextureFiles;
    public Boolean contentHasAnimation;

    //frag4
    public float contentScale;
    public float contentRoation;

    public  DBManager(){
    }

    private static DBManager instance;

    public static DBManager getInstance () {
        if ( instance == null )
            instance = new DBManager();
        return instance;
    }
}
