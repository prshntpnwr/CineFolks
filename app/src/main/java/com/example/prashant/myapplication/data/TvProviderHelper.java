package com.example.prashant.myapplication.data;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

import com.example.prashant.myapplication.data.TvContract.TvEntry;
import com.example.prashant.myapplication.objects.TV;
import com.example.prashant.myapplication.objects.TvDetail;

import java.util.ArrayList;

public class TvProviderHelper {

    public static ArrayList<TV> getTvListFromDatabase(Activity activity) {

        ArrayList<TV> mTVList = new ArrayList<>();
        Uri contentUri = TvEntry.CONTENT_URI;
        Cursor c = activity.getContentResolver().query(contentUri, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {

                TV tv = new TV(c.getString(c.getColumnIndex(TvEntry.KEY_TITLE)),
                        c.getString(c.getColumnIndex(TvEntry.KEY_POSTER)),
                        c.getString(c.getColumnIndex(TvEntry.KEY_DATE)),
                        c.getString(c.getColumnIndex(TvEntry.KEY_OVERVIEW)),
                        c.getString(c.getColumnIndex(TvEntry.KEY_ID)),
                        c.getString(c.getColumnIndex(TvEntry.KEY_RATING)));

                mTVList.add(tv);
            } while (c.moveToNext());
        }
        c.close();
        return mTVList;
    }

    public static boolean isTvInDatabase(Activity mAct, String id) {

        ArrayList<TV> list = new ArrayList<>(TvProviderHelper
                .getTvListFromDatabase(mAct));
        for (TV listItem : list) {
            if (listItem.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public static TvDetail getTvFromDatabase(Activity activity, String ID) {
        TvDetail tv = null;
        Uri contentUri = TvEntry.CONTENT_URI;
        Cursor c = activity.getContentResolver().query(contentUri, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {
                if (ID.equals(c.getString(c.getColumnIndex(TvEntry.KEY_ID)))) {
                    tv = new TvDetail(Integer.valueOf(c.getString(c.getColumnIndex(TvEntry.KEY_ID))),
                            c.getString(c.getColumnIndex(TvEntry.KEY_TITLE)),
                            c.getString(c.getColumnIndex(TvEntry.KEY_RATING)),
                            c.getString(c.getColumnIndex(TvEntry.KEY_GENRE)),
                            c.getString(c.getColumnIndex(TvEntry.KEY_DATE)),
                            c.getString(c.getColumnIndex(TvEntry.KEY_STATUS)),
                            c.getString(c.getColumnIndex(TvEntry.KEY_OVERVIEW)),
                            c.getString(c.getColumnIndex(TvEntry.KEY_BACKDROP)),
                            c.getString(c.getColumnIndex(TvEntry.KEY_VOTE_COUNT)),
                            c.getString(c.getColumnIndex(TvEntry.KEY_RUN_TIME)),
                            c.getString(c.getColumnIndex(TvEntry.KEY_LANGUAGE)),
                            c.getString(c.getColumnIndex(TvEntry.KEY_POPULARITY)),
                            c.getString(c.getColumnIndex(TvEntry.KEY_POSTER)));
                    break;
                }

            } while (c.moveToNext());
        }
        c.close();
        return tv;
    }
}
