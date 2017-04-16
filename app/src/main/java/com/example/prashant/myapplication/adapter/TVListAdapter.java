package com.example.prashant.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.objects.Movies;
import com.example.prashant.myapplication.objects.TV;
import com.example.prashant.myapplication.ui.MovieDetailActivity;

import java.util.ArrayList;

public class TVListAdapter extends RecyclerView.Adapter<TVListAdapter.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private ArrayList<TV> mTvList = new ArrayList<>();
    private Context mContext;

    public TVListAdapter(ArrayList<TV> TvList, Context context) {
        this.mTvList = TvList;
        this.mContext = context;
        Log.d(TAG, "Tv adapter is called");
    }

    @Override
    public TVListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TVListAdapter.ViewHolder holder, int position) {

        holder.titleView.setText(mTvList.get(position).getTitle());

        Glide.with(mContext)
                .load(mTvList.get(position).getImage())
                .placeholder(R.color.colorAccent)
                .error(R.color.colorPrimaryDark)
                .into(holder.imageView);

        Log.d(TAG, "Tv adapter image" + mTvList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mTvList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleView;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image);
            titleView = (TextView) v.findViewById(R.id.title);
        }
    }
}
