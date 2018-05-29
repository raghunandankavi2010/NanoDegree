package raghu.spotifystreamer.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Raghunandan on 23-09-2015.
 */
public class MoviesList {

    private int total_pages;

    @SerializedName("page")
    private int pageCount;

    public int getTotal_pages() {
        return total_pages;
    }

    private ArrayList<Movies> results;

    public ArrayList<Movies> getResults() {
        return results;
    }

    public int getPageCount() {
        return pageCount;
    }


}
