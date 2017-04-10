package com.example.prashant.myapplication.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.ui.Urls;
import com.example.prashant.myapplication.adapter.MovieListAdapter;
import com.example.prashant.myapplication.objects.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PopularListFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private ArrayList<Movies> mMovieList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_list_main, container, false);

        mAdapter = new MovieListAdapter(mMovieList, getContext());
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);

        fetchMovieTask();
        setupRecyclerView(mRecyclerView);
        return mRecyclerView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mMovieList = savedInstanceState.getParcelableArrayList("mMovies");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mMovies", mMovieList);
    }

    private void fetchMovieTask() {
        url = Urls.BASE_URL + Urls.API_KEY + Urls.SORT_POPULARITY;

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest getListData = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG + "this is the response", response.toString());
                    JSONArray mResultArray = response.getJSONArray("results");
                    for (int i = 0; i < mResultArray.length(); i++) {
                        JSONObject mResultObject = mResultArray.getJSONObject(i);
                        Movies movie = new Movies(mResultObject.getString("title"),
                                "http://image.tmdb.org/t/p/w342/" + mResultObject.getString("poster_path"),
                                getResources().getString(R.string.release_date) + mResultObject.getString("release_date"),
                                mResultObject.getString("overview"),
                                String.valueOf(mResultObject.getInt("id")));
                        mMovieList.add(movie);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                showSnackBar("Something went wrong!");
            }
        });

        queue.add(getListData);
    }

    void showSnackBar(String msg) {
        Snackbar.make(mRecyclerView, msg, Snackbar.LENGTH_LONG)
                .show();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
