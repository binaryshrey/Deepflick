package com.example.deepflick.utils;

import android.content.Context;

import com.example.deepflick.model.Movie;
import com.example.deepflick.model.Review;
import com.example.deepflick.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TMDBJsonUtils {

    //method to parse movies_json data
    public static Movie[] parseValuesFromJson(Context context, String data) throws JSONException {
        //data members
        final String ROOT_OBJECT = "results";
        final String TITLE_KEY = "title";
        final String THUMBNAIL_URL = "https://image.tmdb.org/t/p/w500";
        final String THUMBNAIL_KEY = "poster_path";
        final String RATING_KEY = "vote_average";
        final String RELEASE_DATE_KEY = "release_date";
        final String ADULT_CONTENT_KEY = "adult";
        final String OVERVIEW_KEY = "overview";
        final String ID_KEY = "id";

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
            movie.setId(valuesArray.getJSONObject(i).optString(ID_KEY));
            //storing movie data
            results[i] = movie;
        }
        return results;
    }

    //method to parse trailers_json data
    public static Trailer[] parseTrailerValuesFromJson(Context context, String data) throws JSONException {
        //data members
        final String ROOT_OBJECT = "results";
        final String TRAILER_KEY_KEY = "key";
        final String TRAILER_NAME_KEY = "name";

        //initialize json object from json string
        JSONObject json = new JSONObject(data);
        //name a json object
        JSONArray valuesArray = json.getJSONArray(ROOT_OBJECT);
        Trailer trailerResults[] = new Trailer[valuesArray.length()];
        for (int i = 0; i < valuesArray.length(); i++) {
            //creating object of class Trailer using new keyword
            Trailer trailer = new Trailer();
            //calling setters methods
            trailer.setKey(valuesArray.getJSONObject(i).optString(TRAILER_KEY_KEY));
            trailer.setName(valuesArray.getJSONObject(i).optString(TRAILER_NAME_KEY));
            //storing trailer data
            trailerResults[i] = trailer;
        }
        return trailerResults;
    }

    //method to parse reviews_json data
    public static Review[] parseReviewValuesFromJson(Context context, String data) throws JSONException{
        //data members
        final String ROOT_OBJECT = "results";
        final String AUTHOR_KEY = "author";
        final String CONTENT_KEY = "content";
        final String ID_KEY = "id";
        final String URL_KEY = "url";

        //initialize json object from json string
        JSONObject json = new JSONObject(data);
        JSONArray valuesArray = json.getJSONArray(ROOT_OBJECT);
        Review reviewsResults[] = new Review[valuesArray.length()];
        for (int i = 0; i < valuesArray.length(); i++) {
            //creating object of class Review using new keyword
            Review review = new Review();
            //calling setters methods
            review.setAuthor(valuesArray.getJSONObject(i).optString(AUTHOR_KEY));
            review.setContent(valuesArray.getJSONObject(i).optString(CONTENT_KEY));
            review.setId(valuesArray.getJSONObject(i).optString(ID_KEY));
            review.setUrl(valuesArray.getJSONObject(i).optString(URL_KEY));

            //storing review data
            reviewsResults[i] = review;
        }
        return reviewsResults;
    }
}