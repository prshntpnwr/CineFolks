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
import com.example.prashant.myapplication.objects.TV;
import com.example.prashant.myapplication.ui.TvDetailActivity;

import java.util.ArrayList;

public class TVListAdapter extends RecyclerView.Adapter<TVListAdapter.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private ArrayList<TV> mTvList = new ArrayList<>();
    private Context mContext;

    public TVListAdapter(ArrayList<TV> TvList, Context context) {
        this.mTvList = TvList;
        this.mContext = context;
        Log.d(TAG, " Tv adapter TvList " + TvList.size() + " " + mTvList.size());
    }

    public String getItem(int position) {
        return mTvList.get(position).getId();
    }


    @Override
    public TVListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        Log.d(TAG, "Tv adapter onCreateViewHolder");

        final ViewHolder vh = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle args = new Bundle();
                args.putString("id_", getItem(vh.getAdapterPosition()));
                Intent intent = new Intent(v.getContext(), TvDetailActivity.class);
                intent.putExtras(args);
                v.getContext().startActivity(intent);
                Log.d(TAG, "id from tv adapter " + getItem(vh.getAdapterPosition()));

            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(final TVListAdapter.ViewHolder holder, int position) {

        Log.d(TAG, " Tv adapter onBindViewHolder ");
        holder.titleView.setText(mTvList.get(position).getTitle());
        holder.ratingView.setText(mTvList.get(position).getRating());

        Glide.with(mContext)
                .load(mTvList.get(position).getImage())
                .placeholder(R.color.colorAccent)
                .error(R.color.colorPrimaryDark)
                .into(holder.imageView);

        Log.d(TAG, " Tv adapter overview " + mTvList.get(position).getOverview());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, " Tv adapter getItemCount " + mTvList.size());
        return mTvList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleView;
        TextView ratingView;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image);
            titleView = (TextView) v.findViewById(R.id.title);
            ratingView = (TextView) v.findViewById(R.id.list_rating);
        }
    }
}
