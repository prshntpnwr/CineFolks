package com.example.prashant.myapplication.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Slide;
import android.util.Log;
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
import com.example.prashant.myapplication.server.Urls;
import com.example.prashant.myapplication.ui.MovieCallbackInterFace;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_URL = "urlParam";

    private String mUrl;

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

//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mUrl Parameter 1.
     * @return A new instance of fragment MovieFragment.
     */
    public static MovieFragment newInstance(String url) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString(ARG_URL);
        if (savedInstanceState != null) {
            mUrl = savedInstanceState.getString(ARG_URL);
            mMovieList = savedInstanceState.getParcelableArrayList("mMoviesList");
            pageCount = savedInstanceState.getInt("pageCount");
            previousTotal = savedInstanceState.getInt("previousTotal");
            firstVisibleItem = savedInstanceState.getInt("firstVisibleItem");
            visibleItemCount = savedInstanceState.getInt("visibleItemCount");
            totalItemCount = savedInstanceState.getInt("totalItemCount");
            isLoading = savedInstanceState.getBoolean("loading");
        }

        fetchMovieTask(mUrl);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(ARG_URL, mUrl);
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
        Log.e(TAG, "url 1 - " + url);
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
                    String url = mUrl.concat("&page=" + String.valueOf(pageCount));
                    Log.e(TAG, "url 2 - " + url);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
