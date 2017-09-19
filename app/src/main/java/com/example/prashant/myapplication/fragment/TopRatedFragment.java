package com.example.prashant.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.adapter.MovieListAdapter;
import com.example.prashant.myapplication.objects.Movies;
import com.example.prashant.myapplication.server.ServerCall;
import com.example.prashant.myapplication.ui.MovieCallbackInterFace;
import com.example.prashant.myapplication.server.Urls;

import java.util.ArrayList;

public class TopRatedFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private ArrayList<Movies> mMovieList = new ArrayList<>();

    private View mRootView;
    private RecyclerView mRecyclerView;
    private MovieListAdapter mAdapter;
    private StaggeredGridLayoutManager sglm;

    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 4;
    private int pageCount = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_list_main, container, false);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);

        String url = Urls.BASE_URL + Urls.API_KEY + Urls.SORT_TOP_RATED;
        fetchMovieTask(url);
        setupRecyclerView(mRecyclerView);
        return mRootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mMovieList = savedInstanceState.getParcelableArrayList("mMovieList");
            pageCount = savedInstanceState.getInt("pageCount");
            previousTotal = savedInstanceState.getInt("previousTotal");
            firstVisibleItem = savedInstanceState.getInt("firstVisibleItem");
            visibleItemCount = savedInstanceState.getInt("visibleItemCount");
            totalItemCount = savedInstanceState.getInt("totalItemCount");
            loading = savedInstanceState.getBoolean("loading");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("mMovieList", mMovieList);
        bundle.putParcelableArrayList("mMoviesList", mMovieList);
        bundle.putInt("pageCount", pageCount);
        bundle.putInt("previousTotal", previousTotal);
        bundle.putInt("firstVisibleItem", firstVisibleItem);
        bundle.putInt("visibleItemCount", visibleItemCount);
        bundle.putInt("totalItemCount", totalItemCount);
        bundle.putBoolean("loading", loading);
    }

    private void fetchMovieTask(String url) {

        ServerCall.getMovies(getActivity(), url, new MovieCallbackInterFace() {
            @Override
            public void onSuccessResponse(Movies movies) {
                mMovieList.add(movies);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailResponse(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
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

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        pageCount++;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    String url = Urls.BASE_URL + Urls.API_KEY + Urls.SORT_TOP_RATED + "&page=" + String.valueOf(pageCount);
                    Toast.makeText(getContext(), "Loading Page - " + String.valueOf(pageCount), Toast.LENGTH_SHORT).show();
                    fetchMovieTask(url);

                    loading = true;
                }
            }
        });

        mAdapter = new MovieListAdapter(mMovieList, getContext());
        recyclerView.setAdapter(mAdapter);
    }
}
