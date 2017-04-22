package com.example.prashant.myapplication.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class TvDetail implements Parcelable {

    public static final Creator CREATOR = new Creator() {
        public TvDetail createFromParcel(Parcel in) {
            return new TvDetail(in);
        }

        public TvDetail[] newArray(int size) {
            return new TvDetail[size];
        }
    };
    private String title, rating, genre, date, status, overview, backdrop, voteCount,
            runtime, language, popularity, poster, number_of_episodes, number_of_seasons, last_air_date;
    private int id;

    public TvDetail() {
    }

    public TvDetail(int id, String title, String rating, String genre, String date,
                    String status, String overview, String backdrop, String voteCount,
                    String runtime, String language, String popularity, String poster) {
//                    String number_of_episodes, String number_of_seasons, String last_air_date

        this.id = id;
        this.title = title;
        this.rating = rating;
        this.genre = genre;
        this.date = date;
        this.status = status;
        this.overview = overview;
        this.backdrop = backdrop;
        this.voteCount = voteCount;
        this.runtime = runtime;
        this.language = language;
        this.popularity = popularity;
        this.poster = poster;
//        this.number_of_episodes = number_of_episodes;
//        this.number_of_seasons = number_of_seasons;
//        this.last_air_date = last_air_date;
    }

    public TvDetail(Parcel in) {
        String[] data = new String[13];

        in.readStringArray(data);
        this.id = Integer.valueOf(data[0]);
        this.title = data[1];
        this.rating = data[2];
        this.genre = data[3];
        this.date = data[4];
        this.status = data[5];
        this.overview = data[6];
        this.backdrop = data[7];
        this.voteCount = data[8];
        this.runtime = data[9];
        this.language = data[10];
        this.popularity = data[12];
        this.poster = data[13];
//        this.number_of_episodes = data[13];
//        this.number_of_seasons = data[14];
//        this.last_air_date = data[15];

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

//    public String getNumber_of_seasons() {
//        return number_of_seasons;
//    }
//
//    public void setNumber_of_seasons(String number_of_seasons) {
//        this.number_of_seasons = number_of_seasons;
//    }
//
//    public String getNumber_of_episodes() {
//        return number_of_episodes;
//    }
//
//    public void setNumber_of_episodes(String number_of_episodes) {
//        this.number_of_episodes = number_of_episodes;
//    }
//
//    public String getLast_air_date() {
//        return number_of_episodes;
//    }
//
//    public void setLast_air_date(String last_air_date) {
//        this.last_air_date = last_air_date;
//    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(this.id),
                this.title, this.rating, this.genre, this.date,
                this.status, this.overview, this.backdrop,
                this.voteCount, this.runtime, this.language,
                this.popularity, this.poster,
//                this.number_of_seasons, this.number_of_episodes, this.last_air_date
        });
    }
}
