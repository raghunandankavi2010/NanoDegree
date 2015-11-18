package raghu.spotifystreamer.app.model;

import java.util.ArrayList;

/**
 * Created by Raghunandan on 18-11-2015.
 */
public class ReviewsList {

    private int total_pages;

    public int getTotal_pages() {
        return total_pages;
    }

    private ArrayList<Reviews> results;

    public ArrayList<Reviews> getReviewsResults() {
        return results;
    }
}
