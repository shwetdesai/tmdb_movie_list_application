package com.shwet.mymovielistapplication.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {
    @GET("movie/popular")
    Call<MoviesJSON> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MoviesJSON> getTopRatedMovies(@Query("api_key") String apiKey);
}
