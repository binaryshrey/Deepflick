package com.example.deepflick.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FavMovie")
public class FavoriteMovie {
    //data members
    //Primary key - mId
    @PrimaryKey
    private int mId;
    private String mTitle;
    private String mThumbnail;
    private String mRating;
    private String mAdult;
    private String mReleaseDate;
    private String mOverview;

    //constructor
    public FavoriteMovie(int id,String title, String thumbnail, String rating, String adult, String releaseDate, String overview){
        this.mId = id;
        this.mTitle = title;
        this.mThumbnail = thumbnail;
        this.mRating = rating;
        this.mAdult = adult;
        this.mReleaseDate = releaseDate;
        this.mOverview = overview;
    }

    //getter method
    public int getId() { return mId; }
    //setter method
    public void setId(int mId) { this.mId = mId; }

    public String getTitle() { return mTitle; }
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