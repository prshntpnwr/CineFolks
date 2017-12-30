package com.example.prashant.myapplication.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Movies implements Parcelable {

    private String title, date, overview, id, rating;
    private String genre, status, backdrop, voteCount, tagLine,
            runtime, language, popularity, poster;

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    protected Movies(Parcel in) {
        title = in.readString();
        date = in.readString();
        overview = in.readString();
        id = in.readString();
        rating = in.readString();
        genre = in.readString();
        status = in.readString();
        backdrop = in.readString();
        voteCount = in.readString();
        tagLine = in.readString();
        runtime = in.readString();
        language = in.readString();
        popularity = in.readString();
        poster = in.readString();
    }

    public Movies() {
    }

    public Movies(String title, String date, String overview,
                  String id, String rating, String genre, String status,
                  String backdrop, String voteCount, String tagLine,
                  String runtime, String language, String popularity,
                  String poster) {
        this.title = title;
        this.date = date;
        this.overview = overview;
        this.id = id;
        this.rating = rating;
        this.genre = genre;
        this.status = status;
        this.backdrop = backdrop;
        this.voteCount = voteCount;
        this.tagLine = tagLine;
        this.runtime = runtime;
        this.language = language;
        this.popularity = popularity;
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
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

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(overview);
        dest.writeString(id);
        dest.writeString(rating);
        dest.writeString(genre);
        dest.writeString(status);
        dest.writeString(backdrop);
        dest.writeString(voteCount);
        dest.writeString(tagLine);
        dest.writeString(runtime);
        dest.writeString(language);
        dest.writeString(popularity);
        dest.writeString(poster);
    }
}
