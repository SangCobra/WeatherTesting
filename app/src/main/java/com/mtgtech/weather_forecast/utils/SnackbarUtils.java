package com.mtgtech.weather_forecast.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.weather_model.GeoActivity;

/**
 * Notification utils.
 */

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
