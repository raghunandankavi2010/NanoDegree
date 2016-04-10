package com.sam_chordas.android.stockhawk.APIS;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sam_chordas.android.stockhawk.SotckHawkApplication;
import com.sam_chordas.android.stockhawk.models.MyPojo;
import com.squareup.okhttp.ResponseBody;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Raghunandan on 08-04-2016.
 */
public class LineGraphApi {

    @Inject
    Retrofit retrofit;

    @Inject
    Application application;

    public LineGraphApi()
    {
        SotckHawkApplication.netcomponent().inject(this);
    }

    public Observable<MyPojo> fetchDetails(final String companySymbol) {



       Observable<MyPojo> myPojoObservable = Observable.create(new Observable.OnSubscribe<MyPojo>() {
            @Override
            public void call(final Subscriber<? super MyPojo> subscriber) {

                Call<ResponseBody> response = retrofit.create(API.class).getGraphDEtails(companySymbol);
                response.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {

                        if (response.code() == 200) {
                            try {
                                // Trim response string
                                String result = response.body().string();
                                if (result.startsWith("finance_charts_json_callback( ")) {
                                    result = result.substring(29, result.length() - 2);
                                }

                                Gson gson =new GsonBuilder()
                                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
                                        .create();
                                MyPojo pojo = gson.fromJson(result,MyPojo.class);
                                subscriber.onNext(pojo);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {

                        subscriber.onError(t);
                    }
                });
            }
        });

        myPojoObservable.cache();
       return myPojoObservable;
    }
}
