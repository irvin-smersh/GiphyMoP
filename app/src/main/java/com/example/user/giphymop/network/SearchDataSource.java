package com.example.user.giphymop.network;

import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;

import com.example.user.giphymop.Model.Data;
import com.example.user.giphymop.Model.TrendingResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchDataSource extends PositionalDataSource<Data> {

    public static String API_KEY = "vEr4YPzholbqJ8yfz25fA6qjlFEgAlxR";
    private GiphyApi giphyApi;
    public static int COUNT = 10;
    String query;

    public SearchDataSource(String query){
        giphyApi = RetrofitService.createService(GiphyApi.class);
        this.query = query;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Data> callback) {

        int position = computeInitialLoadPosition(params, COUNT);
        int loadSize = computeInitialLoadSize(params, position, COUNT);

        giphyApi.getSearchGifs(API_KEY, query, 10, 10).enqueue(new Callback<TrendingResponse>() {
            @Override
            public void onResponse(Call<TrendingResponse> call, Response<TrendingResponse> response) {
                if(response.isSuccessful()) {
                    TrendingResponse trendingResponse = response.body();
                    ArrayList<Data> gifs;
                    gifs =(ArrayList<Data>) trendingResponse.getData();
                    callback.onResult(gifs, position);
                }
            }

            @Override
            public void onFailure(Call<TrendingResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Data> callback) {
        giphyApi.getSearchGifs(API_KEY, query,params.startPosition+10, params.startPosition + params.loadSize).enqueue(new Callback<TrendingResponse>() {
            @Override
            public void onResponse(Call<TrendingResponse> call, Response<TrendingResponse> response) {
                if(response.isSuccessful()) {
                    TrendingResponse trendingResponse = response.body();
                    List<Data> gifs;
                    gifs = trendingResponse.getData();
                    callback.onResult(gifs);
                }
            }

            @Override
            public void onFailure(Call<TrendingResponse> call, Throwable t) {

            }
        });
    }
}
