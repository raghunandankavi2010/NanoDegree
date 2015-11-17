package raghu.spotifystreamer.app.ui;

/**
 * Created by Raghunandan on 15-11-2015.
 */
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mikepenz on 16.04.15.
 */
public class RippleForegroundListener implements View.OnTouchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Convert to card view coordinates. Assumes the host view is
        // a direct child and the card view is not scrollable.
        float x = event.getX() + v.getLeft();
        float y = event.getY() + v.getTop();

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            // Simulate motion on the card view.
            v.drawableHotspotChanged(x, y);
        }

        // Simulate pressed state on the card view.
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                v.setPressed(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                v.setPressed(false);
                break;
        }

        // Pass all events through to the host view.
        return false;
    }
}