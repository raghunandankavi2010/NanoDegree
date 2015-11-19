package raghu.spotifystreamer.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import raghu.spotifystreamer.app.R;
import raghu.spotifystreamer.app.RxApp;
import raghu.spotifystreamer.app.model.Movies;
import raghu.spotifystreamer.app.model.SpotifyMoviesModel;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Raghunandan on 15-11-2015.
 */
public class PopulatMoviesFrament extends Fragment {
    private static final String STATE_MOVIES = "state_movies";
    private static final String REQUEST_PEDNING = "request_pending";
    private static final String ERROR = "error";
    private static final String LOAD_MORE = "error";
    private ProgressBar mProgress;
    private View mErrorText;
    private int pageCount = 1, totalcount;

    private EmptyRecyclerView mRecyclerView;
    private boolean mRequestPending, mError, mLoadMore;
    private ImageGridAdapter mAdapter;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private SpotifyMoviesModel mModel;
    private GridLayoutManager mGridLayoutManager;


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoadMore == true) {
            mAdapter.remove();
        }

        mSubscriptions.unsubscribe();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movielist, container, false);
        mProgress = (ProgressBar) root.findViewById(R.id.progressBar);
        mErrorText = root.findViewById(R.id.list_empty);
        mRecyclerView = (EmptyRecyclerView) root.findViewById(R.id.recyclerView);


        mAdapter = new ImageGridAdapter();
        mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.addItemDecoration(new MarginItemDecoration(getActivity()));

        mGridLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spanCount = mGridLayoutManager.getSpanCount();
                switch (mAdapter.getItemViewType(position)) {
                    case ImageGridAdapter.VIEW_ITEM:
                        return 1;
                    case ImageGridAdapter.VIEW_PROG:
                        return spanCount;
                    default:
                        return -1;
                }
            }
        });

        mModel = ((RxApp) getActivity().getApplication()).component().spotifyMoviesModel();

        mRecyclerView.addOnScrollListener(new EndlessScrollListener(pageCount) {
            @Override
            public void onLoadMore(int current_page, int totalItemCount) {
                //add progress item
                Log.i("Count is", "" + current_page);
                pageCount = current_page;

                if (totalcount != totalItemCount) {

                    mSubscriptions.add(
                            mModel.getMoviesList("popularity.desc", pageCount)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new MoviesListSubscriber()));
                    mLoadMore = true;
                    mAdapter.add(null);
                }

            }
        });
        return root;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            pageCount = savedInstanceState.getInt("count");
            boolean bool = savedInstanceState.getBoolean(REQUEST_PEDNING, false);
            if (bool) {

                if (mModel.getRequest() != null) {
                    mProgress.setVisibility(View.VISIBLE);
                    mSubscriptions.add(
                            mModel.getRequest()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new MoviesListSubscriber()));

                    //Toast.makeText(getActivity(), "Continuing Subscription", Toast.LENGTH_SHORT).show();
                }
            } else if (savedInstanceState.containsKey(STATE_MOVIES)) {

                //Toast.makeText(getActivity(), "List restored", Toast.LENGTH_SHORT).show();
                ArrayList<Movies> list = savedInstanceState.getParcelableArrayList(STATE_MOVIES);
                mAdapter.addPosts(list);
            } else {
                fetchData();
            }

        } else {
            fetchData();
        }
    }

    private void fetchData() {

        mRequestPending = true;
        mProgress.setVisibility(View.VISIBLE);
        mErrorText.setVisibility(View.INVISIBLE);

        mSubscriptions.add(
                mModel.getMoviesList("popularity.desc", pageCount)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new MoviesListSubscriber()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter.getmList().size() > 0) {
            outState.putParcelableArrayList(STATE_MOVIES, mAdapter.getmList());
        }
        outState.putBoolean(REQUEST_PEDNING, mRequestPending);
        outState.putBoolean(ERROR, mError);
        outState.putInt("count", pageCount);
        //outState.putBoolean(LOAD_MORE, mLoadMore);

    }


    private class MoviesListSubscriber extends Subscriber<ArrayList<Movies>> {

        @Override
        public void onNext(ArrayList<Movies> movies) {

            totalcount = mModel.getTotal_pages();
            if (mLoadMore == true) {
                mAdapter.remove();
                mLoadMore = false;
            }
            mRequestPending = false;
            mProgress.setVisibility(View.INVISIBLE);
            mAdapter.addPosts(movies);

        }

        @Override
        public void onCompleted() {
            mRequestPending = false;
            mProgress.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onError(Throwable t) {
            mRequestPending = false;
            mError = true;

            if (mLoadMore == true) {
                mAdapter.remove();
                mLoadMore =false;
                mErrorText.setVisibility(View.INVISIBLE);
            } else {
                mProgress.setVisibility(View.INVISIBLE);
                mErrorText.setVisibility(View.VISIBLE);
            }
        }
    }
}

