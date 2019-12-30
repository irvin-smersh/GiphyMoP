package com.example.user.giphymop.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.giphymop.Model.Data;
import com.example.user.giphymop.R;
import com.example.user.giphymop.adapters.TrendingAdapter;
import com.example.user.giphymop.viewModels.TrendingViewModel;
import com.example.user.giphymop.interfaces.AdapterCommunicator;

public class FragmentTrending extends Fragment implements AdapterCommunicator {

    SwipeRefreshLayout mSwipe;
    RecyclerView mRecyclerView;
    TrendingAdapter mTrendingAdapter;
    PagedList<Data> dataArrayList;
    TrendingViewModel trendingViewModel;

    public FragmentTrending() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_trending, container, false);
        mRecyclerView = layout.findViewById(R.id.recycler_trending);
        mSwipe = layout.findViewById(R.id.swipe_trending);
        getRecyclerData();
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRecyclerData();
                mSwipe.setRefreshing(false);
            }
        });
        return layout;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onItemClick(String url) {
        ((MainActivity)getActivity()).openPreviewFragment(url);
    }

    private void getRecyclerData(){
        trendingViewModel = ViewModelProviders.of(getActivity()).get(TrendingViewModel.class);
        trendingViewModel.init();
        mTrendingAdapter = new TrendingAdapter(getContext(), new AdapterCommunicator() {
            @Override
            public void onItemClick(String url) {
                ((MainActivity)getActivity()).openPreviewFragment(url);
            }
        });
        trendingViewModel.getGifsPagedList().observe(getActivity(), new Observer<PagedList<Data>>() {
            @Override
            public void onChanged(@Nullable PagedList<Data> dataFromLiveData) {
                dataArrayList=dataFromLiveData;
                mTrendingAdapter.submitList(dataArrayList);
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
                mRecyclerView.setAdapter(mTrendingAdapter);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mTrendingAdapter.notifyDataSetChanged();
            }
        });
    }
}
