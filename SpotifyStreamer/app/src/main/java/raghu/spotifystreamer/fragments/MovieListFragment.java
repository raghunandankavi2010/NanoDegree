package raghu.spotifystreamer.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import raghu.spotifystreamer.Models.Movies;
import raghu.spotifystreamer.R;
import raghu.spotifystreamer.adapters.ImageGridAdapter;
import raghu.spotifystreamer.views.EmptyRecyclerView;


/**
 * Created by Raghunandan on 23-09-2015.
 */
public class MovieListFragment extends Fragment {

    private TextView empty_tv;
    private EmptyRecyclerView mRecyclerview;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar pb;
    private ImageGridAdapter mAdapter;
    private ArrayList<Movies> movies;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pb = (ProgressBar) view.findViewById(R.id.progressBar);
        mRecyclerview = (EmptyRecyclerView) view.findViewById(R.id.recyclerView);
        empty_tv = (TextView) view.findViewById(R.id.list_empty);

    }

    public void displayGridImages(ArrayList<Movies> movies) {
        this.movies = movies;
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

}
