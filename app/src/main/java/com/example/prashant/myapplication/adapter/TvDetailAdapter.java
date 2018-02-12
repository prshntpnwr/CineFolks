package com.example.prashant.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.example.prashant.myapplication.objects.TvDetail;
import com.example.prashant.myapplication.server.Urls;

import java.util.ArrayList;

public class TvDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = getClass().getSimpleName();

    private TvDetail tv;
    private Context mContext;
    private ArrayList<String> trailerInfo;

    public TvDetailAdapter(TvDetail tv, ArrayList<String> trailerInfo, Context context) {
        this.tv = tv;
        this.trailerInfo = trailerInfo;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_holder_details, parent, false);
            vh = new ViewHolderDetails(v);
            return vh;
        } else if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_holder_trailer, parent, false);
            vh = new ViewHolderTrailer(v);
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
                    Glide.with(mContext)
                            .load(tv.getPoster())
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

                                    return false;
                                }
                            })
                            .placeholder(R.color.photo_placeholder)
                            .error(R.color.colorPrimaryDark)
                            .into(((ViewHolderDetails) holder).getImageView());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ((ViewHolderDetails) holder).getTitleView().setText(tv.getTitle());
                ((ViewHolderDetails) holder).getDateStatusView().setText(tv.getDate().concat(
                        " (" + tv.getStatus() + ")"));
                ((ViewHolderDetails) holder).getDurationView().setText(mContext.getString(R.string.duration).concat(
                        tv.getRuntime() + mContext.getString(R.string.min)));
                ((ViewHolderDetails) holder).getRatingView().setText(tv.getRating());

                try {
                    ((ViewHolderDetails) holder).getGenreView().setText(tv.getGenre().substring(0,
                            tv.getGenre().indexOf(",")));
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    ((ViewHolderDetails) holder).getGenreView().setText(tv.getGenre().substring(0,
                            tv.getGenre().indexOf(".")));
                }

                ((ViewHolderDetails) holder).getPopularityView().setText(tv.getPopularity().substring(0, 4));
                ((ViewHolderDetails) holder).getLanguageView().setText(tv.getLanguage());

                ((ViewHolderDetails) holder).getOverviewView().setText(tv.getOverview());

                ((ViewHolderDetails) holder).getVoteCountView().setText(tv.getVoteCount().concat(mContext.getString(R.string.votes)));
                break;

            case 1:
                final String[] data = trailerInfo.get(position - 1).split(",,");

                //image loading using glide
                Glide.with(mContext)
                        .load(Urls.YOUTUBE_THUMB + data[0] + Urls.YOUTUBE_MEDIUM_QUALITY)
                        .placeholder(R.color.colorAccent)
                        .error(R.color.colorPrimaryDark)
                        .into(((ViewHolderTrailer) holder).getImageView());

                Log.d(TAG, "trailer info " + Urls.YOUTUBE_THUMB + data[0] + Urls.YOUTUBE_MEDIUM_QUALITY);

                ((ViewHolderTrailer) holder).getTitleView().setText(data[1]);
                ((ViewHolderTrailer) holder).getSiteView().setText(mContext.getString(R.string.site).concat(data[2]));
                ((ViewHolderTrailer) holder).getQualityView().setText(mContext.getString(R.string.quality).concat(data[3] + "p"));

                ((ViewHolderTrailer) holder).getRippleLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.YOUTUBE_URL + data[0])));
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 1 + trailerInfo.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return 0;
        if (position > 0 && position <= trailerInfo.size())
            return 1;
        return 999;
    }

    public static class ViewHolderDetails extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titleView, tagLineView, dateStatusView, durationView,
                ratingView, genreView, popularityView, languageView, overviewView, voteCountView;
        private ImageView ratingsBackground, genreBackground, popBackground, langBackground;

        ViewHolderDetails(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            titleView = view.findViewById(R.id.title);
            tagLineView = view.findViewById(R.id.tag_line);
            dateStatusView = view.findViewById(R.id.date_status);
            durationView = view.findViewById(R.id.duration);
            ratingView = view.findViewById(R.id.rating);
            genreView = view.findViewById(R.id.genre);
            popularityView = view.findViewById(R.id.popularity);
            languageView = view.findViewById(R.id.language);
            overviewView = view.findViewById(R.id.overview);
            ratingsBackground = view.findViewById(R.id.ratings_background);
            voteCountView = view.findViewById(R.id.vote_count);
            genreBackground = view.findViewById(R.id.genre_background);
            popBackground = view.findViewById(R.id.pop_background);
            langBackground = view.findViewById(R.id.lang_background);
        }

        ImageView getImageView() {
            return imageView;
        }

        TextView getTitleView() {
            return titleView;
        }

        TextView getTaglineView() {
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

    public static class ViewHolderTrailer extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private MaterialRippleLayout rippleLayout;
        private TextView titleView, siteView, qualityView;

        ViewHolderTrailer(View view) {
            super(view);
            rippleLayout = view.findViewById(R.id.ripple);
            imageView = view.findViewById(R.id.trailer_image);
            titleView = view.findViewById(R.id.title_text);
            siteView = view.findViewById(R.id.site_text);
            qualityView = view.findViewById(R.id.quality_text);
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
}
