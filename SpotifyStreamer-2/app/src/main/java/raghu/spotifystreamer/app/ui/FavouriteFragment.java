package raghu.spotifystreamer.app.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.app.FragmentContainer;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.LoaderManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import raghu.spotifystreamer.app.R;
import raghu.spotifystreamer.app.provider.MoviesContract;

/**
 * Created by Raghunandan on 24-11-2015.
 */
public class FavouriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String[] PROJECTION = new String[]{
            BaseColumns._ID,
            MoviesContract.Movies.MOVIE_ID,
            MoviesContract.Movies.MOVIE_TITLE,
            MoviesContract.Movies.MOVIE_OVERVIEW,
            MoviesContract.Movies.MOVIE_GENRE_IDS,
            MoviesContract.Movies.MOVIE_POPULARITY,
            MoviesContract.Movies.MOVIE_VOTE_AVERAGE,
            MoviesContract.Movies.MOVIE_VOTE_COUNT,
            MoviesContract.Movies.MOVIE_BACKDROP_PATH,
            MoviesContract.Movies.MOVIE_POSTER_PATH,
            MoviesContract.Movies.MOVIE_RELEASE_DATE,
            MoviesContract.Movies.MOVIE_FAVORED
    };
    private EmptyRecyclerView mRecyclerView;
    private ProgressBar mProgress;
    private TextView mErrorText;
    private ImageCursorAdapter mAdapter;
    private Cursor mCursor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movielist,container,false);
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        mProgress = (ProgressBar) root.findViewById(R.id.progressBar);
        mErrorText = (TextView) root.findViewById(R.id.list_empty);
        mRecyclerView = (EmptyRecyclerView) root.findViewById(R.id.recyclerView);

        mAdapter = new ImageCursorAdapter(getActivity(),mCursor);
        mRecyclerView.setAdapter(mAdapter);
        return root;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] selectionArgs = { String.valueOf(1) };
        return new CursorLoader(getActivity(),  // Context
                MoviesContract.Movies.CONTENT_URI, // URI
                PROJECTION,                // Projection
                "movie_favored=?",                           // Selection
                selectionArgs,                           // Selection args
                null); // Sort

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursor = data;
        mAdapter.swapCursor(mCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mAdapter.changeCursor(null);
    }
}