package com.example.prashant.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
//    private int lastPosition = -1;

    public MovieListAdapter(ArrayList<Movies> MovieList, Context context) {
        this.mMovieList = MovieList;
        this.mContext = context;
        Log.d(TAG, " Movie adapter MovieList " + MovieList.size() + " " + mMovieList.size());
    }

    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle args = new Bundle();
                args.putParcelable("movie", mMovieList.get((vh.getAdapterPosition())));
                Intent intent = new Intent(v.getContext(), MovieDetailActivity.class);
                intent.putExtras(args);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    final ImageView image = (ImageView) view.findViewById(R.id.image);

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation
                            ((Activity) view.getContext(), image, view.getContext()
                                    .getResources().getString(R.string.transition_movie_photo));
                    view.getContext().startActivity(intent, optionsCompat.toBundle());
                } else {
                    view.getContext().startActivity(intent);
                }

            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final MovieListAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        holder.titleView.setText(mMovieList.get(position).getTitle());
        holder.titleView.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "Roboto-Regular.ttf"));
        holder.ratingView.setText(mMovieList.get(position).getRating());
        if (mMovieList.get(position).getDate() != null) {
            String date = mMovieList.get(position).getDate().replace("Release :", "");
            holder.yearView.setText(date.substring(0, date.indexOf("-")));
        }

    //    setAnimation(holder.itemView, position);

        Glide.with(mContext)
                .load(mMovieList.get(position).getPoster())
                .placeholder(R.color.photo_placeholder)
                .error(R.color.colorPrimaryDark)
                .into(holder.imageView);
        Log.d(TAG, " Movie adapter poster " + mMovieList.get(position).getPoster());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, " Movie adapter getItemCount " + mMovieList.size());
        return mMovieList.size();
    }

//    private void setAnimation(View viewToAnimate, int position) {
//        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition) {
//            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
//            viewToAnimate.startAnimation(animation);
//            lastPosition = position;
//        }
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleView;
        TextView ratingView;
        TextView yearView;

        ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image);
            titleView = (TextView) v.findViewById(R.id.title);
            ratingView = (TextView) v.findViewById(R.id.list_rating);
            yearView = (TextView) v.findViewById(R.id.list_year);
        }
    }
}
