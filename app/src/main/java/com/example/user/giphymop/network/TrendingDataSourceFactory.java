package com.example.user.giphymop.network;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.example.user.giphymop.Model.Data;
import com.example.user.giphymop.network.TrendingDataSource;

public class TrendingDataSourceFactory extends DataSource.Factory<Integer, Data> {

    private TrendingDataSource trendingDataSource;
    private MutableLiveData<TrendingDataSource> mutableLiveData;

    public TrendingDataSourceFactory(){
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        trendingDataSource = new TrendingDataSource();
        mutableLiveData.postValue(trendingDataSource);
        return trendingDataSource;
    }

    public MutableLiveData<TrendingDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
