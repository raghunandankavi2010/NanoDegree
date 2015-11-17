package raghu.spotifystreamer.app.model;

import java.util.ArrayList;

/**
 * Created by Raghunandan on 23-09-2015.
 */
public class MoviesList {

    private int total_pages;

    public int getTotal_pages() {
        return total_pages;
    }

    private ArrayList<Movies> results;

    public ArrayList<Movies> getResults() {
        return results;
    }
}
