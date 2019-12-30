package com.example.user.giphymop.network;

import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.user.giphymop.Model.Data;
import com.example.user.giphymop.Model.TrendingResponse;
import com.example.user.giphymop.MyApplication;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingDataSource extends PositionalDataSource<Data> {

    public static String API_KEY = "vEr4YPzholbqJ8yfz25fA6qjlFEgAlxR";
    private GiphyApi giphyApi;
    public static int COUNT = 10;
    String query = null;

    public TrendingDataSource(){
        giphyApi = RetrofitService.createService(GiphyApi.class);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Data> callback) {

        int position = computeInitialLoadPosition(params, COUNT);
        int loadSize = computeInitialLoadSize(params, position, COUNT);

        giphyApi.getTrendingGifs(API_KEY, loadSize, position).enqueue(new Callback<TrendingResponse>() {
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
                Toast.makeText(MyApplication.getInstance(), "Can't reach the server! Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Data> callback) {
        giphyApi.getTrendingGifs(API_KEY, params.loadSize, params.startPosition + params.loadSize).enqueue(new Callback<TrendingResponse>() {
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
                Toast.makeText(MyApplication.getInstance(), "Can't reach the server! Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
