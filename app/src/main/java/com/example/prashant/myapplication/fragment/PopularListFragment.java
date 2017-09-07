package com.example.prashant.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.adapter.MovieListAdapter;
import com.example.prashant.myapplication.objects.Movies;
import com.example.prashant.myapplication.ui.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PopularListFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private ArrayList<Movies> mMovieList = new ArrayList<>();

    private View mRootView;
    private RecyclerView mRecyclerView;
    private MovieListAdapter mAdapter;
    private StaggeredGridLayoutManager sglm;
    private String url;

    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private int previousTotal = 0;
    private boolean isLoading = true;
    private int visibleThreshold = 4;
    private int pageCount = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_list_main, container, false);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);

        mAdapter = new MovieListAdapter(mMovieList, getContext());

        url = Urls.BASE_URL + Urls.API_KEY + Urls.SORT_POPULARITY;

        fetchMovieTask(url);
        setupRecyclerView(mRecyclerView);
        return mRecyclerView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mMovieList = savedInstanceState.getParcelableArrayList("mMoviesList");
            pageCount = savedInstanceState.getInt("pageCount");
            previousTotal = savedInstanceState.getInt("previousTotal");
            firstVisibleItem = savedInstanceState.getInt("firstVisibleItem");
            visibleItemCount = savedInstanceState.getInt("visibleItemCount");
            totalItemCount = savedInstanceState.getInt("totalItemCount");
            isLoading = savedInstanceState.getBoolean("loading");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("mMoviesList", mMovieList);
        bundle.putInt("pageCount", pageCount);
        bundle.putInt("previousTotal", previousTotal);
        bundle.putInt("firstVisibleItem", firstVisibleItem);
        bundle.putInt("visibleItemCount", visibleItemCount);
        bundle.putInt("totalItemCount", totalItemCount);
        bundle.putBoolean("loading", isLoading);
    }

    private void fetchMovieTask(String url) {

        Log.d(TAG, "URL is - " + url);

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
                                String.valueOf(mResultObject.getInt("id")),
                                String.valueOf(mResultObject.getDouble("vote_average")));
                        Log.d(TAG, " Movies list is - " + movie.toString());
                        mMovieList.add(movie);
                        Log.d(TAG, " mMovieList inside loop is - " + mMovieList);
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
                Log.d(TAG, "fail response from movie api " + error);
            }
        });

        queue.add(getListData);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sglm);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = sglm.getItemCount();

                int[] firstVisibleItemPositions = new int[2];
                firstVisibleItem = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPositions(firstVisibleItemPositions)[0];

                if (isLoading) {
                    if (totalItemCount > previousTotal) {
                        isLoading = false;
                        previousTotal = totalItemCount;
                        pageCount++;
                    }
                }

                if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    String url = Urls.BASE_URL + Urls.API_KEY + Urls.SORT_POPULARITY + "&page=" + String.valueOf(pageCount);
                    Toast.makeText(getContext(), "Loading Page - " + String.valueOf(pageCount), Toast.LENGTH_SHORT).show();
                    fetchMovieTask(url);

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            fetchMovieTask(url);
//                            Toast.makeText(getContext(), "Loading Page - " + String.valueOf(pageCount), Toast.LENGTH_SHORT).show();
//                        }
//                    }, 3000);

                    isLoading = true;
                }
            }
        });

        recyclerView.setAdapter(mAdapter);
    }
}