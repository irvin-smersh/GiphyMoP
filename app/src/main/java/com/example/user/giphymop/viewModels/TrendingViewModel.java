package com.example.user.giphymop.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.example.user.giphymop.Model.Data;
import com.example.user.giphymop.Model.TrendingResponse;
import com.example.user.giphymop.network.TrendingDataSource;
import com.example.user.giphymop.network.TrendingDataSourceFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TrendingViewModel extends ViewModel {

    //private MutableLiveData<TrendingResponse> mutableLiveData;
    private LiveData<TrendingDataSource> trendingDataSourceLiveData;
    private Executor executor;
    private LiveData<PagedList<Data>> gifsPagedList;

    public void init(){
        /*
        if(mutableLiveData != null){
            return;
        }
*/
        TrendingDataSourceFactory trendingDataSourceFactory = new TrendingDataSourceFactory();
        trendingDataSourceLiveData = trendingDataSourceFactory.getMutableLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(5)
                .setPrefetchDistance(30)
                .build();

        executor = Executors.newFixedThreadPool(3);

        gifsPagedList = (new LivePagedListBuilder(trendingDataSourceFactory, config))
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<PagedList<Data>> getGifsPagedList() {
        return gifsPagedList;
    }
}
