package com.mtgtech.weather_forecast.utils;

import static com.mtgtech.weather_forecast.main.MainActivity.MANAGE_ACTIVITY;
import static com.mtgtech.weather_forecast.utils.manager.AdsUtils.currentTime;
import static com.mtgtech.weather_forecast.view.fragment.HomeFragment.TIME_LOAD_INTERS;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.preference.CheckBoxPreference;

import com.common.control.interfaces.AdCallback;
import com.common.control.manager.AdmobManager;

import com.mtgtech.weather_forecast.AdCache;
import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.background.polling.PollingManager;
import com.mtgtech.weather_forecast.databinding.ActivityMainBinding;
import com.mtgtech.weather_forecast.main.MainActivity;
import com.mtgtech.weather_forecast.remoteviews.presenter.notification.NormalNotificationIMP;
import com.mtgtech.weather_forecast.settings.SettingsOptionManager;
import com.mtgtech.weather_forecast.settings.fragment.AbstractSettingsFragment;
import com.mtgtech.weather_forecast.utils.helpter.IntentHelper;

public class NavigationView extends AbstractSettingsFragment {


    public void setUp(MainActivity mainActivity, ActivityMainBinding binding) {

        SettingsOptionManager settingsOptionManager = SettingsOptionManager.getInstance(mainActivity);
        settingsOptionManager.setNotificationStyle("geometric");
        settingsOptionManager.setNotificationCustomColorEnabled(true);
        settingsOptionManager.setNotificationBackgroundColor(Color.CYAN);
        settingsOptionManager.setNotificationCanBeClearedEnabled(true);

        settingsOptionManager.setNotificationMinimalIconEnabled(true);

        clickListeners(mainActivity, binding);
//
//        binding.navLayout.switchNavAlertNotification.setChecked(settingsOptionManager.isNotificationEnabled());
        binding.navLayout.switchNavPrecipitation.setChecked(settingsOptionManager.isNotificationEnabled());
        binding.navLayout.isLockScreen.setChecked(!settingsOptionManager.isNotificationHideInLockScreenEnabled());
        binding.navLayout.changeTermType.setChecked(!settingsOptionManager.isTermChange());
//        binding.navLayout.switchShowNightInfo.setChecked(settingsOptionManager.isShowNightInfoEnabled());
        binding.navLayout.switchWeatherBackground.setChecked(settingsOptionManager.isWeatherBgEnabled());

        changeListeners(mainActivity, binding, settingsOptionManager);


    }

    private void clickListeners(MainActivity mainActivity, com.mtgtech.weather_forecast.databinding.ActivityMainBinding binding) {
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
        if (System.currentTimeMillis() - currentTime >= TIME_LOAD_INTERS) {
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

    private void changeListeners(MainActivity mainActivity, com.mtgtech.weather_forecast.databinding.ActivityMainBinding binding, SettingsOptionManager settingsOptionManager) {
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
            if (buttonView.isPressed()) {
                settingsOptionManager.setNotificationHideInLockScreenEnabled(!isChecked);
                PollingManager.resetNormalBackgroundTask(mainActivity, isChecked);
            }
        });
        binding.navLayout.changeTermType.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
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
//                        mainActivity, mainActivity.mainActivity.getString(R.string.feedback_refresh_ui_after_refresh));
            }
        }));
    }

    private void initNotificationPart(MainActivity mainActivity, SettingsOptionManager settingsOptionManager) {
        // notification enabled.
        findPreference(mainActivity.getString(R.string.key_notification)).setOnPreferenceChangeListener((preference, newValue) -> {
            boolean enabled = (boolean) newValue;
            getSettingsOptionManager().setNotificationEnabled(enabled);
            initNotificationPart(mainActivity, getSettingsOptionManager());
            if (enabled) { // open notification.
                PollingManager.resetNormalBackgroundTask(mainActivity, true);
            } else { // close notification.
                NormalNotificationIMP.cancelNotification(mainActivity);
                PollingManager.resetNormalBackgroundTask(mainActivity, false);
            }
            return true;
        });

        // notification style.
//        ListPreference notificationStyle = findPreference(mainActivity.getString(R.string.key_notification_style));
//        notificationStyle.setSummary(
//                getSettingsOptionManager().getNotificationStyle().getNotificationStyleName(mainActivity)
//        );
//        notificationStyle.setOnPreferenceChangeListener((preference, newValue) -> {
//            getSettingsOptionManager().setNotificationStyle((String) newValue);
//            initNotificationPart(mainActivity, getSettingsOptionManager());
//            preference.setSummary(
//                    getSettingsOptionManager().getNotificationStyle().getNotificationStyleName(mainActivity)
//            );
//            PollingManager.resetNormalBackgroundTask(mainActivity, true);
//            return true;
//        });

        // notification minimal icon.
//        CheckBoxPreference notificationMinimalIcon = findPreference(mainActivity.getString(R.string.key_notification_minimal_icon));
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//            notificationMinimalIcon.setVisible(false);
//        }
//        notificationMinimalIcon.setOnPreferenceChangeListener((preference, newValue) -> {
//            getSettingsOptionManager().setNotificationMinimalIconEnabled((Boolean) newValue);
//            PollingManager.resetNormalBackgroundTask(mainActivity, true);
//            return true;
//        });
//
//        // notification temp icon.
//        CheckBoxPreference notificationTempIcon = findPreference(mainActivity.getString(R.string.key_notification_temp_icon));
//        notificationTempIcon.setOnPreferenceChangeListener((preference, newValue) -> {
//            getSettingsOptionManager().setNotificationTemperatureIconEnabled((Boolean) newValue);
//            PollingManager.resetNormalBackgroundTask(mainActivity, true);
//            return true;
//        });
//
//        // notification color.
//        Preference notificationColor = findPreference(mainActivity.getString(R.string.key_notification_color));
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//            notificationColor.setVisible(false);
//        }
//        notificationColor.setOnPreferenceClickListener(preference -> {
//            pushFragment(new NotificationColorSettingsFragment(), preference.getKey());
//            return true;
//        });
//
//        // notification can be cleared.
//        CheckBoxPreference notificationClearFlag = findPreference(mainActivity.getString(R.string.key_notification_can_be_cleared));
//        notificationClearFlag.setOnPreferenceChangeListener((preference, newValue) -> {
//            getSettingsOptionManager().setNotificationCanBeClearedEnabled((Boolean) newValue);
//            PollingManager.resetNormalBackgroundTask(mainActivity, true);
//            return true;
//        });
//
//        // notification hide icon.
//        CheckBoxPreference hideNotificationIcon = findPreference(mainActivity.getString(R.string.key_notification_hide_icon));
//        hideNotificationIcon.setOnPreferenceChangeListener((preference, newValue) -> {
//            getSettingsOptionManager().setNotificationHideIconEnabled((Boolean) newValue);
//            PollingManager.resetNormalBackgroundTask(mainActivity, true);
//            return true;
//        });

        // notification hide in lock screen.
        CheckBoxPreference hideNotificationInLockScreen = findPreference(mainActivity.getString(R.string.key_notification_hide_in_lockScreen));
        hideNotificationInLockScreen.setOnPreferenceChangeListener((preference, newValue) -> {
            getSettingsOptionManager().setNotificationHideInLockScreenEnabled((Boolean) newValue);
            PollingManager.resetNormalBackgroundTask(mainActivity, true);
            return true;
        });

        // notification hide big view.
//        CheckBoxPreference notificationHideBigView = findPreference(mainActivity.getString(R.string.key_notification_hide_big_view));
//        notificationHideBigView.setOnPreferenceChangeListener((preference, newValue) -> {
//            getSettingsOptionManager().setNotificationHideBigViewEnabled((Boolean) newValue);
//            PollingManager.resetNormalBackgroundTask(mainActivity, true);
//            return true;
//        });


//        notificationHideBigView.setEnabled(sendNotification && !nativeNotification);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }
}
