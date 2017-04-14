package com.example.prashant.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.adapter.MovieListAdapter;
import com.example.prashant.myapplication.data.MoviesProviderHelper;
import com.example.prashant.myapplication.objects.Movies;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private ArrayList<Movies> mMovieList = new ArrayList<>();

    private View mRootView;
    private RecyclerView mRecyclerView;
    private MovieListAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_list_main, container, false);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        mAdapter = new MovieListAdapter(mMovieList, getActivity());
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);

        getMovieList();
        setupRecyclerView(mRecyclerView);
        return mRecyclerView;
    }

    private void getMovieList() {
        Log.d(TAG, "get movie list is called");

        ArrayList<Movies> list = new ArrayList<>(MoviesProviderHelper
                .getMovieListFromDatabase(getActivity()));
        mMovieList.clear();
        for (Movies movie : list) {
            mMovieList.add(movie);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getMovieList();
        mAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
