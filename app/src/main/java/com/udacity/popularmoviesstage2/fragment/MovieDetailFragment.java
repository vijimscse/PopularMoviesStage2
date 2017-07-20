package com.udacity.popularmoviesstage2.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.udacity.popularmoviesstage2.utils.DateFormatter;
import com.udacity.popularmoviesstage2.utils.IBundleKeys;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.popularmoviesstage2.utils.Config.IMAGE_BASE_URL;

/**
 * Created by VijayaLakshmi.IN on 7/14/2017.
 *
 * Showcases the selected movie details
 *
 */
public class MovieDetailFragment extends Fragment {
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
            Movie selectedMovie;
            if ((selectedMovie = bundle.getParcelable(IBundleKeys.SELECTED_MOVIE)) != null) {
                mTitle.setText(selectedMovie.getOriginalTitle());
                mSynopsis.setText(selectedMovie.getOverview());
                mUserRating.setText(String.valueOf(selectedMovie.getVoteAverage()));
                Picasso.with(getActivity()).load(IMAGE_BASE_URL + selectedMovie.getPosterPath())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.image_error)
                        .into(mMoviePoster);
                mReleaseDate.setText(DateFormatter.getDateFormat(selectedMovie.getReleaseDate()));
            }
        }
    }
}
