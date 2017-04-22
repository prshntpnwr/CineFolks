package com.example.prashant.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.prashant.myapplication.data.TvContract.TvEntry;

public class TvDbHelper extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "tv-series.db";

    public TvDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TV_TABLE = "CREATE TABLE " + TvContract.TvEntry.TABLE_NAME + "("
                + TvEntry.KEY_ID + " INTEGER PRIMARY KEY,"
                + TvEntry.KEY_TITLE + " TEXT,"
                + TvEntry.KEY_RATING + " TEXT,"
                + TvEntry.KEY_GENRE + " TEXT,"
                + TvEntry.KEY_DATE + " TEXT,"
                + TvEntry.KEY_STATUS + " TEXT,"
                + TvEntry.KEY_OVERVIEW + " TEXT,"
                + TvEntry.KEY_BACKDROP + " TEXT,"
                + TvEntry.KEY_VOTE_COUNT + " TEXT,"
                + TvEntry.KEY_RUN_TIME + " TEXT,"
                + TvEntry.KEY_LANGUAGE + " TEXT,"
                + TvEntry.KEY_POPULARITY + " TEXT,"
                + TvEntry.KEY_POSTER + " TEXT" + ")";
        db.execSQL(CREATE_TV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TvContract.TvEntry.TABLE_NAME);
        Log.i(TAG, "Old version = " + oldVersion + " New version = " + newVersion);
        onCreate(db);
    }

}
