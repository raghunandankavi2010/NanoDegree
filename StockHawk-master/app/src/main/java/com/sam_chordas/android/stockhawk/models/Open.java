package com.sam_chordas.android.stockhawk.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Raghunandan on 10-04-2016.
 */
public class Open implements Parcelable
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

    protected Open(Parcel in) {

        this.min = in.readString();
        this.max = in.readString();
    }

    public static final Parcelable.Creator<Open> CREATOR = new Parcelable.Creator<Open>() {
        public Open createFromParcel(Parcel source) {
            return new Open(source);
        }

        public Open[] newArray(int size) {
            return new Open[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
