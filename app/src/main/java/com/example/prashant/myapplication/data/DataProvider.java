package com.example.prashant.myapplication.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.prashant.myapplication.data.MoviesContract.MoviesEntry;
import com.example.prashant.myapplication.data.TvContract.TvEntry;

public class DataProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mOpenMovieHelper;
    private TvDbHelper mOpenTvHelper;

    private static final int MOVIES = 0;
    private static final int MOVIES_ID = 1;
    private static final int TV = 2;
    private static final int TV_ID = 3;

    private static UriMatcher buildUriMatcher() {
        //The code passed into the constructor represents the code to return for the root URI.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;
        final String authority_tv = TvContract.CONTENT_AUTHORITY;

        //for the type of URI we want to add, create a corresponding code
        //Movies
        matcher.addURI(authority, MoviesContract.PATH_MOVIE, MOVIES);
        matcher.addURI(authority, MoviesContract.PATH_MOVIE + "/#", TV_ID);

        //tv
        matcher.addURI(authority_tv, TvContract.PATH_TV, TV);
        matcher.addURI(authority_tv, TvContract.PATH_TV + "/#", TV_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenMovieHelper = new MoviesDbHelper(getContext());

        mOpenTvHelper = new TvDbHelper(getContext());

        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return MoviesEntry.CONTENT_ITEM_TYPE;
            case MOVIES_ID:
                return MoviesEntry.CONTENT_ITEM_TYPE;
            case TV:
                return TvEntry.CONTENT_ITEM_TYPE;
            case TV_ID:
                return TvEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        final SQLiteDatabase movieDb = mOpenMovieHelper.getReadableDatabase();
        final SQLiteDatabase tvDb = mOpenTvHelper.getReadableDatabase();

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                retCursor = movieDb.query(MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;
            case MOVIES_ID:
                retCursor = movieDb.query(MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;

            case TV:
                retCursor = tvDb.query(TvEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;
            case TV_ID:
                retCursor = tvDb.query(TvEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        final SQLiteDatabase movieDb = mOpenMovieHelper.getReadableDatabase();
        final SQLiteDatabase tvDb = mOpenTvHelper.getReadableDatabase();

        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES:
                long _id = movieDb.insert(MoviesEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MoviesEntry.buildUri(_id);
                } else {
                    throw new SQLException("Failed to add a record into " + uri);
                }
                break;

            case TV:
                long id_ = tvDb.insert(TvEntry.TABLE_NAME, null, values);
                if (id_ > 0) {
                    returnUri = TvEntry.buildtvUri(id_);
                } else {
                    throw new SQLException("Failed to add a record into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        movieDb.close();
        tvDb.close();

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase movieDb = mOpenMovieHelper.getReadableDatabase();
        final SQLiteDatabase tvDb = mOpenTvHelper.getReadableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowDeleted;

        if (selection == null) selection = "1";

        switch (match) {
            case MOVIES:
                rowDeleted = movieDb.delete(MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case TV:
                rowDeleted = tvDb.delete(TvEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase movieDb = mOpenMovieHelper.getReadableDatabase();
        final SQLiteDatabase tvDb = mOpenTvHelper.getReadableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowUpdated;

        switch (match) {
            case MOVIES:
                rowUpdated = movieDb.update(MoviesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            case TV:
                rowUpdated = tvDb.update(TvEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowUpdated;
    }
}
