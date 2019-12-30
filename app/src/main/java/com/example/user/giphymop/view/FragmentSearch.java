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
import com.example.user.giphymop.viewModels.SearchViewModel;
import com.example.user.giphymop.adapters.TrendingAdapter;
import com.example.user.giphymop.interfaces.AdapterCommunicator;


public class FragmentSearch extends Fragment implements AdapterCommunicator {

    SwipeRefreshLayout mSwipe;
    RecyclerView mRecyclerView;
    TrendingAdapter mTrendingAdapter;
    PagedList<Data> dataArrayList;
    SearchViewModel searchViewModel;
    private String mQuery;

    public FragmentSearch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_search, container, false);
        Bundle args = getArguments();
        mQuery = args.getString("search_text");
        mRecyclerView = layout.findViewById(R.id.recycler_search);
        mSwipe = layout.findViewById(R.id.swipe_search);
        getRecyclerData(mQuery);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRecyclerData(mQuery);
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

    private void getRecyclerData(String query){
        searchViewModel = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);
        searchViewModel.init(mQuery);
        mTrendingAdapter = new TrendingAdapter(getContext(), new AdapterCommunicator() {
            @Override
            public void onItemClick(String url) {
                ((MainActivity)getActivity()).openPreviewFragment(url);
            }
        });
        searchViewModel.getGifsPagedList().observe(getActivity(), new Observer<PagedList<Data>>() {
            @Override
            public void onChanged(@Nullable PagedList<Data> data) {
                dataArrayList = data;
                mTrendingAdapter.submitList(dataArrayList);
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
                mRecyclerView.setAdapter(mTrendingAdapter);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mTrendingAdapter.notifyDataSetChanged();
            }
        });
    }
}
