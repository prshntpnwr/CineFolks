package com.example.prashant.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.objects.Movies;
import com.example.prashant.myapplication.server.Urls;

import java.util.ArrayList;
import java.util.Arrays;

public class MoviesDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = getClass().getSimpleName();

    private Movies movie;
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> trailerInfo;
    private ArrayList<String> reviewInfo;

    public MoviesDetailAdapter(Movies movie, ArrayList<String> trailerInfo, ArrayList<String> reviewInfo, Context context) {
        this.movie = movie;
        this.trailerInfo = trailerInfo;
        this.reviewInfo = reviewInfo;
        this.mContext = context;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == 0) {
            View v = mInflater.inflate(R.layout.layout_holder_details, parent, false);
            vh = new ViewHolderDetails(v);
            return vh;
        }

        if (viewType == 1) {
            View v = mInflater.inflate(R.layout.layout_holder_trailer, parent, false);
            vh = new ViewHolderTrailer(v);
            return vh;
        }

        if (viewType == 2) {
            View v = mInflater.inflate(R.layout.layout_holder_review, parent, false);
            vh = new ViewHolderReview(v);
            return vh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case 0:
                try {

                    //image loading and setting color using glide and palette
                    Log.d(TAG, "movie - " + movie.getId());
                    Glide.with(mContext)
                            .load(movie.getPoster())
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model,
                                                               Target<GlideDrawable> target,
                                                               boolean isFromMemoryCache, boolean isFirstResource) {
                                    Bitmap bitmap = ((GlideBitmapDrawable) resource.getCurrent()).getBitmap();
                                    Palette palette = Palette.generate(bitmap);
                                    int defaultColor = 0xFF333333;
                                    int color = palette.getMutedColor(defaultColor);
                                    ((ViewHolderDetails) holder).getRatingsBackground()
                                            .setColorFilter(color);
                                    ((ViewHolderDetails) holder).getGenreBackground()
                                            .setColorFilter(color);
                                    ((ViewHolderDetails) holder).getPopBackground()
                                            .setColorFilter(color);
                                    ((ViewHolderDetails) holder).getLangBackground()
                                            .setColorFilter(color);
                                    ((ViewHolderDetails) holder).getUpVotes()
                                            .setColorFilter(color);

                                    return false;
                                }
                            })
                            .placeholder(R.color.photo_placeholder)
                            .error(R.color.colorPrimaryDark)
                            .into(((ViewHolderDetails) holder).getImageView());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ((ViewHolderDetails) holder).getTitleView().setText(movie.getTitle());
                ((ViewHolderDetails) holder).getTitleView().setTypeface(Typeface.createFromAsset(mContext.getAssets(), "Roboto-Regular.ttf"));

                if (movie.getTagLine() != null && !movie.getTagLine().equals("")) {
                    ((ViewHolderDetails) holder).getTagLineView().setText("\"" + movie.getTagLine() + "\"");
                } else {
                    ((ViewHolderDetails) holder).getTagLineView().setVisibility(View.GONE);
                }

                ((ViewHolderDetails) holder).getDateStatusView().setText(movie.getDate()
                        + " (" + movie.getStatus() + ")");
                ((ViewHolderDetails) holder).getDurationView().setText(mContext.getString(R.string.duration)
                        + movie.getRuntime() + mContext.getString(R.string.min));
                ((ViewHolderDetails) holder).getRatingView().setText(movie.getRating());

                if (movie.getGenre() != null) {
                    try {
                        ((ViewHolderDetails) holder).getGenreView().setText(movie.getGenre().substring(0,
                                movie.getGenre().indexOf(",")));
                    } catch (StringIndexOutOfBoundsException e) {
                        e.printStackTrace();
                        ((ViewHolderDetails) holder).getGenreView().setText(movie.getGenre().substring(0,
                                movie.getGenre().indexOf(".")));
                    }
                }

                if (movie.getPopularity() != null)
                    ((ViewHolderDetails) holder).getPopularityView().setText(movie.getPopularity().substring(0, 4));
                if (movie.getLanguage() != null)
                    ((ViewHolderDetails) holder).getLanguageView().setText(movie.getLanguage());

                if (movie.getOverview() != null) {
                    ((ViewHolderDetails) holder).getOverviewView().setText(movie.getOverview());
                    ((ViewHolderDetails) holder).getOverviewView().setTypeface(Typeface.createFromAsset(mContext.getAssets(), "Roboto-Medium.ttf"));
                }
                if (movie.getVoteCount() != null) {
                    ((ViewHolderDetails) holder).getVoteCountView().setText(movie.getVoteCount() + " " + mContext.getString(R.string.votes));
                }
                break;

            case 1:
                final String[] data = trailerInfo.get(position - 1).split(",,");
                Log.d(TAG, "trailer - " + Arrays.toString(data));

                //image loading using glide
                Glide.with(mContext)
                        .load(Urls.YOUTUBE_THUMB + data[0] + Urls.YOUTUBE_MEDIUM_QUALITY)
                        .placeholder(R.color.photo_placeholder)
                        .error(R.color.colorPrimaryDark)
                        .into(((ViewHolderTrailer) holder).getImageView());

                ((ViewHolderTrailer) holder).getTitleView().setText(data[1]);
                ((ViewHolderTrailer) holder).getSiteView().setText(mContext.getString(R.string.site) + data[2]);
                ((ViewHolderTrailer) holder).getQualityView().setText(mContext.getString(R.string.quality) + data[3] + "p");

                ((ViewHolderTrailer) holder).getRippleLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.YOUTUBE_URL + data[0])));
                    }
                });
                break;

            case 2:
                ((ViewHolderReview) holder).getReviewView().setText(reviewInfo.get(position - 1 - trailerInfo.size())
                        .substring(reviewInfo.get(position - 1 - trailerInfo.size()).indexOf("-") + 1));
                ((ViewHolderReview) holder).getReviewView().setTypeface(Typeface.createFromAsset(mContext.getAssets(), "Roboto-Medium.ttf"));

                ((ViewHolderReview) holder).getReviewAuthorView().setText(reviewInfo.get(position - 1 - trailerInfo.size())
                        .substring(0, reviewInfo.get(position - 1 - trailerInfo.size()).indexOf("-")));
                ((ViewHolderReview) holder).getReviewView().setTypeface(Typeface.createFromAsset(mContext.getAssets(), "Roboto-Regular.ttf"));

                break;
        }
    }

    @Override
    public int getItemCount() {
        return 1 + trailerInfo.size() + reviewInfo.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return 0;
        if (position > 0 && position <= trailerInfo.size())
            return 1;
        if (position > trailerInfo.size() && position <= trailerInfo.size() + reviewInfo.size())
            return 2;
        return 999;
    }

    private static class ViewHolderDetails extends RecyclerView.ViewHolder {

        private ImageView imageView;

        private TextView titleView, tagLineView, dateStatusView, durationView,
                ratingView, genreView, popularityView, languageView, overviewView, voteCountView;

        private ImageView upVotes, ratingsBackground, genreBackground, popBackground, langBackground;

        ViewHolderDetails(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            titleView = (TextView) view.findViewById(R.id.title);
            tagLineView = (TextView) view.findViewById(R.id.tag_line);
            dateStatusView = (TextView) view.findViewById(R.id.date_status);
            durationView = (TextView) view.findViewById(R.id.duration);
            ratingView = (TextView) view.findViewById(R.id.rating);
            genreView = (TextView) view.findViewById(R.id.genre);
            popularityView = (TextView) view.findViewById(R.id.popularity);
            languageView = (TextView) view.findViewById(R.id.language);
            overviewView = (TextView) view.findViewById(R.id.overview);
            upVotes = (ImageView) view.findViewById(R.id.up_votes);
            ratingsBackground = (ImageView) view.findViewById(R.id.ratings_background);
            voteCountView = (TextView) view.findViewById(R.id.vote_count);
            genreBackground = (ImageView) view.findViewById(R.id.genre_background);
            popBackground = (ImageView) view.findViewById(R.id.pop_background);
            langBackground = (ImageView) view.findViewById(R.id.lang_background);
        }

        ImageView getImageView() {
            return imageView;
        }

        TextView getTitleView() {
            return titleView;
        }

        TextView getTagLineView() {
            return tagLineView;
        }

        TextView getDateStatusView() {
            return dateStatusView;
        }

        TextView getDurationView() {
            return durationView;
        }

        TextView getRatingView() {
            return ratingView;
        }

        TextView getGenreView() {
            return genreView;
        }

        public ImageView getUpVotes() {
            return upVotes;
        }

        TextView getPopularityView() {
            return popularityView;
        }

        TextView getLanguageView() {
            return languageView;
        }

        TextView getOverviewView() {
            return overviewView;
        }

        ImageView getRatingsBackground() {
            return ratingsBackground;
        }

        TextView getVoteCountView() {
            return voteCountView;
        }

        ImageView getGenreBackground() {
            return genreBackground;
        }

        ImageView getPopBackground() {
            return popBackground;
        }

        ImageView getLangBackground() {
            return langBackground;
        }
    }

    private static class ViewHolderTrailer extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private MaterialRippleLayout rippleLayout;
        private TextView titleView, siteView, qualityView;

        ViewHolderTrailer(View view) {
            super(view);
            rippleLayout = (MaterialRippleLayout) view.findViewById(R.id.ripple);
            imageView = (ImageView) view.findViewById(R.id.trailer_image);
            titleView = (TextView) view.findViewById(R.id.title_text);
            siteView = (TextView) view.findViewById(R.id.site_text);
            qualityView = (TextView) view.findViewById(R.id.quality_text);
        }

        MaterialRippleLayout getRippleLayout() {
            return rippleLayout;
        }

        ImageView getImageView() {
            return imageView;
        }

        TextView getTitleView() {
            return titleView;
        }

        TextView getSiteView() {
            return siteView;
        }

        TextView getQualityView() {
            return qualityView;
        }
    }

    private static class ViewHolderReview extends RecyclerView.ViewHolder {

        private TextView reviewView;
        private TextView reviewAuthorView;

        ViewHolderReview(View view) {
            super(view);
            reviewAuthorView = (TextView) view.findViewById(R.id.review_author_text);
            reviewView = (TextView) view.findViewById(R.id.review_text);
        }

        TextView getReviewAuthorView() {
            return reviewAuthorView;
        }

        TextView getReviewView() {
            return reviewView;
        }
    }
}
