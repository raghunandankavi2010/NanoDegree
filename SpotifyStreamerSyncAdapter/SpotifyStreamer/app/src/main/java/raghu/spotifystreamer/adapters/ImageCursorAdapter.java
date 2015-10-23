package raghu.spotifystreamer.adapters;

/**
 * Created by Raghunandan on 22-10-2015.
 */
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import raghu.spotifystreamer.DetailsActivity;
import raghu.spotifystreamer.Models.Movies;
import raghu.spotifystreamer.R;
import raghu.spotifystreamer.Utilities.Constants;
import raghu.spotifystreamer.provider.MoviesContract;

/**
 * Created by skyfishjy on 10/31/14.
 */
public class ImageCursorAdapter extends CursorRecyclerViewAdapter<ImageCursorAdapter.ViewHolder_item>{

    public ImageCursorAdapter(Context context,Cursor cursor){
        super(context,cursor);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder_item onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        ViewHolder_item  viewHolder;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);

        viewHolder = new ViewHolder_item(v);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder_item vh, Cursor cursor) {

        if(vh instanceof ViewHolder_item) {

           /* db.execSQL("CREATE TABLE " + MoviesContract.Movies.TABLE_MOVIES + "("
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
                    + "UNIQUE (" + MoviesContract.Movies.MOVIE_ID + ") ON CONFLICT REPLACE)");*/
            Log.i("path", cursor.getString(1));
            Log.i("path", cursor.getString(2));
            Log.i("path", cursor.getString(3));
            Log.i("path", ""+cursor.getString(4));
            Log.i("path",""+ cursor.getFloat(5));
            Log.i("path", ""+cursor.getInt(6));
            Log.i("path", cursor.getString(7));
            Log.i("path", ""+cursor.getString(8));
            Log.i("path", ""+cursor.getString(9));
            Log.i("path", ""+cursor.getString(10));
            Log.i("path", ""+cursor.getInt(11));

            ViewHolder_item viewHolder = (ViewHolder_item) vh;
            Picasso.with(viewHolder.m_Image.getContext()).
                    load(Constants.BASE_IMAGE_URL +
                            viewHolder.m_Image.getContext().getResources().getString(R.string.phone_size) +
                            cursor.getString(9))
                                    .into(viewHolder.m_Image);
        }
    }



    // inner class to hold a reference to each item of RecyclerView
    public  static class ViewHolder_item extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView m_Image;

        public ViewHolder_item(View itemLayoutView) {
            super(itemLayoutView);
            m_Image = (ImageView) itemLayoutView.findViewById(R.id.imageView);
            m_Image.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

          /*  int pos = getAdapterPosition();
            Movies movie = mList.get(pos);
            Intent intent =  new Intent(view.getContext(),DetailsActivity.class);
            intent.putExtra("movie",movie);
            view.getContext().startActivity(intent);*/
        }
    }




}
