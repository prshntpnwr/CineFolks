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

        url = Urls.BASE_URL + Urls.API_KEY + Urls.SORT_POPULARITY;

        fetchMovieTask(url);
        setupRecyclerView(mRecyclerView);
        return mRootView;
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

        mAdapter = new MovieListAdapter(mMovieList, getContext());
        recyclerView.setAdapter(mAdapter);
    }
}