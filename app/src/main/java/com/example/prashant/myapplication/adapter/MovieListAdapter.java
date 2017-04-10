package com.example.prashant.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

    private ArrayList<Movies> mMovieList = new ArrayList<>();
    private Context mContext;

    public MovieListAdapter(ArrayList<Movies> mMovieList, Context context) {
        this.mMovieList = mMovieList;
        this.mContext = context;
    }

    public String getItem(int position) {
        return mMovieList.get(position).getId();
    }

    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_holder, parent, false);

        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle args = new Bundle();
                args.putString("image", getItem(vh.getAdapterPosition()));
                Intent intent = new Intent(v.getContext(), MovieDetailActivity.class);
                intent.putExtras(args);
                v.getContext().startActivity(intent);

            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final MovieListAdapter.ViewHolder holder, final int position) {

        holder.titleView.setText(mMovieList.get(position).getTitle());

        Glide.with(mContext)
                .load(mMovieList.get(position).getImage())
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
