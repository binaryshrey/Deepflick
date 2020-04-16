package com.example.deepflick.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieAdapterOnClickHandler{

    public MoviesAdapter moviesAdapter;
    public Movie[] jsonMovieData;
    public String query_main = "popular";
    public String query_sec = "top_rated";

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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(moviesAdapter);

        loadData(query_main);
    }
    @OnClick(R.id.retry)
    public void onButtonClick(View view){
        loadData(query_main);
    }
    public void loadData(String str){
        showJsonDataResults();
        new FetchMovieTask().execute(str);
    }

    @Override
    public void onClick(int adapterPosition) {
        Toast.makeText(this, jsonMovieData[adapterPosition].getTitle(), Toast.LENGTH_SHORT).show();
        
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0)
                return null;
            String sortBy = params[0];
            URL requestUrl = NetworkUtils.buitlUrl(sortBy);
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);
                jsonMovieData = TMDBJsonUtils.parseValuesFromJson(MainActivity.this, jsonResponse);
                return jsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] moviesData) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                showJsonDataResults();
                moviesAdapter = new MoviesAdapter(moviesData,MainActivity.this);
                mRecyclerView.setAdapter(moviesAdapter);
            } else
                showErrorMessage();
        }
    }

    private void showJsonDataResults() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRetry.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mRetry.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
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
                loadData(query_main);
                Toast.makeText(this, "Popular Movies", Toast.LENGTH_SHORT).show();
                break;
            case R.id.top_rated:
                loadData(query_sec);
                Toast.makeText(this, "Top Rated Movies", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
