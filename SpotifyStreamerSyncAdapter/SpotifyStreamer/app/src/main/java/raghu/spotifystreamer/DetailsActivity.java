package raghu.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import raghu.spotifystreamer.Models.Movies;
import raghu.spotifystreamer.Utilities.Constants;


/**
 * Created by Raghunandan on 25-09-2015.
 */
public class DetailsActivity extends AppCompatActivity {

    private ImageView thumb;
    private TextView rating,release_date,overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /* Need to use fragment for tablet ui for now its Activity */

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        Movies movie = getIntent().getParcelableExtra("movie");
        toolbar.setTitle(movie.getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_up);
        setSupportActionBar(toolbar);

        thumb = (ImageView) findViewById(R.id.imageView2);
        rating = (TextView) findViewById(R.id.textView);
        release_date = (TextView) findViewById(R.id.textView2);
        overview = (TextView) findViewById(R.id.textView3);
        if(!TextUtils.isEmpty(movie.getPoster_path()))
        Picasso.with(this).
                load(Constants.BASE_IMAGE_URL+
                        getResources().getString(R.string.phone_size)+
                        movie.getPoster_path())
                .into(thumb);

        rating.setText("Rating :"+String.valueOf(movie.getVote_count()));
        release_date.setText("Release Date :"+movie.getRelease_date());
        overview.setText(movie.getOverview());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
