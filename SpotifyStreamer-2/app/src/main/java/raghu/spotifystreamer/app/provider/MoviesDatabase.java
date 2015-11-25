package raghu.spotifystreamer.app.provider;

/**
 * Created by Raghunandan on 18-11-2015.
 */

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Raghunandan on 22-10-2015.
 */
public class MoviesDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "movies.db";
    private static final int DB_VERSION = 1;
    private Context mContext;


    public MoviesDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + MoviesContract.Movies.TABLE_MOVIES + "("
                + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
                + MoviesContract.Movies.MOVIE_ID + " TEXT NOT NULL,"
                + MoviesContract.Movies.MOVIE_TITLE + " TEXT NOT NULL,"
                + MoviesContract.Movies.MOVIE_OVERVIEW + " TEXT,"
                + MoviesContract.Movies.MOVIE_GENRE_IDS + " TEXT,"
                + MoviesContract.Movies.MOVIE_POPULARITY + " REAL,"
                + MoviesContract.Movies.MOVIE_VOTE_AVERAGE + " REAL,"
                + MoviesContract.Movies.MOVIE_VOTE_COUNT + " INTEGER,"
                + MoviesContract.Movies.MOVIE_BACKDROP_PATH + " TEXT,"
                + MoviesContract.Movies.MOVIE_POSTER_PATH + " TEXT,"
                + MoviesContract.Movies.MOVIE_RELEASE_DATE + " TEXT,"
                + MoviesContract.Movies.MOVIE_FAVORED + " INTEGER NOT NULL DEFAULT 0,"
                + MoviesContract.Movies.MOVIE_REVIEW_SAVED + " INTEGER NOT NULL DEFAULT 0,"
                + MoviesContract.Movies.MOVIE_TRAILERS_SAVED + " INTEGER NOT NULL DEFAULT 0,"
                + "UNIQUE (" + MoviesContract.Movies.MOVIE_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + MoviesContract.MoviesSorted.TABLE_MOVIESS_SORTED + "("
                + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
                + MoviesContract.Movies.MOVIE_ID + " TEXT NOT NULL,"
                + MoviesContract.Movies.MOVIE_TITLE + " TEXT NOT NULL,"
                + MoviesContract.Movies.MOVIE_OVERVIEW + " TEXT,"
                + MoviesContract.Movies.MOVIE_GENRE_IDS + " TEXT,"
                + MoviesContract.Movies.MOVIE_POPULARITY + " REAL,"
                + MoviesContract.Movies.MOVIE_VOTE_AVERAGE + " REAL," // floating point number
                + MoviesContract.Movies.MOVIE_VOTE_COUNT + " INTEGER,"
                + MoviesContract.Movies.MOVIE_BACKDROP_PATH + " TEXT,"
                + MoviesContract.Movies.MOVIE_POSTER_PATH + " TEXT,"
                + MoviesContract.Movies.MOVIE_RELEASE_DATE + " TEXT,"
                + MoviesContract.Movies.MOVIE_FAVORED + " INTEGER NOT NULL DEFAULT 0,"
                + "UNIQUE (" + MoviesContract.Movies.MOVIE_ID + ") ON CONFLICT REPLACE)");

        // Trailers Table
        db.execSQL("CREATE TABLE " + MoviesContract.Video.TABLE_VIDEOS + "("
                + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
                + MoviesContract.Video.MOVIE_ID + " TEXT NOT NULL,"
                + MoviesContract.Video.TRAILER_ID + " TEXT NOT NULL,"
                + MoviesContract.Video.TRAILER_ISO + " TEXT,"
                + MoviesContract.Video.TRAILER_KEY + " TEXT,"
                + MoviesContract.Video.TRAILER_NAME + " TEXT,"
                + MoviesContract.Video.TRAILER_TYPE + " TEXT,"
                + MoviesContract.Video.TRAILER_SITE + " TEXT,"
                + "UNIQUE (" + MoviesContract.Video.TRAILER_ID + ") ON CONFLICT REPLACE)");


        // Reviews Table
        db.execSQL("CREATE TABLE " + MoviesContract.Review.TABLE_REVIEWS + "("
                + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
                + MoviesContract.Review.MOVIE_ID + " TEXT NOT NULL,"
                + MoviesContract.Review.REVIEW_ID + " TEXT NOT NULL,"
                + MoviesContract.Review.REVIEW_AUTHOR + " TEXT,"
                + MoviesContract.Review.REVIEW_CONTENT + " TEXT,"
                + MoviesContract.Review.REVIEW_URL + " TEXT,"
                + "UNIQUE (" + MoviesContract.Review.REVIEW_ID + ") ON CONFLICT REPLACE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DB_NAME);
    }
}