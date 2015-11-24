package raghu.spotifystreamer.app.ui;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import raghu.spotifystreamer.app.R;
import raghu.spotifystreamer.app.RxApp;
import raghu.spotifystreamer.app.Utils;
import raghu.spotifystreamer.app.model.Movies;
import raghu.spotifystreamer.app.model.SpotifyMoviesModel;
import raghu.spotifystreamer.app.provider.MoviesContract;

/**
 * Created by Raghunandan on 18-11-2015.
 */
public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       Movies movie = getIntent().getParcelableExtra("movie");

        if(savedInstanceState==null) {
            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putParcelable("key", movie);
            fragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, fragment);
            //transaction.addToBackStack(null);
            transaction.commit();
        }


    }

}
