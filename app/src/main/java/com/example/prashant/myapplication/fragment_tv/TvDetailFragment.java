package com.example.prashant.myapplication.fragment_tv;

import android.content.ContentValues;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.adapter.MoviesDetailAdapter;
import com.example.prashant.myapplication.adapter.TvDetailAdapter;
import com.example.prashant.myapplication.data.MoviesContract.MoviesEntry;
import com.example.prashant.myapplication.data.MoviesProviderHelper;
import com.example.prashant.myapplication.objects.MoviesDetail;
import com.example.prashant.myapplication.objects.TvDetail;
import com.example.prashant.myapplication.ui.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TvDetailFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private TvDetail tv;

    private RecyclerView mRecyclerView;
    private TvDetailAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private ImageView mBackdrop;

    private ArrayList<String> trailerInfo = new ArrayList<>();
    private ArrayList<String> reviewInfo = new ArrayList<>();

    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        String id = getActivity().getIntent().getStringExtra("id");
        Log.d(TAG, " intent receive from adapter is " + id);
        tv = new TvDetail();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_movie_details);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar_layout_movie_details);
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar_movie_details);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mBackdrop = (ImageView) v.findViewById(R.id.backdrop);

        fab = (FloatingActionButton) v.findViewById(R.id.fab);

        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        setupToolbar();

        getTvDataFromID(id);
        return v;
    }

    private void setupToolbar() {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(ContextCompat
                    .getDrawable(getActivity(), R.drawable.ic_back));
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });

            mToolbar.setTitle("");
        }
    }

    private void getTvDataFromID(final String id) {
        Log.d(TAG, " getTvDataFromID id is " + id);

        String url = Urls.TV_BASE_URL + id + "?" + Urls.API_KEY;

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest getDetails = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, " Tv detail response is " + response);
                    tv.setId(Integer.valueOf(id));
                    Log.d(TAG, " Tv detail id is " + id);
                    tv.setTitle(response.getString("title"));
                    Log.d(TAG, " Tv detail title is " + response.getString("title"));
                    tv.setRating(String.valueOf(response.getDouble("vote_average")));
                    Log.d(TAG, " Tv detail rating is " + String.valueOf(response.getDouble("vote_average")));
                    String genres = "";
                    JSONArray genreArray = response.getJSONArray("genres");
                    for (int i = 0; i < genreArray.length(); i++) {
                        String genre = genreArray.getJSONObject(i).getString("name");
                        if (i != genreArray.length() - 1)
                            genres += genre + ", ";
                        else
                            genres += genre + ".";
                    }
                    tv.setGenre(genres);
                    Log.d(TAG, " Tv genres is " + genres);
                    tv.setDate(response.getString("release_date"));
                    Log.d(TAG, " Tv date is " + response.getString("release_date"));
                    tv.setStatus(response.getString("status"));
                    Log.d(TAG, " Tv status is " + response.getString("status"));
                    tv.setOverview(response.getString("overview"));
                    Log.d(TAG, " Tv overview is " + response.getString("overview"));
                    tv.setBackdrop("http://image.tmdb.org/t/p/w780/" + response.getString("backdrop_path"));
                    Log.d(TAG, " Tv backdrop is " + "http://image.tmdb.org/t/p/w780/" + response.getString("backdrop_path"));
                    tv.setVoteCount(String.valueOf(response.getInt("vote_count")));
                    Log.d(TAG, " Tv vote_count is " + String.valueOf(response.getInt("vote_count")));
                    tv.setTagLine(response.getString("tagline"));
                    Log.d(TAG, " Tv tagline is " + response.getString("tagline"));
                    tv.setRuntime(String.valueOf(response.getInt("runtime")));
                    Log.d(TAG, " Tv runtime is " + String.valueOf(response.getInt("runtime")));
                    tv.setLanguage(response.getString("original_language"));
                    Log.d(TAG, " Tv original_language is " + response.getString("original_language"));
                    tv.setPopularity(String.valueOf(response.getDouble("popularity")));
                    Log.d(TAG, " Tv popularity is " + String.valueOf(response.getDouble("popularity")));
                    tv.setPoster("http://image.tmdb.org/t/p/w342/" + response.getString("poster_path"));
                    Log.d(TAG, " Tv poster_path is " + "http://image.tmdb.org/t/p/w342/" + response.getString("poster_path"));

                    mCollapsingToolbarLayout.setTitle(tv.getTitle());

                    try {
                        //image loading and setting color using glide and palette
                        Glide.with(getContext())
                                .load(tv.getBackdrop())
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
                                        mCollapsingToolbarLayout.setContentScrimColor(color);
                                        fab.setBackgroundTintList(ColorStateList.valueOf(color));

                                        return false;
                                    }
                                })
                                .placeholder(R.color.colorAccent)
                                .error(R.color.colorPrimaryDark)
                                .into(mBackdrop);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mAdapter = new TvDetailAdapter(tv, trailerInfo, reviewInfo, getContext());
                    mRecyclerView.setAdapter(mAdapter);

                    getTrailerInfo(id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(getDetails);
    }

    private void getTrailerInfo(final String id) {
        trailerInfo.clear();

        String requestUrl = Urls.TV_BASE_URL + id + "/videos?" + Urls.API_KEY;

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest mTrailerRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray mTrailerArray = response.getJSONArray("results");
                    for (int i = 0; i < mTrailerArray.length(); i++) {
                        JSONObject mTrailerObject = mTrailerArray.getJSONObject(i);
                        trailerInfo.add(mTrailerObject.getString("key") + ",," + mTrailerObject.getString("name")
                                + ",," + mTrailerObject.getString("site") + ",," + mTrailerObject.getString("size"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    mAdapter = new TvDetailAdapter(tv, trailerInfo, reviewInfo, getContext());
                    mAdapter.notifyDataSetChanged();
                    getTvReviews(id);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getTvReviews(id);
                error.printStackTrace();
            }
        });

        queue.add(mTrailerRequest);
    }

    void getTvReviews(String id) {
        reviewInfo.clear();

        String reviewUrl = Urls.TV_BASE_URL + id + "/reviews?" + Urls.API_KEY;

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest mReviewRequest = new JsonObjectRequest(Request.Method.GET, reviewUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int size = response.getInt("total_results");
                    if (size != 0) {
                        JSONArray mReviewArray = response.getJSONArray("results");
                        for (int i = 0; i < mReviewArray.length(); i++) {
                            JSONObject mReview = mReviewArray.getJSONObject(i);
                            reviewInfo.add(mReview.getString("author") + "-" + mReview.getString("content"));
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(mReviewRequest);
    }
}
