package raghu.spotifystreamer.provider;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
