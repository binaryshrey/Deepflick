package com.example.deepflick.utils;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    //url parameters
    final static String BASE_URL = "https://api.themoviedb.org/3/movie";
    final static String API_KEY_PARAM = "api_key";
    final static String API_KEY = "**--INSERT API KEY--**";
    final static String TRAILERS_PATH = "videos";
    final static String REVIEWS_PATH = "reviews";
    final static String LANGUAGE_PARAM = "language";
    final static String LANGUAGE = "en-US";

    //method to build main URL
    public static URL buildMainUrl(String searchQuery){
        Uri builturi = Uri.parse(BASE_URL).buildUpon()
                //appending queryParameter
                .appendEncodedPath(searchQuery)
                //appending API key with value
                .appendQueryParameter(API_KEY_PARAM,API_KEY)
                //appending Lang param key with value
                .appendQueryParameter(LANGUAGE_PARAM,LANGUAGE)
                .build();

        //uri to url
        URL url = null;
        try{
            url = new URL(builturi.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    //method to build trailers URL
    public static URL buildTrailerUrl(String MovieId){
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                //adds movieId to path
                .appendEncodedPath(String.valueOf(MovieId))
                //adds videos to path
                .appendEncodedPath(TRAILERS_PATH)
                //appending API key with value
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                //appending Lang param key with value
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    //method to build reviews URL
    public static URL buildReviewsUrl(String MovieId){
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                //adds movieId to path
                .appendEncodedPath(String.valueOf(MovieId))
                //adds videos to path
                .appendEncodedPath(REVIEWS_PATH)
                //appending API key with value
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                //appending Lang param key with value
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    // getResponseFromHttpUrl method
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}