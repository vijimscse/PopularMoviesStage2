package com.udacity.popularmoviesstage2.io;

import com.udacity.popularmoviesstage2.dto.MovieList;
import com.udacity.popularmoviesstage2.dto.ReviewList;
import com.udacity.popularmoviesstage2.dto.VideoList;
import com.udacity.popularmoviesstage2.utils.Config;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.udacity.popularmoviesstage2.utils.Config.MOVIE_REVIEWS;
import static com.udacity.popularmoviesstage2.utils.Config.SORT_API;
import static com.udacity.popularmoviesstage2.utils.Config.TRAILER_VIDEOS;

/**
 * Created by Vijayalakshmi.IN on 7/13/2017.
 */

public interface APIService {
    @GET(SORT_API)
    Call<MovieList> getMovie(@Path("sort") String order, @Query("api_key") String key);

    @GET(TRAILER_VIDEOS)
    Call<VideoList> requestTrailerVideos(@Path(Config.ID) String id, @Query(Config.API_KEY) String apiKey);


    @GET(MOVIE_REVIEWS)
    Call<ReviewList> requestMovieReviews(@Path(Config.ID) String id, @Query(Config.API_KEY) String apiKey);
}

