package com.example.prashant.myapplication.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
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

    private RecyclerView mRecyclerView;
    private MovieListAdapter mAdapter;
    private StaggeredGridLayoutManager sglm;

    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private int previousTotal = 0;
    private boolean isLoading = true;
    private int visibleThreshold = 4;
    private int pageCount = 1;

    private ProgressBar progressBar;

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
        String url = Urls.BASE_URL + Urls.API_KEY + Urls.SORT_POPULARITY;
        fetchMovieTask(url);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_list_main, container, false);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) mRootView.findViewById(R.id.progressbar);

        loadDetailWindowTransition();

        setupRecyclerView(mRecyclerView);
        return mRootView;
    }

    public void loadDetailWindowTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.excludeTarget(R.id.appbar, true);
            slide.setInterpolator(
                    AnimationUtils.loadInterpolator(getActivity(), android.R.interpolator.linear_out_slow_in));
            slide.setDuration(250);
            getActivity().getWindow().setEnterTransition(slide);
        }
    }

    private void fetchMovieTask(String url) {

        ServerCall.getMovies(getActivity(), url, new MovieCallbackInterFace() {
            @Override
            public void onSuccessResponse(Movies movies) {
                mMovieList.add(movies);
                progressBar.setVisibility(View.GONE);
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
                    progressBar.setVisibility(View.VISIBLE);
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