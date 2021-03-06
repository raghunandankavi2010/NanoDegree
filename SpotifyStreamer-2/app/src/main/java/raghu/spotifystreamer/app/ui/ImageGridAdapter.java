package raghu.spotifystreamer.app.ui;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import raghu.spotifystreamer.app.R;
import raghu.spotifystreamer.app.Utils;
import raghu.spotifystreamer.app.model.Movies;

import java.util.ArrayList;



/**
 * Created by Raghunandan on 23-09-2015.
 */

// Thanks to Chiuki Chan's AutoFitRecyclerView. I picked that square island blog post.
public class ImageGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static OnMovieSelected onMovieSelected;

    private static ArrayList<Movies> mList;


    public static final int VIEW_ITEM = 1;
    public static final int VIEW_PROG = 0;


    public ImageGridAdapter(Fragment fragment) {
        onMovieSelected =(OnMovieSelected)fragment;
        this.mList = new ArrayList<>();
    }

    public void addPosts(ArrayList<Movies> newPosts) {
        mList.addAll(newPosts);
        notifyDataSetChanged();
    }

    public void remove()
    {
        mList.remove(mList.size()-1);
        notifyDataSetChanged();
    }

    public void add(Movies o) {
        mList.add(o);

        notifyDataSetChanged();
    }

    public static ArrayList<Movies> getmList() {
        return mList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        RecyclerView.ViewHolder viewHolder;
        if(viewType==VIEW_ITEM) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_item, parent, false);

            viewHolder = new ViewHolder_item(v);
        }

        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_load_more, parent, false);

            viewHolder = new ProgressViewHolder(v);
        }

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, final int position) {

        if(vh instanceof ViewHolder_item) {

            final ViewHolder_item viewHolder = (ViewHolder_item) vh;

          /*  Transformation transformation = new Transformation() {


                @Override
                public Bitmap transform(Bitmap source) {
                    int targetWidth =viewHolder.m_Image.getWidth();

                    double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                    int targetHeight = (int) (targetWidth * aspectRatio);
                    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;
                }

                @Override
                public String key() {
                    return "transformation" + " desiredWidth";
                }
            };
*/

            Picasso.with(viewHolder.m_Image.getContext()).

                    load(Utils.BASE_IMAGE_URL+
                            viewHolder.m_Image.getContext().getResources().getString(R.string.phone_size)+
                            mList.get(position).getPoster_path())
                    //.transform(transformation)
                    .into(viewHolder.m_Image);
        }else {
            ((ProgressViewHolder)vh).progressBar.setIndeterminate(true);
        }
    }



    // inner class to hold a reference to each item of RecyclerView
    public  class ViewHolder_item extends RecyclerView.ViewHolder implements View.OnClickListener{

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

            ImageView imageview = (ImageView) view;

            int pos = getAdapterPosition();

            Movies movie = mList.get(pos);
            if(onMovieSelected!=null)
                onMovieSelected.movieselected(movie,imageview,getAdapterPosition());
       /*     Intent intent =  new Intent(view.getContext(),DetailActivity.class);
            intent.putExtra("movie",movie);
            view.getContext().startActivity(intent);*/
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position)!=null? VIEW_ITEM: VIEW_PROG;
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        }
    }



}
