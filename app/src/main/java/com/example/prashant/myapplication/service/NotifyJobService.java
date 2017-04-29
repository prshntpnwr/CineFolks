package com.example.prashant.myapplication.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.data.TvContract;
import com.example.prashant.myapplication.objects.TV;
import com.example.prashant.myapplication.ui.MainActivity;
import com.example.prashant.myapplication.ui.Urls;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class NotifyJobService extends JobService {

    private final String TAG = getClass().getSimpleName();

    private int notify_ID = 911;
    private ArrayList<TV> mTvList = new ArrayList<>();
    private String title;
    private String poster;

    @Override
    public boolean onStartJob(JobParameters job) {

        getTodayShows();

        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }

    private ArrayList<TV> getListFromDB() {

        ArrayList<TV> list = new ArrayList<>(getTvListFromDatabase());

        mTvList.clear();
        for (TV tv : list) {
            mTvList.add(tv);
        }

        return mTvList;
    }

    private void getTodayShows() {
        getListFromDB();

        for (int i = 0; i < mTvList.size(); i++) {
            AiringToday(Integer.parseInt(mTvList.get(i).getId()));
        }
    }

    public ArrayList<TV> getTvListFromDatabase() {

        ArrayList<TV> mTVList = new ArrayList<>();
        Uri contentUri = TvContract.TvEntry.CONTENT_URI;
        Cursor c = getContentResolver().query(contentUri, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {

                TV tv = new TV(c.getString(c.getColumnIndex(TvContract.TvEntry.KEY_TITLE)),
                        c.getString(c.getColumnIndex(TvContract.TvEntry.KEY_POSTER)),
                        c.getString(c.getColumnIndex(TvContract.TvEntry.KEY_DATE)),
                        c.getString(c.getColumnIndex(TvContract.TvEntry.KEY_OVERVIEW)),
                        c.getString(c.getColumnIndex(TvContract.TvEntry.KEY_ID)));

                mTVList.add(tv);
            } while (c.moveToNext());
        }
        c.close();
        return mTVList;
    }

    private void AiringToday(final int id) {

        String url = Urls.BASE_URL_TV + Urls.API_KEY + Urls.getAiringToday();
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest getListData = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG + " TV-Series Response - ", response.toString());

                    JSONArray mResultArray = response.getJSONArray("results");
                    Log.d(TAG + " TV Response length - ", String.valueOf(mResultArray.length()));

                    for (int i = 0; i < mResultArray.length(); i++) {
                        Log.d(TAG, " Enter into tv response loop " + i);

                        JSONObject mResultObject = mResultArray.getJSONObject(i);
                        if (id == mResultObject.getInt("id")) {
                            Log.d(TAG, " Airing today ");
                            title = mResultObject.getString("name");
                            Log.d(TAG, "Title is - " + title);
                            poster = "http://image.tmdb.org/t/p/w342/" + mResultObject.getString("backdrop_path");

                            createNotifications();
                        } else Log.d(TAG, " not found ");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, " failed to parse json from TV " + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d(TAG, "fail response from Tv api " + error);
            }
        });
        queue.add(getListData);
    }

    private void createNotifications() {

        final Bitmap[] bitmap = new Bitmap[1];

        Glide.with(this)
                .load(poster)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        bitmap[0] = resource;
                    }
                });

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                // Provides the bitmap for the BigPicture notification
                .bigPicture(bitmap[0]);

        Log.d(TAG, " Poster url is - " + poster);

        // create basic notification here
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Episode Airing Today")
                        .setColor(Color.BLUE)
                        .setContentText(title)
                        .setTicker("Alert New Notification")
                        .setSmallIcon(R.drawable.ic_movie)
                        .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                        .setStyle(bigPictureStyle)
                        .setAutoCancel(true);

        Intent intent = new Intent(this, MainActivity.class);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), notificationBuilder.build());
    }
}