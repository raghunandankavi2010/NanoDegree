package raghu.spotifystreamer.app.ui;

import android.support.v4.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import raghu.spotifystreamer.app.R;
import raghu.spotifystreamer.app.Utils;
import raghu.spotifystreamer.app.model.Movies;
import raghu.spotifystreamer.app.provider.MoviesContract;


/**
 * Created by Raghunandan on 24-11-2015.
 */
public class ImageCursorAdapter extends CursorRecyclerViewAdapter<ImageCursorAdapter.ViewHolder_item> {

    private OnMovieSelected onMovieSelected;

    public ImageCursorAdapter(Context context, Fragment fragment, Cursor cursor) {

        super(context, cursor);
        onMovieSelected = (OnMovieSelected)fragment;

    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder_item onCreateViewHolder(ViewGroup parent,
                                              int viewType) {

        ViewHolder_item viewHolder;

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);

        viewHolder = new ViewHolder_item(v);


        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder_item vh, final Cursor cursor) {



        ViewHolder_item viewHolder = (ViewHolder_item) vh;
        Picasso.with(viewHolder.m_Image.getContext()).
                load(Utils.BASE_IMAGE_URL+
                        viewHolder.m_Image.getContext().getResources().getString(R.string.phone_size)+
                        cursor.getString(cursor.getColumnIndex(MoviesContract.Movies.MOVIE_POSTER_PATH)))
                .into(viewHolder.m_Image);

    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder_item extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView m_Image;
        public CardView cv;

        public ViewHolder_item(View itemLayoutView) {
            super(itemLayoutView);
            cv = (CardView) itemLayoutView.findViewById(R.id.card_view);
            m_Image = (ImageView) itemLayoutView.findViewById(R.id.imageView);
            m_Image.setOnClickListener(this);
            //cv.setOnTouchListener(new RippleForegroundListener());

        }

        @Override
        public void onClick(View view) {
            //int pos = getLayoutPosition();
            //Toast.makeText(view.getContext(),mList.get(pos).getTitle(),Toast.LENGTH_SHORT).show();
           /* if(view.getId()==R.id.card_view)
            {

            }*/

            int pos = getLayoutPosition();
            Cursor cursor = getItem(pos);

            onMovieSelected.movieselected(getMovieFromCursor(cursor),(ImageView)view,getAdapterPosition());
            /*Intent intent =  new Intent(view.getContext(),DetailActivity.class);
            intent.putExtra("movie",getMovieFromCursor(cursor));
            view.getContext().startActivity(intent);*/
        }

    }

    public static Movies getMovieFromCursor(Cursor cursor)
    {

        Movies movies = new Movies();
        movies.setId(cursor.getInt(cursor.getColumnIndex(MoviesContract.Movies.MOVIE_ID)));
        movies.setTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.Movies.MOVIE_TITLE)));
        movies.setOverview(cursor.getString(cursor.getColumnIndex(MoviesContract.Movies.MOVIE_OVERVIEW)));
        movies.setGenerids(cursor.getString(cursor.getColumnIndex(MoviesContract.Movies.MOVIE_GENRE_IDS)));
        movies.setPopularity(cursor.getFloat(cursor.getColumnIndex(MoviesContract.Movies.MOVIE_POPULARITY)));
        movies.setVote_average(cursor.getInt(cursor.getColumnIndex(MoviesContract.Movies.MOVIE_VOTE_AVERAGE)));
        movies.setVote_count(cursor.getInt(cursor.getColumnIndex(MoviesContract.Movies.MOVIE_VOTE_COUNT)));
        movies.setbackdrop_path(cursor.getString(cursor.getColumnIndex(MoviesContract.Movies.MOVIE_BACKDROP_PATH)));
        movies.setPoster_path(cursor.getString(cursor.getColumnIndex(MoviesContract.Movies.MOVIE_POSTER_PATH)));
        movies.setRelease_date(cursor.getString(cursor.getColumnIndex(MoviesContract.Movies.MOVIE_RELEASE_DATE)));
        movies.setFavourite(cursor.getInt(cursor.getColumnIndex(MoviesContract.Movies.MOVIE_FAVORED)));

        return movies;
    }
}

