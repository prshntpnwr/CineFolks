package com.example.prashant.myapplication.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.adapter.MoviesDetailAdapter;
import com.example.prashant.myapplication.objects.MoviesDetail;
import com.example.prashant.myapplication.ui.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDetailFragment extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        String id = getActivity().getIntent().getStringExtra("id");
        movie = new MoviesDetail();
        getMovieDataFromID(id);

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

        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return v;
    }

    private void getMovieDataFromID(final String id) {
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
                    movie.setLanguage(response.getString("original_language"));
                    movie.setPopularity(String.valueOf(response.getDouble("popularity")));
                    movie.setPoster("http://image.tmdb.org/t/p/w342/" + response.getString("poster_path"));

                    Glide.with(getContext())
                            .load(movie.getBackdrop())
                            .placeholder(R.color.colorAccent)
                            .error(R.color.colorPrimaryDark)
                            .into(mBackdrop);
                    mCollapsingToolbarLayout.setTitle(movie.getTitle());

                    mAdapter = new MoviesDetailAdapter(movie, trailerInfo, reviewInfo, getActivity());
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
                    // Specify Adapter
                    mAdapter = new MoviesDetailAdapter(movie, trailerInfo, reviewInfo, getActivity());
                    mAdapter.notifyDataSetChanged();
                    getMovieReviews(id);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getMovieReviews(id);
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

            }
        });

        queue.add(mReviewRequest);
    }
}
