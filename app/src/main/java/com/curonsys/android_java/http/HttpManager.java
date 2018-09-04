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

        /*
    public void requestContentsOfUser(String subUrl, String id, final JsonResponseCallback callback) throws JSONException{
        String url = mBaseUrl + subUrl;

        RequestParams params = new RequestParams();
        params.add("userIdentify",id);

        this.client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                callback.onResponse(responseBody);

            }
        });
    }

    public void requestInfoOfSelectedContents(String subUrl, String id, final JsonResponseCallback callback) throws JSONException{
        String url = mBaseUrl + subUrl;

        RequestParams params = new RequestParams();
        params.add("contentsIndentify",id);

        this.client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                callback.onResponse(responseBody);

            }
        });
    }

    public void requestMarkerRegistInfo(String subUrl, String imgPath, String user_id, float rating, float longitude, float latitude,
                                        String contents_id, float contentsScale, float contentsRotateX, float contentsRotateY, float contentsRotateZ,
                                        final BoolResponseCallback callback) throws IOException{

        String url = mBaseUrl + subUrl;

        RequestParams params = new RequestParams();
        params.add("usersIndentify", user_id);
        params.add("contentsIndentify",contents_id);
        params.put("markerRating",rating);
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        params.put("contentsScale",contentsScale);
        params.put("contentsRotateX",contentsRotateX);
        params.put("contentsRotateY",contentsRotateY);
        params.put("contentsRotateZ",contentsRotateZ);

        //add params of img
        try {
            File img = new File(imgPath);
            params.put("image",img);
        }catch (FileNotFoundException e){e.printStackTrace();}

        //string data request
        this.client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                if(result.equals("True"))
                    callback.onResponse(true);
                else
                    callback.onResponse(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void requestContents(String subUrl, float longitude, float latitude, String imgPath, final JsonResponseCallback callback){
        String url = mBaseUrl + subUrl;

        RequestParams params = new RequestParams();
        params.put("longitude",longitude);
        params.put("latitude",latitude);

        try {
            File img = new File(imgPath);
            params.put("image",img);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        this.client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                callback.onResponse(responseBody);

            }
        });
    }
    */

}
