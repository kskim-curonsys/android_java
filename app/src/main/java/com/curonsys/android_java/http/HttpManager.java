package com.curonsys.android_java.http;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

public class HttpManager {
    private static String BASE_URL = "https://kskim-curonsys.com";

    private static HttpManager instance;

    public static HttpManager getInstance() {
        if (instance == null) {
            instance = new HttpManager();
        }
        return instance;
    }

    Gson gson = new Gson();
    AsyncHttpClient client;

    private HttpManager() {
        client = new AsyncHttpClient();
        client.setTimeout(30000);
    }

    public interface OnResultListener<T> {
        public void onSuccess(T result);

        public void onFail(int code);
    }

    // email_check
    //public void email_check(String email, final OnResultListener<BasicResult> listener) {

    //}
}
