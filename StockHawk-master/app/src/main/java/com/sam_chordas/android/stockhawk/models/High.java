package com.sam_chordas.android.stockhawk.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Raghunandan on 10-04-2016.
 */
public class High implements Parcelable
{
    private String min;

    private String max;

    public String getMin ()
    {
        return min;
    }

    public void setMin (String min)
    {
        this.min = min;
    }

    public String getMax ()
    {
        return max;
    }

    public void setMax (String max)
    {
        this.max = max;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [min = "+min+", max = "+max+"]";
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeString(this.min);

        dest.writeString(this.max);



    }

    protected High(Parcel in) {

        this.min = in.readString();
        this.max = in.readString();
    }

    public static final Parcelable.Creator<High> CREATOR = new Parcelable.Creator<High>() {
        public High createFromParcel(Parcel source) {
            return new High(source);
        }

        public High[] newArray(int size) {
            return new High[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
