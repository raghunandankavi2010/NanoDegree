package raghu.spotifystreamer.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Raghunandan on 20-11-2015.
 */
public class Videos implements Parcelable{

    private String id,iso_639_1,key,name,type,site;
    public static final String SITE_YOUTUBE = "YouTube";
    public static final String TYPE_TRAILER = "Trailer";

    public String getId() {
        return id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Videos()
    {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeString(this.key);
        dest.writeString(this.type);
        dest.writeString(this.iso_639_1);
    }

    protected Videos(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.site = in.readString();

        this.key = in.readString();
        this.type = in.readString();
        this.iso_639_1 = in.readString();
    }

    public static final Creator<Videos> CREATOR = new Creator<Videos>() {
        public Videos createFromParcel(Parcel source) {
            return new Videos(source);
        }

        public Videos[] newArray(int size) {
            return new Videos[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
