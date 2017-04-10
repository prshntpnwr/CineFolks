package com.example.prashant.myapplication.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.fragment.MovieDetailFragment;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, new MovieDetailFragment())
                    .commit();
        }
    }

}
