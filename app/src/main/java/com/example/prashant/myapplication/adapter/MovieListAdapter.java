package com.example.prashant.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.objects.Movies;

import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private ArrayList<Movies> mMovieList = new ArrayList<>();
    private Context mContext;

    public MovieListAdapter(ArrayList<Movies> mMovieList, Context context) {
        this.mMovieList = mMovieList;
        this.mContext = context;
    }

    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_holder, parent, false);

        final ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MovieListAdapter.ViewHolder holder, final int position) {

        holder.titleView.setText(mMovieList.get(position).getTitle());

        Glide.with(mContext)
                .load("http://image.tmdb.org/t/p/w342/" + mMovieList.get(position).getImage())
                .placeholder(R.color.colorAccent)
                .error(R.color.colorPrimaryDark)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
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
