package raghu.spotifystreamer.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Raghunandan on 18-11-2015.
 */
public class Reviews implements Parcelable {

    private String id,author, content, url;

    public Reviews()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);
        dest.writeString(this.id);
    }

    protected Reviews(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();

        this.id = in.readString();
    }

    public static final Creator<Reviews> CREATOR = new Creator<Reviews>() {
        public Reviews createFromParcel(Parcel source) {
            return new Reviews(source);
        }

        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
