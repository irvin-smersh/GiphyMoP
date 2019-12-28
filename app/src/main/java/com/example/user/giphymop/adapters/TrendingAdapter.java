package com.example.user.giphymop.adapters;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.user.giphymop.Model.Data;
import com.example.user.giphymop.R;
import com.example.user.giphymop.interfaces.AdapterCommunicator;

public class TrendingAdapter extends PagedListAdapter<Data, TrendingAdapter.TrendingViewHolder> {

    Context mContext;
    AdapterCommunicator mCommunicator;

    public TrendingAdapter(Context context, AdapterCommunicator adapterCommunicator){
        super(Data.CALLBACK);
        this.mContext = context;
        this.mCommunicator = adapterCommunicator;
    }

    @NonNull
    @Override
    public TrendingAdapter.TrendingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, viewGroup, false);
        return new TrendingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingViewHolder trendingViewHolder, int i) {
        trendingViewHolder.bindData(getItem(i));
    }

    class TrendingViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        String url;

        private void bindData(Data data){
            Glide.with(mContext).load(data.getImages().get480wStill().getUrl()).into(imageView);
            url = data.getImages().getOriginal().getUrl();
        }

        public TrendingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.trending_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommunicator.onItemClick(url);
                }
            });
        }

    }

}
