package com.sam_chordas.android.stockhawk.presenters;

import android.util.Log;

import com.sam_chordas.android.stockhawk.APIS.LineGraphApi;
import com.sam_chordas.android.stockhawk.events.ErrorEvent;
import com.sam_chordas.android.stockhawk.events.GraphDetailEvent;
import com.sam_chordas.android.stockhawk.models.MyPojo;

import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.operators.CompletableOnSubscribeTimeout;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Raghunandan on 08-04-2016.
 */
public class LineGraphDataPresenter  {

    private LineGraphApi lineGraphApi;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Inject
    public LineGraphDataPresenter(LineGraphApi lineGraphApi) {
        this.lineGraphApi = lineGraphApi;
    }

    public void fetchGraphDetails_Symbol(String companySymbol) {

        /* Make sure observable runs on the background thread with scheduler.io */
        /* subscription on ui thread */
        Observable<MyPojo> response =lineGraphApi.fetchDetails(companySymbol);
        compositeSubscription.add(response.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MyPojo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        // use event bus to communicate data. could have used a interface as a callback here
                        EventBus.getDefault().post(new ErrorEvent(e));
                    }

                    @Override
                    public void onNext(MyPojo myPojo) {

                        //Log.i("Presenter",""+myPojo.getSeries().size());
                        EventBus.getDefault().post(new GraphDetailEvent(myPojo));
                    }
                }));


    }

    public void onDestroy()
    {
        // unsubscribe subscription
        compositeSubscription.unsubscribe();
    }



}
