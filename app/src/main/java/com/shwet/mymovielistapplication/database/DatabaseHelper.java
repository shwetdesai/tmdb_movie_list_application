package com.shwet.mymovielistapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shwet.mymovielistapplication.models.Movies;

import java.util.ArrayList;
import java.util.List;

import static com.shwet.mymovielistapplication.common.Common.DATABASE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;
    static final String TAG = "DatabaseHelper";


    /**Activities Table*/
    private static final String moviesTable = "movies_table";
    private static final String moviesTable_Col_1 = "id";
    private static final String moviesTable_Col_2 = "adult";
    private static final String moviesTable_Col_3 = "backdrop_path";
    private static final String moviesTable_Col_4 = "genre_ids";
    private static final String moviesTable_Col_5 = "media_type";
    private static final String moviesTable_Col_6 = "original_language";
    private static final String moviesTable_Col_7 = "original_title";
    private static final String moviesTable_Col_8 = "overview";
    private static final String moviesTable_Col_9 = "popularity";
    private static final String moviesTable_Col_10 = "poster_path";
    private static final String moviesTable_Col_11 = "release_date";
    private static final String moviesTable_Col_12 = "title";
    private static final String moviesTable_Col_13 = "video";
    private static final String moviesTable_Col_14 = "vote_average";
    private static final String moviesTable_Col_15 = "vote_count";

    /**End */


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ moviesTable + "("+moviesTable_Col_1 + " INTEGER,"+ moviesTable_Col_2 + " TEXT,"+ moviesTable_Col_3 + " TEXT,"+ moviesTable_Col_4 + " TEXT,"+ moviesTable_Col_5 + " TEXT,"
                + moviesTable_Col_6 + " TEXT,"+ moviesTable_Col_7 + " TEXT,"+ moviesTable_Col_8 + " TEXT,"+ moviesTable_Col_9 + " DOUBLE,"+ moviesTable_Col_10 + " TEXT,"
                + moviesTable_Col_11 + " TEXT,"+ moviesTable_Col_12 + " TEXT,"+ moviesTable_Col_13 + " TEXT,"+ moviesTable_Col_14 + " FLOAT,"+ moviesTable_Col_15 + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + moviesTable);
    }


    public boolean addMovie (Movies movie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put(moviesTable_Col_1, movie.getId());
        contentValues.put(moviesTable_Col_2, ""+movie.getAdult());
        contentValues.put(moviesTable_Col_3, movie.getBackdropPath());
        contentValues.put(moviesTable_Col_4, movie.getGenreIds());
        contentValues.put(moviesTable_Col_5, movie.getMediaType());
        contentValues.put(moviesTable_Col_6, movie.getOriginalLanguage());
        contentValues.put(moviesTable_Col_7, movie.getOriginalTitle());
        contentValues.put(moviesTable_Col_8, movie.getOverview());
        contentValues.put(moviesTable_Col_9, movie.getPopularity());
        contentValues.put(moviesTable_Col_10, movie.getPosterPath());
        contentValues.put(moviesTable_Col_11, movie.getReleaseDate());
        contentValues.put(moviesTable_Col_12, movie.getTitle());
        contentValues.put(moviesTable_Col_13, ""+movie.getVideo());
        contentValues.put(moviesTable_Col_14, ""+movie.getVoteAverage());
        contentValues.put(moviesTable_Col_15, ""+movie.getVoteCount());

        long result = -1;
        if(getMovieCheck(movie.getId()))
          result = db.insert(moviesTable, null, contentValues);

        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean getMovieCheck (int movieId){
        SQLiteDatabase db1 = this.getReadableDatabase();

        int newMovieId;

        String movieIdQuery = "SELECT " +  moviesTable_Col_1 + " FROM "+ moviesTable + " WHERE " + moviesTable_Col_1 + " = "+  movieId + " ORDER BY ID DESC LIMIT 1 ;";
        Cursor cursorMovieId = db1.rawQuery(movieIdQuery, null);

        newMovieId = (cursorMovieId.moveToFirst() ? cursorMovieId.getInt(0) : 0);

        Log.i(TAG,"Movie exists "+newMovieId);


        cursorMovieId.close();
        if(newMovieId == 0)
            return true;
        else
            return false;
    }


    public List<Movies> getAllMovies(){

        List<Movies> movies = new ArrayList<>();


        int id,voteCount;
        float voteAverage;
        String isAdult,backdropPath,genreId,mediaType,originalLanguage,originalTitle,overview,posterPath,releaseDate,title,isVideo;
        int[] genreIds;
        double popularity;

        SQLiteDatabase db = this.getReadableDatabase();

        String movieQuery = "SELECT * FROM "+moviesTable;
        Cursor cursorMovie = db.rawQuery(movieQuery,null);

        if(cursorMovie.moveToFirst()){
            while(!cursorMovie.isAfterLast()){
                id = cursorMovie.getInt(0);
                isAdult = cursorMovie.getString(1);
                backdropPath = cursorMovie.getString(2);
                genreId  = cursorMovie.getString(3);
                mediaType  = cursorMovie.getString(4);
                originalLanguage  = cursorMovie.getString(5);
                originalTitle  =  cursorMovie.getString(6);
                overview  =  cursorMovie.getString(7);
                popularity  =  cursorMovie.getDouble(8);
                posterPath  =  cursorMovie.getString(9);
                releaseDate  = cursorMovie.getString(10);
                title  =  cursorMovie.getString(11);
                isVideo  =  cursorMovie.getString(12);
                voteAverage  =  cursorMovie.getFloat(13);
                voteCount  =  cursorMovie.getInt(14);


                String[] genreIdArray = genreId.split(",");
                genreIds = new int[genreIdArray.length];
                for(int i = 0; i < genreIdArray.length;i++)
                    genreIds[i] = Integer.parseInt(genreIdArray[i]);


                boolean adult = isAdult.compareTo("true") == 0;
                boolean video = isVideo.compareTo("true") == 0;

                Movies movie = new Movies(adult,backdropPath,genreIds,id,mediaType,originalLanguage,originalTitle,overview,popularity,posterPath,releaseDate,title,video,voteAverage,voteCount);
                movies.add(movie);
                cursorMovie.moveToNext();
            }
        }

        cursorMovie.close();
        return movies;
    }



}
