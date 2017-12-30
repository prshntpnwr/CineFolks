package com.example.prashant.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.prashant.myapplication.data.MoviesContract.MoviesEntry;

public class MoviesDbHelper extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + "("
                + MoviesEntry.KEY_ID + " INTEGER PRIMARY KEY,"
                + MoviesEntry.KEY_TITLE + " TEXT,"
                + MoviesEntry.KEY_RATING + " TEXT,"
                + MoviesEntry.KEY_GENRE + " TEXT,"
                + MoviesEntry.KEY_DATE + " TEXT,"
                + MoviesEntry.KEY_STATUS + " TEXT,"
                + MoviesEntry.KEY_OVERVIEW + " TEXT,"
                + MoviesEntry.KEY_BACKDROP + " TEXT,"
                + MoviesEntry.KEY_VOTE_COUNT + " TEXT,"
                + MoviesEntry.KEY_TAG_LINE + " TEXT,"
                + MoviesEntry.KEY_RUN_TIME + " TEXT,"
                + MoviesEntry.KEY_LANGUAGE + " TEXT,"
                + MoviesEntry.KEY_POPULARITY + " TEXT,"
                + MoviesEntry.KEY_POSTER + " TEXT" + ")";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        Log.i(TAG, "Old version = " + oldVersion + " New version = " + newVersion);
        onCreate(db);
    }
}
