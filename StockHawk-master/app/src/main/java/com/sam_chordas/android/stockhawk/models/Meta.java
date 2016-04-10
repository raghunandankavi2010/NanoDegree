package com.sam_chordas.android.stockhawk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghunandan on 10-04-2016.
 */
public class Meta implements Parcelable{
    @SerializedName("first-trade")
    private String first_trade;

    private String timestamp;

    private String unit;

    private String ticker;

    private String previous_close_price;

    @SerializedName("Exchange-Name")
    private String Exchange_Name;

    @SerializedName("Company-Name")
    private String Company_Name;

    @SerializedName("last-trade")
    private String last_trade;

    private String uri;

    private String currency;

    public String getFirst_trade() {
        return first_trade;
    }

    public void setFirst_trade(String first_trade) {
        this.first_trade = first_trade;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getPrevious_close_price() {
        return previous_close_price;
    }

    public void setPrevious_close_price(String previous_close_price) {
        this.previous_close_price = previous_close_price;
    }

    public String getExchange_Name() {
        return Exchange_Name;
    }

    public void setExchange_Name(String Exchange_Name) {
        this.Exchange_Name = Exchange_Name;
    }

    public String getCompany_Name() {
        return Company_Name;
    }

    public void setCompany_Name(String Company_Name) {
        this.Company_Name = Company_Name;
    }

    public String getLast_trade() {
        return last_trade;
    }

    public void setLast_trade(String last_trade) {
        this.last_trade = last_trade;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "ClassPojo [first-trade = " + first_trade + ", timestamp = " + timestamp
                + ", unit = " + unit + ", ticker = " + ticker + ", previous_close_price = " + previous_close_price
                + ", Exchange-Name = " + Exchange_Name + ", Company-Name = " + Company_Name
                + ", last-trade = " + last_trade + ", uri = " + uri + ", currency = " + currency + "]";
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.first_trade);
        dest.writeString(this.timestamp);
        dest.writeString(this.unit);
        dest.writeString(this.ticker);
        dest.writeString(this.previous_close_price);
        dest.writeString(this.Exchange_Name);
        dest.writeString(this.Company_Name);
        dest.writeString(this.last_trade);
        dest.writeString(this.uri);
        dest.writeString(this.currency);

    }

    protected Meta(Parcel in) {

        this.first_trade = in.readString();
        this.timestamp = in.readString();
        this.unit = in.readString();
        this.ticker = in.readString();
        this.previous_close_price = in.readString();
        this.Exchange_Name = in.readString();
        this.Company_Name = in.readString();
        this.last_trade = in.readString();
        this.uri = in.readString();
        this.currency = in.readString();


    }

    public static final Parcelable.Creator<Meta> CREATOR = new Parcelable.Creator<Meta>() {
        public Meta createFromParcel(Parcel source) {
            return new Meta(source);
        }

        public Meta[] newArray(int size) {
            return new Meta[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
