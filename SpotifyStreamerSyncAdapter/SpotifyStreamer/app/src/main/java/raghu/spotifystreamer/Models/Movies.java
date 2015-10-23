package raghu.spotifystreamer.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Raghunandan on 23-09-2015.
 */
public class Movies implements Parcelable {

    private int id,vote_count,favourite;
    private float vote_average;

    private String original_language,original_title,overview,release_date,poster_path,popularity,title,generids,backdrop;
    private boolean video;


    public String getGenerids() {
        return generids;
    }

    public int getId() {
        return id;
    }

    public int getVote_count() {
        return vote_count;
    }

    public float getVote_avarage() {
        return vote_average;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public int getFavourtite() {
        return favourite;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getTitle() {
        return title;
    }

    public boolean isVideo() {
        return video;
    }

    // Parcelling part

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.original_language);
        dest.writeString(this.original_title);
        dest.writeString(this.overview);
        dest.writeString(this.poster_path);
        dest.writeString(this.popularity);
        dest.writeString(this.generids);
        dest.writeString(this.title);
        dest.writeString(this.release_date);

        dest.writeString(this.backdrop);

        dest.writeInt(this.favourite);
        dest.writeInt(this.id);
        dest.writeInt(this.vote_count);
        dest.writeFloat(this.vote_average);

    }

    protected Movies(Parcel in) {
        this.original_language = in.readString();
        this.original_title = in.readString();
        this.overview = in.readString();
        this.poster_path = in.readString();
        this.popularity = in.readString();
        this.generids = in.readString();
        this.title = in.readString();
        this.release_date = in.readString();
        this.backdrop = in.readString();

        this.favourite = in.readInt();
        this.id = in.readInt();
        this.vote_count = in.readInt();
        this.vote_average =in.readFloat();

    }

    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {
        public Movies createFromParcel(Parcel source) {
            return new Movies(source);
        }

        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
