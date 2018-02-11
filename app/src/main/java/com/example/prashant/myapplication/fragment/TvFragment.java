package com.example.prashant.myapplication.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.adapter.TVListAdapter;
import com.example.prashant.myapplication.objects.TV;
import com.example.prashant.myapplication.server.ServerCall;
import com.example.prashant.myapplication.server.Urls;
import com.example.prashant.myapplication.ui.TvCallbackInterface;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TvFragment} interface
 * to handle interaction events.
 * Use the {@link TvFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TvFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_URL = "urlParam";

    private String mUrl;

    private RecyclerView mRecyclerView;
    private TVListAdapter mAdapter;

    private StaggeredGridLayoutManager sglm;

    private ArrayList<TV> mTVList = new ArrayList<>();

    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 4;
    private int pageCount = 1;

//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mUrl Parameter 1.
     * @return A new instance of fragment TvFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TvFragment newInstance(String mUrl) {
        TvFragment fragment = new TvFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, mUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_URL);
        }

        if (savedInstanceState != null) {
            mUrl = savedInstanceState.getString(ARG_URL);
            mTVList = savedInstanceState.getParcelableArrayList("TvList");
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
        bundle.putString(ARG_URL, mUrl);
        bundle.putParcelableArrayList("TvList", mTVList);
        bundle.putInt("pageCount", pageCount);
        bundle.putInt("previousTotal", previousTotal);
        bundle.putInt("firstVisibleItem", firstVisibleItem);
        bundle.putInt("visibleItemCount", visibleItemCount);
        bundle.putInt("totalItemCount", totalItemCount);
        bundle.putBoolean("loading", loading);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_list_main, container, false);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);

        fetchTvTask(mUrl);
        setupRecyclerView(mRecyclerView);
        return mRootView;
    }

    private void fetchTvTask(String url) {
        ServerCall.getTvShows(getActivity(), url, new TvCallbackInterface() {
            @Override
            public void onSuccessResponse(TV tv) {
                mTVList.add(tv);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailResponse(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
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
                    String url = mUrl.concat("&page=" + String.valueOf(pageCount));
                    fetchTvTask(url);

                    loading = true;
                }
            }
        });

        mAdapter = new TVListAdapter(mTVList, getContext());
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
    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
