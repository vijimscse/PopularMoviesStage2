package com.udacity.popularmoviesstage2.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmoviesstage2.R;
import com.udacity.popularmoviesstage2.dto.Movie;
import com.udacity.popularmoviesstage2.dto.Review;
import com.udacity.popularmoviesstage2.dto.ReviewList;
import com.udacity.popularmoviesstage2.dto.Video;
import com.udacity.popularmoviesstage2.dto.VideoList;
import com.udacity.popularmoviesstage2.io.IOManager;
import com.udacity.popularmoviesstage2.utils.DateFormatter;
import com.udacity.popularmoviesstage2.utils.IBundleKeys;
import com.udacity.popularmoviesstage2.utils.NetworkUtility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.udacity.popularmoviesstage2.utils.Config.IMAGE_BASE_URL;

/**
 * Created by VijayaLakshmi.IN on 7/14/2017.
 *
 * Showcases the selected movie details
 *
 */
public class MovieDetailFragment extends MovieBaseFragment implements View.OnClickListener {

    private Movie mSelectedMovie;

    @BindView(R.id.movie_title)
    TextView mTitle;

    @BindView(R.id.synopsis)
    TextView mSynopsis;

    @BindView(R.id.userRating)
    TextView mUserRating;

    @BindView(R.id.releaseDate)
    TextView mReleaseDate;

    @BindView(R.id.movie_poster)
    ImageView mMoviePoster;

    @BindView(R.id.favorite)
    ImageView mFavorite;

    @BindView(R.id.trailer_list_container)
    ViewGroup mTrailerListContainer;

    @BindView(R.id.review_list_container)
    ViewGroup mReviewListContainer;

    @BindView(R.id.trailer_container)
    ViewGroup mTrailerContainer;

    @BindView(R.id.review_container)
    ViewGroup mReviewContainer;

    private ArrayList<Video> mVideoList = new ArrayList<>();
    private ArrayList<Review> mReviewList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.movie_detail);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_details, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if ((mSelectedMovie = bundle.getParcelable(IBundleKeys.SELECTED_MOVIE)) != null) {
                mTitle.setText(mSelectedMovie.getOriginalTitle());
                mSynopsis.setText(mSelectedMovie.getOverview());
                mUserRating.setText(String.valueOf(mSelectedMovie.getVoteAverage()));
                Picasso.with(getActivity()).load(IMAGE_BASE_URL + mSelectedMovie.getPosterPath())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.image_error)
                        .into(mMoviePoster);
                mReleaseDate.setText(DateFormatter.getDateFormat(mSelectedMovie.getReleaseDate()));
                if (mSelectedMovie.isFavorite()) {
                    mFavorite.setImageResource(R.drawable.favorite_selected);
                } else {
                    mFavorite.setImageResource(R.drawable.favorite_unselected);
                }
                fetchMovieReviews();
                fetchTrailerVideos();
            }
        }
    }

    private void fetchTrailerVideos() {
        if (getActivity() != null &&
                NetworkUtility.isInternetConnected(getActivity()) && !isDetached()) {
            IOManager.requestTrailerVideos(mSelectedMovie.getId(), new Callback<VideoList>() {
                @Override
                public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                    if (!isDetached() && response != null && response.body() != null) {
                        List videoList = response.body().getVideos();
                        if (videoList != null && !videoList.isEmpty()) {
                            mVideoList.addAll(videoList);
                            mTrailerContainer.setVisibility(View.VISIBLE);
                            showVideoList();
                        } else {
                            mTrailerContainer.setVisibility(View.GONE);
                        }
                    } else {
                        mTrailerContainer.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<VideoList> call, Throwable t) {
                    mTrailerContainer.setVisibility(View.GONE);
                }
            });
        }
    }

    private void showVideoList() {
        mTrailerListContainer.removeAllViews();

        for (int i = 0; i < mVideoList.size(); i++) {
            Video video = mVideoList.get(i);

            View trailerView = LayoutInflater.from(getActivity()).inflate(R.layout.video_row, null);
            trailerView.setTag(i);
            TextView title = (TextView) trailerView.findViewById(R.id.video_title);
            title.setText(video.getName());
            trailerView.setOnClickListener(this);

            mTrailerListContainer.addView(trailerView);
        }
    }

    private void showReviewList() {
        mReviewListContainer.removeAllViews();

        for (Review review : mReviewList) {
            View reviewView = LayoutInflater.from(getActivity()).inflate(R.layout.review_row, null);

            TextView title = (TextView) reviewView.findViewById(R.id.review);
            title.setText(review.getContent());

            mReviewListContainer.addView(reviewView);
        }
    }

    private void fetchMovieReviews() {
        if (getActivity() != null &&
                NetworkUtility.isInternetConnected(getActivity()) && !isDetached()) {
            IOManager.requestMovieReviews(mSelectedMovie.getId(), new Callback<ReviewList>() {
                @Override
                public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                    if (!isDetached() && response != null && response.body() != null) {
                        List reviewList = response.body().getReviews();
                        if (reviewList != null && !reviewList.isEmpty()) {
                            mReviewList.addAll(reviewList);
                            mReviewContainer.setVisibility(View.VISIBLE);
                            showReviewList();
                        } else {
                            mReviewContainer.setVisibility(View.GONE);
                        }
                    } else {
                        mReviewContainer.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ReviewList> call, Throwable t) {
                    mReviewContainer.setVisibility(View.GONE);
                }
            });
        }
    }

    @OnClick(R.id.favorite)
    public void onFavClick(View view) {
        if (!mSelectedMovie.isFavorite()) {
            // Add the item to DB
            mSelectedMovie.setmIsFavorite(true);

            insertIntoDB(mSelectedMovie);
        } else {
            // delete the item from DB and update the current list if already in favorites view
            deleteFromDB(mSelectedMovie.getId());
            mSelectedMovie.setmIsFavorite(false);
        }
        if (mSelectedMovie.isFavorite()) {
            mFavorite.setImageResource(R.drawable.favorite_selected);
        } else {
            mFavorite.setImageResource(R.drawable.favorite_unselected);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.video_row:
                int position = (int) view.getTag();
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" +  mVideoList.get(position).getKey()));
                intent.putExtra("force_fullscreen", true);
                // Verify that the intent will resolve to an activity
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    getActivity().startActivity(intent);
                }
                break;
        }
    }
}
