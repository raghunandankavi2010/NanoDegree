package raghu.spotifystreamer.app.network;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;



public class Listing<T> {

    private LiveData<PagedList<T>> pagedListLiveData;
    private LiveData<NetworkState> initalLoading;
    private LiveData<NetworkState> networkState;

    public Listing(LiveData<PagedList<T>> pagedListLiveData, LiveData<NetworkState> initalLoading, LiveData<NetworkState> networkState) {
        this.pagedListLiveData = pagedListLiveData;
        this.initalLoading = initalLoading;
        this.networkState = networkState;
    }

    public LiveData<NetworkState> getInitalLoading() {
        return initalLoading;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<T>> getPagedListLiveData() {
        return pagedListLiveData;
    }
}
