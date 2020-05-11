package com.example.deepflick.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteMovie.class}, version = 1, exportSchema = false)
public abstract class FavoriteMovieDatabase extends RoomDatabase {
    //reference taken from TODO_PROJECT lessons in Android Architecture lectures

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "FavMoviesList";
    private static FavoriteMovieDatabase sInstance;

    public static FavoriteMovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavoriteMovieDatabase.class, FavoriteMovieDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract FavoriteMovieDAO movieDao();
}