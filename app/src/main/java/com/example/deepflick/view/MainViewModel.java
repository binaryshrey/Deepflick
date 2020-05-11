package com.example.deepflick.view;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.deepflick.database.FavoriteMovie;
import com.example.deepflick.database.FavoriteMovieDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    //reference taken from TODO_PROJECT lessons in Android Architecture lectures
    private LiveData<List<FavoriteMovie>> movies;

    public MainViewModel(Application application) {
        super(application);
        FavoriteMovieDatabase database= FavoriteMovieDatabase.getInstance(this.getApplication());
        movies = database.movieDao().loadAllFavMovies();
    }

    public LiveData<List<FavoriteMovie>> getMovies() {
        return movies;
    }
}