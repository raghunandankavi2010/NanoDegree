package raghu.spotifystreamer.app.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import java.util.concurrent.Executor;

import raghu.spotifystreamer.app.model.Movies;

public class MoviesDataSourceFactory extends DataSource.Factory<Integer,Movies> {

    private MutableLiveData<MoviesDataSource> mutableLiveData;
    private MoviesDataSource moviesDataSource;
    private Executor executor;

    public MoviesDataSourceFactory(Executor executor) {
        this.mutableLiveData = new MutableLiveData<MoviesDataSource>();
        this.executor = executor;
    }


    @Override
    public DataSource<Integer,Movies> create() {
        moviesDataSource = new MoviesDataSource(executor);
        mutableLiveData.postValue(moviesDataSource);
        return moviesDataSource;
    }

    public MutableLiveData<MoviesDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

}
