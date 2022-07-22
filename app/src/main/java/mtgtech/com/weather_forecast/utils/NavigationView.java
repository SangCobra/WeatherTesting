package mtgtech.com.weather_forecast.utils;

import androidx.core.view.GravityCompat;

import mtgtech.com.weather_forecast.AdCache;
import mtgtech.com.weather_forecast.background.polling.PollingManager;
import mtgtech.com.weather_forecast.databinding.ActivityMainBinding;
import mtgtech.com.weather_forecast.main.MainActivity;
import mtgtech.com.weather_forecast.remoteviews.presenter.notification.ForecastNotificationIMP;
import mtgtech.com.weather_forecast.remoteviews.presenter.notification.NormalNotificationIMP;
import mtgtech.com.weather_forecast.settings.SettingsOptionManager;
import mtgtech.com.weather_forecast.settings.activity.MySettingsActivity;
import mtgtech.com.weather_forecast.utils.helpter.IntentHelper;

import static mtgtech.com.weather_forecast.main.MainActivity.MANAGE_ACTIVITY;
import static mtgtech.com.weather_forecast.utils.manager.AdsUtils.currentTime;
import static mtgtech.com.weather_forecast.view.fragment.HomeFragment.TIME_LOAD_INTERS;

import android.app.Activity;

import com.common.control.interfaces.AdCallback;
import com.common.control.manager.AdmobManager;

public class NavigationView {



    public void setUp(MainActivity mainActivity, ActivityMainBinding binding) {


        SettingsOptionManager settingsOptionManager = SettingsOptionManager.getInstance(mainActivity);


        clickListeners(mainActivity, binding);
//
//        binding.navLayout.switchNavAlertNotification.setChecked(settingsOptionManager.isNotificationEnabled());
        binding.navLayout.switchNavPrecipitation.setChecked(settingsOptionManager.isPrecipitationPushEnabled());
        binding.navLayout.isLockScreen.setChecked(settingsOptionManager.isNotificationHideInLockScreenEnabled());
        binding.navLayout.changeTermType.setChecked(!settingsOptionManager.isTermChange());
//        binding.navLayout.switchShowNightInfo.setChecked(settingsOptionManager.isShowNightInfoEnabled());
        binding.navLayout.switchWeatherBackground.setChecked(settingsOptionManager.isWeatherBgEnabled());


        changeListeners(mainActivity,binding, settingsOptionManager);


    }

    private void clickListeners(MainActivity mainActivity, mtgtech.com.weather_forecast.databinding.ActivityMainBinding binding) {
        binding.navLayout.navSetting.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.showDialogSettingUnit();
        });
        binding.navLayout.weatherRadar.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.goToMap();
            showIntersAd(mainActivity);
        });
        binding.navLayout.navShare.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.share();
        });
        binding.navLayout.navRate.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.rate(false);
        });
        binding.navLayout.navFeedback.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.feedBack("verifiedapps.help@gmail.com");
        });
        binding.navLayout.navPrivacy.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.privacy();
        });

//        binding.navLayout.navSetting..llNavTemp.setOnClickListener(view -> {
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//            mainActivity.showTempUnitDialog();
//        });
//
//
//        binding.navLayout.llNavPressure.setOnClickListener(view -> {
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//            mainActivity.showPressureUnitDialog();
//
//        });
//
//
//        binding.navLayout.llNavPrecip.setOnClickListener(view -> {
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//            mainActivity.showPrecipUnitDialog();
//        });
//
//
//        binding.navLayout.llNavDistance.setOnClickListener(view -> {
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//            mainActivity.showDistanceUnitDialog();
//        });
//
//
//        binding.navLayout.llNavSpeedUnit.setOnClickListener(view -> {
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//            mainActivity.showSpeedUnitDialog();
//        });
//

        binding.navLayout.tvEditLocation.setOnClickListener(view -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            IntentHelper.startManageActivityForResult(mainActivity, MANAGE_ACTIVITY);
            showIntersAd(mainActivity);
        });

//        binding.navLayout.tvNavCurrentLocation.setOnClickListener(view -> {
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//            mainActivity.resetNavLocation(mainActivity.currentLocationFormattedId);
//        });
//
//        binding.navLayout.llNavRefreshRate.setOnClickListener(view -> {
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//            mainActivity.showRefreshRateDialog();
//        });
//
//        binding.navLayout.tvShareWithFriends.setOnClickListener(view -> {
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//            mainActivity.shareToFriend();
//        });
    }

    private void showIntersAd(Activity activity) {
        if (System.currentTimeMillis() - currentTime >= TIME_LOAD_INTERS){
            AdmobManager.getInstance().showInterstitial(activity, AdCache.getInstance().getInterstitialAd(), new AdCallback() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    AdCache.getInstance().setInterstitialAd(null);
                    currentTime = System.currentTimeMillis();
                }
            });
        }
    }

    private void changeListeners(MainActivity mainActivity, mtgtech.com.weather_forecast.databinding.ActivityMainBinding binding, SettingsOptionManager settingsOptionManager) {
//        binding.navLayout.switchNavAlertNotification.setOnCheckedChangeListener(((compoundButton, b) -> {
//            if (compoundButton.isPressed()) {
//                settingsOptionManager.setNotificationEnabled(b);
//                if (b) { // open notification.
//                    PollingManager.resetNormalBackgroundTask(mainActivity, true);
//                } else { // close notification.
//                    NormalNotificationIMP.cancelNotification(mainActivity);
//                    PollingManager.resetNormalBackgroundTask(mainActivity, false);
//                }
//            }
//        }));


        binding.navLayout.switchNavPrecipitation.setOnCheckedChangeListener(((compoundButton, b) -> {
//            if (compoundButton.isPressed()) {
//                settingsOptionManager.setPrecipitationPushEnabled(b);
//            }
            if (compoundButton.isPressed()) {
                settingsOptionManager.setPrecipitationPushEnabled(b);
                if (b) { // open notification.
                    PollingManager.resetNormalBackgroundTask(mainActivity, true);
                } else { // close notification.
                    NormalNotificationIMP.cancelNotification(mainActivity);
                    PollingManager.resetNormalBackgroundTask(mainActivity, false);
                }
//                toggleNotificationItems(binding.llNotification);
            }
        }));
        binding.navLayout.isLockScreen.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isPressed()){
                settingsOptionManager.setNotificationHideInLockScreenEnabled(isChecked);
                PollingManager.resetNormalBackgroundTask(mainActivity, true);
            }
        });
        binding.navLayout.changeTermType.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()){
                settingsOptionManager.setTermChange(!isChecked);
                mainActivity.onRefresh();
            }
        });
//        binding.navLayout.switchShowNightInfo.setOnCheckedChangeListener(((compoundButton, b) -> {
//            if (compoundButton.isPressed()) {
//                settingsOptionManager.setShowNightInfoEnabled(b);
//                mainActivity.redrawAfterNightInfo();
//            }
//        }));

        binding.navLayout.switchWeatherBackground.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setWeatherBgEnabled(b);
                mainActivity.onRefresh();
//                SnackbarUtils.showSnackbar(
//                        mainActivity, mainActivity.getString(R.string.feedback_refresh_ui_after_refresh));
            }
        }));
    }

}
