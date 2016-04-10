package com.sam_chordas.android.stockhawk.APIS;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Raghunandan on 08-04-2016.
 */
public interface API {

    @GET("{companySymbol}/chartdata;type=quote;range=5y/json")
    Call<ResponseBody> getGraphDEtails(@Path("companySymbol") String symbol);

}
