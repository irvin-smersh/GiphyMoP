package com.example.user.giphymop.network;

import com.example.user.giphymop.Model.TrendingResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface GiphyApi {
    @GET("v1/gifs/trending")
    Call<TrendingResponse> getTrendingGifs(@Query("api_key") String apiKey,
                                           @Query("limit") int count,
                                           @Query("offset") int offset);

    @GET("v1/gifs/search")
    Call<TrendingResponse> getSearchGifs(
            @Query("api_key") String apiKey,
            @Query("q") String query,
            @Query("limit") int count,
            @Query("offset") int offset);


    @Multipart
    @POST("v1/gifs?api_key=vEr4YPzholbqJ8yfz25fA6qjlFEgAlxR")
    Call<ResponseBody> uploadVideo(
            @Part MultipartBody.Part video
    );
}
