package com.curonsys.android_java.model;

import java.util.Map;

public class TransferModel {
    private String mPath;
    private String mSuffix;
    private String mContentType;
    private String mName;
    private String mMD5Hash;
    private String mErrorMessage;

    private long mSize;
    private long mCreationTimeMillis;
    private long mUpdatedTimeMillis;

    public TransferModel() {
        mPath = "";
        mSuffix = "";
        mContentType = "";
        mName = "";
        mMD5Hash = "";
        mErrorMessage = "";
        mSize = 0;
        mCreationTimeMillis = 0;
        mUpdatedTimeMillis = 0;
    }

    public TransferModel(Map<String, Object> data) {
        mPath = (String) data.get("path");
        mSuffix = (String) data.get("suffix");
        mContentType = (String) data.get("content_type");
        mName = (String) data.get("name");
        mMD5Hash = (String) data.get("md5hash");
        mErrorMessage = (String) data.get("error");
        mSize = (long) data.get("size");
        if (data.containsValue("creation_time")) {
            mCreationTimeMillis = (long) data.get("creation_time");
        } else {
            mCreationTimeMillis = 0;
        }
        if (data.containsValue("updated_time")) {
            mUpdatedTimeMillis = (long) data.get("updated_time");
        } else {
            mUpdatedTimeMillis = 0;
        }
    }

    public String getPath() {
        return mPath;
    }

    public String getSuffix() {
        return mSuffix;
    }

    public String getContentType() {
        return mContentType;
    }

    public String getName() {
        return mName;
    }

    public String getMD5Hash() {
        return mMD5Hash;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public long getSize() {
        return mSize;
    }

    public long getCreationTimeMillis() {
        return mCreationTimeMillis;
    }

    public long getUpdatedTimeMillis() {
        return mUpdatedTimeMillis;
    }
}
