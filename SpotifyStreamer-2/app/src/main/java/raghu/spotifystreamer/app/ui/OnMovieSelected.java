package raghu.spotifystreamer.app.ui;

import android.view.View;

import raghu.spotifystreamer.app.model.Movies;

/**
 * Created by Raghunandan on 24-11-2015.
 */
public interface OnMovieSelected {
     void movieselected(Movies movie, View view,int position);
}
