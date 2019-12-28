package com.example.user.giphymop.Model;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrendingResponse {
    @SerializedName("data")
    @Expose
    private List<Data> data = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public static final DiffUtil.ItemCallback<TrendingResponse> CALLBACK = new DiffUtil.ItemCallback<TrendingResponse>() {
        @Override
        public boolean areItemsTheSame(@NonNull TrendingResponse trendingResponse, @NonNull TrendingResponse t1) {
            return trendingResponse==t1;
        }

        @Override
        public boolean areContentsTheSame(@NonNull TrendingResponse trendingResponse, @NonNull TrendingResponse t1) {
            return true;
        }

    };
}
