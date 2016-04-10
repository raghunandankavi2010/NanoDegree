package com.sam_chordas.android.stockhawk.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raghunandan on 10-04-2016.
 */
public class MyPojo implements Parcelable
{
    private List<Series> series;

    private Date Date;

    private List<String> labels;

    private Ranges ranges;

    private Meta meta;

    public List getSeries ()
    {
        return series;
    }

    public void setSeries (ArrayList<Series> series)
    {
        this.series = series;
    }

    public Date getDate ()
    {
        return Date;
    }

    public void setDate (Date Date)
    {
        this.Date = Date;
    }

    public List<String> getLabels ()
    {
        return labels;
    }

    public void setLabels (List<String> labels)
    {
        this.labels = labels;
    }

    public Ranges getRanges ()
    {
        return ranges;
    }

    public void setRanges (Ranges ranges)
    {
        this.ranges = ranges;
    }

    public Meta getMeta ()
    {
        return meta;
    }

    public void setMeta (Meta meta)
    {
        this.meta = meta;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [series = "+series+", Date = "+Date+", labels = "+labels+", ranges = "+ranges+", meta = "+meta+"]";
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeTypedList(this.series);
        dest.writeParcelable(this.Date, flags);
        dest.writeStringList(this.labels);
        dest.writeParcelable(this.ranges, flags);
        dest.writeParcelable(this.meta, flags);
    }

    protected MyPojo(Parcel in) {

        in.readTypedList(series, Series.CREATOR);
        this.Date = in.readParcelable(Date.class.getClassLoader());
        in.readStringList(labels);
        this.ranges = in.readParcelable(Ranges.class.getClassLoader());
        this.meta = in.readParcelable(Meta.class.getClassLoader());
    }

    public static final Parcelable.Creator<MyPojo> CREATOR = new Parcelable.Creator<MyPojo>() {
        public MyPojo createFromParcel(Parcel source) {
            return new MyPojo(source);
        }

        public MyPojo[] newArray(int size) {
            return new MyPojo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}

