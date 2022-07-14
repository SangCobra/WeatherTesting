package anaxxes.com.weatherFlow.utils;

import android.widget.LinearLayout;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.background.polling.PollingManager;
import anaxxes.com.weatherFlow.basic.model.weather.Temperature;
import anaxxes.com.weatherFlow.databinding.ActivityMainBinding;
import anaxxes.com.weatherFlow.main.MainActivity;
import anaxxes.com.weatherFlow.remoteviews.presenter.notification.NormalNotificationIMP;
import anaxxes.com.weatherFlow.settings.SettingsOptionManager;
import anaxxes.com.weatherFlow.settings.activity.MySettingsActivity;
import anaxxes.com.weatherFlow.utils.helpter.IntentHelper;

import static anaxxes.com.weatherFlow.main.MainActivity.MANAGE_ACTIVITY;

public class NavigationView {



    public void setUp(MainActivity mainActivity, ActivityMainBinding binding) {


        SettingsOptionManager settingsOptionManager = SettingsOptionManager.getInstance(mainActivity);


        clickListeners(mainActivity, binding);
//
//        binding.navLayout.switchNavAlertNotification.setChecked(settingsOptionManager.isNotificationEnabled());
        binding.navLayout.switchNavPrecipitation.setChecked(settingsOptionManager.isNotificationEnabled());
        binding.navLayout.isLockScreen.setChecked(settingsOptionManager.isNotificationHideInLockScreenEnabled());
        binding.navLayout.changeTermType.setChecked(!settingsOptionManager.isTermChange());
//        binding.navLayout.switchShowNightInfo.setChecked(settingsOptionManager.isShowNightInfoEnabled());
        binding.navLayout.switchWeatherBackground.setChecked(settingsOptionManager.isWeatherBgEnabled());


        changeListeners(mainActivity,binding, settingsOptionManager);


    }

    private void clickListeners(MainActivity mainActivity, anaxxes.com.weatherFlow.databinding.ActivityMainBinding binding) {
        binding.navLayout.navSetting.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.showDialogSettingUnit();
        });
        binding.navLayout.weatherRadar.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.goToMap();
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

    private void changeListeners(MainActivity mainActivity,anaxxes.com.weatherFlow.databinding.ActivityMainBinding binding, SettingsOptionManager settingsOptionManager) {
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
                settingsOptionManager.setNotificationEnabled(b);
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
                settingsOptionManager.setNotificationHideInLockScreenEnabled(!isChecked);
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
                SnackbarUtils.showSnackbar(
                        mainActivity, mainActivity.getString(R.string.feedback_refresh_ui_after_refresh));
            }
        }));
    }

}
