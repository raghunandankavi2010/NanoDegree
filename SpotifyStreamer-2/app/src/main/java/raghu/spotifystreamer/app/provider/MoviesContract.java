package raghu.spotifystreamer.app.provider;

/**
 * Created by Raghunandan on 18-11-2015.
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class MoviesContract {

    public static final String QUERY_PARAMETER_DISTINCT = "distinct";



    public interface MoviesColumns {
        String MOVIE_ID = "movie_id";
        String MOVIE_TITLE = "movie_title";
        String MOVIE_OVERVIEW = "movie_overview";
        String MOVIE_GENRE_IDS = "movie_genre_ids";
        String MOVIE_POPULARITY = "movie_popularity";
        String MOVIE_VOTE_AVERAGE = "movie_vote_average";
        String MOVIE_VOTE_COUNT = "movie_vote_count";
        String MOVIE_BACKDROP_PATH = "movie_backdrop_path";
        String MOVIE_POSTER_PATH = "movie_poster_path";
        String MOVIE_RELEASE_DATE = "movie_release_date";
        String MOVIE_FAVORED = "movie_favored";

    }

    public static final String CONTENT_AUTHORITY = "raghu.spotifystreamer.app.provider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_MOVIES = "movies";
    private static final String PATH_SORTED_MOVIES = "sortedmovies";


    public static class Movies implements MoviesColumns, BaseColumns {

        public static final String TABLE_MOVIES="Movies";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();


        /**
         * MIME type for All Movies.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.popularmovies.movies";
        /**
         * MIME type for individual Movies.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.popularmovies.movie";

        /** Default "ORDER BY" clause. */
        public static final String DEFAULT_SORT = BaseColumns._ID + " DESC";

        /** Build {@link Uri} for requested {@link #MOVIE_ID}. */
        public static Uri buildMovieUri(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }

        /** Read {@link #MOVIE_ID} from {@link Movies} {@link Uri}. */
        public static String getMovieId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class MoviesSorted implements MoviesColumns, BaseColumns {

        public static final String TABLE_MOVIESS_SORTED="Sortedmovies";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SORTED_MOVIES).build();

        /**
         * MIME type for All Movies.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.popularmovies.moviessorted";
        /**
         * MIME type for individual Movies.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.popularmovies.moviesorted";

        /** Default "ORDER BY" clause. */
        public static final String DEFAULT_SORT = BaseColumns._ID + " DESC";

        /** Build {@link Uri} for requested {@link #MOVIE_ID}. */
        public static Uri buildMovieUri(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }

        /** Read {@link #MOVIE_ID} from {@link Movies} {@link Uri}. */
        public static String getMovieId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    private MoviesContract() {
        throw new AssertionError("No instances.");
    }
}
