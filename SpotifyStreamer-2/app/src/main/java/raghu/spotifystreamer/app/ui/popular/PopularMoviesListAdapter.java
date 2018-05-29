package raghu.spotifystreamer.app.ui.popular;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import raghu.spotifystreamer.app.R;
import raghu.spotifystreamer.app.Utils;
import raghu.spotifystreamer.app.model.Movies;
import raghu.spotifystreamer.app.network.NetworkState;
import raghu.spotifystreamer.app.ui.OnMovieSelected;

public class PopularMoviesListAdapter  extends PagedListAdapter<Movies, RecyclerView.ViewHolder> {

    OnMovieSelected onMovieSelected;
    public static final int VIEW_ITEM = 1;
    public static final int VIEW_PROG = 0;
    private NetworkState networkState;


    protected PopularMoviesListAdapter(Fragment fragment) {
        super(DIFF_CALLBACK);
        onMovieSelected =(OnMovieSelected)fragment;
    }



   public static DiffUtil.ItemCallback<Movies> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movies>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movies oldItem, @NonNull Movies newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movies oldItem, @NonNull Movies newItem) {
            return oldItem.equals(newItem);
        }
    };



    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        RecyclerView.ViewHolder viewHolder;
        if(viewType==VIEW_ITEM) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_item, parent, false);

            viewHolder = new PopularMoviesListAdapter.ViewHolder_item(v);
        }

        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_load_more, parent, false);

            viewHolder = new PopularMoviesListAdapter.Progress_ViewHolder(v);
        }

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, final int position) {

        if(vh instanceof PopularMoviesListAdapter.ViewHolder_item) {

            final PopularMoviesListAdapter.ViewHolder_item viewHolder = (PopularMoviesListAdapter.ViewHolder_item) vh;



            Picasso.with(viewHolder.m_Image.getContext()).

                    load(Utils.BASE_IMAGE_URL+
                            viewHolder.m_Image.getContext().getResources().getString(R.string.phone_size)+
                            getItem(position).getPoster_path())
                    //.transform(transformation)
                    .into(viewHolder.m_Image);

            viewHolder.bind(getItem(position),onMovieSelected,position);

        }else {
            ((PopularMoviesListAdapter.Progress_ViewHolder)vh).progressBar.setIndeterminate(true);
        }
    }



    // inner class to hold a reference to each item of RecyclerView
    public  class ViewHolder_item extends RecyclerView.ViewHolder {

        public ImageView m_Image;
        public CardView cv;

        public ViewHolder_item(View itemLayoutView) {
            super(itemLayoutView);
            cv = (CardView) itemLayoutView.findViewById(R.id.card_view);
            m_Image = (ImageView) itemLayoutView.findViewById(R.id.imageView);

            //cv.setOnTouchListener(new RippleForegroundListener());

        }



        public void bind(final Movies item, final OnMovieSelected onMovieSelected,final int position) {
            m_Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMovieSelected.movieselected(item,view,position);
                }
            });
        }
    }




    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return VIEW_PROG;
        } else {
            return VIEW_ITEM;
        }
    }

    public static class Progress_ViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public Progress_ViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        }
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }



}
