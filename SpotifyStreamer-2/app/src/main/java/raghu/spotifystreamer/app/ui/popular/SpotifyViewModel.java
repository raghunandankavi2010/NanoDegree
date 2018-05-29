package raghu.spotifystreamer.app.ui.popular;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import raghu.spotifystreamer.app.data.MoviesDataSource;
import raghu.spotifystreamer.app.data.MoviesDataSourceFactory;
import raghu.spotifystreamer.app.model.Movies;
import raghu.spotifystreamer.app.network.NetworkState;


public class SpotifyViewModel extends ViewModel {

    public LiveData<PagedList<Movies>> moviesList;
    public LiveData<NetworkState> networkState;
    public LiveData<NetworkState> initalLoading;


    public SpotifyViewModel() {
        Executor executor = Executors.newFixedThreadPool(5);
        MoviesDataSourceFactory moviesDataSourceFactory = new MoviesDataSourceFactory(executor);

        LiveData<MoviesDataSource> tDataSource = moviesDataSourceFactory.getMutableLiveData();
        initalLoading = Transformations.switchMap(tDataSource, new Function<MoviesDataSource, LiveData<NetworkState>>() {
            @SuppressWarnings("unchecked")
            @Override
            public LiveData<NetworkState> apply(MoviesDataSource dataSource) {
                return dataSource.getInitialLoading();
            }
        });


        networkState = Transformations.switchMap(tDataSource, new Function<MoviesDataSource, LiveData<NetworkState>>() {
            @SuppressWarnings("unchecked")
            @Override
            public LiveData<NetworkState> apply(MoviesDataSource dataSource) {
                return dataSource.getNetworkState();
            }
        });

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(8)
                        .setPageSize(8)
                        .build();


        moviesList = (new LivePagedListBuilder<>(moviesDataSourceFactory, pagedListConfig))
                .setFetchExecutor(executor)
                .build();
    }
}
