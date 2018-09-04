package com.curonsys.android_java.model;

public class DownloadModel {
    private String mPath;
    private String mSuffix;
    private long mSize;

    public DownloadModel() {
        mPath = "";
        mSuffix = "";
        mSize = 0;
    }

    public DownloadModel(String path, String suffix, long size) {
        mPath = path;
        mSuffix = suffix;
        mSize = size;
    }

    public String getPath () {
        return mPath;
    }

    public String getSuffix() {
        return mSuffix;
    }

    public long getSize() {
        return mSize;
    }
}
