package com.example.deepflick.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deepflick.R;
import com.example.deepflick.adapter.MoviesAdapter;
import com.example.deepflick.model.Movie;
import com.example.deepflick.utils.NetworkUtils;
import com.example.deepflick.utils.TMDBJsonUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//MainActivity implementing onClickHandler
public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieAdapterOnClickHandler{

    //defining data members
    public MoviesAdapter moviesAdapter;
    public List<Movie> jsonMovieData;
    public String query = "popular";
    public static final String LIFECYCLE_STATE = "state";

    //using @BindView along with the id of the view to declare view variable
    @BindView(R.id.recyclerview_movies)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.error_msg)
    TextView mErrorMessage;
    @BindView(R.id.retry)
    Button mRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null)
            query = savedInstanceState.getString(LIFECYCLE_STATE);

        setContentView(R.layout.activity_main);

        //binding the view using butterknife
        ButterKnife.bind(this);

        //defining grid layout that spans two columns
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(moviesAdapter);

        //passing the query parameter into the loadData method
        loadData(query);
    }

    //onSaveInstanceState method to persist dataChanges on change of device configuration
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        String lifeCycleSortState = query;
        outState.putString(LIFECYCLE_STATE,lifeCycleSortState);
        super.onSaveInstanceState(outState);

    }


    //method to fetch data on button click after detecting no internet connection
    @OnClick(R.id.retry)
    public void onButtonClick(View view){
        loadData(query);
    }

    //method to fetch data based on query paramater
    public void loadData(String str){
        showJsonDataResults();
        new FetchMovieTask().execute(str);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //setting progressbar as visible
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            if (params.length == 0)
                return null;
            String sortBy = params[0];
            //method call to build main url
            URL requestUrl = NetworkUtils.buildMainUrl(sortBy);
            try {
                //storing jsonResponse
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);
                jsonMovieData = TMDBJsonUtils.parseValuesFromJson(MainActivity.this, jsonResponse);
                return jsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> moviesData) {
            //setting progress bar as invisible
            mProgressBar.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                //invoking showJsonDataResults method
                showJsonDataResults();
                moviesAdapter = new MoviesAdapter(moviesData,MainActivity.this);
                mRecyclerView.setAdapter(moviesAdapter);
            } else
                showErrorMessage();
        }
    }

    //setting error message and retry button as invisible and recyclerview as visible
    private void showJsonDataResults() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRetry.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    //setting error message and retry button as visible and recyclerview as invisible
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mRetry.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(int adapterPosition){
        //method to send info to detail activity
        Intent intent = new Intent(MainActivity.this,DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,adapterPosition);
        intent.putExtra("title",jsonMovieData.get(adapterPosition).getTitle());
        intent.putExtra("poster", jsonMovieData.get(adapterPosition).getThumbnail());
        intent.putExtra("rate", jsonMovieData.get(adapterPosition).getRating());
        intent.putExtra("release", jsonMovieData.get(adapterPosition).getReleaseDate());
        intent.putExtra("adult", jsonMovieData.get(adapterPosition).getAdult());
        intent.putExtra("overview", jsonMovieData.get(adapterPosition).getOverview());
        intent.putExtra("id",jsonMovieData.get(adapterPosition).getId());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemThatWasSelected = item.getItemId();
        switch (itemThatWasSelected){
            case R.id.popular:
                query = "popular";
                loadData(query);
                Toast.makeText(this, "Popular Movies", Toast.LENGTH_SHORT).show();
                break;
            case R.id.top_rated:
                query = "top_rated";
                loadData(query);
                Toast.makeText(this, "Top Rated Movies", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
