package com.shwet.mymovielistapplication.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.io.File;

public class Common {
    public final static String API_KEY = "9be2cc33e7b3c61d945a09c14611391c";
    public final static String API_URL = "http://api.themoviedb.org/3/";
    public final static String SMALL_POSTER_URL = "http://image.tmdb.org/t/p/w185";
    public final static String LARGE_POSTER_URL = "http://image.tmdb.org/t/p/w500";
    public final static String INTENT_MOVIE_DETAIL = "INTENT_MOVIE_DETAIL";
    public final static String DATABASE_NAME = "tmbd_database";
    public final static String MAIN_FOLDER_NAME = Environment.getExternalStorageDirectory() + File.separator+ "TMDB";
    public final static String SMALL_POSTER_FOLDER_NAME = MAIN_FOLDER_NAME + "/small_posters";
    public final static String LARGE_POSTER_FOLDER_NAME = MAIN_FOLDER_NAME + "/large_posters";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
