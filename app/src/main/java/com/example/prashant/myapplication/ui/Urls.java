package com.example.prashant.myapplication.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Urls {

    //request base urls movie
    public static String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    public static String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static String MOVIE_BASE_SEARCH_URL = "http://api.themoviedb.org/3/search/movie?";

    //request base urls tv
    public static String BASE_URL_TV = "http://api.themoviedb.org/3/discover/tv?";
    public static String TV_BASE_URL = "http://api.themoviedb.org/3/tv/";
    public static String TV_BASE_SEARCH_URL = "http://api.themoviedb.org/3/search/tv?";

    //API key
    public static String API_KEY = "api_key=b7f57ee32644eb6ddfdca9ca38b5513e";

    //sort-by string endpoint
    public static String SORT_POPULARITY = "&sort_by=popularity.desc";
    public static String SORT_TOP_RATED
            = "&certification_country=US&certification=R&sort_by=vote_average.desc&vote_count.gte=250";
    private static String SORT_PLAYING_NOW;
    private static String SORT_UPCOMING;

    private static String SORT_AIRING_TODAY;
    private static String SORT_CURRENTLY_AIRING;

    //youtube strings url/endpoint
    public static String YOUTUBE_URL = "http://www.youtube.com/watch?v=";
    public static String YOUTUBE_THUMB = "http://img.youtube.com/vi/";
    public static String YOUTUBE_MEDIUM_QUALITY = "/mqdefault.jpg";

    private static String getDate(int month, int date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, +month);
        c.add(Calendar.DATE, +date);

        Date d = c.getTime();
        return format.format(d);
    }

    //movies
    public static String getPlayingNow() {
        SORT_PLAYING_NOW = "&release_date.lte=" + getDate(0, 3) + "&" + "release_date.gte=" + getDate(-1, -2);
        return SORT_PLAYING_NOW;
    }

    public static String getUpcoming() {
        SORT_UPCOMING = "&release_date.lte=" + getDate(8, 0) + "&" + "release_date.gte=" + getDate(0, 10);
        return SORT_UPCOMING;
    }

    //tv-series
    public static String getAiringToday() {
        SORT_AIRING_TODAY = "&air_date.lte=" + getDate(0, 0) + "&" + "air_date.gte=" + getDate(0, 0);
        return SORT_AIRING_TODAY;
    }

    public static String getCurrentlyAiring() {
        SORT_CURRENTLY_AIRING = "&air_date.gte=" + getDate(0, 0) + "&" + "first_air_date.lte=" + getDate(0, 0);
        return SORT_CURRENTLY_AIRING;
    }
}
