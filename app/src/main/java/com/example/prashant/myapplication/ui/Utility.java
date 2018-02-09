package com.example.prashant.myapplication.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Random;

/**
 * Created by prashant on 30/12/17.
 */

public class Utility {
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile_info = conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi_info = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mobile_info == null || wifi_info == null) return false;
        if (mobile_info.getState() == NetworkInfo.State.CONNECTED
                || wifi_info.getState() == NetworkInfo.State.CONNECTED) {

            return true;

        } else if (mobile_info.getState() == NetworkInfo.State.DISCONNECTED
                || wifi_info.getState() == NetworkInfo.State.DISCONNECTED) {

            return false;
        }
        return false;
    }

    private static ColorDrawable[] mutedColorList = {
                    new ColorDrawable(Color.parseColor("#75A5A5")), new ColorDrawable(Color.parseColor("#ACE5B8")),
                    new ColorDrawable(Color.parseColor("#B7C187")), new ColorDrawable(Color.parseColor("#DACC8F")),
                    new ColorDrawable(Color.parseColor("#D8AC97")), new ColorDrawable(Color.parseColor("#D2A2D8")),
                    new ColorDrawable(Color.parseColor("#D17777")), new ColorDrawable(Color.parseColor("#5B7A6C"))
            };

    public static ColorDrawable getRandomDrawableColor() {
        int idx = new Random().nextInt(mutedColorList.length);
        return mutedColorList[idx];
    }
}
