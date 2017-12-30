package com.example.prashant.myapplication.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.adapter.MoviesDetailAdapter;
import com.example.prashant.myapplication.data.MoviesContract.MoviesEntry;
import com.example.prashant.myapplication.data.MoviesProviderHelper;
import com.example.prashant.myapplication.helper.AppController;
import com.example.prashant.myapplication.objects.Movies;
import com.example.prashant.myapplication.objects.MoviesDetail;
import com.example.prashant.myapplication.server.Urls;
import com.example.prashant.myapplication.ui.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDetailFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private MoviesDetail moviesDetail = new MoviesDetail();

    private Movies movie;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private ImageView mBackdrop;

    private ArrayList<String> trailerInfo = new ArrayList<>();
    private ArrayList<String> reviewInfo = new ArrayList<>();

    private FloatingActionButton fab;

    private static final String MoviesApp_SHARE_HASHTAG = "#MoviesApp";

    private String id = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null)
            movie = bundle.getParcelable("movie");

        if (movie != null) {
            id = movie.getId();
        }
        Log.d(TAG, "intent receive from adapter with id - " + id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_movie_details);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) mRootView.findViewById(R.id.collapsing_toolbar_layout_movie_details);
        mToolbar = (Toolbar) mRootView.findViewById(R.id.toolbar_movie_details);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mBackdrop = (ImageView) mRootView.findViewById(R.id.backdrop);

        fab = (FloatingActionButton) mRootView.findViewById(R.id.fab);

        mCollapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getContext(), android.R.color.white));

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        setupToolbar();

        bindView(id);
        return mRootView;
    }

    private void setupToolbar() {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(ContextCompat
                    .getDrawable(getActivity(), R.drawable.ic_back));
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().supportFinishAfterTransition();
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
                                            + " " + MoviesApp_SHARE_HASHTAG)
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

    private void bindView(final String id) {
        Log.d(TAG, " bindView id is " + id);

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

        if (Utility.isNetworkAvailable(getActivity()))
            getMovieDataFromID(id);

    }

    private void getMovieDataFromID(final String id) {
        Log.d(TAG, " getMovieDataFromID id is " + id);
        String url = Urls.MOVIE_BASE_URL + id + "?" + Urls.API_KEY;

        JsonObjectRequest getDetails = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
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
                    movie.setStatus(response.getString("status"));
                    movie.setOverview(response.getString("overview"));
                    movie.setVoteCount(String.valueOf(response.getInt("vote_count")));
                    movie.setTagLine(response.getString("tagline"));
                    movie.setRuntime(String.valueOf(response.getInt("runtime")));
                    movie.setLanguage(response.getString("original_language"));
                    movie.setPopularity(String.valueOf(response.getDouble("popularity")));

                    mAdapter.notifyDataSetChanged();

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
        AppController.getInstance().addToRequestQueue(getDetails);
    }

    private void getTrailerInfo(final String id) {
        trailerInfo.clear();

        String requestUrl = Urls.MOVIE_BASE_URL + id + "/videos?" + Urls.API_KEY;

        JsonObjectRequest trailerRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
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

        AppController.getInstance().addToRequestQueue(trailerRequest);
    }

    void getMovieReviews(String id) {
        reviewInfo.clear();

        String reviewUrl = Urls.MOVIE_BASE_URL + id + "/reviews?" + Urls.API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, reviewUrl, null, new Response.Listener<JSONObject>() {
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

        AppController.getInstance().addToRequestQueue(request);
    }

    private void fabAction() {

        boolean isMovieInDB = MoviesProviderHelper
                .isMovieInDatabase(getActivity(),
                        String.valueOf(moviesDetail.getId()));

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
                                String.valueOf(moviesDetail.getId()));

                if (isMovieInDB) {
                    Uri contentUri = MoviesEntry.CONTENT_URI;
                    getActivity().getContentResolver().delete(contentUri, "id=?", new String[]{String.valueOf(moviesDetail.getId())});
                    Snackbar.make(view, getResources().getString(R.string.remove_favourites), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_unfavorite));

                } else {
                    ContentValues values = new ContentValues();
                    values.put(MoviesEntry.KEY_ID, moviesDetail.getId());
                    values.put(MoviesEntry.KEY_TITLE, moviesDetail.getTitle());
                    values.put(MoviesEntry.KEY_RATING, moviesDetail.getRating());
                    values.put(MoviesEntry.KEY_GENRE, moviesDetail.getGenre());
                    values.put(MoviesEntry.KEY_DATE, moviesDetail.getDate());
                    values.put(MoviesEntry.KEY_STATUS, moviesDetail.getStatus());
                    values.put(MoviesEntry.KEY_OVERVIEW, moviesDetail.getOverview());
                    values.put(MoviesEntry.KEY_BACKDROP, moviesDetail.getBackdrop());
                    values.put(MoviesEntry.KEY_VOTE_COUNT, moviesDetail.getVoteCount());
                    values.put(MoviesEntry.KEY_TAG_LINE, moviesDetail.getTagLine());
                    values.put(MoviesEntry.KEY_RUN_TIME, moviesDetail.getRuntime());
                    values.put(MoviesEntry.KEY_LANGUAGE, moviesDetail.getLanguage());
                    values.put(MoviesEntry.KEY_POPULARITY, moviesDetail.getPopularity());
                    values.put(MoviesEntry.KEY_POSTER, moviesDetail.getPoster());

                    getActivity().getContentResolver().insert(MoviesEntry.CONTENT_URI, values);

                    Snackbar.make(view, getResources().getString(R.string.add_favourites), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite));
                }
            }
        });
    }
}