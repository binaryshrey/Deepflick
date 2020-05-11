package com.example.deepflick.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteMovieDAO {
    @Query("SELECT * FROM FavMovie ORDER BY mId")
    LiveData<List<FavoriteMovie>> loadAllFavMovies();

    @Insert
    void insertFavMovie(FavoriteMovie favoriteMovie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavMovie(FavoriteMovie favoriteMovie);

    @Delete
    void deleteFavMovie(FavoriteMovie favoriteMovie);

    @Query("SELECT * FROM FavMovie WHERE mId = :id")
    FavoriteMovie loadMovieById(int id);

}