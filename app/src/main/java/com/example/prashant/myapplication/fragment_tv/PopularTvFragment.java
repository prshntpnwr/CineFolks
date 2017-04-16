package com.example.prashant.myapplication.fragment_tv;

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
import com.example.prashant.myapplication.adapter.TVListAdapter;
import com.example.prashant.myapplication.objects.Movies;
import com.example.prashant.myapplication.objects.TV;
import com.example.prashant.myapplication.ui.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PopularTvFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private View mRootView;
    private RecyclerView mRecyclerView;
    private TVListAdapter mAdapter;

    private StaggeredGridLayoutManager sglm;

    private ArrayList<TV> mTVList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_list_main, container, false);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);

        mAdapter = new TVListAdapter(mTVList, getContext());

        fetchTvTask();
        setupRecyclerView(mRecyclerView);
        return mRecyclerView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    private void fetchTvTask() {

        String url = "http://api.themoviedb.org/3/discover/tv?api_key=b7f57ee32644eb6ddfdca9ca38b5513e";

        Log.d(TAG, " URL - " + url);

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest getListData = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG + " TV-Series Response - ", response.toString());

                    JSONArray mResultArray = response.getJSONArray("results");
                    Log.d(TAG + " TV Response length - ", String.valueOf(mResultArray.length()));

                    for (int i = 0; i < mResultArray.length(); i++) {
                        Log.d(TAG, " Enter into tv response loop "+ i);

                        JSONObject mResultObject = mResultArray.getJSONObject(i);
                        TV tv;tv = new TV(mResultObject.getString("name"),
                                "http://image.tmdb.org/t/p/w342/" + mResultObject.getString("poster_path"),
                                getResources().getString(R.string.release_date) + mResultObject.getString("first_air_date"),
                                mResultObject.getString("overview"),
                                String.valueOf(mResultObject.getInt("id")));
                        Log.d(TAG, " TV list is " + tv.toString());
                        mTVList.add(tv);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, " failed to parse json from TV" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d(TAG, "fail response from api " + error);
            }
        });

        queue.add(getListData);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sglm);
        recyclerView.setAdapter(mAdapter);
    }
}