package com.udacity.popularmoviesstage2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MoviesDBHelper extends SQLiteOpenHelper {
	public static final String LOG_TAG = MoviesDBHelper.class.getSimpleName();

	//name & version
	private static final String DATABASE_NAME = "movies.db";
	private static final int DATABASE_VERSION = 1;

	public MoviesDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Create the database
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
				MoviesContract.MovieEntry.TABLE_MOVIES + "(" + MoviesContract.MovieEntry._ID +
				MoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				MoviesContract.MovieEntry.MOVIE_ID + " TEXT UNIQUE NOT NULL ON CONFLICT REPLACE, " +
				MoviesContract.MovieEntry.MOVIE_SHORT_TITLE + " TEXT, " +
				MoviesContract.MovieEntry.MOVIE_ORIGINAL_TITLE + " TEXT, " +
				MoviesContract.MovieEntry.POSTER_PATH + " TEXT, " +
				MoviesContract.MovieEntry.RELEASE_DATE + " TEXT, " +
				MoviesContract.MovieEntry.OVERVIEW + " TEXT, " +
				MoviesContract.MovieEntry.USER_RATING + " REAL DEFAULT 0.0 )";

		sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		//Right now, Database version is 1. When i release the next update and
		// if there are some changes, I will add Alter SQL statement
	}
}
