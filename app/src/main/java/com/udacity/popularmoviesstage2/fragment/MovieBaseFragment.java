package com.udacity.popularmoviesstage2.fragment;

import android.content.ContentValues;
import android.support.v4.app.Fragment;

import com.udacity.popularmoviesstage2.db.MoviesContract;
import com.udacity.popularmoviesstage2.dto.Movie;

/**
 * Created by VijayaLakshmi.IN on 7/21/2017.
 */

public class MovieBaseFragment extends Fragment {

    protected void insertIntoDB(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.MovieEntry.MOVIE_SHORT_TITLE, movie.getTitle());
        contentValues.put(MoviesContract.MovieEntry.MOVIE_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(MoviesContract.MovieEntry.OVERVIEW, movie.getOverview());
        contentValues.put(MoviesContract.MovieEntry.MOVIE_ID, movie.getId());
        contentValues.put(MoviesContract.MovieEntry.RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MoviesContract.MovieEntry.USER_RATING, movie.getVoteAverage());
        contentValues.put(MoviesContract.MovieEntry.POSTER_PATH, movie.getPosterPath());

        getActivity().getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, contentValues);
    }

    protected int deleteFromDB(Movie movie) {
        return getActivity().getContentResolver().delete(MoviesContract.MovieEntry.buildMoviesUri(movie.getId()),
                null, null);
    }
}
