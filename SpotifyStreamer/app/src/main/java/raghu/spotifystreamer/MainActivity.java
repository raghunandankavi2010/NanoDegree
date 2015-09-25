package raghu.spotifystreamer;

import android.support.v4.app.FragmentManager;
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
import raghu.spotifystreamer.Utilities.Constants;
import raghu.spotifystreamer.fragments.MovieListFragment;

public class MainActivity extends AppCompatActivity implements FragmentNetWorkRequest.TaskCallbacks {


    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    private FragmentNetWorkRequest mTaskFragment;
    private MovieListFragment movieFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)this.findViewById(R.id.toolbar);
        toolbar.setTitle("Spotify Movies List");
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_drawer);

        FragmentManager fm = getSupportFragmentManager();
        mTaskFragment = (FragmentNetWorkRequest) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null) {
            mTaskFragment = new FragmentNetWorkRequest();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

        if (savedInstanceState != null && savedInstanceState.containsKey("bool") && savedInstanceState.getBoolean("bool")) {
           /*mList = savedInstanceState.getParcelableArrayList("list");*/
            movieFrag = (MovieListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.list);
            movieFrag.displayGridImages(mTaskFragment.getMoviesList());
        }

    }

    @Override
    public void onPreExecute() {

         movieFrag = (MovieListFragment)
                getSupportFragmentManager().findFragmentById(R.id.list);

        if (movieFrag != null) {
            movieFrag.displayProgressBar();
        }
    }

    @Override
    public void onProgressUpdate(int percent) {

    }

    @Override
    public void onCancelled() {


    }



    @Override
    public void onPostExecute() {


        if (movieFrag != null) {
            movieFrag.hideProgressBar();
            movieFrag.displayGridImages(mTaskFragment.getMoviesList());
        }else
        {
            Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mTaskFragment.getMoviesList()!=null && mTaskFragment.getMoviesList().size()>0)
            outState.putBoolean("bool",true);

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
                mTaskFragment.setSortOrder("Popularity");
                return true;
            case R.id.sortR:
                mTaskFragment.setSortOrder("Average");
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
