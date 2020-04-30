package com.example.deepflick.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepflick.R;
import com.example.deepflick.adapter.ReviewsAdapter;
import com.example.deepflick.adapter.TrailersAdapter;
import com.example.deepflick.model.Review;
import com.example.deepflick.model.Trailer;
import com.example.deepflick.utils.NetworkUtils;
import com.example.deepflick.utils.TMDBJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    //data members
    public TrailersAdapter mTrailerAdapter;
    public ArrayList<Trailer> jsonTrailerData;

    public ReviewsAdapter mReviewAdapter;
    public ArrayList<Review> jsonReviewData;

    public String id;

    //using @BindView along with the id of the view to declare view variable
    @BindView(R.id.detail_thumbnail)
    ImageView mDetailThumbnail;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.rating)
    TextView mRating;

    @BindView(R.id.release_date)
    TextView mReleaseDate;

    @BindView(R.id.adult)
    TextView mAdult;

    @BindView(R.id.overview)
    TextView mOverview;

    @BindView(R.id.trailers_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress_bar_trailer)
    ProgressBar mProgressBar;

    @BindView(R.id.error_msg_trailer)
    TextView mErrorMessage;

    @BindView(R.id.reviews_rv)
    RecyclerView mReviewRecyclerView;

    @BindView(R.id.progress_bar_review)
    ProgressBar mReviewProgressBar;

    @BindView(R.id.error_msg_review)
    TextView mReviewErrorMessage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting contentView layout
        setContentView(R.layout.activity_detail);
        //binding the view using butterknife
        ButterKnife.bind(this);


        //horizontal layoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        //setting the layout
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        //set trailer adapter for recycler view
        mRecyclerView.setAdapter(mTrailerAdapter);


        LinearLayoutManager reviewsLM = new LinearLayoutManager(this);
        mReviewRecyclerView.setLayoutManager(reviewsLM);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewAdapter = new ReviewsAdapter(jsonReviewData);
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        //loading image with picasso into mDetailThumbnail view
        Picasso.get()
                .load(getIntent().getStringExtra("poster"))
                .into(mDetailThumbnail);

        //setting the title of the activity as movie title
        setTitle(getIntent().getStringExtra("title"));
        //getting values from intent and setting it in their respective views
        mTitle.setText(getIntent().getStringExtra("title"));
        mRating.append(getIntent().getStringExtra("rate"));
        mReleaseDate.append(getIntent().getStringExtra("release"));
        mOverview.setText(getIntent().getStringExtra("overview"));
        //storing the value of adult content in a variable
        String adult = getIntent().getStringExtra("adult");
        //rendering the value based on given condition
        if(adult.equals("true"))
            mAdult.setText("Yes");
        else
            mAdult.setText("No");
        //get Movie Id
        id = getIntent().getStringExtra("id");

        //method to load the trailers based on specific movie id
        loadTrailers(id);
        loadReviews(id);

    }

    //method to load the trailers based on specific movie id
    private void loadTrailers(String MovieId) {
        new FetchTrailerTask().execute(MovieId);
    }
    private void loadReviews(String id) {
        new FetchReviewTask().execute(id);
    }

    // Async Task for fetch all trailers
    public class FetchTrailerTask extends AsyncTask<String, Void, ArrayList<Trailer>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //setting progressbar as visible
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {
            if (params.length == 0)
                return null;

            //method call to build main url
            URL trailersUrl = NetworkUtils.buildTrailerUrl(id);

            try {
                //storing jsonResponse
                String jsonTrailerResponse = NetworkUtils.getResponseFromHttpUrl(trailersUrl);
                jsonTrailerData = TMDBJsonUtils.parseTrailerValuesFromJson(DetailActivity.this, jsonTrailerResponse);
                return jsonTrailerData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> trailerData) {
            //setting progress bar as invisible
            mProgressBar.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            if (trailerData != null) {
                mErrorMessage.setVisibility(View.INVISIBLE);
                mTrailerAdapter = new TrailersAdapter(trailerData, DetailActivity.this);
                mRecyclerView.setAdapter(mTrailerAdapter);
            } else {
                mErrorMessage.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
            }
        }
    }

    //Async task for reviews
    public class FetchReviewTask extends AsyncTask<String, Void, ArrayList<Review>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mReviewProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Review> doInBackground(String... params) {
            if (params.length == 0){
                return null;
            }

            URL movieRequestUrl = NetworkUtils.buildReviewsUrl(id);

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                jsonReviewData
                        = TMDBJsonUtils.parseReviewValuesFromJson(DetailActivity.this, jsonMovieResponse);

                return jsonReviewData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviewData) {
            mReviewProgressBar.setVisibility(View.INVISIBLE);
            mReviewRecyclerView.setVisibility(View.VISIBLE);
            if (reviewData != null) {
                mReviewErrorMessage.setVisibility(View.INVISIBLE);
                mReviewAdapter = new ReviewsAdapter(reviewData);
                mReviewRecyclerView.setAdapter(mReviewAdapter);
            }else{
                mReviewErrorMessage.setVisibility(View.VISIBLE);
                mReviewErrorMessage.setVisibility(View.INVISIBLE);
            }
        }

    }


}