package com.sam_chordas.android.stockhawk.events;

import com.sam_chordas.android.stockhawk.models.MyPojo;

/**
 * Created by Raghunandan on 08-04-2016.
 */
public class GraphDetailEvent {

    private MyPojo graphDetails;
    public GraphDetailEvent(MyPojo graphDetails)
    {

        this.graphDetails = graphDetails;
    }

    public MyPojo getGraphDetails() {
        return graphDetails;
    }
}
