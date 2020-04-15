package com.example.deepflick.model;

public class Movie {
    //data members
    private String mTitle;
    private String mThumbnail;
    private String mRating;
    private String mAdult;
    private String mReleaseDate;
    private String mOverview;

    //constructor
    public Movie(){}

    //getter method
    public String getTitle() { return mTitle; }
    //setter method
    public void setTitle(String mTitle) { this.mTitle = mTitle; }

    public String getThumbnail() { return  mThumbnail; }
    public void setThumbnail(String mThumbnail) { this.mThumbnail = mThumbnail; }

    public String getRating() {return  mRating; }
    public void setRating(String mRating) { this.mRating = mRating; }

    public String  getAdult() {return  mAdult; }
    public void setAdult(String  mAdult) { this.mAdult = mAdult; }

    public String getReleaseDate() {return  mReleaseDate; }
    public void setReleaseDate(String mReleaseDate) { this.mReleaseDate = mReleaseDate; }

    public String getOverview() {return  mOverview; }
    public void setOverview(String mOverview) { this.mOverview = mOverview; }

}