package com.curonsys.android_java.model;

public class MarkerModel {
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
    */

    private String user_id;
    private String marker_url;
    private double marker_latitude;
    private double marker_longitude;
    private float marker_rating;
    private String content_id;
    private float content_scale;
    private float content_rotation_x;
    private float content_rotation_y;
    private float content_rotation_z;

    public MarkerModel(String userid, String url, double latitude, double longitude, float rating, String contentid,
                       float scale, float x, float y, float z) {
        user_id = userid;
        marker_url = url;
        marker_latitude = latitude;
        marker_longitude = longitude;
        marker_rating = rating;
        content_id = contentid;
        content_scale = scale;
        content_rotation_x = x;
        content_rotation_y = y;
        content_rotation_z = z;
    }

    public MarkerModel() {
        user_id = "";
        marker_url = "";
        marker_latitude = 0;
        marker_longitude = 0;
        marker_rating = 0;
        content_id = "";
        content_scale = 0;
        content_rotation_x = 0;
        content_rotation_y = 0;
        content_rotation_z = 0;
    }

    public String getUserId() {
        return user_id;
    }

    public String getMarkerUrl() {
        return marker_url;
    }

    public double getMarkerLatitude() {
        return marker_latitude;
    }

    public double getMarkerLongitude() {
        return marker_longitude;
    }

    public float getRating() {
        return marker_rating;
    }

    public String getContentId() {
        return content_id;
    }

    public float getContentScale() {
        return content_scale;
    }

    public float getContentRotationX() {
        return content_rotation_x;
    }

    public float getContentRotationY() {
        return content_rotation_y;
    }

    public float getContentRotationZ() {
        return content_rotation_z;
    }
}
