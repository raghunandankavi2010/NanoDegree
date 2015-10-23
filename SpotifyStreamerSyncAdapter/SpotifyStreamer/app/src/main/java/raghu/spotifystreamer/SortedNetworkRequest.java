package raghu.spotifystreamer;

/**
 * Created by Raghunandan on 30-09-2015.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import raghu.spotifystreamer.Models.Movies;
import raghu.spotifystreamer.Models.MoviesList;
import raghu.spotifystreamer.Utilities.Utils;

/**
 * Created by Raghunandan on 23-09-2015.
 */

// Source picked from Alex LockWood blog post
// http://www.androiddesignpatterns.com/2013/04/retaining-objects-across-config-changes.html
// Required to handle orientation change. Fragment retain state and callback required to update ui
public class SortedNetworkRequest  extends Fragment {


    public static FragmentNetWorkRequest  newInstance(String url)
    {
        FragmentNetWorkRequest f = new FragmentNetWorkRequest();
        Bundle b = new Bundle();
        b.putString("url", url);
        f.setArguments(b);
        return f;
    }
    private static final String TAG = FragmentNetWorkRequest.class.getSimpleName();
    private ArrayList<Movies> moviesList;
    private String sortOrder;

    private static final boolean DEBUG = true;
    public void setMoviesList(ArrayList<Movies> moviesList) {
        this.moviesList = moviesList;
    }

    public ArrayList<Movies> getMoviesList() {
        return moviesList;
    }

    public void setSortOrder(String url) {
        this.sortOrder = sortOrder;
        mTask = new DummyTask();
        mTask.execute(url);
    }

    public String getSortOrder() {

        return sortOrder;
    }

    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    public interface TaskCallbacks2 {
        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute();
    }

    private TaskCallbacks2 mCallbacks;
    private DummyTask mTask;
    private boolean mRunning;

    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Activity activity = (Activity) context;
        super.onAttach(context);



    }*/

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
        if (!(getTargetFragment() instanceof TaskCallbacks2)) {
            throw new IllegalStateException("Target fragment must implement the TaskCallbacks interface.");
        }

        // Hold a reference to the target fragment so we can report back the task's
        // current progress and results.
        mCallbacks = (TaskCallbacks2) getTargetFragment();

        // Create and execute the background task.
        mTask = new DummyTask();
        mTask.execute();
        mRunning = true;

    }

    /**
     * Note that this method is <em>not</em> called when the Fragment is being
     * retained across Activity instances. It will, however, be called when its
     * parent Activity is being destroyed for good (such as when the user clicks
     * the back button, etc.).
     */
    @Override
    public void onDestroy() {
        if (DEBUG) Log.i(TAG, "onDestroy()");
        super.onDestroy();
        cancel();
    }

    /*****************************/
    /***** TASK FRAGMENT API *****/
    /*****************************/

    /**
     * Start the background task.
     */
    public void start(String url) {
        if (!mRunning) {

        }
    }

    /**
     * Cancel the background task.
     */
    public void cancel() {
        if (mRunning) {
            mTask.cancel(false);
            mTask = null;
            mRunning = false;
        }
    }
    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * A dummy task that performs some (dumb) background work and
     * proxies progress updates and results back to the Activity.
     *
     * Note that we need to check if the callbacks are null in each
     * method in case they are invoked after the Activity's and
     * Fragment's onDestroy() method have been called.
     */
    private class DummyTask extends AsyncTask<String, Integer, ArrayList<Movies>> {

        @Override
        protected void onPreExecute() {
            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
            }
        }

        /**
         * Note that we do NOT call the callback object's methods
         * directly from the background thread, as this could result
         * in a race condition.
         */
        @Override
        protected ArrayList<Movies> doInBackground(String... ignore) {

            HttpURLConnection urlConn=null;
            String response;
            URL url;
            try {
                url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=6d32f2a6596004bb66069187b4c9b933");
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setReadTimeout(5000) ;
                urlConn.setConnectTimeout(5000);

                InputStream in = new BufferedInputStream(urlConn.getInputStream());
                response = Utils.getStringFromInputStream(in);

                Gson gson = new Gson();
                MoviesList responseModel = gson.fromJson(response, MoviesList.class);

                moviesList = responseModel.getResults();

                Log.i(TAG, "Response is " + response);

            } catch (Exception e) {
                e.printStackTrace();
            } finally
            {
                if(urlConn!=null)
                    urlConn.disconnect();
            }

            return moviesList;


        }

        @Override
        protected void onProgressUpdate(Integer... percent) {
            if (mCallbacks != null) {

                mCallbacks.onProgressUpdate(percent[0]);
            }
        }

        @Override
        protected void onCancelled() {
            if (mCallbacks != null) {
                mCallbacks.onCancelled();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> ignore) {
            if (mCallbacks != null) {
                mCallbacks.onPostExecute();
                setMoviesList(ignore);

            }
        }
    }
}