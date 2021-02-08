package com.shwet.mymovielistapplication.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class Movies {
    @SerializedName("adult")
    public boolean adult;
    @SerializedName("backdrop_path")
    public String backdropPath;
    @SerializedName("genre_ids")
    public int[] genreIds;
    @SerializedName("id")
    public int id;
    @SerializedName("media_type")
    public String mediaType;
    @SerializedName("original_language")
    public String originalLanguage;
    @SerializedName("original_title")
    public String originalTitle;
    @SerializedName("overview")
    public String overview;
    @SerializedName("popularity")
    public double popularity;
    @SerializedName("poster_path")
    public String posterPath;
    @SerializedName("release_date")
    public String releaseDate;
    @SerializedName("title")
    public String title;
    @SerializedName("video")
    public boolean video;
    @SerializedName("vote_average")
    public float voteAverage;
    @SerializedName("vote_count")
    public int voteCount;

    public Movies(boolean adult, String backdropPath, int[] genreIds, int id, String mediaType, String originalLanguage, String originalTitle, String overview, double popularity, String posterPath, String releaseDate, String title, boolean video, float voteAverage, int voteCount){
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.genreIds = genreIds;
        this.id = id;
        this.mediaType = mediaType;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public String getTitle(){ return this.title; }

    public String getOriginalTitle(){ return this.originalTitle; }

    public String getPosterPath(){
        return this.posterPath;
    }

    public boolean getAdult(){ return this.adult; }

    public String getMediaType(){ return this.mediaType; }

    public String getReleaseDate(){ return this.releaseDate; }

    public double getPopularity(){ return this.popularity; }

    public String getOverview(){ return this.overview; }

    public int getId(){ return this.id; }

    public String getBackdropPath(){ return this.backdropPath; }

    public String getOriginalLanguage(){ return this.originalLanguage; }

    public boolean getVideo(){ return this.video; }

    public String getGenreIds(){
        String genreIds = "";
        for (int i = 0; i<this.genreIds.length;i++)
            genreIds += ""+ this.genreIds[i]+",";
        return genreIds;
    }

    public float getVoteAverage(){ return this.voteAverage; }

    public int getVoteCount() { return this.voteCount; }


}
