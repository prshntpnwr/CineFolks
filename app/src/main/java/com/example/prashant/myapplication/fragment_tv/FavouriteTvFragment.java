package com.example.prashant.myapplication.fragment_tv;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
    private FrameLayout mEmptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_list_main, container, false);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);

        mEmptyView = (FrameLayout) mRootView.findViewById(R.id.empty_view_container);

        getTvList();
        setupRecyclerView();
        return mRootView;
    }

    private void getTvList() {
        Log.d(TAG, "get tv list is called");

        ArrayList<TV> list = new ArrayList<>(TvProviderHelper
                .getTvListFromDatabase(getActivity()));

        mTvList.clear();
        for (TV tv : list) {
            mTvList.add(tv);
        }

        if (list.isEmpty()) {
            mEmptyView.setVisibility(View.VISIBLE);
            ImageView emptyImage = (ImageView) mRootView.findViewById(R.id.empty_image);
            emptyImage.setImageResource(R.drawable.ic_unwatch);
            TextView emptyText = (TextView) mRootView.findViewById(R.id.empty_text);
            emptyText.setText(getActivity().getResources().getString(R.string.empty_view_text_fab));
            mRecyclerView.setVisibility(View.GONE);
            Log.d(TAG, "Empty view visible");
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            Log.d(TAG, "Empty view gone");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getTvList();
        mAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new TVListAdapter(mTvList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(sglm);
    }
}
