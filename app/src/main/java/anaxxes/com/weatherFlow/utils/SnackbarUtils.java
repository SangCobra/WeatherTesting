package anaxxes.com.weatherFlow.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import android.view.View;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.GeoActivity;

/**
 * Notification utils.
 * */

public class SnackbarUtils {

    public static void showSnackbar(@NonNull GeoActivity activity, String txt) {
        Snackbar.make(activity.getSnackBarContainer(), txt, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackbar(@NonNull GeoActivity activity, String txt, String action,
                                    @NonNull View.OnClickListener l) {
        showSnackbar(activity, txt, action, l, null);
    }

    public static void showSnackbar(@NonNull GeoActivity activity, String txt, String action,
                                    @NonNull View.OnClickListener l,
                                    @Nullable Snackbar.Callback callback) {
        if (callback == null) {
            callback = new Snackbar.Callback();
        }

        Snackbar.make(
                activity.getSnackBarContainer(),
                txt,
                Snackbar.LENGTH_LONG
        ).setAction(action, l)
                .setActionTextColor(ContextCompat.getColor(activity, R.color.colorTextAlert))
                .addCallback(callback)
                .show();
    }
}
