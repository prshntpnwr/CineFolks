package com.example.prashant.myapplication.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.ui.MainActivity;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class NotifyJobService extends JobService {

    private final int notify_ID = 911;

    @Override
    public boolean onStartJob(JobParameters job) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                // Provides the bitmap for the BigPicture notification
                .bigPicture(BitmapFactory.decodeResource(
                        getResources(), R.drawable.ic_notification));

        // create basic notification here
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                .setContentTitle("TV Show")
                .setContentText("New Episode Airing Today")
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
        notificationManager.notify(notify_ID, notificationBuilder.build());
        
        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }
}