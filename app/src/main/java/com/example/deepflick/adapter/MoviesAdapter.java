package com.example.deepflick.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepflick.R;
import com.example.deepflick.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {
    //declaring data members
    public ArrayList<Movie> mMovieData;
    public final MovieAdapterOnClickHandler mClickHandler;

    //constructor
    public MoviesAdapter(ArrayList<Movie> movie, MovieAdapterOnClickHandler clickHandler) {
        mMovieData = movie;
        mClickHandler = clickHandler;
    }

    //onClickHandler interface
    public interface MovieAdapterOnClickHandler {
        void onClick(int adapterPosition);
    }

    //methods onCreateViewHolder, onBindViewHolder, getItemCount
    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        //inflate list item xml into a view
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder holder, int position) {
        //set the movie for list item's position
        String movieToBind = mMovieData.get(position).getThumbnail();
        //loading image with picasso into mDetailThumbnail view
        Picasso.get()
                .load(movieToBind)
                .into(holder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        //returning the count of mMovieData
        if (mMovieData == null)
            return 0;
        return mMovieData.size();
    }

    //ViewHolder class
    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //using @BindView along with the id of the view to declare view variable
        @BindView(R.id.iv_thumbnail)
        ImageView mThumbnail;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            //binding the view using butterknife
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }
        //onClick method
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }
}