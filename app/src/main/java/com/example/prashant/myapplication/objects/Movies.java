package com.example.prashant.myapplication.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Movies implements Parcelable {

    public static final Creator CREATOR = new Creator() {
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
    private String title, image, date, overview, id, rating;

    public Movies(String title, String image, String date, String overview, String id, String rating) {
        this.title = title;
        this.image = image;
        this.date = date;
        this.overview = overview;
        this.id = id;
        this.rating = rating;
    }

    public Movies(Parcel in) {
        String[] data = new String[6];

        in.readStringArray(data);
        this.title = data[0];
        this.image = data[1];
        this.date = data[2];
        this.overview = data[3];
        this.id = data[4];
        this.rating = data[5];
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

    public String getRating() {
        return rating;
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
        dest.writeStringArray(new String[]{this.title,
                this.image,
                this.date,
                this.overview,
                this.id,
                this.rating
        });
    }
}
