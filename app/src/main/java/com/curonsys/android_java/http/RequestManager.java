package com.curonsys.android_java.http;

import android.graphics.Bitmap;
import android.widget.Toast;

import com.curonsys.android_java.model.ContentModel;
import com.curonsys.android_java.model.ContentsListModel;
import com.curonsys.android_java.model.MarkerListModel;
import com.curonsys.android_java.model.MarkerModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;
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

    private String baseUrl = "";
    private String absoluteUrl = "";
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

    public RequestManager(String url){
        this.baseUrl = url;
        mFirestore = FirebaseFirestore.getInstance();
    }


    public void requestContentsList(String userid, final ContentsListCallback callback) {
        ArrayList<ContentsListModel> data = new ArrayList<ContentsListModel>();

        //...
        callback.onResponse(data);
    }

    public void requestContentInfo(String contentid, final ContentCallback callback) {
        ContentModel model = new ContentModel();

        //...
        callback.onResponse(model);
    }

    public void requestMarkerList(LatLng location, final MarkerListCallback callback) {
        ArrayList<MarkerListModel> data = new ArrayList<MarkerListModel>();

        //...
        callback.onResponse(data);
    }

    public void requestMarkerInfo(String markerid, final MarkerCallback callback) {
        MarkerModel model = new MarkerModel();

        //...
        callback.onResponse(model);
    }




    public void requestContentsOfUser(String subUrl, String id, final JsonResponseCallback callback) throws JSONException{
        this.absoluteUrl = baseUrl + subUrl;

        RequestParams params = new RequestParams();
        params.add("userIdentify",id);


        this.client.get(absoluteUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                callback.onResponse(responseBody);

                /*
                   response 데이터 :
                   ** ContentsIndentify
                      ContentsName
                      ContentsDescribe
                      ThumbNailUrl
                */

            }
        });
    }

    public void requestInfoOfSelectedContents(String subUrl, String id, final JsonResponseCallback callback) throws JSONException{
        this.absoluteUrl = baseUrl + subUrl;

        RequestParams params = new RequestParams();
        params.add("contentsIndentify",id);


        this.client.get(absoluteUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                callback.onResponse(responseBody);
                /*
                ContentsUrl : 다운받을 컨텐츠 URL( .jet or .jpg(png) or mp4)
                ContentsTextureUrl : 다운받을 textureURL (여러 개이거나 null 가능)
                ContentsSoundUrl : 다운받을 Sound(mp3 파일)의 URL(null 가능)
                ContentsType : 가져오는 컨텐츠의 type(video or 3d contents or 2d img)
                */
            }
        });
    }

    public void requestMarkerRegistInfo(String subUrl, String imgPath, String user_id, float rating, float longitude, float latitude,
                                        String contents_id, float contentsScale, float contentsRotateX, float contentsRotateY, float contentsRotateZ,
                                        final BoolResponseCallback callback) throws IOException{
        /*
        UserIdentify    = 등록하는 사용자의 식별 정보
        MarkerRating    = 최종적으로 평가한 마커 평점
        MarkerImage     = 마커로 사용할 이미지
        Longitude       = 경도값
        Latitude        = 위도값
        ContentsIndentify = 3d 컨텐츠의 식별값
        ContentsScale   = 3d 컨텐츠의 스케일 조정 값
        ContentsRotateX = 3d 컨텐츠의 회전X축 조정 값
        ContentsRotateY = 3d 컨텐츠의 회전Y축 조정 값
        ContentsRotateZ = 3d 컨텐츠의 회전Z축 조정 값
        * */
        this.absoluteUrl = baseUrl + subUrl;

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
        this.client.get(absoluteUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                if(result.equals("True"))
                    callback.onResponse(true);
                else
                    callback.onResponse(false);
                /*
                ContentsUrl : 다운받을 컨텐츠 URL( .jet or .jpg(png) or mp4)
                ContentsTextureUrl : 다운받을 textureURL (여러 개이거나 null 가능)
                ContentsSoundUrl : 다운받을 Sound(mp3 파일)의 URL(null 가능)
                ContentsType : 가져오는 컨텐츠의 type(video or 3d contents or 2d img)
                */
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void requestContents(String subUrl, float longitude, float latitude, String imgPath, final JsonResponseCallback callback){
        this.absoluteUrl = baseUrl + subUrl;

        RequestParams params = new RequestParams();
        params.put("longitude",longitude);
        params.put("latitude",latitude);


        try {
            File img = new File(imgPath);
            params.put("image",img);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        this.client.get(absoluteUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                callback.onResponse(responseBody);
                /*
                    ContentsURL            =   다운받을 컨텐츠 Url
                    ContentsTextureURL     =   다운받을 textureUrl (여러 개일 수 있음)
                    ContentsType           =   가져오는 컨텐츠의 type(video or 3d contents or 2d img)
                    MarkerImageURL         =   트래킹할 마커 이미지URL
                    ContentsScale          =   컨텐츠 크기값
                    ContentsRotationX      =   컨텐츠 X축 방향값
                    ContentsRotationY      =   컨텐츠 Y축 방향값
                    ContentsRotationZ      =   컨텐츠 Z축 방향값
                    ContentsSoundUrl       =   가져오는 컨텐츠의 type(video or 3d contents or 2d img)
                */
            }
        });
    }

}
