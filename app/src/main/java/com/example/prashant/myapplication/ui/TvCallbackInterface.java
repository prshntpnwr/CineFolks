package com.example.prashant.myapplication.ui;

import com.example.prashant.myapplication.objects.TV;

public interface TvCallbackInterface {
    void onSuccessResponse(TV tv);

    void onFailResponse(String message);
}
