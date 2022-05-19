package anaxxes.com.weatherFlow.utils.helpter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import android.view.View;

import java.util.ArrayList;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.background.polling.basic.AwakeForegroundUpdateService;
import anaxxes.com.weatherFlow.basic.GeoActivity;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.basic.model.weather.Weather;
import anaxxes.com.weatherFlow.daily.DailyWeatherActivity;
import anaxxes.com.weatherFlow.main.HourlyListActivity;
import anaxxes.com.weatherFlow.settings.activity.CardDisplayManageActivity;
import anaxxes.com.weatherFlow.settings.activity.DailyTrendDisplayManageActivity;
import anaxxes.com.weatherFlow.settings.activity.MySettingsActivity;
import anaxxes.com.weatherFlow.ui.activity.AllergenActivity;
import anaxxes.com.weatherFlow.ui.activity.DailyListActivity;
import anaxxes.com.weatherFlow.wallpaper.material.MaterialLiveWallpaperService;
import anaxxes.com.weatherFlow.settings.activity.AboutActivity;
import anaxxes.com.weatherFlow.ui.activity.AlertActivity;
import anaxxes.com.weatherFlow.settings.activity.SelectProviderActivity;
import anaxxes.com.weatherFlow.main.MainActivity;
import anaxxes.com.weatherFlow.ui.activity.ManageActivity;
import anaxxes.com.weatherFlow.settings.activity.PreviewIconActivity;
import anaxxes.com.weatherFlow.ui.activity.SearchActivity;
import anaxxes.com.weatherFlow.settings.activity.SettingsActivity;
import anaxxes.com.weatherFlow.utils.SnackbarUtils;

/**
 * Intent helper.
 * */

public class IntentHelper {

    public static void startMainActivity(Context context) {
        context.startActivity(
                new Intent(MainActivity.ACTION_MAIN)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        );
    }

    public static Intent buildMainActivityIntent(@Nullable Location location) {
        String formattedId = "";
        if (location != null) {
            formattedId = location.getFormattedId();
        }

        return new Intent(MainActivity.ACTION_MAIN)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra(MainActivity.KEY_MAIN_ACTIVITY_LOCATION_FORMATTED_ID, formattedId);
    }

    public static Intent buildMainActivityShowAlertsIntent(@Nullable Location location) {
        String formattedId = "";
        if (location != null) {
            formattedId = location.getFormattedId();
        }

        return new Intent(MainActivity.ACTION_SHOW_ALERTS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra(MainActivity.KEY_MAIN_ACTIVITY_LOCATION_FORMATTED_ID, formattedId);
    }

    public static Intent buildMainActivityShowDailyForecastIntent(@Nullable Location location,
                                                                  int index) {
        String formattedId = "";
        if (location != null) {
            formattedId = location.getFormattedId();
        }

        return new Intent(MainActivity.ACTION_SHOW_DAILY_FORECAST)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra(MainActivity.KEY_MAIN_ACTIVITY_LOCATION_FORMATTED_ID, formattedId)
                .putExtra(MainActivity.KEY_DAILY_INDEX, index);
    }

    public static Intent buildAwakeUpdateActivityIntent() {
        return new Intent("com.wangdaye.geometricweather.UPDATE")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static void startDailyWeatherActivity(Activity activity, String formattedId, int index) {
        Intent intent = new Intent(activity, DailyWeatherActivity.class);
        intent.putExtra(DailyWeatherActivity.KEY_FORMATTED_LOCATION_ID, formattedId);
        intent.putExtra(DailyWeatherActivity.KEY_CURRENT_DAILY_INDEX, index);
        activity.startActivity(intent);
    }

    public static void startDailyListActivity(Activity activity, String formattedId, int index) {
        Intent intent = new Intent(activity, DailyListActivity.class);
        intent.putExtra(DailyWeatherActivity.KEY_FORMATTED_LOCATION_ID, formattedId);
        intent.putExtra(DailyWeatherActivity.KEY_CURRENT_DAILY_INDEX, index);
        activity.startActivity(intent);
    }

    public static void startHourlyWeatherActivity(Activity activity, String formattedId, int index) {
        Intent intent = new Intent(activity, HourlyListActivity.class);
        intent.putExtra(DailyWeatherActivity.KEY_FORMATTED_LOCATION_ID, formattedId);
        intent.putExtra(DailyWeatherActivity.KEY_CURRENT_DAILY_INDEX, index);
        activity.startActivity(intent);
    }

    public static void startAlertActivity(Activity activity, Weather weather) {
        Intent intent = new Intent(activity, AlertActivity.class);
        intent.putParcelableArrayListExtra(
                AlertActivity.KEY_ALERT_ACTIVITY_ALERT_LIST,
                (ArrayList<? extends Parcelable>) weather.getAlertList()
        );
        activity.startActivity(intent);
    }

    public static void startAllergenActivity(Activity activity, Location location) {
        Intent intent = new Intent(activity, AllergenActivity.class);
        intent.putExtra(
                AllergenActivity.KEY_ALLERGEN_ACTIVITY_LOCATION_FORMATTED_ID,
                location.getFormattedId()
        );
        activity.startActivity(intent);
    }

    public static void startManageActivityForResult(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, ManageActivity.class), requestCode);
    }

    public static void startSearchActivityForResult(Activity activity, View bar, int requestCode) {
        Intent intent = new Intent(activity, SearchActivity.class);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivityForResult(intent, requestCode);
            activity.overridePendingTransition(R.anim.activity_search_in, 0);
        } else {
            ActivityCompat.startActivityForResult(
                    activity,
                    intent,
                    requestCode,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                            activity,
                            Pair.create(bar, activity.getString(R.string.transition_activity_search_bar))
                    ).toBundle()
            );
        }
    }

    public static void startSettingsActivityForResult(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, SettingsActivity.class), requestCode);
    }
    public static void startMySettingsActivityForResult(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, MySettingsActivity.class), requestCode);
    }

    public static void startCardDisplayManageActivityForResult(Activity activity, int requestCode) {
        activity.startActivityForResult(
                new Intent(activity, CardDisplayManageActivity.class), requestCode);
    }

    public static void startDailyTrendDisplayManageActivityForResult(Activity activity, int requestCode) {
        activity.startActivityForResult(
                new Intent(activity, DailyTrendDisplayManageActivity.class), requestCode);
    }

    public static void startSelectProviderActivity(Activity activity) {
        activity.startActivity(new Intent(activity, SelectProviderActivity.class));
    }

    public static void startSelectProviderActivityForResult(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, SelectProviderActivity.class), requestCode);
    }

    public static void startPreviewIconActivity(Activity activity, String packageName) {
        activity.startActivity(
                new Intent(activity, PreviewIconActivity.class).putExtra(
                        PreviewIconActivity.KEY_ICON_PREVIEW_ACTIVITY_PACKAGE_NAME,
                        packageName
                )
        );
    }

    public static void startAboutActivity(Activity activity) {
        activity.startActivity(new Intent(activity, AboutActivity.class));
    }

    public static void startApplicationDetailsActivity(Context context) {
        startApplicationDetailsActivity(context, context.getPackageName());
    }

    public static void startApplicationDetailsActivity(Context context, String pkgName) {
        context.startActivity(
                new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.fromParts("package", pkgName, null))
        );
    }

    public static void startLocationSettingsActivity(Context context) {
        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public static void startLiveWallpaperActivity(GeoActivity activity) {
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(activity, MaterialLiveWallpaperService.class)
        );
        if (isIntentAvailable(activity, intent)) {
            activity.startActivity(intent);
        } else {
            SnackbarUtils.showSnackbar(
                    activity,
                    activity.getString(R.string.feedback_cannot_start_live_wallpaper_activity)
            );
        }
    }

    public static void startAppStoreDetailsActivity(GeoActivity activity) {
        startAppStoreDetailsActivity(activity, activity.getPackageName());
    }

    public static void startAppStoreDetailsActivity(GeoActivity activity, String packageName) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + packageName)
        );
        if (isIntentAvailable(activity, intent)) {
            activity.startActivity(intent);
        } else {
            SnackbarUtils.showSnackbar(activity, "Unavailable AppStore.");
        }
    }

    public static void startAppStoreSearchActivity(GeoActivity activity, String query) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://search?q=" + query)
        );
        if (isIntentAvailable(activity, intent)) {
            activity.startActivity(intent);
        } else {
            SnackbarUtils.showSnackbar(activity, "Unavailable AppStore.");
        }
    }

    public static void startWebViewActivity(GeoActivity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (isIntentAvailable(activity, intent)) {
            activity.startActivity(intent);
        } else {
            SnackbarUtils.showSnackbar(activity, "Unavailable internet browser.");
        }
    }

    public static void startEmailActivity(GeoActivity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
        if (isIntentAvailable(activity, intent)) {
            activity.startActivity(intent);
        } else {
            SnackbarUtils.showSnackbar(activity, "Unavailable e-mail.");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("BatteryLife")
    public static void startBatteryOptimizationActivity(GeoActivity activity) {
        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        if (isIntentAvailable(activity, intent)) {
            activity.startActivity(intent);
        } else {
            SnackbarUtils.showSnackbar(activity, "Unavailable battery optimization activity.");
        }
    }

    public static void sendBackgroundUpdateBroadcast(Context context, Location location) {
        context.sendBroadcast(
                new Intent(MainActivity.ACTION_UPDATE_WEATHER_IN_BACKGROUND)
                        .putExtra(MainActivity.KEY_LOCATION_FORMATTED_ID, location.getFormattedId())
        );
    }

    public static void startAwakeForegroundUpdateService(Context context) {
        ContextCompat.startForegroundService(context, getAwakeForegroundUpdateServiceIntent(context));
    }

    public static Intent getAwakeForegroundUpdateServiceIntent(Context context) {
        return new Intent(context, AwakeForegroundUpdateService.class);
    }

    @SuppressLint("WrongConstant")
    private static boolean isIntentAvailable(Context context, Intent intent) {
        return context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.GET_ACTIVITIES)
                .size() > 0;
    }
}
