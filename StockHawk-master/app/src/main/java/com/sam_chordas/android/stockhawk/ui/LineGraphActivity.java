package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.APIS.LineGraphApi;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.SotckHawkApplication;
import com.sam_chordas.android.stockhawk.events.ErrorEvent;
import com.sam_chordas.android.stockhawk.events.GraphDetailEvent;
import com.sam_chordas.android.stockhawk.models.MyPojo;
import com.sam_chordas.android.stockhawk.models.Series;
import com.sam_chordas.android.stockhawk.presenters.LineGraphDataPresenter;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Raghunandan on 08-04-2016.
 */
public class LineGraphActivity extends AppCompatActivity {

    @Inject
    LineGraphDataPresenter lineGraphDataPresenter;
    private com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar progressCircle;
    private TextView errorMessage;

    private ValueLineChart lineChart;
    private MyPojo mypojo;
    private String name;

    private boolean isLoaded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SotckHawkApplication.component().inject(this);
        setContentView(R.layout.activity_line_graph);

        lineChart = (ValueLineChart) findViewById(R.id.line_chart);
        progressCircle = (com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar) findViewById(R.id.progress_circle);
        errorMessage = (TextView) findViewById(R.id.error_message);

        String companySymbol = getIntent().getStringExtra("symbol");

        if (savedInstanceState == null) {
            lineGraphDataPresenter.fetchGraphDetails_Symbol(companySymbol);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe
    public void onEvent(GraphDetailEvent graphDetailEvent) throws ParseException {

        this.mypojo = graphDetailEvent.getGraphDetails();

        name = this.mypojo.getMeta().getCompany_Name();
        loadGraph(name,mypojo);
    }

    private void loadGraph(String name, MyPojo mypojo) throws ParseException {

        this.mypojo = mypojo;
        setTitle(name);

        progressCircle.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);
        for (int i = 0; i < mypojo.getSeries().size(); i++) {
            Series myseries = (Series) mypojo.getSeries().get(i);
            Log.i("Date",""+myseries.getDate());
            SimpleDateFormat srcFormat = new SimpleDateFormat("yyyyMMdd");
            String date = android.text.format.DateFormat.
                    getMediumDateFormat(getApplicationContext()).
                    format(srcFormat.parse(String.valueOf(myseries.getDate())));
            series.addPoint(new ValueLinePoint(date, myseries.getClose()));
        }
        if (!isLoaded) {
            lineChart.startAnimation();
        }
        lineChart.addSeries(series);
        lineChart.setVisibility(View.VISIBLE);

        isLoaded = true;
    }


    @Subscribe
    public void onEvent(ErrorEvent errorEvent) {
        errorEvent.getT().printStackTrace();
        progressCircle.setVisibility(View.GONE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isLoaded && this.mypojo!=null) {
            outState.putParcelable("mypojo",this.mypojo);
            outState.putString("company_name",name);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("mypojo")) {
            isLoaded = true;

            name = savedInstanceState.getString("company_name");
            MyPojo mypojo = savedInstanceState.getParcelable("mypojo");
            try {
                loadGraph(name,mypojo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
