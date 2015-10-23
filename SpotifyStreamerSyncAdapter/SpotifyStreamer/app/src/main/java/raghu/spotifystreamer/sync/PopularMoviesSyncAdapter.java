package raghu.spotifystreamer.sync;

/**
 * Created by Raghunandan on 20-10-2015.
 */
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.util.Log;

import com.google.gson.Gson;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import raghu.spotifystreamer.Models.Movies;
import raghu.spotifystreamer.Models.MoviesList;
import raghu.spotifystreamer.R;
import raghu.spotifystreamer.Utilities.Utils;
import raghu.spotifystreamer.provider.MoviesContract;
import timber.log.Timber;


public class PopularMoviesSyncAdapter extends AbstractThreadedSyncAdapter {



    private static final String[] PROJECTION = new String[] {
            BaseColumns._ID,
            MoviesContract.Movies.MOVIE_ID,
            MoviesContract.Movies.MOVIE_TITLE,
            MoviesContract.Movies.MOVIE_OVERVIEW,
            MoviesContract.Movies.MOVIE_GENRE_IDS,
            MoviesContract.Movies.MOVIE_POPULARITY,
            MoviesContract.Movies.MOVIE_VOTE_AVERAGE,
            MoviesContract.Movies.MOVIE_VOTE_COUNT,
            MoviesContract.Movies.MOVIE_BACKDROP_PATH,
            MoviesContract.Movies.MOVIE_POSTER_PATH,
            MoviesContract.Movies.MOVIE_RELEASE_DATE,
            MoviesContract.Movies.MOVIE_FAVORED,


    };


    // Constants representing column positions from PROJECTION.
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_MOVIE_ID = 1;
    public static final int COLUMN_TITLE = 2;
    public static final int COLUMN_OVERVIEW = 3;
    public static final int COLUMN_GENERE_IDS = 4;
    public static final int COLUMN_POPULARITY = 5;
    public static final int COLUMN_VOTE_AVERAGE = 6;
    public static final int COLUMN_VOTE_COUNT = 7;
    public static final int COLUMN_BACKDROP_PATH = 8;
    public static final int COLUMN_POSTER_PATH = 9;
    public static final int COLUMN_RELEASE_DATE = 10;
    public static final int COLUMN_FAVOURED = 11;




    public static final String TAG = PopularMoviesSyncAdapter.class.getSimpleName();

    /**
     * URL to fetch content from during a sync.
     *
     * <p>This points to the Android Developers Blog. (Side note: We highly recommend reading the
     * Android Developer Blog to stay up to date on the latest Android platform developments!)
     */
    private static final String FEED_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=6d32f2a6596004bb66069187b4c9b933";

    /**
     * Network connection timeout, in milliseconds.
     */
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    private ContentResolver mContentResolver;

    public PopularMoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.i(TAG, "Beginning network synchronization");
        try {
            final URL location = new URL(FEED_URL);
            InputStream stream = null;

            try {
                Log.i(TAG, "Streaming data from network: " + location);
                stream = downloadUrl(location);
                updateLocalFeedData(stream, syncResult);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Feed URL is malformed", e);
            syncResult.stats.numParseExceptions++;
            return;
        } catch (IOException e) {
            Log.e(TAG, "Error reading from network: " + e.toString());
            syncResult.stats.numIoExceptions++;
            return;
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (RemoteException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        } catch (OperationApplicationException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        }
        Log.i(TAG, "Network synchronization complete");
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        //ContentResolver.requestSync(getSyncAccount(context), MoviesContract.CONTENT_AUTHORITY, bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

        }
        return newAccount;
    }


    /**
     * Read XML from an input stream, storing it into the content provider.
     *
     * <p>This is where incoming data is persisted, committing the results of a sync. In order to
     * minimize (expensive) disk operations, we compare incoming data with what's already in our
     * database, and compute a merge. Only changes (insert/update/delete) will result in a database
     * write.
     *
     * <p>As an additional optimization, we use a batch operation to perform all database writes at
     * once.
     *
     * <p>Merge strategy:
     * 1. Get cursor to all items in feed<br/>
     * 2. For each item, check if it's in the incoming data.<br/>
     *    a. YES: Remove from "incoming" list. Check if data has mutated, if so, perform
     *            database UPDATE.<br/>
     *    b. NO: Schedule DELETE from database.<br/>
     * (At this point, incoming database only contains missing items.)<br/>
     * 3. For any items remaining in incoming list, ADD to database.
     */
    public void updateLocalFeedData(final InputStream stream, final SyncResult syncResult)
            throws IOException, XmlPullParserException, RemoteException,
            OperationApplicationException, ParseException {

        Log.i(TAG, "Parsing Json");
        String response = Utils.getStringFromInputStream(stream);

        Gson gson = new Gson();
        MoviesList responseModel = gson.fromJson(response, MoviesList.class);

         ArrayList<Movies> moviesList = responseModel.getResults();
        final ContentResolver contentResolver = getContext().getContentResolver();



        Log.i(TAG, "Parsing complete. Found " + moviesList.size() + " entries");


        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<Integer, Movies> moviesMap = new HashMap<Integer, Movies>();
        for (Movies e : moviesList) {
            moviesMap.put(e.getId(), e);
        }

        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = MoviesContract.Movies.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, PROJECTION, null, null, null);
        assert c != null;
        if(c!=null) {
            Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

            // Find stale data
            int id, favoured, votecount;
            float average;
            String movieId, title, overview, popularity, genereIds,
                    releasedate, favored, path, backdropath;

            while (c.moveToNext()) {
                syncResult.stats.numEntries++;
                id = c.getInt(COLUMN_ID);
                movieId = c.getString(COLUMN_MOVIE_ID);
                title = c.getString(COLUMN_TITLE);
                overview = c.getString(COLUMN_OVERVIEW);
                popularity = c.getString(COLUMN_POPULARITY);
                genereIds = c.getString(COLUMN_GENERE_IDS);
                votecount = c.getInt(COLUMN_VOTE_COUNT);
                average = c.getFloat(COLUMN_VOTE_AVERAGE);
                releasedate = c.getString(COLUMN_RELEASE_DATE);
                favoured = c.getInt(COLUMN_FAVOURED);
                path = c.getString(COLUMN_POSTER_PATH);
                backdropath = c.getString(COLUMN_BACKDROP_PATH);
                Movies match = moviesMap.get(movieId);
                if (match != null) {
                    // Entry exists. Remove from entry map to prevent insert later.
                    moviesMap.remove(movieId);
                /* Check to see if the entry needs to be updated */
                    Uri existingUri = MoviesContract.Movies.CONTENT_URI.buildUpon()
                            .appendPath(Integer.toString(id)).build();
                    if (String.valueOf(match.getId()) != null && !String.valueOf(match.getId()).equals(String.valueOf(id)) ||
                            (match.getTitle() != null && !match.getTitle().equals(title)) ||
                            (match.getOverview() != null && !match.getOverview().equals(overview)) ||
                            (match.getPopularity() != null && !match.getPopularity().equals(popularity)) ||
                            (match.getGenerids()) != null && !match.getGenerids().equals(genereIds) ||
                            (String.valueOf(match.getVote_count())) != null ||
                            (String.valueOf(match.getVote_avarage())) != null ||
                            (match.getRelease_date() != null && !match.getRelease_date().equals(releasedate)) ||
                            (String.valueOf(match.getFavourtite()) != null) ||
                            (match.getPoster_path() != null && !match.getPoster_path().equals(path)) ||
                            (match.getBackdrop() != null && !match.getBackdrop().equals(popularity))) {
                        // Update existing record

                       /* public static final int COLUMN_ID = 0;
                        public static final int COLUMN_MOVIE_ID = 1;
                        public static final int COLUMN_TITLE = 2;
                        public static final int COLUMN_OVERVIEW = 3;
                        public static final int COLUMN_GENERE_IDS = 4;
                        public static final int COLUMN_POPULARITY = 5;
                        public static final int COLUMN_VOTE_AVERAGE = 6;
                        public static final int COLUMN_VOTE_COUNT = 7;
                        public static final int COLUMN_BACKDROP_PATH = 8;
                        public static final int COLUMN_POSTER_PATH = 9;
                        public static final int COLUMN_RELEASE_DATE = 10;
                        public static final int COLUMN_FAVOURED = 11;*/
                        Log.i(TAG, "Scheduling update: " + existingUri);
                        batch.add(ContentProviderOperation.newUpdate(existingUri)
                                .withValue(MoviesContract.Movies.MOVIE_ID, match.getId())
                                .withValue(MoviesContract.Movies.MOVIE_TITLE, match.getTitle())
                                .withValue(MoviesContract.Movies.MOVIE_OVERVIEW, match.getOverview())
                                .withValue(MoviesContract.Movies.MOVIE_GENRE_IDS, match.getGenerids())
                                .withValue(MoviesContract.Movies.MOVIE_POPULARITY, match.getPopularity())
                                .withValue(MoviesContract.Movies.MOVIE_VOTE_AVERAGE, match.getVote_avarage())
                                .withValue(MoviesContract.Movies.MOVIE_VOTE_COUNT, match.getVote_count())
                                .withValue(MoviesContract.Movies.MOVIE_BACKDROP_PATH, match.getBackdrop())
                                .withValue(MoviesContract.Movies.MOVIE_POSTER_PATH, match.getPoster_path())
                                .withValue(MoviesContract.Movies.MOVIE_RELEASE_DATE, match.getRelease_date())
                                .withValue(MoviesContract.Movies.MOVIE_FAVORED, match.getFavourtite())
                                .build());
                        syncResult.stats.numUpdates++;
                    } else {
                        Log.i(TAG, "No action: " + existingUri);
                    }
                } else {
                    // Entry doesn't exist. Remove it from the database.
                    Uri deleteUri = MoviesContract.Movies.CONTENT_URI.buildUpon()
                            .appendPath(Integer.toString(id)).build();
                    Log.i(TAG, "Scheduling delete: " + deleteUri);
                    batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                    syncResult.stats.numDeletes++;
                }
            }
            c.close();
        }


        // Add new items
        for (Movies e : moviesMap.values()) {
            Log.i(TAG, "Scheduling insert: moview_id=" + e.getId());
            batch.add(ContentProviderOperation.newInsert(MoviesContract.Movies.CONTENT_URI)
                    .withValue(MoviesContract.Movies.MOVIE_ID, e.getId())
                    .withValue(MoviesContract.Movies.MOVIE_TITLE, e.getTitle())
                    .withValue(MoviesContract.Movies.MOVIE_OVERVIEW, e.getOverview())
                    .withValue(MoviesContract.Movies.MOVIE_GENRE_IDS, e.getGenerids())
                    .withValue(MoviesContract.Movies.MOVIE_POPULARITY, e.getPopularity())
                    .withValue(MoviesContract.Movies.MOVIE_VOTE_AVERAGE, e.getVote_avarage())
                    .withValue(MoviesContract.Movies.MOVIE_VOTE_COUNT, e.getVote_count())
                    .withValue(MoviesContract.Movies.MOVIE_BACKDROP_PATH, e.getBackdrop())
                    .withValue(MoviesContract.Movies.MOVIE_POSTER_PATH, e.getPoster_path())
                    .withValue(MoviesContract.Movies.MOVIE_RELEASE_DATE, e.getRelease_date())
                    .withValue(MoviesContract.Movies.MOVIE_FAVORED, e.getFavourtite())
                    .build());
            syncResult.stats.numInserts++;
        }
        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(MoviesContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                MoviesContract.Movies.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }



    /**
     * Given a string representation of a URL, sets up a connection and gets an input stream.
     */
    private InputStream downloadUrl(final URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(NET_READ_TIMEOUT_MILLIS /* milliseconds */);
        conn.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}