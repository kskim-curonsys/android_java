package com.curonsys.android_java.http;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.curonsys.android_java.model.ContentModel;
import com.curonsys.android_java.model.ContentsListModel;
import com.curonsys.android_java.model.MarkerListModel;
import com.curonsys.android_java.model.MarkerModel;
import com.curonsys.android_java.model.UserContentsModel;
import com.curonsys.android_java.model.UserModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ijin-yeong on 2018. 7. 23..
 */

public class RequestManager {
    private static final String TAG = RequestManager.class.getSimpleName();

    private static String mBaseUrl = "https://kskim-curonsys.com";

    private static RequestManager mInstance;
    public static RequestManager getInstance() {
        if (mInstance == null) {
            mInstance = new RequestManager();
        }
        return mInstance;
    }

    private AsyncHttpClient client;
    private FirebaseFirestore mFirestore;

    public interface JsonResponseCallback{
        public void onResponse(JSONObject success);
    }

    public interface BoolResponseCallback{
        public void onResponse(Boolean success);
    }

    public interface StringResponseCallback{
        public void onResponse(String success);
    }

    public interface UserCallback {
        public void onResponse(UserModel response);
    }

    public interface ContentsListCallback {
        public void onResponse(ArrayList<ContentsListModel> response);
    }

    public interface ContentCallback {
        public void onResponse(ContentModel response);
    }

    public interface MarkerListCallback {
        public void onResponse(ArrayList<MarkerListModel> response);
    }

    public interface MarkerCallback {
        public void onResponse(MarkerModel response);
    }

    public RequestManager() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    public RequestManager(String baseurl) {
        mBaseUrl = baseurl;
        mFirestore = FirebaseFirestore.getInstance();
    }

    public void requestGetUserInfo(String userid, final UserCallback callback) {
        DocumentReference docRef = mFirestore.collection("users").document(userid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                UserModel model = new UserModel(documentSnapshot.getData());
                callback.onResponse(model);
            }
        });
    }

    public void requestSetUserInfo(UserModel data, final UserCallback callback) {

    }

    public void requestGetContentInfo(String contentid, final ContentCallback callback) {
        DocumentReference docRef = mFirestore.collection("models").document(contentid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                ContentModel model = new ContentModel(documentSnapshot.getData());
                callback.onResponse(model);
            }
        });
    }

    public void requestSetContentInfo(ContentModel data, final ContentCallback callback) {

    }

    public void requestGetMarkerList(LatLng location, final MarkerListCallback callback) {
        ArrayList<MarkerListModel> data = new ArrayList<MarkerListModel>();

        //...
        callback.onResponse(data);
    }

    public void requestGetMarkerInfo(String markerid, final MarkerCallback callback) {
        MarkerModel model = new MarkerModel();

        //...
        callback.onResponse(model);
    }

    public void requestSetMarkerInfo(MarkerModel data, final MarkerCallback callback) {

    }


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
