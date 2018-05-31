package raghu.spotifystreamer.app.network;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import raghu.spotifystreamer.app.data.MoviesDataSource;
import raghu.spotifystreamer.app.data.MoviesDataSourceFactory;
import raghu.spotifystreamer.app.model.Movies;

public class PopularMoviesRepository {


    public Listing<Movies> movies() {

        Executor executor = Executors.newFixedThreadPool(5);
        MoviesDataSourceFactory moviesDataSourceFactory = new MoviesDataSourceFactory(executor);

        LiveData<MoviesDataSource> tDataSource = moviesDataSourceFactory.getMutableLiveData();
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(8)
                        .setPageSize(8)
                        .build();


        LiveData<PagedList<Movies>> pagedListLiveData =
                (new LivePagedListBuilder<>(moviesDataSourceFactory, pagedListConfig))
                .setFetchExecutor(executor)
                .build();


        LiveData<NetworkState> initalLoading = Transformations.switchMap(tDataSource, new Function<MoviesDataSource, LiveData<NetworkState>>() {
            @SuppressWarnings("unchecked")
            @Override
            public LiveData<NetworkState> apply(MoviesDataSource dataSource) {
                return dataSource.getInitialLoading();
            }
        });


        LiveData<NetworkState> networkState = Transformations.switchMap(tDataSource, new Function<MoviesDataSource, LiveData<NetworkState>>() {
            @SuppressWarnings("unchecked")
            @Override
            public LiveData<NetworkState> apply(MoviesDataSource dataSource) {
                return dataSource.getNetworkState();
            }
        });

        return new Listing<Movies>(pagedListLiveData,initalLoading,networkState);
    }

}
