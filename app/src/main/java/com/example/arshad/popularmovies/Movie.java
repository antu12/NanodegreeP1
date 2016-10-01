package com.example.arshad.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arshad on 9/29/16.
 */

public class Movie implements Parcelable {

    private String title; // original_title
    private String poster; // poster_path
    private String overview;
    private String rating; // vote_average
    private String date; // release_date

    public Movie(){

    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setPoster(String poster){
        this.poster = poster;
    }
    public void setOverview(String overview){
        this.overview = overview;
    }
    public void setRating(String rating){
        this.rating = rating;
    }
    public void setDate(String date){
        this.date = date;
    }

    public String getTitle(){
        return title;
    }
    public String getPoster(){
        return poster;
    }
    public String getOverview(){
        return overview;
    }
    public String getRating(){
        return rating;
    }
    public String getDate(){
        return date;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        poster = in.readString();
        overview = in.readString();
        rating = in.readString();
        date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeString(rating);
        dest.writeString(date);
    }
}
