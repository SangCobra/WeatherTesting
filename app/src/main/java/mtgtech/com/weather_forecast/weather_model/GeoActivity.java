package mtgtech.com.weather_forecast.weather_model;

import static mtgtech.com.weather_forecast.main.MainActivity.isGotoSettings;
import static mtgtech.com.weather_forecast.main.MainActivity.isShowAds;
import static mtgtech.com.weather_forecast.main.MainActivity.isStartAgain;
import static mtgtech.com.weather_forecast.view.fragment.HomeFragment.TIME_LOAD_INTERS;

import android.Manifest;
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

import com.common.control.interfaces.AdCallback;
import com.common.control.interfaces.PermissionCallback;
import com.common.control.manager.AdmobManager;
import com.common.control.manager.AppOpenManager;
import com.common.control.utils.PermissionUtils;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.util.Locale;

import mtgtech.com.weather_forecast.AdCache;
import mtgtech.com.weather_forecast.BuildConfig;
import mtgtech.com.weather_forecast.WeatherFlow;
import mtgtech.com.weather_forecast.main.MainActivity;
import mtgtech.com.weather_forecast.main.dialog.DialogPer1;
import mtgtech.com.weather_forecast.main.dialog.DialogPer2;
import mtgtech.com.weather_forecast.settings.SettingsOptionManager;
import mtgtech.com.weather_forecast.utils.DisplayUtils;
import mtgtech.com.weather_forecast.utils.LanguageUtils;
import mtgtech.com.weather_forecast.utils.MyUtils;

/**
 * Geometric weather activity.
 * */

public abstract class GeoActivity extends AppCompatActivity {

    private boolean foreground;

    @Nullable private OnRequestPermissionsResultListener permissionsListener;
    private boolean denyPermission = false;
    public PermissionCallback permissionCallback;

    public PermissionCallback getPermissionCallback() {
        return permissionCallback;
    }

    public void setPermissionCallback(PermissionCallback permissionCallback) {
        this.permissionCallback = permissionCallback;
    }

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WeatherFlow.getInstance().addActivity(this);

        LanguageUtils.setLanguage(
                this,
                new Locale("en", "GB")
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
        isStartAgain = true;
        activity.startActivity(intent);
        AppOpenManager.getInstance().disableAppResume();
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permission, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permission, grantResult);
        MyUtils.requestCode = requestCode;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            for (int j : grantResult) {
                if (j != PackageManager.PERMISSION_GRANTED && requestCode == 1) {
                    DialogPer1.start(this);
                    DialogPer1.listener = () -> {
                        gotoSettings(this);
                    };
                    return;
                }

            }
            if (PermissionUtils.permissionGranted(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            )){
                isGotoSettings = true;
            }
        }
        else {
//            boolean isPermissionDeny = false;
//            for (int j : grantResult) {
//                if (j != PackageManager.PERMISSION_GRANTED) {
//                    isPermissionDeny = true;
//                    break;
//                }
//            }
//            if (isPermissionDeny){
//                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) && shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)){
//                    DialogPer2.start(this);
//                    DialogPer2.listener = () -> {
//                        gotoSettings(this);
//                    };
//                }
//                else {
//                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
//                }
//            }
            if (permissionCallback != null){
                if (PermissionUtils.permissionGranted(this, permission)){
//                    isGotoSettings = true;
                    permissionCallback.onPermissionGranted();
                }
                else {
                    permissionCallback.onPermissionDenied();
//                    onRequestPermissionsResult(requestCode, permission, grantResult);
                }
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
