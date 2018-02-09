package com.example.prashant.myapplication.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.example.prashant.myapplication.adapter.TvDetailAdapter;
import com.example.prashant.myapplication.data.TvContract.TvEntry;
import com.example.prashant.myapplication.data.TvProviderHelper;
import com.example.prashant.myapplication.helper.AppController;
import com.example.prashant.myapplication.objects.TvDetail;
import com.example.prashant.myapplication.server.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TvDetailFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private TvDetail tv;

    private RecyclerView mRecyclerView;
    private TvDetailAdapter mAdapter;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private ImageView mBackdrop;

    private ArrayList<String> trailerInfo = new ArrayList<>();

    private FloatingActionButton fab;
    private static final String MoviesApp_SHARE_HASHTAG = " #MoviesApp";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        String id = getActivity().getIntent().getStringExtra("id_");
        Log.d(TAG, " intent receive from adapter is " + id);
        tv = new TvDetail();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_movie_details);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar_layout_movie_details);
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar_movie_details);
        mBackdrop = (ImageView) v.findViewById(R.id.backdrop);

        fab = (FloatingActionButton) v.findViewById(R.id.fab);

        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new TvDetailAdapter(tv, trailerInfo, getContext());
        mRecyclerView.setAdapter(mAdapter);

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
            mToolbar.inflateMenu(R.menu.menu_detail);
            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_share) {
                        String[] data = trailerInfo.get(0).split(",,");

                        try {
                            startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                                    .setType("text/plain")
                                    .setText(Urls.YOUTUBE_URL + data[0] + "\n\n"
                                            + MoviesApp_SHARE_HASHTAG)
                                    .getIntent(), getString(R.string.action_share)));

                            Log.d(TAG, "shared trailer : " + Urls.YOUTUBE_URL + data[0]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                }
            });
        }
    }

    private void getTvDataFromID(final String id) {
        Log.d(TAG, " getTvDataFromID id is " + id);

        String url = Urls.TV_BASE_URL + id + "?" + Urls.API_KEY;

        JsonObjectRequest getDetails = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, " Tv detail response is " + response);
                    tv.setId(Integer.valueOf(id));
                    Log.d(TAG, " Tv detail id is " + id);
                    tv.setTitle(response.getString("name"));
                    Log.d(TAG, " Tv detail title is " + response.getString("name"));
                    tv.setRating(String.valueOf(response.getDouble("vote_average")));
                    Log.d(TAG, " Tv detail rating is " + String.valueOf(response.getDouble("vote_average")));

                    String genres = "";
                    JSONArray genreArray = response.getJSONArray("genres");
                    for (int i = 0; i < genreArray.length(); i++) {
                        String genre = genreArray.getJSONObject(i).getString("name");
                        Log.d(TAG, " Tv genres in loop is -  " + genres);
                        if (i != genreArray.length() - 1)
                            genres += genre + ", ";
                        else
                            genres += genre + ".";
                    }

                    tv.setGenre(genres);
                    Log.d(TAG, " Tv genres is - " + genres);

                    tv.setDate(response.getString("first_air_date"));
                    Log.d(TAG, " Tv date is - " + response.getString("first_air_date"));

                    tv.setStatus(response.getString("status"));
                    Log.d(TAG, " Tv status is - " + response.getString("status"));

                    tv.setOverview(response.getString("overview"));
                    Log.d(TAG, " Tv overview is - " + response.getString("overview"));

                    tv.setBackdrop("http://image.tmdb.org/t/p/w780/" + response.getString("backdrop_path"));
                    Log.d(TAG, " Tv backdrop is - " + "http://image.tmdb.org/t/p/w780/" + response.getString("backdrop_path"));

                    tv.setVoteCount(String.valueOf(response.getInt("vote_count")));
                    Log.d(TAG, " Tv vote_count is - " + String.valueOf(response.getInt("vote_count")));

                    tv.setPoster("http://image.tmdb.org/t/p/w342/" + response.getString("poster_path"));
                    Log.d(TAG, " Tv poster_path is - " + "http://image.tmdb.org/t/p/w342/" + response.getString("poster_path"));

                    tv.setLanguage(response.getString("original_language"));
                    Log.d(TAG, " Tv original_language is - " + response.getString("original_language"));

                    tv.setPopularity(String.valueOf(response.getDouble("popularity")));
                    Log.d(TAG, " Tv popularity is - " + String.valueOf(response.getDouble("popularity")));

                    tv.setRuntime(String.valueOf(response.getString("episode_run_time")));
                    Log.d(TAG, " Tv runtime is - " + String.valueOf(response.getString("episode_run_time")));

                    mCollapsingToolbarLayout.setTitle(tv.getTitle());

                    fabAction();

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
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    mAdapter.notifyDataSetChanged();
                    getTrailerInfo(id);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(getDetails);
    }

    private void getTrailerInfo(final String id) {
        String requestUrl = Urls.TV_BASE_URL + id + "/videos?" + Urls.API_KEY;

        JsonObjectRequest mTrailerRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    trailerInfo.clear();
                    JSONArray mTrailerArray = response.getJSONArray("results");
                    for (int i = 0; i < mTrailerArray.length(); i++) {
                        JSONObject mTrailerObject = mTrailerArray.getJSONObject(i);
                        trailerInfo.add(mTrailerObject.getString("key") + ",," + mTrailerObject.getString("name")
                                + ",," + mTrailerObject.getString("site") + ",," + mTrailerObject.getString("size"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    Log.d(TAG, "Trailers are - " + trailerInfo);
                    mAdapter.notifyDataSetChanged();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        AppController.getInstance().addToRequestQueue(mTrailerRequest);
    }

    private void fabAction() {

        boolean isMovieInDB = TvProviderHelper
                .isTvInDatabase(getActivity(),
                        String.valueOf(tv.getId()));

        if (isMovieInDB) {
            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_watchlist));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_unwatch));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isMovieInDB = TvProviderHelper
                        .isTvInDatabase(getActivity(),
                                String.valueOf(tv.getId()));

                if (isMovieInDB) {
                    Uri contentUri = TvEntry.CONTENT_URI;
                    getActivity().getContentResolver().delete(contentUri, "id=?", new String[]{String.valueOf(tv.getId())});
                    Snackbar.make(view, getResources().getString(R.string.remove_tv_favourites), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_unwatch));

                } else {
                    ContentValues values = new ContentValues();
                    values.put(TvEntry.KEY_ID, tv.getId());
                    values.put(TvEntry.KEY_TITLE, tv.getTitle());
                    values.put(TvEntry.KEY_RATING, tv.getRating());
                    values.put(TvEntry.KEY_GENRE, tv.getGenre());
                    values.put(TvEntry.KEY_DATE, tv.getDate());
                    values.put(TvEntry.KEY_STATUS, tv.getStatus());
                    values.put(TvEntry.KEY_OVERVIEW, tv.getOverview());
                    values.put(TvEntry.KEY_BACKDROP, tv.getBackdrop());
                    values.put(TvEntry.KEY_VOTE_COUNT, tv.getVoteCount());
                    values.put(TvEntry.KEY_RUN_TIME, tv.getRuntime());
                    values.put(TvEntry.KEY_LANGUAGE, tv.getLanguage());
                    values.put(TvEntry.KEY_POPULARITY, tv.getPopularity());
                    values.put(TvEntry.KEY_POSTER, tv.getPoster());

                    getActivity().getContentResolver().insert(TvEntry.CONTENT_URI, values);

                    Snackbar.make(view, getResources().getString(R.string.add_tv_favourites), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_watchlist));
                }
            }
        });
    }
}
