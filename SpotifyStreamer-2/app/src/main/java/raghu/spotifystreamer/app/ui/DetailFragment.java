package raghu.spotifystreamer.app.ui;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import raghu.spotifystreamer.app.R;
import raghu.spotifystreamer.app.RxApp;
import raghu.spotifystreamer.app.Utils;
import raghu.spotifystreamer.app.model.Movies;
import raghu.spotifystreamer.app.model.Reviews;
import raghu.spotifystreamer.app.model.Videos;
import raghu.spotifystreamer.app.ui.popular.SpotifyMoviesModel;
import raghu.spotifystreamer.app.provider.MoviesContract;
import raghu.spotifystreamer.app.provider.MoviesDatabase;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Raghunandan on 18-11-2015.
 */
public class DetailFragment extends Fragment {


    private boolean mRequestPending, mRequestPendingV, vis;
    private static final String STATE_REVIEWS = "state_reviews";
    private static final String REQUEST_PEDNING = "request_pending";
    private static final String STATE_TRAILERS = "state_trailers";
    private static final String REQUEST_PEDNINGT = "request_pendingT";
    private static final String ERROR = "error";
    private ContentResolver contentResolver;
    private ImageButton fab;
    private int val, review_saved, trailer_saved, pageCount = 1, totalcount;
    private Movies movie;
    private TextView ratings, release, content, average, mName;
    private ImageView mImage;
    private SpotifyMoviesModel mModel;
    private CardView review_cardView, trailers_cardView;
    private LinearLayout cont;
    private MenuItem mMenuItemShare;


    private CompositeDisposable mSubscriptions = new CompositeDisposable();
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
            MoviesContract.Movies.MOVIE_FAVORED,
            MoviesContract.Movies.MOVIE_REVIEW_SAVED,
            MoviesContract.Movies.MOVIE_TRAILERS_SAVED,
    };

    private static final String[] PROJECTION_REVIEWS = new String[]{
            BaseColumns._ID,
            MoviesContract.Review.MOVIE_ID,
            MoviesContract.Review.REVIEW_ID,
            MoviesContract.Review.REVIEW_AUTHOR,
            MoviesContract.Review.REVIEW_CONTENT,
            MoviesContract.Review.REVIEW_URL,

    };

    private static final String[] PROJECTION_VIDEOS = new String[]{
            BaseColumns._ID,
            MoviesContract.Video.MOVIE_ID,
            MoviesContract.Video.TRAILER_ID,
            MoviesContract.Video.TRAILER_ISO,
            MoviesContract.Video.TRAILER_KEY,
            MoviesContract.Video.TRAILER_NAME,
            MoviesContract.Video.TRAILER_TYPE,
            MoviesContract.Video.TRAILER_SITE

    };


    private Uri uri = MoviesContract.Movies.CONTENT_URI;
    private Uri review_uri = MoviesContract.Review.CONTENT_URI;
    private Uri trailer_uri = MoviesContract.Video.CONTENT_URI;

    private TextView review;

    private ArrayList<Reviews> mList = new ArrayList<>();
    private ArrayList<Videos> mVideos = new ArrayList<>();

    private boolean mError;
    private String firstTrailer, movie_id;
    private BriteDatabase db;
    private Observable<SqlBrite.Query> mMovieObservable, mReviewObservable, mVideoObservable;


    public DetailFragment() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mReviewObservable!=null)
        mReviewObservable.unsubscribeOn(Schedulers.io());
        if(mVideoObservable!=null)
        mVideoObservable.unsubscribeOn(Schedulers.io());
        mSubscriptions.dispose();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        getActivity().supportPostponeEnterTransition();
        mModel = ((RxApp) getActivity().getApplication()).component().spotifyMoviesModel();
        fab = (ImageButton) view.findViewById(R.id.imageButton);
        mName = (TextView) view.findViewById(R.id.name);
        average = (TextView) view.findViewById(R.id.average);
        review_cardView = (CardView) view.findViewById(R.id.reviews);
        trailers_cardView = (CardView) view.findViewById(R.id.trailers);
        cont = (LinearLayout) view.findViewById(R.id.containerv);
        ratings = (TextView) view.findViewById(R.id.ratings);
        release = (TextView) view.findViewById(R.id.release);
        content = (TextView) view.findViewById(R.id.content);
        mImage = (ImageView) view.findViewById(R.id.backdrop);
        review = (TextView) view.findViewById(R.id.reviewstext);

        SqlBrite sqlBrite = SqlBrite.create();
        db = sqlBrite.wrapDatabaseHelper(new MoviesDatabase(getActivity()));

        movie = getArguments().getParcelable("key");
        movie_id = String.valueOf(movie.getId());
        contentResolver = getActivity().getContentResolver();

        Cursor cursor = contentResolver.query(uri, PROJECTION, "movie_title=?", new String[]{movie.getTitle()}, null, null);
        if (cursor.moveToFirst()) {
            val = cursor.getInt(11);
            review_saved = cursor.getInt(cursor.getColumnIndex(MoviesContract.Movies.MOVIE_REVIEW_SAVED));
            trailer_saved = cursor.getInt(cursor.getColumnIndex(MoviesContract.Movies.MOVIE_TRAILERS_SAVED));
            Log.i("DetailFragment", "" + review_saved);
            Log.i("DetailFragment", "" + trailer_saved);
            Log.i("DetailFragment", "" + movie_id);

        }

        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (movie != null) {
            if (!TextUtils.isEmpty(movie.getTitle())) {
                mName.setText("Name :" + movie.getTitle());
            }
            ratings.setText("Ratings :" + String.valueOf(movie.getVote_avarage()));
            if (!TextUtils.isEmpty(movie.getRelease_date())) {
                release.setText("Release :" + movie.getRelease_date());
            }


            average.setText("Vote Average :" + String.valueOf(movie.getVote_count()));

            if (!TextUtils.isEmpty(movie.getOverview()))
                content.setText("Description :" + movie.getOverview());
            else
                content.setVisibility(View.GONE);
        }


        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).setTitle("Details");


        Log.i("URL",""+ Utils.BASE_IMAGE_URL +
                getResources().getString(R.string.size_500) +
                movie.getBackdrop_path());
        if (!TextUtils.isEmpty(movie.getTitle())) {

           Picasso.with(getActivity())
                    .load(Utils.BASE_IMAGE_URL +
                            getResources().getString(R.string.size_500) +
                            movie.getBackdrop_path())
                    .placeholder(R.color.primaryColor)
                    .into(mImage, new Callback() {
                        @Override
                        public void onSuccess() {
                           // ViewCompat.setTransitionName(mImage,"detailimage");

                           // startPostponedEnterTransition()
                            getActivity().supportStartPostponedEnterTransition();

                        }

                        @Override
                        public void onError() {
                            //startPostponedEnterTransition()
                            //ViewCompat.setTransitionName(mImage,"detailimage");
                            getActivity().supportStartPostponedEnterTransition();

                        }
                    });
         /*  Picasso.with(getActivity()).
                    load(Utils.BASE_IMAGE_URL +
                            getResources().getString(R.string.size_500) +
                            movie.getBackdrop_path())
                    .placeholder(R.color.primaryColor)
                    .into(mImage);*/
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

            if (review_saved == 0) {
                fetchData();
            } else {
                fetchReviewsFromDatabase();
            }
            if (trailer_saved == 0) {
                fetchVidoes();
            } else {
                fetchTrailersFromDatabase();
            }

        } else {

            if (savedInstanceState.getBoolean(REQUEST_PEDNINGT, false)) {
                if (mModel.getVideoRequest() != null) {
                    mSubscriptions.add(
                            mModel.getVideoRequest()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(new VideoListSubscriber()));

                    //Toast.makeText(getActivity(), "Continuing Subscription", Toast.LENGTH_SHORT).show();
                }
            } else if (savedInstanceState.containsKey(STATE_TRAILERS)) {
                trailers_cardView.setVisibility(View.VISIBLE);
                //Toast.makeText(getActivity(), "List restored", Toast.LENGTH_SHORT).show();
                ArrayList<Videos> list = savedInstanceState.getParcelableArrayList(STATE_TRAILERS);
                //Toast.makeText(getActivity(), "List restored"+list.size(), Toast.LENGTH_SHORT).show();
                mVideos = list;

                firstTrailer = "http://www.youtube.com/watch?v=" + mVideos.get(0).getKey();
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
                if (trailer_saved == 0) {
                    fetchVidoes();
                } else {
                    fetchTrailersFromDatabase();
                }
            }


            boolean bool = savedInstanceState.getBoolean(REQUEST_PEDNING, false);
            if (bool) {

                if (mModel.getReviewsRequest() != null) {
                    mSubscriptions.add(
                            mModel.getReviewsRequest()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(new ReviewListSubscriber()));

                    //Toast.makeText(getActivity(), "Continuing Subscription", Toast.LENGTH_SHORT).show();
                }
            } else if (savedInstanceState.containsKey(STATE_REVIEWS)) {
                review_cardView.setVisibility(View.VISIBLE);
                //Toast.makeText(getActivity(), "List restored", Toast.LENGTH_SHORT).show();
                ArrayList<Reviews> list = savedInstanceState.getParcelableArrayList(STATE_REVIEWS);
                //Toast.makeText(getActivity(), "List restored"+list.size(), Toast.LENGTH_SHORT).show();
                mList = list;

                for (Reviews reviews1 : mList) {
                    review.append("Author :" + reviews1.getAuthor());
                    review.append("\n");
                    review.append("Content :" + reviews1.getContent());
                    review.append("\n");
                    review.append("\n");
                }


            } else {
                //Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                if (review_saved == 0) {
                    fetchVidoes();
                } else {
                    fetchTrailersFromDatabase();
                }
            }

        }


    }

    private void fetchReviewsFromDatabase() {

        Log.i("DetailFragment", "Reviews");

        if (!TextUtils.isEmpty(movie_id)) {

            mReviewObservable = db.createQuery("Reviews", "SELECT * FROM Reviews WHERE movie_id=" + movie.getId());
            mReviewObservable.subscribeOn(Schedulers.io());

            mReviewObservable.observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .subscribe(new Action1<SqlBrite.Query>() {
                        @Override
                        public void call(SqlBrite.Query query) {
                            Cursor cursor = query.run();
                            review_cardView.setVisibility(View.VISIBLE);
                            while (cursor.moveToNext()) {
                                Log.i("DetailFragment", "Reviews Inside" + cursor.getString(cursor.getColumnIndex(MoviesContract.Review.REVIEW_AUTHOR)));
                                mList.add(getReviewFromCursor(cursor));
                                review.append("Author :" + cursor.getString(cursor.getColumnIndex(MoviesContract.Review.REVIEW_AUTHOR)));
                                review.append("\n");
                                review.append("Content :" + cursor.getString(cursor.getColumnIndex(MoviesContract.Review.REVIEW_CONTENT)));
                                review.append("\n");
                                review.append("\n");
                            }

                        }
                    });
        /*    Cursor cursor = null;
            try {
                cursor = contentResolver.query(review_uri, PROJECTION_REVIEWS, "movie_id=?", new String[]{movie_id}, null, null);
                review_cardView.setVisibility(View.VISIBLE);
                while (cursor.moveToNext()) {
                    Log.i("DetailFragment", "Reviews Inside" + cursor.getString(cursor.getColumnIndex(MoviesContract.Review.REVIEW_AUTHOR)));
                    mList.add(getReviewFromCursor(cursor));
                    review.append("Author :" + cursor.getString(cursor.getColumnIndex(MoviesContract.Review.REVIEW_AUTHOR)));
                    review.append("\n");
                    review.append("Content :" + cursor.getString(cursor.getColumnIndex(MoviesContract.Review.REVIEW_CONTENT)));
                    review.append("\n");
                    review.append("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();

            }*/

        }
    }

    private void fetchTrailersFromDatabase() {
        Log.i("DetailFragment", "Trailers");
        if (!TextUtils.isEmpty(movie_id)) {

            mVideoObservable = db.createQuery("Videos", "SELECT * FROM Videos WHERE movie_id=" + movie.getId());
            mVideoObservable.subscribeOn(Schedulers.io());
            mVideoObservable.observeOn( rx.android.schedulers.AndroidSchedulers.mainThread())
                    .subscribe(new Action1<SqlBrite.Query>() {
                        @Override
                        public void call(SqlBrite.Query query) {
                            Cursor cursor = query.run();
                            boolean once =false;
                            trailers_cardView.setVisibility(View.VISIBLE);

                            getActivity().invalidateOptionsMenu();
                            while (cursor.moveToNext()) {
                                Videos video = getVideoFromCursor(cursor);
                                mVideos.add(video);
                                if(once ==false)
                                {
                                    firstTrailer = "http://www.youtube.com/watch?v=" + video.getKey();
                                    once =true;
                                }
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
                    });



        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();

    }

    public void fetchVidoes() {
        mRequestPendingV = true;
        mSubscriptions.add(
                mModel.getVideoList(movie.getId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new VideoListSubscriber()));
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
            contentValues.put(MoviesContract.Movies.MOVIE_REVIEW_SAVED, movie.getReviewsaved());
            contentValues.put(MoviesContract.Movies.MOVIE_TRAILERS_SAVED, movie.getTrailersaved());


            //contentResolver.insert(uri, contentValues);

            AsyncQueryHandler handler = new AsyncQueryHandler(contentResolver) {
            };
            handler.startInsert(-1, null, uri, contentValues);

            if (mList.size() > 0)
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
        //outState.putBoolean("check", mMenuItemShare.isVisible());


        //outState.putBoolean(LOAD_MORE, mLoadMore);

    }

    public Drawable getImage(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(id, getActivity().getTheme());
        } else {
            return ContextCompat.getDrawable(getActivity(),id);//getResources().getDrawable(id);
        }
    }

    private void fetchData() {

        mRequestPending = true;

        mSubscriptions.add(
                mModel.getReviewsList(movie.getId(), pageCount)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new ReviewListSubscriber()));
    }


    private class ReviewListSubscriber extends DisposableObserver<ArrayList<Reviews>> {

        @Override
        public void onNext(ArrayList<Reviews> reviews) {
            review_cardView.setVisibility(View.VISIBLE);
            mRequestPending = false;
            totalcount = mModel.getTotal_pages();
            mList = reviews;
            for (Reviews reviews1 : reviews) {
                review.append("Author :" + reviews1.getAuthor());
                review.append("\n");
                review.append("Content :" + reviews1.getContent());
                review.append("\n");
                review.append("\n");
                ContentValues contentValues = new ContentValues();
                contentValues.put(MoviesContract.Review.MOVIE_ID, movie_id);
                contentValues.put(MoviesContract.Review.REVIEW_ID, reviews1.getId());
                contentValues.put(MoviesContract.Review.REVIEW_AUTHOR, reviews1.getAuthor());
                contentValues.put(MoviesContract.Review.REVIEW_CONTENT, reviews1.getContent());
                contentValues.put(MoviesContract.Review.REVIEW_URL, reviews1.getUrl());
                //contentResolver.insert(review_uri, contentValues);

                AsyncQueryHandler handler = new AsyncQueryHandler(contentResolver) {
                };
                handler.startInsert(-1, null, review_uri, contentValues);

            }

            if (!TextUtils.isEmpty(movie_id)) {

                //startUpdate (int token, Object cookie, Uri uri, ContentValues values, String selection, String[] selectionArgs)
                Log.i("DetailFragment", "Reviews Saved");
                ContentValues values = new ContentValues();
                values.put(MoviesContract.Movies.MOVIE_REVIEW_SAVED, 1);
                AsyncQueryHandler handler = new AsyncQueryHandler(contentResolver) {
                };
                handler.startUpdate(-1, null, uri, values, "movie_title=?", new String[]{movie.getTitle()});
                //contentResolver.update(uri,
                //        values, "movie_title=?", new String[]{movie.getTitle()});
                movie.setReviewsaved(1);
            }

        }


        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
            mRequestPending = false;
            mError = true;
            review_cardView.setVisibility(View.GONE);
        }

        @Override
        public void onComplete() {
            mRequestPending = false;
        }
    }


    private class VideoListSubscriber extends DisposableObserver<ArrayList<Videos>> {

        @Override
        public void onNext(ArrayList<Videos> videos) {
            trailers_cardView.setVisibility(View.VISIBLE);
            mRequestPendingV = false;
            mVideos = videos;

            firstTrailer = "http://www.youtube.com/watch?v=" + videos.get(0).getKey();
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


                if (!TextUtils.isEmpty(movie_id)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MoviesContract.Video.MOVIE_ID, movie_id);
                    contentValues.put(MoviesContract.Video.TRAILER_ID, video.getId());
                    contentValues.put(MoviesContract.Video.TRAILER_ISO, video.getIso_639_1());
                    contentValues.put(MoviesContract.Video.TRAILER_KEY, video.getKey());
                    contentValues.put(MoviesContract.Video.TRAILER_NAME, video.getName());
                    contentValues.put(MoviesContract.Video.TRAILER_SITE, video.getSite());
                    contentValues.put(MoviesContract.Video.TRAILER_TYPE, video.getType());
                    //contentResolver.insert(trailer_uri, contentValues);


                    AsyncQueryHandler handler = new AsyncQueryHandler(contentResolver) {
                    };
                    handler.startInsert(-1, null, trailer_uri, contentValues);
                }

            }
            if (!TextUtils.isEmpty(movie_id)) {

                Log.i("DetailFragment", "Videos Saved");
                ContentValues values = new ContentValues();
                values.put(MoviesContract.Movies.MOVIE_TRAILERS_SAVED, 1);
                contentResolver.update(uri,
                        values, "movie_title=?", new String[]{movie.getTitle()});
                movie.setTrailersaved(1);
            }

        }

        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
            mRequestPendingV = false;
            cont.setVisibility(View.GONE);

        }

        @Override
        public void onComplete() {
            mRequestPendingV = false;
        }

    }

    public void playVideo(Videos video) {
        if (video.getSite().equals(Videos.SITE_YOUTUBE))
            getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey())));
        else
            Log.i("DetailFragment", "Cannot play video format");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu, menu);

        mMenuItemShare = menu.findItem(R.id.menu_item_share);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:
               if(!TextUtils.isEmpty(firstTrailer)) {
                   Intent sendIntent = new Intent();
                   sendIntent.setAction(Intent.ACTION_SEND);
                   sendIntent.putExtra(Intent.EXTRA_TEXT, firstTrailer);
                   sendIntent.setType("text/plain");
                   // Verify the original intent will resolve to at least one activity
                   if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                       getActivity().startActivity(sendIntent);
                   }

               }else
               {
                   Toast.makeText(getActivity().getApplication(),"No trailers to share",Toast.LENGTH_SHORT).show();
               }
                return true;
        }
        return false;
    }

    public Reviews getReviewFromCursor(Cursor cursor) {
        Reviews review = new Reviews();
        review.setId(cursor.getString(cursor.getColumnIndex(MoviesContract.Review.REVIEW_ID)));
        review.setAuthor(cursor.getString(cursor.getColumnIndex(MoviesContract.Review.REVIEW_AUTHOR)));
        review.setContent(cursor.getString(cursor.getColumnIndex(MoviesContract.Review.REVIEW_CONTENT)));
        review.setUrl(cursor.getString(cursor.getColumnIndex(MoviesContract.Review.REVIEW_URL)));
        return review;
    }


    public Videos getVideoFromCursor(Cursor cursor) {
        Videos video = new Videos();
        video.setId(cursor.getString(cursor.getColumnIndex(MoviesContract.Video.TRAILER_ID)));
        video.setIso_639_1(cursor.getString(cursor.getColumnIndex(MoviesContract.Video.TRAILER_ISO)));
        video.setKey(cursor.getString(cursor.getColumnIndex(MoviesContract.Video.TRAILER_KEY)));
        video.setName(cursor.getString(cursor.getColumnIndex(MoviesContract.Video.TRAILER_NAME)));
        video.setSite(cursor.getString(cursor.getColumnIndex(MoviesContract.Video.TRAILER_SITE)));
        video.setType(cursor.getString(cursor.getColumnIndex(MoviesContract.Video.TRAILER_TYPE)));

        return video;
    }


}
