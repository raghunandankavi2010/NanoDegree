package raghu.spotifystreamer.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Raghunandan on 18-11-2015.
 */
public class ExampleScrollView extends ScrollView {

    private OnScrollViewListener mOnScrollViewListener;

    public void setOnScrollViewListener(OnScrollViewListener l) {
        this.mOnScrollViewListener = l;
    }

    public interface OnScrollViewListener {
        void onScrollChanged( ExampleScrollView v, int l, int t, int oldl, int oldt );
    }

    public ExampleScrollView(Context context) {
        super(context);
    }

    public ExampleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExampleScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        mOnScrollViewListener.onScrollChanged( this, l, t, oldl, oldt );
        super.onScrollChanged( l, t, oldl, oldt );
    }
}
