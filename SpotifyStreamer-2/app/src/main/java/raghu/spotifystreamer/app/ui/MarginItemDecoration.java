package raghu.spotifystreamer.app.ui;

/**
 * Created by Raghunandan on 17-11-2015.
 */
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import raghu.spotifystreamer.app.R;

public class MarginItemDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public MarginItemDecoration(Context context) {
        margin = context.getResources().getDimensionPixelSize(R.dimen.spacing);
    }

    @Override
    public void getItemOffsets(
            Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(margin, margin, margin, margin);
    }
}