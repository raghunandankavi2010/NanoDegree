package com.sam_chordas.android.stockhawk.events;

/**
 * Created by Raghunandan on 08-04-2016.
 */
public class ErrorEvent {

    private Throwable t;
    public ErrorEvent(Throwable t)
    {
        this.t = t;
    }

    public Throwable getT() {
        return t;
    }
}
