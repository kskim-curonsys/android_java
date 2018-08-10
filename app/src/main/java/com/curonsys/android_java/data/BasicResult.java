package com.curonsys.android_java.data;

import java.util.ArrayList;

public class BasicResult {
    public String code;
    public String message;
    public String version;
    public ArrayList<BasicInfo> data;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getVersion() {
        return version;
    }
}
