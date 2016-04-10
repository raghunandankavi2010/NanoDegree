package com.sam_chordas.android.stockhawk.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Raghunandan on 10-04-2016.
 */

public class Volume implements Parcelable
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

    protected Volume(Parcel in) {

        this.min = in.readString();
        this.max = in.readString();
    }

    public static final Parcelable.Creator<Volume> CREATOR = new Parcelable.Creator<Volume>() {
        public Volume createFromParcel(Parcel source) {
            return new Volume(source);
        }

        public Volume[] newArray(int size) {
            return new Volume[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}