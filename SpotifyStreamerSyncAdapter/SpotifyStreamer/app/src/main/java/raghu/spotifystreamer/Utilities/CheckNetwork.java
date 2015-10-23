package raghu.spotifystreamer.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Raghunandan on 23-09-2015.
 */

public class CheckNetwork {


    private static final String TAG = CheckNetwork.class.getSimpleName();


    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


}
