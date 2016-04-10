package com.sam_chordas.android.stockhawk.models;

/**
 * Created by Raghunandan on 10-04-2016.
 */

import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Series implements Parcelable{

    @SerializedName("Date")
    @Expose
    private long Date;
    @SerializedName("close")
    @Expose
    private float close;
    @SerializedName("high")
    @Expose
    private double high;
    @SerializedName("low")
    @Expose
    private double low;
    @SerializedName("open")
    @Expose
    private double open;
    @SerializedName("volume")
    @Expose
    private long volume;

    /**
     *
     * @return
     * The Date
     */
    public long getDate() {
        return Date;
    }

    /**
     *
     * @param Date
     * The Date
     */
    public void setDate(long Date) {
        this.Date = Date;
    }

    /**
     *
     * @return
     * The close
     */
    public float getClose() {
        return close;
    }

    /**
     *
     * @param close
     * The close
     */
    public void setClose(float close) {
        this.close = close;
    }

    /**
     *
     * @return
     * The high
     */
    public double getHigh() {
        return high;
    }

    /**
     *
     * @param high
     * The high
     */
    public void setHigh(double high) {
        this.high = high;
    }

    /**
     *
     * @return
     * The low
     */
    public double getLow() {
        return low;
    }

    /**
     *
     * @param low
     * The low
     */
    public void setLow(double low) {
        this.low = low;
    }

    /**
     *
     * @return
     * The open
     */
    public double getOpen() {
        return open;
    }

    /**
     *
     * @param open
     * The open
     */
    public void setOpen(double open) {
        this.open = open;
    }

    /**
     *
     * @return
     * The volume
     */
    public long getVolume() {
        return volume;
    }

    /**
     *
     * @param volume
     * The volume
     */
    public void setVolume(long volume) {
        this.volume = volume;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(this.Date);
        dest.writeFloat(this.close);
        dest.writeDouble(this.high);
        dest.writeDouble(this.low);
        dest.writeDouble(this.open);
        dest.writeLong(this.volume);

    }



    protected Series(Parcel in) {

        this.Date = in.readLong();
        this.close = in.readFloat();
        this.high = in.readDouble();
        this.low = in.readDouble();
        this.open = in.readDouble();
        this.volume = in.readLong();;

    }

    public static final Parcelable.Creator<Series> CREATOR = new Parcelable.Creator<Series>() {
        public Series createFromParcel(Parcel source) {
            return new Series(source);
        }

        public Series[] newArray(int size) {
            return new Series[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}