package com.shwet.mymovielistapplication.adapter;

import android.content.Context;
import android.graphics.Movie;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shwet.mymovielistapplication.R;
import com.shwet.mymovielistapplication.common.Common;
import com.shwet.mymovielistapplication.models.Movies;

import java.io.File;
import java.util.List;

import static com.shwet.mymovielistapplication.common.Common.SMALL_POSTER_FOLDER_NAME;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Movies> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private boolean isOnline;
    private static final String TAG = "Adapter";

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, List<Movies> data,boolean isOnline) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
        this.isOnline = isOnline;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Typeface faceExtraBold = Typeface.createFromAsset(context.getAssets(),
                "font/gilroy_extraBold.otf");
        Typeface faceSemiBold = Typeface.createFromAsset(context.getAssets(),
                "font/gilroy_semiBold.ttf");
        String movieTitle = mData.get(position).getTitle();
        holder.textViewMovieName.setText(movieTitle);
        holder.textViewMovieName.setTypeface(faceExtraBold);
        String releaseDate = mData.get(position).getReleaseDate();
        holder.textViewReleaseDate.setText(releaseDate);
        holder.textViewReleaseDate.setTypeface(faceSemiBold);
        if(isOnline) {
            String posterPath = Common.SMALL_POSTER_URL + "/"+mData.get(position).getPosterPath();
            Glide.with(context)
                    .load(posterPath)
                    .into(holder.imageView);

        }else{
            String posterSmallPath = SMALL_POSTER_FOLDER_NAME + File.separator + "small_"+mData.get(position).getPosterPath().substring(1);
            Glide.with(context)
                    .load(posterSmallPath)
                    .into(holder.imageView);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatTextView textViewMovieName,textViewReleaseDate;
        AppCompatImageView imageView;


        ViewHolder(View itemView) {
            super(itemView);
            textViewMovieName = itemView.findViewById(R.id.textViewMovieName);
            textViewReleaseDate = itemView.findViewById(R.id.textViewMovieReleaseDate);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Movies getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
