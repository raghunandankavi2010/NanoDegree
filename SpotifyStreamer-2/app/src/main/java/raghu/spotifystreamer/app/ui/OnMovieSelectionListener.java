package raghu.spotifystreamer.app.ui;

import raghu.spotifystreamer.app.model.Movies;

/**
 * Created by Raghunandan on 24-11-2015.
 */
public interface OnMovieSelectionListener {

    void onMovieSelected(Movies movie,String yes);
}