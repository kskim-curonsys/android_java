package com.curonsys.android_java.model;

import java.util.ArrayList;
import java.util.Map;

public class ContentModel {
    private String mContentId;
    private String mName;
    private ArrayList<String> mFiles;
    private ArrayList<String> mTextures;
    private boolean m3D;
    private boolean mAnimation;
    private String mFormat;
    private ArrayList<Float> mRotation;
    private Number mScale;
    private String mVersion;

    public ContentModel() {
        mContentId = "";
        mName = "";
        mFiles = new ArrayList<String>();
        mTextures = new ArrayList<String>();
        m3D = true;
        mAnimation = true;
        mFormat = "";
        mRotation = new ArrayList<Float>();
        mScale = 0;
        mVersion = "0.0.0";
    }

    public ContentModel(Map<String, Object> data) {
        mContentId = (String) data.get("content_id");
        mName = (String) data.get("name");
        mFiles = (ArrayList<String>) data.get("files");
        mTextures = (ArrayList<String>) data.get("textures");
        m3D = (boolean) data.get("3d");
        mAnimation = (boolean) data.get("animation");
        mFormat = (String) data.get("format");
        mRotation = (ArrayList<Float>) data.get("rotation");
        mScale = (Number) data.get("scale");
        mVersion = (String) data.get("version");
    }

    public String getContentId() {
        return mContentId;
    }

    public String getContentName() {
        return mName;
    }

    public ArrayList<String> getContentUrl() {
        return mFiles;
    }

    public ArrayList<String> getTextureUrl() {
        return mTextures;
    }

    public boolean get3D() {
        return m3D;
    }

    public boolean getAnimation() {
        return mAnimation;
    }

    public String getFormat() {
        return  mFormat;
    }

    public ArrayList<Float> getRotation() {
        return mRotation;
    }

    public Number getContentScale() {
        return mScale;
    }

    public String getVersion() {
        return mVersion;
    }
}
