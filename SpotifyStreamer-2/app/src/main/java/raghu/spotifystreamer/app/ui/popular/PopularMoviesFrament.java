package raghu.spotifystreamer.app.ui.popular;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import raghu.spotifystreamer.app.R;
import raghu.spotifystreamer.app.model.Movies;
import raghu.spotifystreamer.app.network.NetworkState;
import raghu.spotifystreamer.app.network.Status;
import raghu.spotifystreamer.app.ui.EmptyRecyclerView;
import raghu.spotifystreamer.app.ui.OnMovieSelected;
import raghu.spotifystreamer.app.ui.OnMovieSelectionListener;


/**
 * Created by Raghunandan on 15-11-2015.
 */
public class PopularMoviesFrament extends Fragment implements OnMovieSelected {


    private ProgressBar mProgress;
    private View mErrorText;
    private PopularMoviesListAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private OnMovieSelectionListener onMovieSelectionListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movielist, container, false);
        mProgress = (ProgressBar) root.findViewById(R.id.progressBar);
        mErrorText = root.findViewById(R.id.list_empty);
        EmptyRecyclerView mRecyclerView = (EmptyRecyclerView) root.findViewById(R.id.recyclerView);


        SpotifyViewModel viewModel = ViewModelProviders.of(this).get(SpotifyViewModel.class);

        mAdapter = new PopularMoviesListAdapter(this);

        viewModel.moviesList.observe(this, new Observer<PagedList<Movies>>() {
            @Override
            public void onChanged(@Nullable PagedList<Movies> pagedList) {
                mAdapter.submitList(pagedList);
            }
        });

        viewModel.networkState.observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                Log.i("NetworkState",""+networkState.getStatus());
                mAdapter.setNetworkState(networkState);

            }
        });

        viewModel.initalLoading.observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if(networkState.getStatus()== Status.RUNNING){
                    mProgress.setVisibility(View.VISIBLE);
                    mErrorText.setVisibility(View.GONE);
                }else if(networkState.getStatus() == Status.FAILED){
                    mProgress.setVisibility(View.GONE);
                    mErrorText.setVisibility(View.VISIBLE);
                }else {
                    mProgress.setVisibility(View.GONE);
                    mErrorText.setVisibility(View.GONE);
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.addItemDecoration(new MarginItemDecoration(getActivity()));

        mGridLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spanCount = mGridLayoutManager.getSpanCount();
                switch (mAdapter.getItemViewType(position)) {
                    case PopularMoviesListAdapter.VIEW_ITEM:
                        return 1;
                    case PopularMoviesListAdapter.VIEW_PROG:
                        return spanCount;
                    default:
                        return -1;
                }
            }
        });



        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onMovieSelectionListener = (OnMovieSelectionListener)context;
    }

    @Override
    public void movieselected(Movies movie, View view, int position) {

        onMovieSelectionListener.onMovieSelected(movie);
    }


}

