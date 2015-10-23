package raghu.spotifystreamer.fragments;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import raghu.spotifystreamer.FragmentNetWorkRequest;
import raghu.spotifystreamer.Models.Movies;
import raghu.spotifystreamer.R;
import raghu.spotifystreamer.Utilities.CheckNetwork;
import raghu.spotifystreamer.adapters.ImageGridAdapter;
import raghu.spotifystreamer.provider.MoviesContract;
import raghu.spotifystreamer.sync.PopularMoviesAuthenticatorService;
import raghu.spotifystreamer.views.EmptyRecyclerView;


/**
 * Created by Raghunandan on 23-09-2015.
 */
public class MovieListFragment extends Fragment implements FragmentNetWorkRequest.TaskCallbacks {

    private TextView empty_tv;
    private EmptyRecyclerView mRecyclerview;
    private ProgressBar pb;
    private ImageGridAdapter mAdapter;
    private ArrayList<Movies> movies= new ArrayList<Movies>();

    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    private FragmentNetWorkRequest mTaskFragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        if(mTaskFragment!=null)
        {
            Log.i("not null", "Great"+mTaskFragment.getMoviesList().size());
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pb = (ProgressBar) view.findViewById(R.id.progressBar);
        mRecyclerview = (EmptyRecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setEmptyView(empty_tv);
        mAdapter = new ImageGridAdapter(movies);
        mRecyclerview.setAdapter(mAdapter);
        empty_tv = (TextView) view.findViewById(R.id.list_empty);

       if(CheckNetwork.isNetworkAvailable(getActivity())) {
           displayProgressBar();
           FragmentManager fm = getActivity().getSupportFragmentManager();
           mTaskFragment = (FragmentNetWorkRequest) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

           // If we haven't retained the worker fragment, then create it
           // and set this UIFragment as the TaskFragment's target fragment.
           if (mTaskFragment == null) {
               mTaskFragment = FragmentNetWorkRequest.newInstance("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=6d32f2a6596004bb66069187b4c9b933");
               mTaskFragment.setFragment(this);
               fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
           } else {

           }
       }

        if(savedInstanceState!=null && mTaskFragment!=null)
        {
            displayGridImages();
        }else if(savedInstanceState == null && mTaskFragment!=null && mTaskFragment.getMoviesList()!=null && mTaskFragment.getMoviesList().size()>0)
        {
            displayGridImages();
        }
    }

    public void displayGridImages() {
        Log.i("Hiding","Great");
        hideProgressBar();
        if(mTaskFragment.getMoviesList()!=null) {
            this.movies.addAll(mTaskFragment.getMoviesList());
            mAdapter.notifyDataSetChanged();
        }

    }

    public void displayProgressBar() {
        pb.setVisibility(View.VISIBLE);

    }

    public void hideProgressBar() {
        pb.setVisibility(View.GONE);

    }
    @Override
    public void onPreExecute() {

        displayProgressBar();
    }

    @Override
    public void onProgressUpdate(int percent) {

    }

    @Override
    public void onCancelled() {


    }

    @Override
    public void onPostExecute() {

        displayGridImages();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mTaskFragment!=null)
        mTaskFragment.setTargetFragment(null,-1);
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
                        return;
                    }

                    // Test the ContentResolver to see if the sync adapter is active or pending.
                    // Set the state of the refresh button accordingly.
                    boolean syncActive = ContentResolver.isSyncActive(
                            account, MoviesContract.CONTENT_AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(
                            account, MoviesContract.CONTENT_AUTHORITY);
                   // setRefreshActionButtonState(syncActive || syncPending);
                }
            });
        }
    };
}
