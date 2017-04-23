package com.example.prashant.myapplication.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.fragment.SearchFragment;
import com.example.prashant.myapplication.fragment_tv.TvDetailFragment;

public class SearchActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_search, new SearchFragment())
                    .commit();

            Log.d(TAG, "Transition happened");
        }
    }
}
