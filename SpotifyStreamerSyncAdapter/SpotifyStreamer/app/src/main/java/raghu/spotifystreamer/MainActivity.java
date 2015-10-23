package raghu.spotifystreamer;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.SortedList;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import raghu.spotifystreamer.Models.Movies;
import raghu.spotifystreamer.Utilities.CheckNetwork;
import raghu.spotifystreamer.Utilities.Constants;
import raghu.spotifystreamer.fragments.MovieListFragment;
import raghu.spotifystreamer.fragments.MoviesListFragmentSyncTest;
import raghu.spotifystreamer.fragments.SortedMoviesFragment;

public class MainActivity extends AppCompatActivity {


    private MovieListFragment movieFrag;
    private MoviesListFragmentSyncTest movieFrag1;
    private SortedMoviesFragment movieSort;
    private static final String TAG_TASK_FRAGMENT = "task_fragment";
    private static final String TAG_MOVIES_FRAGMENT = "movies_fragment";
    private static final String TAG_AVERAGE_FRAGMENT = "average_rating_fragment";

    private String mWhichFragment;
    private FragmentManager fm ;
    private static boolean check;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)this.findViewById(R.id.toolbar);
        toolbar.setTitle("Spotify Movies List");
        setSupportActionBar(toolbar);


        fm = getSupportFragmentManager();

        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        if(savedInstanceState == null && check==false)
        {
           // mWhichFragment = "movieFrag";
            if(movieFrag == null) {
                movieFrag1 = new MoviesListFragmentSyncTest();
                FragmentManager fm = getSupportFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container, movieFrag1, TAG_MOVIES_FRAGMENT);
                fragmentTransaction.commit();
                //Toast.makeText(this, " attached", Toast.LENGTH_SHORT).show();
            }


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortP:

               /* mWhichFragment = "movieFrag";
                movieFrag = new MovieListFragment();
                FragmentManager fm = getSupportFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container, movieFrag, TAG_MOVIES_FRAGMENT);
                fragmentTransaction.commit();*/

                return true;
            case R.id.sortR:
             /*   check = true;
                mWhichFragment = "averageFrag";
                movieSort = new SortedMoviesFragment();
                FragmentManager fm1 = getSupportFragmentManager();
                fragmentTransaction = fm1.beginTransaction();
                fragmentTransaction.replace(R.id.container, movieSort, TAG_AVERAGE_FRAGMENT);
                fragmentTransaction.commit();*/
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
