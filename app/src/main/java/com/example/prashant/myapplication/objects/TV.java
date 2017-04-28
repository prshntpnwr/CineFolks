package com.example.prashant.myapplication.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class TV implements Parcelable, Comparable<TV> {

    public static final Creator CREATOR = new Creator() {
        public TV createFromParcel(Parcel in) {
            return new TV(in);
        }

        public TV[] newArray(int size) {
            return new TV[size];
        }
    };
    private String title, image, date, overview, id;

    public TV(String title, String image, String date, String overview, String id) {
        this.title = title;
        this.image = image;
        this.date = date;
        this.overview = overview;
        this.id = id;
    }

    public TV(Parcel in) {
        String[] data = new String[5];

        in.readStringArray(data);
        this.title = data[0];
        this.image = data[1];
        this.date = data[2];
        this.overview = data[3];
        this.id = data[4];
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int compareTo(@NonNull TV tv) {
        if (getDate() == null || tv.getDate() == null)
            return 0;

        return getDate().compareTo(tv.getDate());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.title,
                this.image,
                this.date,
                this.overview,
                this.id});
    }
}
