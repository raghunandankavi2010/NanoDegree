package raghu.spotifystreamer.app.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import raghu.spotifystreamer.app.R;
import raghu.spotifystreamer.app.RxApp;
import raghu.spotifystreamer.app.Utils;
import raghu.spotifystreamer.app.model.Movies;
import raghu.spotifystreamer.app.model.Reviews;
import raghu.spotifystreamer.app.model.SpotifyMoviesModel;
import raghu.spotifystreamer.app.model.Videos;
import raghu.spotifystreamer.app.provider.MoviesContract;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Raghunandan on 18-11-2015.
 */
public class DetailFragment extends Fragment {


    private boolean mRequestPending,mRequestPendingV;
    private static final String STATE_REVIEWS = "state_reviews";
    private static final String REQUEST_PEDNING = "request_pending";
    private static final String STATE_TRAILERS = "state_trailers";
    private static final String REQUEST_PEDNINGT = "request_pendingT";
    private static final String ERROR = "error";
    private ContentResolver contentResolver;
    private ImageButton fab;
    private int val, pageCount = 1, totalcount;
    private Movies movie;
    private TextView ratings, release,content;
    private ImageView mImage;
    private SpotifyMoviesModel mModel;
    private CardView review_cardView;
    private LinearLayout cont;


    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private static final String[] PROJECTION = new String[]{
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
            MoviesContract.Movies.MOVIE_FAVORED
    };
    private Uri uri = MoviesContract.Movies.CONTENT_URI;

    private TextView review;

    private ArrayList<Reviews> mList = new ArrayList<>();
    private ArrayList<Videos> mVideos = new ArrayList<>();

    private boolean mError;


    public DetailFragment() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        mSubscriptions.unsubscribe();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        mModel = ((RxApp) getActivity().getApplication()).component().spotifyMoviesModel();
        fab = (ImageButton) view.findViewById(R.id.imageButton);
        review_cardView = (CardView) view.findViewById(R.id.reviews);
        cont = (LinearLayout)view.findViewById(R.id.containerv);
        ratings = (TextView) view.findViewById(R.id.ratings);
        release = (TextView) view.findViewById(R.id.release);
        content = (TextView) view.findViewById(R.id.content);
        mImage = (ImageView) view.findViewById(R.id.backdrop);
        review = (TextView) view.findViewById(R.id.reviewstext);

        movie = getArguments().getParcelable("key");
        contentResolver = getActivity().getContentResolver();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ratings.append("Movie Name"+movie.getTitle());
        ratings.append("\n");
        ratings.append("Ratings :"+String.valueOf(movie.getVote_avarage()));
        release.append(movie.getRelease_date());

        if(!TextUtils.isEmpty(movie.getOverview()))
        content.append(movie.getOverview());
        else
        content.setVisibility(View.GONE);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).setTitle("Details");

        Cursor cursor = contentResolver.query(uri, PROJECTION, "movie_title=?", new String[]{movie.getTitle()}, null, null);
        if (cursor.moveToFirst())
            val = cursor.getInt(11);

        //Toast.makeText(getActivity(), "" + val, Toast.LENGTH_SHORT).show();
        if (!TextUtils.isEmpty(movie.getTitle())) {
            Picasso.with(getActivity()).
                    load(Utils.BASE_IMAGE_URL +
                            getResources().getString(R.string.size_500) +
                            movie.getBackdrop_path())
                    .placeholder(R.color.primaryColor)
                    .into(mImage);
        } else {
            mImage.setVisibility(View.GONE);
        }


        if (val == 0) {
            fab.setImageDrawable(getImage(R.drawable.ic_favorite_border));
            //fab.setSelected(false);
        } else if (val == 1) {
            fab.setImageDrawable(getImage(R.drawable.ic_favorite_full));
            //fab.setSelected(true);
        }
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (val == 0) {
                    fab.setImageDrawable(getImage(R.drawable.ic_favorite_full));
                    val = 1;
                } else if (val == 1) {
                    fab.setImageDrawable(getImage(R.drawable.ic_favorite_border));
                    val = 0;
                }
                setMovieFavored(movie, val);
            }
        });


        if (savedInstanceState == null) {
            //Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
            fetchData();
            fetchVidoes();
        } else {
            if (savedInstanceState.getBoolean(REQUEST_PEDNINGT, false)) {
                if (mModel.getVideoRequest() != null) {
                    mSubscriptions.add(
                            mModel.getVideoRequest()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new VideoListSubscriber()));

                    //Toast.makeText(getActivity(), "Continuing Subscription", Toast.LENGTH_SHORT).show();
                }
            } else if (savedInstanceState.containsKey(STATE_TRAILERS)) {

                //Toast.makeText(getActivity(), "List restored", Toast.LENGTH_SHORT).show();
                ArrayList<Videos> list = savedInstanceState.getParcelableArrayList(STATE_TRAILERS);
                //Toast.makeText(getActivity(), "List restored"+list.size(), Toast.LENGTH_SHORT).show();
                mVideos = list;
                for (Videos video : mVideos) {
                    final View videoView = getActivity().getLayoutInflater().inflate(R.layout.item_video, cont, false);
                    final TextView videoNameView = (TextView) videoView.findViewById(R.id.video_name);

                    videoNameView.setText(video.getSite() + ": " + video.getName());
                    videoView.setTag(video);
                    videoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    cont.addView(videoView);
                }


            } else {
                fetchVidoes();
            }




            boolean bool = savedInstanceState.getBoolean(REQUEST_PEDNING, false);
            if (bool) {

                if (mModel.getReviewsRequest() != null) {
                    mSubscriptions.add(
                            mModel.getReviewsRequest()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new ReviewListSubscriber()));

                    //Toast.makeText(getActivity(), "Continuing Subscription", Toast.LENGTH_SHORT).show();
                }
            } else if (savedInstanceState.containsKey(STATE_REVIEWS)) {

                //Toast.makeText(getActivity(), "List restored", Toast.LENGTH_SHORT).show();
                ArrayList<Reviews> list = savedInstanceState.getParcelableArrayList(STATE_REVIEWS);
                //Toast.makeText(getActivity(), "List restored"+list.size(), Toast.LENGTH_SHORT).show();
                mList = list;

                //Toast.makeText(getActivity(), "List restored" + list.size(), Toast.LENGTH_SHORT).show();
                review.append(mList.get(0).getAuthor());
                review.append("\n");
                review.append(mList.get(0).getContent());
                review.append("\n");

            } else {
                //Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                fetchData();
                fetchVidoes();
            }

        }



    }

    public void fetchVidoes()
    {
        mRequestPendingV = true;
        mSubscriptions.add(
                mModel.getVideoList(movie.getId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new VideoListSubscriber()));
    }
    public void setMovieFavored(Movies movie, int val) {


        if (val == 1) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesContract.Movies.MOVIE_ID, movie.getId());
            contentValues.put(MoviesContract.Movies.MOVIE_TITLE, movie.getTitle());
            contentValues.put(MoviesContract.Movies.MOVIE_OVERVIEW, movie.getOverview());
            contentValues.put(MoviesContract.Movies.MOVIE_GENRE_IDS, movie.getGenerids());
            contentValues.put(MoviesContract.Movies.MOVIE_POPULARITY, movie.getPopularity());
            contentValues.put(MoviesContract.Movies.MOVIE_VOTE_AVERAGE, movie.getVote_avarage());
            contentValues.put(MoviesContract.Movies.MOVIE_VOTE_COUNT, movie.getVote_count());
            contentValues.put(MoviesContract.Movies.MOVIE_BACKDROP_PATH, movie.getBackdrop_path());
            contentValues.put(MoviesContract.Movies.MOVIE_POSTER_PATH, movie.getPoster_path());
            contentValues.put(MoviesContract.Movies.MOVIE_RELEASE_DATE, movie.getRelease_date());
            contentValues.put(MoviesContract.Movies.MOVIE_FAVORED, 1);
            contentResolver.insert(uri, contentValues);
            movie.setFavourite(1);
            //fab.setImageDrawable(getImage(R.drawable.ic_favorite_full));
        } else if (val == 0) {
            contentResolver.delete(
                    uri,
                    "movie_title=?",
                    new String[]{movie.getTitle()});
            // contentResolver.delete(uri,MoviesContract.Movies.MOVIE_TITLE +" = "+movie.getTitle(),PROJECTION);
            //fab.setImageDrawable(getImage(R.drawable.ic_favorite_border));
            movie.setFavourite(0);


        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mList.size() > 0) {
            outState.putParcelableArrayList(STATE_REVIEWS, mList);
        }
        if (mVideos.size() > 0) {
            outState.putParcelableArrayList(STATE_TRAILERS, mVideos);
        }
        outState.putBoolean(REQUEST_PEDNING, mRequestPending);
        outState.putBoolean(REQUEST_PEDNINGT, mRequestPendingV);
        outState.putBoolean(ERROR, mError);


        //outState.putBoolean(LOAD_MORE, mLoadMore);

    }

    public Drawable getImage(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(id, getActivity().getTheme());
        } else {
            return getResources().getDrawable(id);
        }
    }

    private void fetchData() {

        mRequestPending = true;

        mSubscriptions.add(
                mModel.getReviewsList(movie.getId(), pageCount)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ReviewListSubscriber()));
    }


    private class ReviewListSubscriber extends Subscriber<ArrayList<Reviews>> {

        @Override
        public void onNext(ArrayList<Reviews> reviews) {
            mRequestPending = false;
            totalcount = mModel.getTotal_pages();
            mList = reviews;

            review.append("Author :" + mList.get(0).getAuthor());
            review.append("\n");
            review.append("Content :" + mList.get(0).getContent());
            review.append("\n");

        }

        @Override
        public void onCompleted() {
            mRequestPending = false;

        }

        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
            mRequestPending = false;
            mError = true;
            review_cardView.setVisibility(View.GONE);
        }
    }


    private class VideoListSubscriber extends Subscriber<ArrayList<Videos>> {

        @Override
        public void onNext(ArrayList<Videos> videos) {

            Toast.makeText(getActivity(),"Videos"+videos.size(),Toast.LENGTH_SHORT).show();
            mRequestPendingV = false;
            mVideos = videos;
            for (Videos video : videos) {
                final View videoView = getActivity().getLayoutInflater().inflate(R.layout.item_video, cont, false);
                final TextView videoNameView = (TextView) videoView.findViewById(R.id.video_name);

                videoNameView.setText(video.getSite() + ": " + video.getName());
                videoView.setTag(video);
                videoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Videos video = (Videos) view.getTag();
                        playVideo(video);
                    }
                });
                    cont.addView(videoView);



            }

        }

        @Override
        public void onCompleted() {
            mRequestPendingV = false;

        }

        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
            mRequestPendingV = false;
            cont.setVisibility(View.GONE);

        }
    }

    public void playVideo(Videos video) {
        if (video.getSite().equals(Videos.SITE_YOUTUBE))
            getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey())));
        else
            Log.i("DetailFragment","Cannot play video format");
    }
}
