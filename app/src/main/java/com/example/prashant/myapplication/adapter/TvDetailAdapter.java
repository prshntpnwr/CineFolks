package com.example.prashant.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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
    private LayoutInflater mInflater;
    private ArrayList<String> trailerInfo;

    public TvDetailAdapter(TvDetail tv, ArrayList<String> trailerInfo, Context context) {
        this.tv = tv;
        this.trailerInfo = trailerInfo;
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
                ((ViewHolderDetails) holder).getTitleView().setTypeface(Typeface.createFromAsset(mContext.getAssets(), "Roboto-Regular.ttf"));

                ((ViewHolderDetails) holder).getDateStatusView().setText(tv.getDate()
                        + " (" + tv.getStatus() + ")");
                ((ViewHolderDetails) holder).getDurationView().setText(mContext.getString(R.string.duration)
                        + tv.getRuntime() + mContext.getString(R.string.min));
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
                ((ViewHolderDetails) holder).getOverviewView().setTypeface(Typeface.createFromAsset(mContext.getAssets(), "Roboto-Medium.ttf"));

                ((ViewHolderDetails) holder).getVoteCountView().setText(tv.getVoteCount() + " " + mContext.getString(R.string.votes));
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
                ((ViewHolderTrailer) holder).getSiteView().setText(mContext.getString(R.string.site) + data[2]);
                ((ViewHolderTrailer) holder).getQualityView().setText(mContext.getString(R.string.quality) + data[3] + "p");

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

        private TextView titleView, taglineView, dateStatusView, durationView,
                ratingView, genreView, popularityView, languageView, overviewView, voteCountView;

        private ImageView ratingsBackground, genreBackground, popBackground, langBackground;

        public ViewHolderDetails(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            titleView = (TextView) view.findViewById(R.id.title);
            taglineView = (TextView) view.findViewById(R.id.tag_line);
            dateStatusView = (TextView) view.findViewById(R.id.date_status);
            durationView = (TextView) view.findViewById(R.id.duration);
            ratingView = (TextView) view.findViewById(R.id.rating);
            genreView = (TextView) view.findViewById(R.id.genre);
            popularityView = (TextView) view.findViewById(R.id.popularity);
            languageView = (TextView) view.findViewById(R.id.language);
            overviewView = (TextView) view.findViewById(R.id.overview);
            ratingsBackground = (ImageView) view.findViewById(R.id.ratings_background);
            voteCountView = (TextView) view.findViewById(R.id.vote_count);
            genreBackground = (ImageView) view.findViewById(R.id.genre_background);
            popBackground = (ImageView) view.findViewById(R.id.pop_background);
            langBackground = (ImageView) view.findViewById(R.id.lang_background);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getTitleView() {
            return titleView;
        }

        public TextView getTaglineView() {
            return taglineView;
        }

        public TextView getDateStatusView() {
            return dateStatusView;
        }

        public TextView getDurationView() {
            return durationView;
        }

        public TextView getRatingView() {
            return ratingView;
        }

        public TextView getGenreView() {
            return genreView;
        }

        public TextView getPopularityView() {
            return popularityView;
        }

        public TextView getLanguageView() {
            return languageView;
        }

        public TextView getOverviewView() {
            return overviewView;
        }

        public ImageView getRatingsBackground() {
            return ratingsBackground;
        }

        public TextView getVoteCountView() {
            return voteCountView;
        }

        public ImageView getGenreBackground() {
            return genreBackground;
        }

        public ImageView getPopBackground() {
            return popBackground;
        }

        public ImageView getLangBackground() {
            return langBackground;
        }
    }

    public static class ViewHolderTrailer extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private MaterialRippleLayout rippleLayout;
        private TextView titleView, siteView, qualityView;

        public ViewHolderTrailer(View view) {
            super(view);
            rippleLayout = (MaterialRippleLayout) view.findViewById(R.id.ripple);
            imageView = (ImageView) view.findViewById(R.id.trailer_image);
            titleView = (TextView) view.findViewById(R.id.title_text);
            siteView = (TextView) view.findViewById(R.id.site_text);
            qualityView = (TextView) view.findViewById(R.id.quality_text);
        }

        public MaterialRippleLayout getRippleLayout() {
            return rippleLayout;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getTitleView() {
            return titleView;
        }

        public TextView getSiteView() {
            return siteView;
        }

        public TextView getQualityView() {
            return qualityView;
        }
    }
}
