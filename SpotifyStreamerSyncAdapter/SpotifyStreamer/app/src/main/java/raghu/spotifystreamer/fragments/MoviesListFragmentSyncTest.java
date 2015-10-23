package raghu.spotifystreamer.fragments;

/**
 * Created by Raghunandan on 22-10-2015.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import raghu.spotifystreamer.FragmentNetWorkRequest;
import raghu.spotifystreamer.Models.Movies;
import raghu.spotifystreamer.R;
import raghu.spotifystreamer.Utilities.CheckNetwork;
import raghu.spotifystreamer.Utilities.SyncUtils;
import raghu.spotifystreamer.adapters.ImageCursorAdapter;
import raghu.spotifystreamer.adapters.ImageGridAdapter;
import raghu.spotifystreamer.provider.MoviesContract;
import raghu.spotifystreamer.views.EmptyRecyclerView;


/**
 * Created by Raghunandan on 23-09-2015.
 */
public class MoviesListFragmentSyncTest extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private TextView empty_tv;
    private EmptyRecyclerView mRecyclerview;
    private ProgressBar pb;
    private Cursor mCursor;


    private Object mSyncObserverHandle;

    /**
     * Options menu used to populate ActionBar.
     */
    private Menu mOptionsMenu;

    /**
     * Projection for querying the content provider.
     */
    private static final String[] PROJECTION = new String[] {
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
            MoviesContract.Movies.MOVIE_FAVORED,


    };



    private ImageCursorAdapter mAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);


    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SyncUtils.CreateSyncAccount(getActivity());
        //SyncUtils.TriggerRefresh();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        getLoaderManager().initLoader(0, null, this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pb = (ProgressBar) view.findViewById(R.id.progressBar);
        mRecyclerview = (EmptyRecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerview.setHasFixedSize(true);
        empty_tv = (TextView) view.findViewById(R.id.list_empty);
        mRecyclerview.setEmptyView(empty_tv);

       /* mAdapter = new ImageGridAdapter(movies);
        mRecyclerview.setAdapter(mAdapter);*/



    }



    public void displayProgressBar() {
        pb.setVisibility(View.VISIBLE);

    }

    public void hideProgressBar() {
        pb.setVisibility(View.GONE);

    }


    /**
     * Create a new anonymous SyncStatusObserver. It's attached to the app's ContentResolver in
     * onResume(), and removed in onPause(). If status changes, it sets the state of the Refresh
     * button. If a sync is active or pending, the Refresh button is replaced by an indeterminate
     * ProgressBar; otherwise, the button itself is displayed.
     */
    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        /** Callback invoked with the sync adapter status changes. */
        @Override
        public void onStatusChanged(int which) {
            getActivity().runOnUiThread(new Runnable() {
                /**
                 * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
                 * runs on the UI thread.
                 */
                @Override
                public void run() {

                    AccountManager accountManager = (AccountManager) getActivity().getSystemService(Context.ACCOUNT_SERVICE);

                    // Create the account type and default account
                    Account account = new Account(getActivity().getString(R.string.app_name), getActivity().getString(R.string.account_type));

                    if (account == null) {
                        // GetAccount() returned an invalid value. This shouldn't happen, but
                        // we'll set the status to "not refreshing".
                        //setRefreshActionButtonState(false);
                        check(false);
                        return;
                    }

                    // Test the ContentResolver to see if the sync adapter is active or pending.
                    // Set the state of the refresh button accordingly.
                    boolean syncActive = ContentResolver.isSyncActive(
                            account, MoviesContract.CONTENT_AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(
                            account, MoviesContract.CONTENT_AUTHORITY);
                    check(syncActive || syncPending);
                    // setRefreshActionButtonState(syncActive || syncPending);
                }
            });
        }
    };

    public void check(boolean bool)
    {
        if(bool)
        {
            //displayProgressBar();

        }else {
            //hideProgressBar();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mSyncStatusObserver.onStatusChanged(0);

        // Watch for sync state changes
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }

    /**
     * Query the content provider for data.
     *
     * <p>Loaders do queries in a background thread. They also provide a ContentObserver that is
     * triggered when data in the content provider changes. When the sync adapter updates the
     * content provider, the ContentObserver responds by resetting the loader and then reloading
     * it.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // We only have one loader, so we can ignore the value of i.
        // (It'll be '0', as set in onCreate().)
        return new CursorLoader(getActivity(),  // Context
                MoviesContract.Movies.CONTENT_URI, // URI
                PROJECTION,                // Projection
                null,                           // Selection
                null,                           // Selection args
                null); // Sort
    }

    /**
     * Move the Cursor returned by the query into the ListView adapter. This refreshes the existing
     * UI with the data in the Cursor.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter = new ImageCursorAdapter(getActivity(),cursor);
        mRecyclerview.setAdapter(mAdapter);
        Log.i("MoviesListFragment",""+cursor.getCount());



    }

    /**
     * Called when the ContentObserver defined for the content provider detects that data has
     * changed. The ContentObserver resets the loader, and then re-runs the loader. In the adapter,
     * set the Cursor value to null. This removes the reference to the Cursor, allowing it to be
     * garbage-collected.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.changeCursor(null);
    }

}

