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
import rx.schedulers.Schedulers;

/**
 * Created by Raghunandan on 08-04-2016.
 */
public class LineGraphDataPresenter  {

    private LineGraphApi lineGraphApi;
    @Inject
    public LineGraphDataPresenter(LineGraphApi lineGraphApi) {
        this.lineGraphApi = lineGraphApi;
    }

    public void fetchGraphDetails_Symbol(String companySymbol) {

        Observable<MyPojo> response =lineGraphApi.fetchDetails(companySymbol);
        response.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MyPojo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        EventBus.getDefault().post(new ErrorEvent(e));
                    }

                    @Override
                    public void onNext(MyPojo myPojo) {

                        Log.i("Presenter",""+myPojo.getSeries().size());
                        EventBus.getDefault().post(new GraphDetailEvent(myPojo));
                    }
                });


    }


}
