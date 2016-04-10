package com.sam_chordas.android.stockhawk.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Raghunandan on 10-04-2016.
 */
public class Ranges implements Parcelable
{
    private Open open;

    private Volume volume;

    private High high;

    private Low low;


    public Open getOpen ()
    {
        return open;
    }

    public void setOpen (Open open)
    {
        this.open = open;
    }

    public Volume getVolume ()
    {
        return volume;
    }

    public void setVolume (Volume volume)
    {
        this.volume = volume;
    }

    public High getHigh ()
    {
        return high;
    }

    public void setHigh (High high)
    {
        this.high = high;
    }

    public Low getLow ()
    {
        return low;
    }

    public void setLow (Low low)
    {
        this.low = low;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [open = "+open+", volume = "+volume+", high = "+high+", low = "+low+"]";
    }
    //dest.writeParcelable(this.user, flags);
    //this.user = in.readParcelable(MyQuestionsFetchedUser.class.getClassLoader());

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(this.open, flags);
        dest.writeParcelable(this.volume, flags);
        dest.writeParcelable(this.high, flags);
        dest.writeParcelable(this.low, flags);



    }

    protected Ranges(Parcel in) {

        this.open = in.readParcelable(Open.class.getClassLoader());
        this.volume = in.readParcelable(Volume.class.getClassLoader());
        this.high = in.readParcelable(High.class.getClassLoader());
        this.low = in.readParcelable(Low.class.getClassLoader());

    }

    public static final Parcelable.Creator<Ranges> CREATOR = new Parcelable.Creator<Ranges>() {
        public Ranges createFromParcel(Parcel source) {
            return new Ranges(source);
        }

        public Ranges[] newArray(int size) {
            return new Ranges[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}

