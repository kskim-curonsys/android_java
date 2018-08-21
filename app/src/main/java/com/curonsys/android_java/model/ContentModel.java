package com.curonsys.android_java.model;

import java.util.ArrayList;

public class ContentModel {
    /*
    ContentsURL            =   다운받을 컨텐츠 Url
    ContentsTextureURL     =   다운받을 textureUrl (여러 개일 수 있음)
    ContentsType           =   가져오는 컨텐츠의 type(video or 3d contents or 2d img)
    MarkerImageURL         =   트래킹할 마커 이미지URL
    ContentsScale          =   컨텐츠 크기값 (default)
    ContentsRotationX      =   컨텐츠 X축 방향값 (default)
    ContentsRotationY      =   컨텐츠 Y축 방향값 (default)
    ContentsRotationZ      =   컨텐츠 Z축 방향값 (default)
    ContentsSoundUrl       =   가져오는 컨텐츠의 type(video or 3d contents or 2d img)
    */

    private String content_id;
    private String content_name;
    private String content_describe;
    private ArrayList<String> content_url;
    private ArrayList<String> texture_url;
    private Integer content_type;
    private String marker_url;
    private float content_scale;
    private float content_rotation_x;
    private float content_rotation_y;
    private float content_rotation_z;
    private String sound_url;
    private String video_url;
    private String content_version;

    public ContentModel(String id, String name, String describe, ArrayList<String> contents, ArrayList<String> textures,
                        Integer type, String marker, float scale, float x, float y, float z,
                        String sound, String video, String version) {
        content_id = id;
        content_name = name;
        content_describe = describe;
        content_url = contents;
        texture_url = textures;
        content_type = type;
        marker_url = marker;
        content_scale = scale;
        content_rotation_x = x;
        content_rotation_y = y;
        content_rotation_z = z;
        sound_url = sound;
        video_url = video;
        content_version = version;
    }

    public ContentModel() {
        content_id = "";
        content_name = "";
        content_describe = "";
        content_url = new ArrayList<String>();
        texture_url = new ArrayList<String>();
        content_type = 0;
        marker_url = "";
        content_scale = 0;
        content_rotation_x = 0;
        content_rotation_y = 0;
        content_rotation_z = 0;
        sound_url = "";
        video_url = "";
        content_version = "";
    }

    public String getContentId() {
        return content_id;
    }

    public String getContentName() {
        return content_name;
    }

    public String getContentDescribe() {
        return content_describe;
    }

    public ArrayList<String> getContentUrl() {
        return content_url;
    }

    public ArrayList<String> getTextureUrl() {
        return texture_url;
    }

    public Integer getContentType() {
        return content_type;
    }

    public String getMarkerUrl() {
        return marker_url;
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

    public String getSoundUrl() {
        return sound_url;
    }

    public String getVideoUrl() {
        return video_url;
    }

    public String getContentVersion() {
        return content_version;
    }
}
