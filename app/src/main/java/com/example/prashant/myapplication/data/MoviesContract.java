package com.example.prashant.myapplication.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.prashant.myapplication";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movies";

    public static final class MoviesEntry implements BaseColumns {

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.example.prashant.myapplication.movies";
        static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.example.prashant.myapplication.movies";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String TABLE_NAME = "movie";

        public static final String KEY_ID = "id";
        public static final String KEY_TITLE = "title";
        public static final String KEY_RATING = "rating";
        public static final String KEY_GENRE = "genre";
        public static final String KEY_DATE = "date";
        public static final String KEY_STATUS = "status";
        public static final String KEY_OVERVIEW = "overview";
        public static final String KEY_BACKDROP = "backdrop";
        public static final String KEY_VOTE_COUNT = "vote_count";
        public static final String KEY_TAG_LINE = "tag_line";
        public static final String KEY_RUN_TIME = "runtime";
        public static final String KEY_LANGUAGE = "language";
        public static final String KEY_POPULARITY = "popularity";
        public static final String KEY_POSTER = "poster";

        static Uri buildUri(long id) {
            //ContentUris.withAppendedId() is a helper method to create an id-based URI
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
