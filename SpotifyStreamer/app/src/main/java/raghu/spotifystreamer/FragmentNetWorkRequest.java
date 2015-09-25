package raghu.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import raghu.spotifystreamer.Models.Movies;
import raghu.spotifystreamer.Models.MoviesList;
import raghu.spotifystreamer.Utilities.CheckNetwork;
import raghu.spotifystreamer.Utilities.Constants;
import raghu.spotifystreamer.Utilities.Utils;

/**
 * Created by Raghunandan on 23-09-2015.
 */

// Source picked from Alex LockWood blog post
// http://www.androiddesignpatterns.com/2013/04/retaining-objects-across-config-changes.html
// Required to handle orientation change. Fragment retain state and callback required to update ui
public class FragmentNetWorkRequest  extends Fragment {

    private static final String TAG = FragmentNetWorkRequest.class.getSimpleName();
    private ArrayList<Movies> moviesList;
    private String sortOrder;

    public void setMoviesList(ArrayList<Movies> moviesList) {
        this.moviesList = moviesList;
    }

    public ArrayList<Movies> getMoviesList() {
        return moviesList;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
            mTask = new DummyTask();
            mTask.execute();
    }

    public String getSortOrder() {

        return sortOrder;
    }

    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    interface TaskCallbacks {
        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute();
    }

    private TaskCallbacks mCallbacks;
    private DummyTask mTask;

    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Activity activity = (Activity) context;
        mCallbacks = (TaskCallbacks) context;

    }

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        // Create and execute the background task.
        if(CheckNetwork.isNetworkAvailable(getActivity())) {
            mTask = new DummyTask();
            mTask.execute();
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
    private class DummyTask extends AsyncTask<Void, Integer, ArrayList<Movies>> {

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
        protected ArrayList<Movies> doInBackground(Void... ignore) {

            HttpURLConnection urlConn=null;
            String response;
            URL url;
            try {
                if(!TextUtils.isEmpty(getSortOrder()) && sortOrder.equals("popularity"))
                {
                    url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=yourspotifykey");
                }else if (!TextUtils.isEmpty(getSortOrder()) && sortOrder.equals("Average"))
                {
                    url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=yourspotifykey");
                }else {
                    url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=yourspotifykey");
                }
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setReadTimeout(5000) ;
                urlConn.setConnectTimeout(5000);

                InputStream in = new BufferedInputStream(urlConn.getInputStream());
                response = Utils.getStringFromInputStream(in);

                Gson  gson = new Gson();
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

          /*  SpotifyApi api = new SpotifyApi();

            // Most (but not all) of the Spotify Web API endpoints require authorisation.
            // If you know you'll only use the ones that don't require authorisation you can skip this step
            api.setAccessToken("6d32f2a6596004bb66069187b4c9b933");

            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists("Beyonce");*/

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