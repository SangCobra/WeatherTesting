package anaxxes.com.weatherFlow.utils;

import android.widget.LinearLayout;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.background.polling.PollingManager;
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

        DisplayUtils.disableEditText(binding.navLayout.etNavTempUnit);
        DisplayUtils.disableEditText(binding.navLayout.etNavPressureUnit);
        DisplayUtils.disableEditText(binding.navLayout.etNavDistanceUnit);
        DisplayUtils.disableEditText(binding.navLayout.etNavSpeedUnit);
        DisplayUtils.disableEditText(binding.navLayout.etNavPrecipUnit);
        DisplayUtils.disableEditText(binding.navLayout.etNavRefreshRate);


        binding.navLayout.etNavTempUnit.setText(settingsOptionManager.getTemperatureUnit().getAbbreviation(mainActivity));
        binding.navLayout.etNavPressureUnit.setText(settingsOptionManager.getPressureUnit().getAbbreviation(mainActivity));
        binding.navLayout.etNavDistanceUnit.setText(settingsOptionManager.getDistanceUnit().getAbbreviation(mainActivity));
        binding.navLayout.etNavSpeedUnit.setText(settingsOptionManager.getSpeedUnit().getAbbreviation(mainActivity));
        binding.navLayout.etNavPrecipUnit.setText(settingsOptionManager.getPrecipitationUnit().getAbbreviation(mainActivity));
        binding.navLayout.etNavRefreshRate.setText(settingsOptionManager.getUpdateInterval().getUpdateIntervalName(mainActivity));

        clickListeners(mainActivity, binding);

        binding.navLayout.switchNavAlertNotification.setChecked(settingsOptionManager.isNotificationEnabled());
        binding.navLayout.switchNavPrecipitation.setChecked(settingsOptionManager.isPrecipitationPushEnabled());

        binding.navLayout.switchShowNightInfo.setChecked(settingsOptionManager.isShowNightInfoEnabled());
        binding.navLayout.switchWeatherBackground.setChecked(settingsOptionManager.isWeatherBgEnabled());


        changeListeners(mainActivity,binding, settingsOptionManager);


    }

    private void clickListeners(MainActivity mainActivity, anaxxes.com.weatherFlow.databinding.ActivityMainBinding binding) {
        binding.navLayout.llNavTemp.setOnClickListener(view -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.showTempUnitDialog();
        });


        binding.navLayout.llNavPressure.setOnClickListener(view -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.showPressureUnitDialog();

        });


        binding.navLayout.llNavPrecip.setOnClickListener(view -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.showPrecipUnitDialog();
        });


        binding.navLayout.llNavDistance.setOnClickListener(view -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.showDistanceUnitDialog();
        });


        binding.navLayout.llNavSpeedUnit.setOnClickListener(view -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.showSpeedUnitDialog();
        });


        binding.navLayout.tvEditLocation.setOnClickListener(view -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            IntentHelper.startManageActivityForResult(mainActivity, MANAGE_ACTIVITY);
        });

        binding.navLayout.tvNavCurrentLocation.setOnClickListener(view -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.resetNavLocation(mainActivity.currentLocationFormattedId);
        });

        binding.navLayout.llNavRefreshRate.setOnClickListener(view -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.showRefreshRateDialog();
        });

        binding.navLayout.tvShareWithFriends.setOnClickListener(view -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            mainActivity.shareToFriend();
        });
    }

    private void changeListeners(MainActivity mainActivity,anaxxes.com.weatherFlow.databinding.ActivityMainBinding binding, SettingsOptionManager settingsOptionManager) {
        binding.navLayout.switchNavAlertNotification.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setNotificationEnabled(b);
                if (b) { // open notification.
                    PollingManager.resetNormalBackgroundTask(mainActivity, true);
                } else { // close notification.
                    NormalNotificationIMP.cancelNotification(mainActivity);
                    PollingManager.resetNormalBackgroundTask(mainActivity, false);
                }
            }
        }));


        binding.navLayout.switchNavPrecipitation.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setPrecipitationPushEnabled(b);
            }
        }));

        binding.navLayout.switchShowNightInfo.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setShowNightInfoEnabled(b);
                mainActivity.redrawAfterNightInfo();
            }
        }));

        binding.navLayout.switchWeatherBackground.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setWeatherBgEnabled(b);
                SnackbarUtils.showSnackbar(
                        mainActivity, mainActivity.getString(R.string.feedback_refresh_ui_after_refresh));
            }
        }));
    }

}
