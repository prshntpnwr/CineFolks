package com.example.prashant.myapplication.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.fragment.MovieDetailFragment;
import com.example.prashant.myapplication.fragment_tv.TvDetailFragment;

public class TvDetailActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, new TvDetailFragment())
                    .commit();

            Log.d(TAG, "Transition happened");
        }
    }
}
