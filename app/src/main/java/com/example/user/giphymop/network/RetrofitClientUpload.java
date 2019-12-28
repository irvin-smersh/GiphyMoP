package com.example.user.giphymop.network;

public class RetrofitClientUpload {
    public static final String BASE_URL = "http://upload.giphy.com/";

    public static GiphyApi getFileService(){
        return RetrofitServiceUpload.getClient(BASE_URL).create(GiphyApi.class);
    }
}