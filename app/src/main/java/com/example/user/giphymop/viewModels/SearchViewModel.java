package com.example.user.giphymop.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.example.user.giphymop.Model.Data;
import com.example.user.giphymop.Model.TrendingResponse;
import com.example.user.giphymop.network.SearchDataSource;
import com.example.user.giphymop.network.SearchDataSourceFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<TrendingResponse> mutableLiveData;
    private String query;
    private LiveData<SearchDataSource> searchDataSourceLiveData;
    private Executor executor;
    private LiveData<PagedList<Data>> gifsPagedList;

    public void init(String query){
        if(mutableLiveData != null){
            return;
        }
        this.query = query;
        SearchDataSourceFactory searchDataSourceFactory = new SearchDataSourceFactory(query);
        searchDataSourceLiveData = searchDataSourceFactory.getMutableLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(5)
                .setPrefetchDistance(30)
                .build();

        executor = Executors.newFixedThreadPool(3);

        gifsPagedList = (new LivePagedListBuilder(searchDataSourceFactory, config))
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<PagedList<Data>> getGifsPagedList() {
        return gifsPagedList;
    }
}
