package com.example.prashant.myapplication.ui;

public class Urls {

    //request base urls
    public static String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    public static String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";

    //API key
    public static String API_KEY = "api_key=b7f57ee32644eb6ddfdca9ca38b5513e";

    //sort-by string endpoint
    public static String SORT_POPULARITY = "&sort_by=popularity.desc";
    public static String SORT_TOP_RATED = "&certification_country=US&certification=R&sort_by=vote_average.desc&vote_count.gte=250";
    public static String SORT_PLAYING_NOW = "&sort_by=now-playing.desc";

    //youtube strings url/endpoint
    public static String YOUTUBE_URL = "http://www.youtube.com/watch?v=";
    public static String YOUTUBE_THUMB = "http://img.youtube.com/vi/";
    public static String YOUTUBE_MEDIUM_QUALITY = "/mqdefault.jpg";
}
