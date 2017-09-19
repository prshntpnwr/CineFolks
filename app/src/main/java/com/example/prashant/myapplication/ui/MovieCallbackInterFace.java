package com.example.prashant.myapplication.ui;

import com.example.prashant.myapplication.objects.Movies;

public interface MovieCallbackInterFace {

    void onSuccessResponse(Movies movies);

    void onFailResponse(String message);
}
