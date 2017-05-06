package com.example.prashant.myapplication.data;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

import com.example.prashant.myapplication.data.MoviesContract.MoviesEntry;
import com.example.prashant.myapplication.objects.Movies;
import com.example.prashant.myapplication.objects.MoviesDetail;

import java.util.ArrayList;

public class MoviesProviderHelper {

    public static ArrayList<Movies> getMovieListFromDatabase(Activity mAct) {

        ArrayList<Movies> mMovieList = new ArrayList<>();
        Uri contentUri = MoviesContract.MoviesEntry.CONTENT_URI;
        Cursor c = mAct.getContentResolver().query(contentUri, null, null, null, null);
        if (c.moveToFirst()) {
            do {

                Movies movie = new Movies(c.getString(c.getColumnIndex(MoviesEntry.KEY_TITLE)),
                        c.getString(c.getColumnIndex(MoviesEntry.KEY_POSTER)),
                        c.getString(c.getColumnIndex(MoviesEntry.KEY_DATE)),
                        c.getString(c.getColumnIndex(MoviesEntry.KEY_OVERVIEW)),
                        c.getString(c.getColumnIndex(MoviesEntry.KEY_ID)),
                        c.getString(c.getColumnIndex(MoviesEntry.KEY_RATING)));

                mMovieList.add(movie);
            } while (c.moveToNext());
        }
        c.close();
        return mMovieList;
    }

    public static boolean isMovieInDatabase(Activity mAct, String id) {

        ArrayList<Movies> list = new ArrayList<>(MoviesProviderHelper
                .getMovieListFromDatabase(mAct));
        for (Movies listItem : list) {
            if (listItem.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public static MoviesDetail getMovieFromDatabase(Activity activity, String ID) {
        MoviesDetail movie = null;
        Uri contentUri = MoviesEntry.CONTENT_URI;
        Cursor c = activity.getContentResolver().query(contentUri, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                if (ID.equals(c.getString(c.getColumnIndex(MoviesEntry.KEY_ID)))) {
                    movie = new MoviesDetail(Integer.valueOf(c.getString(c.getColumnIndex(MoviesEntry.KEY_ID))),
                            c.getString(c.getColumnIndex(MoviesEntry.KEY_TITLE)),
                            c.getString(c.getColumnIndex(MoviesEntry.KEY_RATING)),
                            c.getString(c.getColumnIndex(MoviesEntry.KEY_GENRE)),
                            c.getString(c.getColumnIndex(MoviesEntry.KEY_DATE)),
                            c.getString(c.getColumnIndex(MoviesEntry.KEY_STATUS)),
                            c.getString(c.getColumnIndex(MoviesEntry.KEY_OVERVIEW)),
                            c.getString(c.getColumnIndex(MoviesEntry.KEY_BACKDROP)),
                            c.getString(c.getColumnIndex(MoviesEntry.KEY_VOTE_COUNT)),
                            c.getString(c.getColumnIndex(MoviesEntry.KEY_TAG_LINE)),
                            c.getString(c.getColumnIndex(MoviesEntry.KEY_RUN_TIME)),
                            c.getString(c.getColumnIndex(MoviesEntry.KEY_LANGUAGE)),
                            c.getString(c.getColumnIndex(MoviesEntry.KEY_POPULARITY)),
                            c.getString(c.getColumnIndex(MoviesEntry.KEY_POSTER)));
                    break;
                }

            } while (c.moveToNext());
        }
        c.close();
        return movie;
    }
}
