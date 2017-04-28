package com.example.prashant.myapplication.fragment_tv;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.adapter.TVListAdapter;
import com.example.prashant.myapplication.data.TvProviderHelper;
import com.example.prashant.myapplication.objects.TV;

import java.util.ArrayList;

public class FavouriteTvFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private ArrayList<TV> mTvList = new ArrayList<>();

    private View mRootView;
    private RecyclerView mRecyclerView;
    private TVListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_list_main, container, false);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        mAdapter = new TVListAdapter(mTvList, getActivity());

        getTvList();
        setupRecyclerView(mRecyclerView);
        return mRecyclerView;
    }

    private void getTvList() {
        Log.d(TAG, "get tv list is called");

        ArrayList<TV> list = new ArrayList<>(TvProviderHelper
                .getTvListFromDatabase(getActivity()));

        mTvList.clear();
        for (TV tv : list) {
            mTvList.add(tv);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getTvList();
        mAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setAdapter(mAdapter);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sglm);
    }
}
