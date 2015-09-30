package raghu.spotifystreamer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import raghu.spotifystreamer.FragmentNetWorkRequest;
import raghu.spotifystreamer.Models.Movies;
import raghu.spotifystreamer.R;
import raghu.spotifystreamer.SortedNetworkRequest;
import raghu.spotifystreamer.adapters.ImageGridAdapter;
import raghu.spotifystreamer.views.EmptyRecyclerView;

/**
 * Created by Raghunandan on 28-09-2015.
 */
public class SortedMoviesFragment extends Fragment implements SortedNetworkRequest.TaskCallbacks2 {

    private TextView empty_tv;
    private EmptyRecyclerView mRecyclerview;
    private ProgressBar pb;
    private ImageGridAdapter mAdapter;
    private ArrayList<Movies> movies;

    private static final String TAG_TASK_FRAGMENT_SORTED = "task_fragment_sorted";

    private SortedNetworkRequest mTaskFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        mTaskFragment = (SortedNetworkRequest) fm.findFragmentByTag(TAG_TASK_FRAGMENT_SORTED);

        // If we haven't retained the worker fragment, then create it
        // and set this UIFragment as the TaskFragment's target fragment.
        if (mTaskFragment == null) {
            mTaskFragment =  new SortedNetworkRequest();//.newInstance("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=6d32f2a6596004bb66069187b4c9b933");
            mTaskFragment.setTargetFragment(this,1);
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT_SORTED).commit();
        }


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pb = (ProgressBar) view.findViewById(R.id.progressBar);
        mRecyclerview = (EmptyRecyclerView) view.findViewById(R.id.recyclerView);
        empty_tv = (TextView) view.findViewById(R.id.list_empty);
        displayProgressBar();
        if(savedInstanceState!=null)
        {

            displayGridImages();
        }else if(savedInstanceState == null  && mTaskFragment.getMoviesList()!=null && mTaskFragment.getMoviesList().size()>0)
        {
            displayGridImages();
        }

    }


    public void displayGridImages() {
        hideProgressBar();
        this.movies = mTaskFragment.getMoviesList();
        Log.i("Size", "Movies Size" + movies.size());
        mRecyclerview.setHasFixedSize(true);
        //layoutManager = new GridLayoutManager(getActivity(), 2);
        //mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.setEmptyView(empty_tv);
        mAdapter = new ImageGridAdapter(movies);
        mRecyclerview.setAdapter(mAdapter);
    }

    public void displayProgressBar() {
        pb.setVisibility(View.VISIBLE);

    }

    public void hideProgressBar() {
        pb.setVisibility(View.GONE);
    }


    @Override
    public void onPreExecute() {


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
        mTaskFragment.setTargetFragment(null,-1);
    }

}



