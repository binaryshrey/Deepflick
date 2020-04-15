package com.example.deepflick.utils;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    final static String BASE_URL = "https://api.themoviedb.org/3/movie";
    final static String API_KEY_PARAM = "api_key";
    final static String API_KEY = "**--INSERT API KEY--**";
    final static String LANGUAGE_PARAM = "language";
    final static String LANGUAGE = "en-US";

    public static URL buitlUrl(String searchQuery){
        Uri builturi = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(searchQuery)
                .appendQueryParameter(API_KEY_PARAM,API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM,LANGUAGE)
                .build();

        URL url = null;
        try{
            url = new URL(builturi.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

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