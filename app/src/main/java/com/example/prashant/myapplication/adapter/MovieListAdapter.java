package com.example.prashant.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.example.prashant.myapplication.ui.MovieDetailActivity;

import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private ArrayList<Movies> mMovieList = new ArrayList<>();
    private Context mContext;

    public MovieListAdapter(ArrayList<Movies> MovieList, Context context) {
        this.mMovieList = MovieList;
        this.mContext = context;
        Log.d(TAG, " Movie adapter MovieList " + MovieList.size() + " " + mMovieList.size());
    }

    private String getItem(int position) {
        return mMovieList.get(position).getId();
    }

    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle args = new Bundle();
                args.putString("id", getItem(vh.getAdapterPosition()));
                Intent intent = new Intent(v.getContext(), MovieDetailActivity.class);
                intent.putExtras(args);
                v.getContext().startActivity(intent);
                Log.d(TAG, "detail intent send");

            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final MovieListAdapter.ViewHolder holder, int position) {

        holder.titleView.setText(mMovieList.get(position).getTitle());
        holder.titleView.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "Roboto-Regular.ttf"));
        holder.ratingView.setText(mMovieList.get(position).getRating());

        Log.d(TAG, "movie adapter onBindViewHolder");
        Glide.with(mContext)
                .load(mMovieList.get(position).getImage())
                .placeholder(R.color.photo_placeholder)
                .error(R.color.colorPrimaryDark)
                .into(holder.imageView);
        Log.d(TAG, " Movie adapter poster " + mMovieList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, " Movie adapter getItemCount " + mMovieList.size());
        return mMovieList.size();
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
