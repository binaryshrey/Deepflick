package com.example.deepflick.utils;

import android.content.Context;

import com.example.deepflick.model.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TMDBJsonUtils {
    //data members
    private static final String ROOT_OBJECT = "results";
    private static final String TITLE_KEY = "title";
    private static final String THUMBNAIL_URL = "https://image.tmdb.org/t/p/w500";
    private static final String THUMBNAIL_KEY = "poster_path";
    private static final String RATING_KEY = "vote_average";
    private static final String RELEASE_DATE_KEY = "release_date";
    private static final String ADULT_CONTENT_KEY = "adult";
    private static final String OVERVIEW_KEY = "overview";

    //method to parse json data
    public static Movie[] parseValuesFromJson(Context context, String data) throws JSONException {

        //initialize json object from json string
        JSONObject json = new JSONObject(data);
        //name a json object
        JSONArray valuesArray = json.getJSONArray(ROOT_OBJECT);
        Movie results[] = new Movie[valuesArray.length()];
        for(int i=0;i<valuesArray.length();i++){
            //creating object of class Movie using new keyword
            Movie movie = new Movie();
            //calling setters methods
            movie.setTitle(valuesArray.getJSONObject(i).optString(TITLE_KEY));
            movie.setThumbnail(THUMBNAIL_URL + valuesArray.getJSONObject(i).optString(THUMBNAIL_KEY));
            movie.setRating(valuesArray.getJSONObject(i).optString(RATING_KEY));
            movie.setReleaseDate(valuesArray.getJSONObject(i).optString(RELEASE_DATE_KEY));
            movie.setAdult(valuesArray.getJSONObject(i).optString(ADULT_CONTENT_KEY));
            movie.setOverview(valuesArray.getJSONObject(i).optString(OVERVIEW_KEY));
            //storing movie data
            results[i] = movie;
        }
        return results;
    }
}