package com.curonsys.android_java.model;

public class DownloadModel {
    private String mPath;
    private long mSize;

    public DownloadModel() {
        mPath = "";
        mSize = 0;
    }

    public DownloadModel(String path, long size) {
        mPath = path;
        mSize = size;
    }

    public String getPath () {
        return mPath;
    }

    public long getSize() {
        return mSize;
    }
}
