package com.shwet.mymovielistapplication.screen;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.google.gson.Gson;
import com.shwet.mymovielistapplication.R;
import com.shwet.mymovielistapplication.common.Common;
import com.shwet.mymovielistapplication.models.Movies;

import java.io.File;

import static com.shwet.mymovielistapplication.common.Common.LARGE_POSTER_FOLDER_NAME;
import static com.shwet.mymovielistapplication.common.Common.SMALL_POSTER_FOLDER_NAME;
import static com.shwet.mymovielistapplication.screen.MainActivity.EXTRA_DATA;

public class MovieDetailsActivity extends AppCompatActivity {

    Movies movie;
    AppCompatImageView imageView;
    AppCompatTextView[] textViewLabels,textViewDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        getMovieObject();
    }

    public void getMovieObject(){
        Intent intent = getIntent();
        movie = (new Gson()).fromJson(intent.getStringExtra(EXTRA_DATA),Movies.class);
        if(movie != null) {
            setUpImageView(movie);
        }
    }

    public void setUpImageView(Movies movie){
        imageView = findViewById(R.id.imageView);
        if(Common.isNetworkAvailable(getApplicationContext())) {
            String moviePoster = Common.LARGE_POSTER_URL + "/" + movie.getPosterPath();
            Glide.with(getApplicationContext())
                    .load(moviePoster)
                    .into(imageView);
        }else{
            String posterSmallPath = LARGE_POSTER_FOLDER_NAME + File.separator + "large_"+movie.getPosterPath().substring(1);
            Glide.with(getApplicationContext())
                    .load(posterSmallPath)
                    .into(imageView);
        }
        setUpToolbar(movie.getTitle());
        setUpTextViews(movie);
    }

    public void setUpToolbar(String movieName){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(movieName);
        setSupportActionBar(toolbar);
    }

    public void setUpTextViews(Movies movie){

        Typeface faceExtraBold = Typeface.createFromAsset(getAssets(),
                "font/gilroy_extraBold.otf");
        Typeface faceSemiBold = Typeface.createFromAsset(getAssets(),
                "font/gilroy_semiBold.ttf");

        textViewLabels = new AppCompatTextView[6];
        textViewDetails = new AppCompatTextView[6];

        textViewLabels[0] = findViewById(R.id.textViewLabelAdult);
        textViewDetails[0] = findViewById(R.id.textViewAdult);
        textViewDetails[0].setText(""+movie.getAdult());

        textViewLabels[1] = findViewById(R.id.textViewLabelMediaType);
        textViewDetails[1] = findViewById(R.id.textViewMediaType);
        textViewDetails[1].setText(movie.getMediaType());

        textViewLabels[2] = findViewById(R.id.textViewLabelReleaseDate);
        textViewDetails[2] = findViewById(R.id.textViewReleaseDate);
        textViewDetails[2].setText(movie.getReleaseDate());

        textViewLabels[3] = findViewById(R.id.textViewLabelPopularity);
        textViewDetails[3] = findViewById(R.id.textViewPopularity);
        textViewDetails[3].setText(""+movie.getPopularity());

        textViewLabels[4] = findViewById(R.id.textViewLabelOverview);
        textViewDetails[4] = findViewById(R.id.textViewOverview);
        textViewDetails[4].setText(movie.getOverview());

        textViewLabels[5] = findViewById(R.id.textViewLabelOriginalLanguage);
        textViewDetails[5] = findViewById(R.id.textViewOriginalLanguage);
        textViewDetails[5].setText(movie.getOriginalLanguage());



        for(int i = 0; i < textViewDetails.length;i++ ){
            textViewLabels[i].setTypeface(faceExtraBold);
            textViewLabels[i].setTextColor(getResources().getColor(R.color.colorPrimary));
            textViewDetails[i].setTypeface(faceSemiBold);
            textViewDetails[i].setTextColor(getResources().getColor(R.color.textColorPrimaryDark));
        }
    }
}