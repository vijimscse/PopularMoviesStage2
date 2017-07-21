package com.udacity.popularmoviesstage2.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.udacity.popularmoviesstage2.R;
import com.udacity.popularmoviesstage2.adapter.MovieRecyclerViewAdapter;
import com.udacity.popularmoviesstage2.db.MoviesContract;
import com.udacity.popularmoviesstage2.dto.Movie;
import com.udacity.popularmoviesstage2.dto.MovieList;
import com.udacity.popularmoviesstage2.io.IOManager;
import com.udacity.popularmoviesstage2.utils.DialogUtility;
import com.udacity.popularmoviesstage2.utils.IBundleKeys;
import com.udacity.popularmoviesstage2.utils.NetworkUtility;
import com.udacity.popularmoviesstage2.utils.SortType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by VijayaLakshmi.IN on 7/14/2017.
 *
 * Showcases the list of movies based on SortType
 */
public class MovieListFragment extends MovieBaseFragment implements MovieRecyclerViewAdapter.MovieItemClickListener {

    private static final String TAG = MovieListFragment.class.getSimpleName();
    private static final int MIN_GRID_SIZE = 2;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private MovieRecyclerViewAdapter mMovieRecyclerViewAdapter;
    private final List<Movie> mMovieList = new ArrayList<>();
    private IMovieListFragmentListener mMovieListFragmentListener;

    private boolean mFavoritesView;

    public interface IMovieListFragmentListener {
        void onMovieSelected(Movie selectedMovie);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (! (context instanceof IMovieListFragmentListener)) {
            throw new IllegalStateException(((FragmentActivity)context).getClass().getSimpleName() +  "must implement" + IMovieListFragmentListener.class.getSimpleName());
        }
        mMovieListFragmentListener = (IMovieListFragmentListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.movie_list, container, false);
        ButterKnife.bind(this, view);

        Log.d("TAG", "Fragment onCreateView ");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("TAG", "Fragment onActivityCreated");

        if (savedInstanceState != null) {
            ArrayList<Movie> movieList = savedInstanceState.getParcelableArrayList(IBundleKeys.MOVIE_LIST);
            mMovieList.clear();
            if (movieList != null && !movieList.isEmpty()) {
                mMovieList.addAll(movieList);
            }
        }
        if (getActivity() != null && getView() != null) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns()));

            mMovieRecyclerViewAdapter = new MovieRecyclerViewAdapter(getActivity(), mMovieList, this);
            mRecyclerView.setAdapter(mMovieRecyclerViewAdapter);

            if (mMovieList == null || mMovieList.isEmpty()) {
                requestMoviesFromServer(SortType.POPULAR);
            } else {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Request movie list based on the selected sort type.
     * @param sortType
     */
    public void requestMoviesFromServer(int sortType) {
        if (NetworkUtility.isInternetConnected(getActivity())) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            IOManager.requestMovies(sortType, new Callback<MovieList>() {
                @Override
                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                    mProgressBar.setVisibility(View.GONE);
                    if (response != null && response.body() != null &&
                            response.body().getMovies() != null) {
                        mMovieList.clear();
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mMovieList.addAll(response.body().getMovies());
                        updateMovieListFavMovies();
                        mMovieRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<MovieList> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    DialogUtility.showToast(getActivity(), getString(R.string.generic_error));
                }
            });
        } else {
            DialogUtility.showToast(getActivity(), getString(R.string.no_internet_connection));
        }
    }

    private void updateMovieListFavMovies() {
        List<Movie> movieList = getCachedFavMovieList();

        if (movieList != null && !movieList.isEmpty()) {
            for (Movie cachedMovie : movieList) {
                for (Movie movie : mMovieList) {
                    if (movie.getId() == cachedMovie.getId()) {
                        movie.setmIsFavorite(true);
                    }
                }
            }
        }
    }

    private int numberOfColumns() {
        int nColumns = 0;

        if (getActivity() != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int widthDivider = 400;
            int width = displayMetrics.widthPixels;
            nColumns = width / widthDivider;

            if (nColumns < MIN_GRID_SIZE) return MIN_GRID_SIZE;
        }

        return nColumns;
    }

    @Override
    public void onItemClick(int position) {
        mMovieListFragmentListener.onMovieSelected(mMovieList.get(position));
    }

    @Override
    public void onFavSelect(int position) {
        Movie movie = mMovieList.get(position);

        if (!movie.isFavorite()) {
            // Add the item to DB
            movie.setmIsFavorite(true);

            insertIntoDB(movie);
            mMovieRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            // delete the item from DB and update the current list if already in favorites view
            int count = deleteFromDB(movie);

            if (count > 0) {
                if (mFavoritesView) {
                    mMovieList.remove(position);
                } else {
                    movie.setmIsFavorite(false);
                }
                mMovieRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(IBundleKeys.MOVIE_LIST, (ArrayList<? extends Parcelable>) mMovieList);
        Log.d("TAG", "Fragment onsave");
    }

    private void showPopup(View anchor) {
        PopupMenu popup = new PopupMenu(getActivity(), anchor);
        MenuInflater inflate = popup.getMenuInflater();
        inflate.inflate(R.menu.sort_menu_options, popup.getMenu());

        popup.setOnMenuItemClickListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.popular:
                            mFavoritesView = false;
                            requestMoviesFromServer(SortType.POPULAR);
                            break;

                        case R.id.top_rated:
                            mFavoritesView = false;
                            requestMoviesFromServer(SortType.TOP_RATED);
                            break;

                        case R.id.favorites:
                            mFavoritesView = true;
                            fetchFavMoviesFromDB();
                            break;


                    }
                    return true;
                });

        popup.show();
    }

    private void fetchFavMoviesFromDB() {
        mMovieList.clear();
        mMovieList.addAll(getCachedFavMovieList());
        mMovieRecyclerViewAdapter.notifyDataSetChanged();
    }

    private List<Movie> getCachedFavMovieList() {
        List<Movie> movieList = new ArrayList<>();
        Cursor cursor = getActivity().getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();

                movie.setTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_SHORT_TITLE)));
                movie.setId(cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_ID)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MoviesContract.MovieEntry.USER_RATING)));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.MOVIE_ORIGINAL_TITLE)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.RELEASE_DATE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.OVERVIEW)));
                movie.setmIsFavorite(true);

                movieList.add(movie);
            } while (cursor.moveToNext());
        }

        return movieList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by:
                if (getActivity() != null && getActivity().findViewById(R.id.sort_by) != null) {
                    showPopup(getActivity().findViewById(R.id.sort_by));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
