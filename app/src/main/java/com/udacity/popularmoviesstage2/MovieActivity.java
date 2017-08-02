package com.udacity.popularmoviesstage2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.udacity.popularmoviesstage2.dto.Movie;
import com.udacity.popularmoviesstage2.fragment.MovieDetailFragment;
import com.udacity.popularmoviesstage2.fragment.MovieListFragment;

import butterknife.ButterKnife;

import static com.udacity.popularmoviesstage2.utils.IBundleKeys.SELECTED_MOVIE;

/**
 * Created by Vijayalakshmi.IN on 14/07/2017
 * <p>
 * Movie screen displays the list of movies and its details
 */
public class MovieActivity extends AppCompatActivity implements MovieListFragment.IMovieListFragmentListener {

    private static final String TAG = MovieActivity.class.getSimpleName();

    private MovieListFragment mMovieListFragment;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            return;
        }
        addMovieListFragment();
    }

    private void addMovieListFragment() {
        if (mMenu != null) {
            mMenu.findItem(R.id.sort_by).setVisible(false);
        }

        mMovieListFragment = new MovieListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, mMovieListFragment);
        transaction.commit();
    }

    private void addMovieDetailFragment(Movie selectedMovie) {
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SELECTED_MOVIE, selectedMovie);
        movieDetailFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, movieDetailFragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onMovieSelected(Movie selectedMovie) {
        addMovieDetailFragment(selectedMovie);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_options, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof MovieListFragment) {
            mMenu.findItem(R.id.sort_by).setVisible(true);
        } else {
            mMenu.findItem(R.id.sort_by).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
