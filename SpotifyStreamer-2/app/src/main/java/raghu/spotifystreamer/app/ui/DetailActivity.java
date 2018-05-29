package raghu.spotifystreamer.app.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;

import raghu.spotifystreamer.app.R;
import raghu.spotifystreamer.app.model.Movies;

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
       int position = getIntent().getIntExtra("pos",0);

        if(savedInstanceState==null) {
            DetailFragment fragment = new DetailFragment();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.setSharedElementEnterTransition(new DetailsTransition());
                fragment.setEnterTransition(new Fade());
                fragment.setSharedElementReturnTransition(new DetailsTransition());

            }

            Bundle args = new Bundle();
            args.putInt("pos",position);
            args.putParcelable("key", movie);

            fragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true) ;

            transaction.replace(R.id.fragment_container, fragment);
            //transaction.addToBackStack(null);
            transaction.commit();

        }


    }

}
