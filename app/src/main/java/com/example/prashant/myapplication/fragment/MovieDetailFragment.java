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
import com.example.prashant.myapplication.adapter.MoviesDetailAdapter;
import com.example.prashant.myapplication.data.MoviesContract.MoviesEntry;
import com.example.prashant.myapplication.data.MoviesProviderHelper;
import com.example.prashant.myapplication.objects.MoviesDetail;
import com.example.prashant.myapplication.ui.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDetailFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private MoviesDetail movie;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private ImageView mBackdrop;

    private ArrayList<String> trailerInfo = new ArrayList<>();
    private ArrayList<String> reviewInfo = new ArrayList<>();

    private FloatingActionButton fab;

    private static final String MoviesApp_SHARE_HASHTAG = " #MoviesApp";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        String id = getActivity().getIntent().getStringExtra("id");
        Log.d(TAG, "intent receive from adapter is " + id);
        movie = new MoviesDetail();

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

        getMovieDataFromID(id);
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

    private void getMovieDataFromID(final String id) {
        Log.d(TAG, " getMovieDataFromID id is " + id);
        String url = Urls.MOVIE_BASE_URL + id + "?" + Urls.API_KEY;

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest getDetails = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    movie.setId(Integer.valueOf(id));
                    movie.setTitle(response.getString("title"));
                    movie.setRating(String.valueOf(response.getDouble("vote_average")));
                    String genres = "";
                    JSONArray genreArray = response.getJSONArray("genres");
                    for (int i = 0; i < genreArray.length(); i++) {
                        String genre = genreArray.getJSONObject(i).getString("name");
                        Log.d(TAG, "Movie genre is - " + genre);
                        if (i != genreArray.length() - 1)
                            genres += genre + ", ";
                        else
                            genres += genre + ".";
                    }
                    movie.setGenre(genres);
                    movie.setDate(response.getString("release_date"));
                    movie.setStatus(response.getString("status"));
                    movie.setOverview(response.getString("overview"));
                    movie.setBackdrop("http://image.tmdb.org/t/p/w780/" + response.getString("backdrop_path"));
                    movie.setVoteCount(String.valueOf(response.getInt("vote_count")));
                    movie.setTagLine(response.getString("tagline"));
                    movie.setRuntime(String.valueOf(response.getInt("runtime")));
                    Log.d(TAG, "Movie duration is - " + String.valueOf(response.getInt("runtime")));
                    movie.setLanguage(response.getString("original_language"));
                    Log.d(TAG, "Movie language is - " + response.getString("original_language"));
                    movie.setPopularity(String.valueOf(response.getDouble("popularity")));
                    movie.setPoster("http://image.tmdb.org/t/p/w342/" + response.getString("poster_path"));

                    mCollapsingToolbarLayout.setTitle(movie.getTitle());
                    fabAction();

                    try {
                        //image loading and setting color using glide and palette
                        Glide.with(getContext())
                                .load(movie.getBackdrop())
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

                    mAdapter = new MoviesDetailAdapter(movie, trailerInfo, reviewInfo, getContext());
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

        String requestUrl = Urls.MOVIE_BASE_URL + id + "/videos?" + Urls.API_KEY;

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
                    mAdapter = new MoviesDetailAdapter(movie, trailerInfo, reviewInfo, getContext());
                    mAdapter.notifyDataSetChanged();
                    getMovieReviews(id);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getMovieReviews(id);
                error.printStackTrace();
            }
        });

        queue.add(mTrailerRequest);
    }

    void getMovieReviews(String id) {
        reviewInfo.clear();

        String reviewUrl = Urls.MOVIE_BASE_URL + id + "/reviews?" + Urls.API_KEY;

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

    private void fabAction() {

        boolean isMovieInDB = MoviesProviderHelper
                .isMovieInDatabase(getActivity(),
                        String.valueOf(movie.getId()));

        if (isMovieInDB) {
            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_unfavorite));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isMovieInDB = MoviesProviderHelper
                        .isMovieInDatabase(getActivity(),
                                String.valueOf(movie.getId()));

                if (isMovieInDB) {
                    Uri contentUri = MoviesEntry.CONTENT_URI;
                    getActivity().getContentResolver().delete(contentUri, "id=?", new String[]{String.valueOf(movie.getId())});
                    Snackbar.make(view, getResources().getString(R.string.remove_favourites), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_unfavorite));

                } else {
                    ContentValues values = new ContentValues();
                    values.put(MoviesEntry.KEY_ID, movie.getId());
                    values.put(MoviesEntry.KEY_TITLE, movie.getTitle());
                    values.put(MoviesEntry.KEY_RATING, movie.getRating());
                    values.put(MoviesEntry.KEY_GENRE, movie.getGenre());
                    values.put(MoviesEntry.KEY_DATE, movie.getDate());
                    values.put(MoviesEntry.KEY_STATUS, movie.getStatus());
                    values.put(MoviesEntry.KEY_OVERVIEW, movie.getOverview());
                    values.put(MoviesEntry.KEY_BACKDROP, movie.getBackdrop());
                    values.put(MoviesEntry.KEY_VOTE_COUNT, movie.getVoteCount());
                    values.put(MoviesEntry.KEY_TAG_LINE, movie.getTagLine());
                    values.put(MoviesEntry.KEY_RUN_TIME, movie.getRuntime());
                    values.put(MoviesEntry.KEY_LANGUAGE, movie.getLanguage());
                    values.put(MoviesEntry.KEY_POPULARITY, movie.getPopularity());
                    values.put(MoviesEntry.KEY_POSTER, movie.getPoster());

                    getActivity().getContentResolver().insert(MoviesEntry.CONTENT_URI, values);

                    Snackbar.make(view, getResources().getString(R.string.add_favourites), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite));
                }
            }
        });
    }
}
