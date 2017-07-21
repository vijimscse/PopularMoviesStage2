package com.udacity.popularmoviesstage2.io;

import com.udacity.popularmoviesstage2.dto.MovieList;
import com.udacity.popularmoviesstage2.dto.ReviewList;
import com.udacity.popularmoviesstage2.dto.VideoList;
import com.udacity.popularmoviesstage2.utils.Config;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.udacity.popularmoviesstage2.utils.Config.MOVIE_REVIES;
import static com.udacity.popularmoviesstage2.utils.Config.POPULAR;
import static com.udacity.popularmoviesstage2.utils.Config.TOP_RATED;
import static com.udacity.popularmoviesstage2.utils.Config.TRAILER_VIDEOS;

/**
 * Created by Vijayalakshmi.IN on 7/13/2017.
 */

public interface APIService {

    @GET(POPULAR)
    Call<MovieList> requestPopularMovies(@Query(Config.API_KEY) String apiKey);

    @GET(TOP_RATED)
    Call<MovieList> requestTopRatedMovies(@Query(Config.API_KEY) String apiKey);


    @GET(TRAILER_VIDEOS)
    Call<VideoList> requestTrailerVideos(@Path(Config.ID) String id, @Query(Config.API_KEY) String apiKey);


    @GET(MOVIE_REVIES)
    Call<ReviewList> requestMovieReviews(@Path(Config.ID) String id, @Query(Config.API_KEY) String apiKey);
}

