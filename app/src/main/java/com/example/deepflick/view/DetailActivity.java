package com.example.deepflick.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepflick.R;
import com.example.deepflick.adapter.ReviewsAdapter;
import com.example.deepflick.adapter.TrailersAdapter;
import com.example.deepflick.database.FavoriteMovie;
import com.example.deepflick.database.FavoriteMovieDatabase;
import com.example.deepflick.model.Review;
import com.example.deepflick.model.Trailer;
import com.example.deepflick.utils.AppExecutors;
import com.example.deepflick.utils.NetworkUtils;
import com.example.deepflick.utils.TMDBJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    //data members
    public TrailersAdapter mTrailerAdapter;
    public List<Trailer> jsonTrailerData;
    public ReviewsAdapter mReviewAdapter;
    public List<Review> jsonReviewData;
    private FavoriteMovieDatabase mDB;
    public boolean isFavoriteMovie = false;
    public String id,title,thumbnail,rating,adult,releaseDate,overview;

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

    @BindView(R.id.fav)
    ImageView mFavButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting contentView layout
        setContentView(R.layout.activity_detail);
        //binding the view using butterknife
        ButterKnife.bind(this);


        //horizontal layoutManager for Trailers
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        //setting the layout
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        //set trailer adapter for recycler view
        mRecyclerView.setAdapter(mTrailerAdapter);

        //Vertical layoutManager for Reviews
        LinearLayoutManager reviewsLM = new LinearLayoutManager(this);
        //setting the layout
        mReviewRecyclerView.setLayoutManager(reviewsLM);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewAdapter = new ReviewsAdapter(jsonReviewData);
        //set reviews adapter for recycler view
        mReviewRecyclerView.setAdapter(mReviewAdapter);


        //storing the data from intent
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        thumbnail = getIntent().getStringExtra("poster");
        rating = getIntent().getStringExtra("rate");
        releaseDate = getIntent().getStringExtra("release");
        overview = getIntent().getStringExtra("overview");
        adult = getIntent().getStringExtra("adult");


        mDB = FavoriteMovieDatabase.getInstance(getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(() -> {
            final FavoriteMovie fm = mDB.movieDao().loadMovieById(Integer.parseInt(id));
            //setting favorites movie
            Favorite((fm != null)? true : false);
        });

        //method to load the trailers based on specific movie id
        loadTrailers(id);
        loadReviews(id);

    }

    //method to display a toast and change icon color according to the condition
    private void Favorite(boolean fav){
        if (fav) {
            isFavoriteMovie = true;
            mFavButton.setImageResource(R.drawable.ic_favorite_red_24dp);
        } else {
            isFavoriteMovie = false;
            mFavButton.setImageResource(R.drawable.ic_favorite_white_24dp);
        }
    }

    //method to render intentValues
    public void displayIntentValues(){
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


        // Favorite button on clickListener
        mFavButton.setOnClickListener(v -> {
            final FavoriteMovie fmov = new FavoriteMovie(Integer.parseInt(id),title,thumbnail,rating,adult,releaseDate,overview);
            AppExecutors.getInstance().diskIO().execute(() -> {
                if (isFavoriteMovie)
                    // delete movie if present
                    mDB.movieDao().deleteFavMovie(fmov);
                else
                    // insert movie if not present
                    mDB.movieDao().insertFavMovie(fmov);
                runOnUiThread(() -> Favorite(!isFavoriteMovie));
            });
        });
    }


    //method to load the trailers based on specific movie id
    private void loadTrailers(String MovieId) {
        new FetchTrailerTask().execute(MovieId);
    }
    private void loadReviews(String id) {
        new FetchReviewTask().execute(id);
    }

    // Async Task for fetch all trailers
    public class FetchTrailerTask extends AsyncTask<String, Void, List<Trailer>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //setting progressbar as visible
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Trailer> doInBackground(String... params) {
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
        protected void onPostExecute(List<Trailer> trailerData) {
            //setting progress bar as invisible
            mProgressBar.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            if (trailerData != null) {
                mErrorMessage.setVisibility(View.INVISIBLE);
                mTrailerAdapter = new TrailersAdapter(trailerData, DetailActivity.this);
                mRecyclerView.setAdapter(mTrailerAdapter);
                displayIntentValues();
            } else {
                mErrorMessage.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
            }
        }
    }

    //Async task for reviews
    public class FetchReviewTask extends AsyncTask<String, Void, List<Review>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mReviewProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Review> doInBackground(String... params) {
            if (params.length == 0)
                return null;

            URL movieRequestUrl = NetworkUtils.buildReviewsUrl(id);

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                jsonReviewData = TMDBJsonUtils.parseReviewValuesFromJson(DetailActivity.this, jsonMovieResponse);
                return jsonReviewData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Review> reviewData) {
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