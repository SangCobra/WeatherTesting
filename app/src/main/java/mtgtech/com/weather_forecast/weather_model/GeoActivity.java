package mtgtech.com.weather_forecast.weather_model;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.view.View;

import mtgtech.com.weather_forecast.WeatherFlow;
import mtgtech.com.weather_forecast.main.dialog.DialogPer1;
import mtgtech.com.weather_forecast.main.dialog.DialogPer2;
import mtgtech.com.weather_forecast.settings.SettingsOptionManager;
import mtgtech.com.weather_forecast.utils.DisplayUtils;
import mtgtech.com.weather_forecast.utils.LanguageUtils;

/**
 * Geometric weather activity.
 * */

public abstract class GeoActivity extends AppCompatActivity {

    private boolean foreground;

    @Nullable private OnRequestPermissionsResultListener permissionsListener;
    private boolean denyPermission = false;

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WeatherFlow.getInstance().addActivity(this);

        LanguageUtils.setLanguage(
                this,
                SettingsOptionManager.getInstance(this).getLanguage().getLocale()
        );

        boolean darkMode = DisplayUtils.isDarkMode(this);
        DisplayUtils.setSystemBarStyle(this, getWindow(),
                false, false, true, !darkMode);
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        foreground = true;
    }

    @CallSuper
    @Override
    protected void onPause() {
        super.onPause();
        foreground = false;
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        WeatherFlow.getInstance().removeActivity(this);
    }

    public abstract View getSnackBarContainer();

    public boolean isForeground() {
        return foreground;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermissions(@NonNull String[] permissions, int requestCode,
                                   @Nullable OnRequestPermissionsResultListener l) {
        permissionsListener = l;
        requestPermissions(permissions, requestCode);


    }

    public void gotoSettings(GeoActivity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permission, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permission, grantResult);
        for (int j : grantResult) {
            if (j != PackageManager.PERMISSION_GRANTED && requestCode == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    DialogPer1.start(this);
                    DialogPer1.listener = () -> {
                        gotoSettings(this);
                    };
                } else {
                    DialogPer2.start(this);
                    DialogPer2.listener = () -> {
                        gotoSettings(this);
                    };
                }
                break;
            }

        }
        if (permissionsListener != null) {
            permissionsListener.onRequestPermissionsResult(requestCode, permission, grantResult);
            permissionsListener = null;
        }
    }

    public interface OnRequestPermissionsResultListener {
        void onRequestPermissionsResult(int requestCode,
                                        @NonNull String[] permission, @NonNull int[] grantResult);
    }
}
