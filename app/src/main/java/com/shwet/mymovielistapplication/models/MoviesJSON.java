package com.shwet.mymovielistapplication.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesJSON {

    @SerializedName("id")
    public int id;
    @SerializedName("page")
    public int page;
    @SerializedName("total_pages")
    public int totalPages;
    @SerializedName("total_results")
    public int totalResults;
    @SerializedName("results")
    public List<Movies> results = null;

}
