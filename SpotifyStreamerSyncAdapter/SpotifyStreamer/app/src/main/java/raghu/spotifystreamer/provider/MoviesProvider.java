package raghu.spotifystreamer.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import timber.log.Timber;

/**
 * Created by Raghunandan on 20-10-2015.
 */
public class MoviesProvider extends ContentProvider {

    private static final String TAG= MoviesProvider.class.getSimpleName();


    /**
     * Content authority for this provider.
     */
    private static final String AUTHORITY = MoviesContract.CONTENT_AUTHORITY;

    // The constants below represent individual URI routes, as IDs. Every URI pattern recognized by
    // this ContentProvider is defined using sUriMatcher.addURI(), and associated with one of these
    // IDs.
    //
    // When a incoming URI is run through sUriMatcher, it will be tested against the defined
    // URI patterns, and the corresponding route ID will be returned.

    private static final int MOVIES = 1;
    private static final int MOVIES_SORTED = 2;
    private static final int MOVIE_TYPE = 3;
    private static final int MOVIE_SORTED_TYPE = 4;

    private MoviesDatabase mOpenHelper;


    /**
     * UriMatcher, used to decode incoming URIs.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {

        sUriMatcher.addURI(AUTHORITY, "movies", MOVIES);
        sUriMatcher.addURI(AUTHORITY, "movies/*", MOVIES_SORTED);

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        SelectionBuilder builder = new SelectionBuilder();
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {

            case MOVIES:
                // Return all known entries.
                builder.table(MoviesContract.Movies.TABLE_MOVIES)
                        .where(selection, selectionArgs);
                Cursor c = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctx = getContext();
                assert ctx != null;
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return MoviesContract.Movies.CONTENT_TYPE;
            case MOVIES_SORTED:
                return MoviesContract.MoviesSorted.CONTENT_TYPE;
            case MOVIE_TYPE:
                return MoviesContract.Movies.CONTENT_ITEM_TYPE;
            case MOVIE_SORTED_TYPE:
                return MoviesContract.MoviesSorted.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        Timber.tag(TAG).v("insert(uri=" + uri + ", values=" + contentValues.toString() + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES: {
                db.insertOrThrow(MoviesContract.Movies.TABLE_MOVIES, null, contentValues);
                notifyChange(uri);
                return MoviesContract.Movies.buildMovieUri(contentValues.getAsString(MoviesContract.Movies.MOVIE_ID));
            }
            case MOVIES_SORTED: {
                db.insertOrThrow(MoviesContract.MoviesSorted.TABLE_MOVIESS_SORTED, null, contentValues);
                notifyChange(uri);
                return MoviesContract.MoviesSorted.buildMovieUri(contentValues.getAsString(MoviesContract.MoviesSorted.MOVIE_ID));
            }
            default: {
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
            }
        }
    }

    private void notifyChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case MOVIES:
                count = builder.table(MoviesContract.Movies.TABLE_MOVIES)
                        .where(selection, selectionArgs)
                        .update(db, contentValues);
                break;
            case MOVIES_SORTED:
                count = builder.table(MoviesContract.MoviesSorted.TABLE_MOVIESS_SORTED)
                        .where(selection, selectionArgs)
                        .update(db, contentValues);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }
}
