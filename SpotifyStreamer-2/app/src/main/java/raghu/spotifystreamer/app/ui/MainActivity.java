package raghu.spotifystreamer.app.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import raghu.spotifystreamer.app.R;
import raghu.spotifystreamer.app.model.Movies;

public class MainActivity extends AppCompatActivity implements OnMovieSelectionListener{

    private Toolbar toolbar;
    private SharedPreferences sp;
    private boolean mTwoPane;

    private int identifier;
    private static final String MOVIE_DETAILS_FRAGMENT_TAG = "fragment_movie_details";


    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTwoPane = findViewById(R.id.fragment_container2) != null;

        if(mTwoPane)
        {
          Toast.makeText(this,"Two Pane",Toast.LENGTH_SHORT).show();
        }

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        identifier = sp.getInt("id", -1);
        Toast.makeText(this,""+identifier,Toast.LENGTH_SHORT).show();

        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("");

        mSpinner = (Spinner) toolbar.findViewById(R.id.toolbar_spinner);
        if (savedInstanceState != null) {
            identifier = savedInstanceState.getInt("key");
        }

        View spinnerContainer = LayoutInflater.from(this).inflate(R.layout.widget_toolbar_spinner,
                toolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toolbar.addView(spinnerContainer, lp);

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter();
        spinnerAdapter.addItem("Movies List");
        spinnerAdapter.addItem("Popular Movies");
        spinnerAdapter.addItem("Favorite Movies");


        mSpinner = (Spinner) spinnerContainer.findViewById(R.id.toolbar_spinner);
        mSpinner.setAdapter(spinnerAdapter);
        if(savedInstanceState==null)
        {
        if(identifier==1)
        {
            identifier=-1;
          mSpinner.setSelection(0);


        }
        else if(identifier==2)
        {
            identifier=-1;
            mSpinner.setSelection(1);

        }else if(identifier==3){
            identifier=-1;
            mSpinner.setSelection(2);

        }

        }
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int position, long itemId) {
                switch (position) {
                    case 0:
                        if (identifier != 1) {
                            replaceMoviesFragment(new MoviesListFragment());
                            identifier = 1;
                        }

                        break;
                    case 1:
                        if (identifier != 2) {
                            replaceMoviesFragment(new PopulatMoviesFrament());
                            identifier = 2;
                        }
                        break;
                    case 2:
                        if (identifier != 3) {
                            replaceMoviesFragment(new FavouriteFragment());
                            identifier = 3;
                        }

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void replaceMoviesFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment,MOVIE_DETAILS_FRAGMENT_TAG)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("key", identifier);

    }

    @Override
    public void onMovieSelected(Movies movie,String check) {
        if(mTwoPane)
        {

            if(check.equals("Yes")) {
                DetailFragment detailFragment = new DetailFragment();
                Bundle args = new Bundle();
                args.putParcelable("key", movie);
                detailFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container2, detailFragment,MOVIE_DETAILS_FRAGMENT_TAG)
                        .commit();
            }else if(check.equals("No"))
            {
                Fragment detailFragment =  getSupportFragmentManager().findFragmentByTag(MOVIE_DETAILS_FRAGMENT_TAG);
                if(detailFragment!=null && detailFragment.isVisible())
                getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();

            }
        }
        else
        {
            Intent intent =  new Intent(this,DetailActivity.class);
            intent.putExtra("movie",movie);
            startActivity(intent);
        }
    }

    private class SpinnerAdapter extends BaseAdapter {
        private List<String> mItems = new ArrayList<>();

        public void clear() {
            mItems.clear();
        }

        public void addItem(String yourObject) {
            mItems.add(yourObject);
        }

        public void addItems(List<String> yourObjectList) {
            mItems.addAll(yourObjectList);
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getDropDownView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
                view = getLayoutInflater().inflate(R.layout.toolbar_item_dropdown, parent, false);
                view.setTag("DROPDOWN");
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getTitle(position));

            return view;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
                view = getLayoutInflater().inflate(R.layout.
                        toolbar_spinner_item_actionbar, parent, false);
                view.setTag("NON_DROPDOWN");
            }
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getTitle(position));
            return view;
        }

        private String getTitle(int position) {
            return position >= 0 && position < mItems.size() ? mItems.get(position) : "";
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sp.edit().putInt("id",identifier).commit();
    }
}

