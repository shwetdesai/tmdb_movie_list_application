package com.shwet.mymovielistapplication.screen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.shwet.mymovielistapplication.R;
import com.shwet.mymovielistapplication.adapter.MyRecyclerViewAdapter;
import com.shwet.mymovielistapplication.common.Common;
import com.shwet.mymovielistapplication.database.DatabaseHelper;
import com.shwet.mymovielistapplication.models.APIClient;
import com.shwet.mymovielistapplication.models.Movies;
import com.shwet.mymovielistapplication.models.MovieApi;
import com.shwet.mymovielistapplication.models.MoviesJSON;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shwet.mymovielistapplication.common.Common.LARGE_POSTER_FOLDER_NAME;
import static com.shwet.mymovielistapplication.common.Common.MAIN_FOLDER_NAME;
import static com.shwet.mymovielistapplication.common.Common.SMALL_POSTER_FOLDER_NAME;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MovieApi movieApi;
    static final String TAG = "MainActivity";
    public static final String EXTRA_DATA = "movie_data";

    MyRecyclerViewAdapter adapter;
    List<Movies> movies;
    DatabaseHelper databaseHelper;
    boolean isFABOpen = false;
    RecyclerView recyclerView;
    FloatingActionButton fab, fab1, fab2, fab3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpFloatingOption();
        databaseHelper = new DatabaseHelper(getApplicationContext());
        if(Common.isNetworkAvailable(getApplicationContext()))
            setUpMovieAPI();
        else
            getAllMovies();

    }

    public void setUpFloatingOption(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab_1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab_2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab_3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(movies, new Comparator<Movies>() {
                    @Override
                    public int compare(Movies o1, Movies o2) {
                        double popularity1 = o1.getPopularity();
                        double popularity2 = o2.getPopularity();
                        if (popularity1 < popularity2) {
                            return -1;
                        } else if (popularity1 > popularity2) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(movies, new Comparator<Movies>() {
                    @Override
                    public int compare(Movies o1, Movies o2) {
                        String title1 = o1.getTitle();
                        String title2 = o2.getTitle();
                        return title1.compareTo(title2);
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(movies, new Comparator<Movies>() {
                    @Override
                    public int compare(Movies o1, Movies o2) {
                        String date1 = o1.getReleaseDate();
                        String date2 = o2.getReleaseDate();
                        return date1.compareTo(date2);
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_65));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_125));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_185));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
    }

    public void setUpRecycleAdapter(boolean isOnline){
        // set up the RecyclerView
        recyclerView = findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(getApplicationContext(), movies,isOnline);
        adapter.setClickListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.animate();
        recyclerView.setAdapter(adapter);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }


    public void setUpMovieAPI(){
        movieApi = APIClient.getClient().create(MovieApi.class);

        Call<MoviesJSON> call = movieApi.getPopularMovies(Common.API_KEY);
        call.enqueue(new Callback<MoviesJSON>() {
            @Override
            public void onResponse(Call<MoviesJSON> call, Response<MoviesJSON> response) {
                movies = response.body().results;
                for(int i = 0; i <movies.size(); i++)
                    addMovie(movies.get(i));
                setUpRecycleAdapter(true);

            }

            @Override
            public void onFailure(Call<MoviesJSON> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    public void addMovie(Movies movie){
        if(databaseHelper.addMovie(movie)){
            boolean success = createFolder();
            Log.i(TAG,"Success "+success);
            if (success) {
                String[] posterArray = new String[3];
                posterArray[0] = Common.SMALL_POSTER_URL +  movie.getPosterPath();
                posterArray[1] = Common.LARGE_POSTER_URL +  movie.getPosterPath();
                posterArray[2] = movie.getPosterPath();
                if(isStoragePermissionGranted())
                    new AsyncTaskDownloadImages().execute();
            }
        }
    }

    public boolean createFolder(){
        File folder = new File(MAIN_FOLDER_NAME);
        File folder_small = new File(SMALL_POSTER_FOLDER_NAME);
        File folder_large = new File(LARGE_POSTER_FOLDER_NAME);
        boolean success = true;
        if (!folder.exists() || !folder_small.exists() || !folder_large.exists()) {
            success = folder.mkdirs();
            success = folder_small.mkdir();
            success = folder_large.mkdir();
        }
        return success;
    }

    public void getAllMovies(){
        movies = databaseHelper.getAllMovies();
        setUpRecycleAdapter(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this,MovieDetailsActivity.class);
        intent.putExtra(EXTRA_DATA,(new Gson()).toJson(movies.get(position)));
        startActivity(intent);
    }

    private class AsyncTaskDownloadImages extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
                // Download the image
                URL urlSmallPoster = null;
                URL urlLargePoster = null;
                try {
                    urlSmallPoster = new URL(params[0]);
                    urlLargePoster = new URL(params[1]);
                    InputStream inputSmall = urlSmallPoster.openStream();
                    File storagePath = new File(SMALL_POSTER_FOLDER_NAME+ "/" + "small_" + params[2].substring(1));
                    if(!storagePath.exists()) {
                        try {

                            OutputStream output = new FileOutputStream(storagePath);
                            try {
                                byte[] buffer = new byte[1024];
                                int bytesRead = 0;
                                while ((bytesRead = inputSmall.read(buffer, 0, buffer.length)) >= 0) {
                                    output.write(buffer, 0, bytesRead);
                                }
                            } finally {
                                output.close();
                            }
                        } finally {
                            inputSmall.close();
                        }
                    }


                    storagePath = new File(LARGE_POSTER_FOLDER_NAME);
                    InputStream inputLarge = urlLargePoster.openStream();
                    if(!storagePath.exists()) {
                        try {

                            OutputStream output = new FileOutputStream(storagePath + "/" + "large_" + params[2].substring(1));
                            try {
                                byte[] buffer = new byte[1024];
                                int bytesRead = 0;
                                while ((bytesRead = inputLarge.read(buffer, 0, buffer.length)) >= 0) {
                                    output.write(buffer, 0, bytesRead);
                                }
                            } finally {
                                output.close();
                            }
                        } finally {
                            inputLarge.close();
                        }
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return "";
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation


        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}

