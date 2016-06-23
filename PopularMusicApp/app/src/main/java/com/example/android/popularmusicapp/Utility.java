package com.example.android.popularmusicapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Utility {

    public Utility() {
    }

    private static final String LOG_TAG = Utility.class.getSimpleName();

    // http://api.themoviedb.org/3/movie/top_rated?api_key=[]
    public String getUriForPref(Context context, String pref,String valueMostPopular, String valueTopRated) {
        Uri.Builder uriBuilder = new Uri.Builder();


        uriBuilder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie");
        if(pref.equals(valueMostPopular)) {
            uriBuilder.appendPath("popular");
        }
        else if(pref.equals(valueTopRated)) {
            uriBuilder.appendPath("top_rated");
        }
        else {
            return "";
        }

        uriBuilder.appendQueryParameter("api_key", context.getString(R.string.api_key));

        try {
            return uriBuilder.build().toString();
        } catch (UnsupportedOperationException e) {
            Log.e(LOG_TAG, e.getMessage());
            return "";
        }
    }


    public String getUriStringForImages(Context context, String posterPath) {
        Uri.Builder uriBuilder = new Uri.Builder();

        uriBuilder.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185")
                .appendPath(posterPath)
                .appendQueryParameter("api_key", context.getString(R.string.api_key));


        try {
            return uriBuilder.build().toString();
        } catch (UnsupportedOperationException e) {
            Log.e(LOG_TAG, e.getMessage());
            return "";
        }
    }

    public List<Map<String,String>> formatJsonResponse(String jsonStr)
            throws JSONException {

        List resultList = new ArrayList();
        JSONObject o = new JSONObject(jsonStr);
//        final Integer TOTAL_PAGES = (o.getInt("total_pages"));
        JSONArray arr = o.getJSONArray("results");

        if (arr.length()>0) {
            for (int i = 0; i < arr.length(); i++) {
                Map<String, String> hashMap = new HashMap<>();
                JSONObject obj = arr.getJSONObject(i);
                hashMap.put(Constants.POSTER_PATH, obj.getString(Constants.POSTER_PATH));
                hashMap.put(Constants.TITLE, obj.getString(Constants.TITLE));
                hashMap.put(Constants.LANG, obj.getString(Constants.LANG));
                hashMap.put(Constants.POPULARITY, obj.getString(Constants.POPULARITY));
                hashMap.put(Constants.VOTE_COUNT, obj.getString(Constants.VOTE_COUNT));
                hashMap.put(Constants.VOTE_AVE, obj.getString(Constants.VOTE_AVE));
                hashMap.put(Constants.RELEASE_DATE, obj.getString(Constants.RELEASE_DATE));
                hashMap.put(Constants.OVERVIEW, obj.getString(Constants.OVERVIEW));
                resultList.add(hashMap);
            }
        }
        else {
            Log.d(LOG_TAG, "No movies.");
            return Collections.EMPTY_LIST;
        }
        return resultList;

    }


//    public static double renderRatingStars(double rating) {
//        double halfNumb = rating / 2.0;
//        double integer = Math.floor(halfNumb);
//        double diff =  halfNumb - integer;
//        final double step = 0.5d;
//        if(diff>=step) {
//            integer += step;
//        }
//        return integer;
//    }
}

