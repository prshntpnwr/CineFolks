package com.example.prashant.myapplication.server;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prashant.myapplication.R;
import com.example.prashant.myapplication.objects.Movies;
import com.example.prashant.myapplication.objects.TV;
import com.example.prashant.myapplication.ui.MovieCallbackInterFace;
import com.example.prashant.myapplication.ui.TvCallbackInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerCall {

    private static String TAG = ServerCall.class.getSimpleName();

    public static void getMovies(final Context context, final String URL, final MovieCallbackInterFace callbackInterFace) {
        Log.d(TAG, "URL is - " + URL);

        JsonObjectRequest getListData = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "response - " + response.toString());

                    JSONArray mResultArray = response.getJSONArray("results");
                    if (mResultArray.length() > 0) {
                        for (int i = 0; i < mResultArray.length(); i++) {
                            JSONObject mResultObject = mResultArray.getJSONObject(i);
                            Movies movie = new Movies(mResultObject.getString("title"),
                                    "http://image.tmdb.org/t/p/w342/" + mResultObject.getString("poster_path"),
                                    context.getResources().getString(R.string.release_date) + mResultObject.getString("release_date"),
                                    mResultObject.getString("overview"),
                                    String.valueOf(mResultObject.getInt("id")),
                                    String.valueOf(mResultObject.getDouble("vote_average")));

                            callbackInterFace.onSuccessResponse(movie);
                        }
                    } else callbackInterFace.onFailResponse("No Movies Available");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d(TAG, "fail response from movie api " + error);
            }
        });

        AppController.getInstance().addToRequestQueue(getListData);
    }

    public static void getTvShows(final Context context, final String URL, final TvCallbackInterface callbackInterface) {
        Log.d(TAG, " URL - " + URL);

        JsonObjectRequest getListData = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, " TV-Series- " + response.toString());

                    JSONArray mResultArray = response.getJSONArray("results");

                    if (mResultArray.length() > 0) {

                        for (int i = 0; i < mResultArray.length(); i++) {
                            Log.d(TAG, " Enter into tv response loop " + i);

                            JSONObject mResultObject = mResultArray.getJSONObject(i);
                            TV tv = new TV(mResultObject.getString("name"),
                                    "http://image.tmdb.org/t/p/w342/" + mResultObject.getString("poster_path"),
                                    context.getResources().getString(R.string.release_date) + mResultObject.getString("first_air_date"),
                                    mResultObject.getString("overview"),
                                    String.valueOf(mResultObject.getInt("id")),
                                    String.valueOf(mResultObject.getDouble("vote_average")));
                            Log.d(TAG, " TV list is " + tv.toString());
//                        Collections.sort(mTVList);
//                        Collections.reverse(mTVList);

                            callbackInterface.onSuccessResponse(tv);
                        }
                    } else
                        callbackInterface.onFailResponse("Not Tv Show found");

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

        AppController.getInstance().addToRequestQueue(getListData);
    }
}
