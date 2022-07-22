package mtgtech.com.weather_forecast.utils;

import androidx.core.view.GravityCompat;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;

import mtgtech.com.weather_forecast.AdCache;
import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.background.polling.PollingManager;
import mtgtech.com.weather_forecast.databinding.ActivityMainBinding;
import mtgtech.com.weather_forecast.main.MainActivity;
import mtgtech.com.weather_forecast.remoteviews.presenter.notification.ForecastNotificationIMP;
import mtgtech.com.weather_forecast.remoteviews.presenter.notification.NormalNotificationIMP;
import mtgtech.com.weather_forecast.settings.SettingsOptionManager;
import mtgtech.com.weather_forecast.settings.activity.MySettingsActivity;
import mtgtech.com.weather_forecast.settings.fragment.NotificationColorSettingsFragment;
import mtgtech.com.weather_forecast.utils.helpter.IntentHelper;
import mtgtech.com.weather_forecast.weather_model.model.option.NotificationStyle;

import static mtgtech.com.weather_forecast.main.MainActivity.MANAGE_ACTIVITY;
import static mtgtech.com.weather_forecast.utils.manager.AdsUtils.currentTime;
import static mtgtech.com.weather_forecast.view.fragment.HomeFragment.TIME_LOAD_INTERS;

import android.app.Activity;
import android.os.Build;

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
//                        mainActivity, mainActivity.mainActivity.getString(R.string.feedback_refresh_ui_after_refresh));
            }
        }));
    }

    private void initNotificationPart(MainActivity mainActivity, SettingsOptionManager settingsOptionManager) {
        // notification enabled.
        findPreference(mainActivity.getString(R.string.key_notification)).setOnPreferenceChangeListener((preference, newValue) -> {
            boolean enabled = (boolean) newValue;
            settingsOptionManager.setNotificationEnabled(enabled);
            initNotificationPart(mainActivity, settingsOptionManager);
            if (enabled) { // open notification.
                PollingManager.resetNormalBackgroundTask(mainActivity, true);
            } else { // close notification.
                NormalNotificationIMP.cancelNotification(mainActivity);
                PollingManager.resetNormalBackgroundTask(mainActivity, false);
            }
            return true;
        });

        // notification style.
        ListPreference notificationStyle = findPreference(mainActivity.getString(R.string.key_notification_style));
        notificationStyle.setSummary(
                settingsOptionManager.getNotificationStyle().getNotificationStyleName(mainActivity)
        );
        notificationStyle.setOnPreferenceChangeListener((preference, newValue) -> {
            settingsOptionManager.setNotificationStyle((String) newValue);
            initNotificationPart(mainActivity, settingsOptionManager);
            preference.setSummary(
                    settingsOptionManager.getNotificationStyle().getNotificationStyleName(mainActivity)
            );
            PollingManager.resetNormalBackgroundTask(mainActivity, true);
            return true;
        });

        // notification minimal icon.
        CheckBoxPreference notificationMinimalIcon = findPreference(mainActivity.getString(R.string.key_notification_minimal_icon));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            notificationMinimalIcon.setVisible(false);
        }
        notificationMinimalIcon.setOnPreferenceChangeListener((preference, newValue) -> {
            settingsOptionManager.setNotificationMinimalIconEnabled((Boolean) newValue);
            PollingManager.resetNormalBackgroundTask(mainActivity, true);
            return true;
        });

        // notification temp icon.
        CheckBoxPreference notificationTempIcon = findPreference(mainActivity.mainActivity.getString(R.string.key_notification_temp_icon));
        notificationTempIcon.setOnPreferenceChangeListener((preference, newValue) -> {
            settingsOptionManager.setNotificationTemperatureIconEnabled((Boolean) newValue);
            PollingManager.resetNormalBackgroundTask(mainActivity, true);
            return true;
        });

        // notification color.
        Preference notificationColor = findPreference(mainActivity.getString(R.string.key_notification_color));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            notificationColor.setVisible(false);
        }
        notificationColor.setOnPreferenceClickListener(preference -> {
            pushFragment(new NotificationColorSettingsFragment(), preference.getKey());
            return true;
        });

        // notification can be cleared.
        CheckBoxPreference notificationClearFlag = findPreference(mainActivity.getString(R.string.key_notification_can_be_cleared));
        notificationClearFlag.setOnPreferenceChangeListener((preference, newValue) -> {
            settingsOptionManager.setNotificationCanBeClearedEnabled((Boolean) newValue);
            PollingManager.resetNormalBackgroundTask(mainActivity, true);
            return true;
        });

        // notification hide icon.
        CheckBoxPreference hideNotificationIcon = findPreference(mainActivity.getString(R.string.key_notification_hide_icon));
        hideNotificationIcon.setOnPreferenceChangeListener((preference, newValue) -> {
            settingsOptionManager.setNotificationHideIconEnabled((Boolean) newValue);
            PollingManager.resetNormalBackgroundTask(mainActivity, true);
            return true;
        });

        // notification hide in lock screen.
        CheckBoxPreference hideNotificationInLockScreen = findPreference(mainActivity.getString(R.string.key_notification_hide_in_lockScreen));
        hideNotificationInLockScreen.setOnPreferenceChangeListener((preference, newValue) -> {
            settingsOptionManager.setNotificationHideInLockScreenEnabled((Boolean) newValue);
            PollingManager.resetNormalBackgroundTask(mainActivity, true);
            return true;
        });

        // notification hide big view.
        CheckBoxPreference notificationHideBigView = findPreference(mainActivity.getString(R.string.key_notification_hide_big_view));
        notificationHideBigView.setOnPreferenceChangeListener((preference, newValue) -> {
            settingsOptionManager.setNotificationHideBigViewEnabled((Boolean) newValue);
            PollingManager.resetNormalBackgroundTask(mainActivity, true);
            return true;
        });

        boolean sendNotification = settingsOptionManager.isNotificationEnabled();
        boolean nativeNotification = settingsOptionManager.getNotificationStyle() == NotificationStyle.NATIVE;
        boolean androidL = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        notificationStyle.setEnabled(sendNotification);
        notificationMinimalIcon.setEnabled(sendNotification && !nativeNotification);
        notificationTempIcon.setEnabled(sendNotification);
        notificationColor.setEnabled(sendNotification && !nativeNotification);
        notificationClearFlag.setEnabled(sendNotification);
        hideNotificationIcon.setEnabled(sendNotification);
        hideNotificationInLockScreen.setEnabled(sendNotification && androidL);
        notificationHideBigView.setEnabled(sendNotification && !nativeNotification);
    }
}
