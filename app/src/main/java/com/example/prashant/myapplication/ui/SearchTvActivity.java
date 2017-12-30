package com.example.prashant.myapplication.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.fragment.SearchTvFragment;

public class SearchTvActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_search, new SearchTvFragment())
                    .commit();

            Log.d(TAG, "Transition happened");
        }
    }
}
