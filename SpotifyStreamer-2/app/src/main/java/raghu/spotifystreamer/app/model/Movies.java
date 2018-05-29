package raghu.spotifystreamer.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;


/**
 * Created by Raghunandan on 23-09-2015.
 */
public class Movies implements Parcelable {



    private int id,vote_count,favourite,reviewsaved,trailersaved;
    private float vote_average,popularity;

    private String original_language,original_title,overview,release_date,poster_path,title,generids,backdrop_path;
    private boolean video,favored;

    public Movies()
    {

    }

    public void setReviewsaved(int reviewsaved) {
        this.reviewsaved = reviewsaved;
    }

    public void setTrailersaved(int trailersaved) {
        this.trailersaved = trailersaved;
    }

    public int getTrailersaved() {
        return trailersaved;
    }

    public int getReviewsaved() {
        return reviewsaved;
    }

    public void setFavored(boolean favored) {
        this.favored = favored;
    }



    public void setId(int id) {
        this.id = id;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }


    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenerids(String generids) {
        this.generids = generids;
    }

    public void setbackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

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

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public int getFavourtite() {
        return favourite;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public float getPopularity() {
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

        dest.writeString(this.generids);
        dest.writeString(this.title);
        dest.writeString(this.release_date);

        dest.writeString(this.backdrop_path);

        dest.writeInt(this.favourite);
        dest.writeInt(this.id);
        dest.writeInt(this.vote_count);
        dest.writeFloat(this.vote_average);
        dest.writeFloat(this.popularity);


    }

    protected Movies(Parcel in) {
        this.original_language = in.readString();
        this.original_title = in.readString();
        this.overview = in.readString();
        this.poster_path = in.readString();
        this.generids = in.readString();
        this.title = in.readString();
        this.release_date = in.readString();
        this.backdrop_path = in.readString();

        this.favourite = in.readInt();
        this.id = in.readInt();
        this.vote_count = in.readInt();
        this.vote_average =in.readFloat();
        this.popularity = in.readFloat();

    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
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
