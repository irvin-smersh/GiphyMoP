package com.example.user.giphymop.network;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.example.user.giphymop.Model.Data;

public class SearchDataSourceFactory extends DataSource.Factory<Integer, Data> {

    private SearchDataSource searchDataSource;
    private MutableLiveData<SearchDataSource> mutableLiveData;
    private String query;

    public SearchDataSourceFactory(String query){
        mutableLiveData = new MutableLiveData<>();
        this.query = query;
    }

    @Override
    public DataSource<Integer, Data> create() {
        searchDataSource = new SearchDataSource(query);
        mutableLiveData.postValue(searchDataSource);
        return searchDataSource;
    }

    public MutableLiveData<SearchDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
