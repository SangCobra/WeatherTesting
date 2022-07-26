package com.mtgtech.weather_forecast.settings.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.WeatherFlow;
import com.mtgtech.weather_forecast.background.polling.PollingManager;
import com.mtgtech.weather_forecast.databinding.ActivityMySettingsBinding;
import com.mtgtech.weather_forecast.remoteviews.presenter.notification.NormalNotificationIMP;
import com.mtgtech.weather_forecast.settings.SettingsOptionManager;
import com.mtgtech.weather_forecast.settings.dialog.TimeSetterDialog;
import com.mtgtech.weather_forecast.utils.DisplayUtils;
import com.mtgtech.weather_forecast.weather_model.GeoActivity;

public class MySettingsActivity extends GeoActivity {


    private ActivityMySettingsBinding binding;
    private SettingsOptionManager settingsOptionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
    }

    @Override
    public View getSnackBarContainer() {
        return binding.getRoot();
    }

    private void initViews() {
        settingsOptionManager = SettingsOptionManager.getInstance(this);

        switch (settingsOptionManager.getDarkMode()) {
            case LIGHT:
                setThemePreview(false);
                break;

            case DARK:
                setThemePreview(true);

                break;
        }

        enableDisableViews();

        binding.switchSendPercip.setChecked(settingsOptionManager.isPrecipitationPushEnabled());
        binding.tvRefreshRateValue.setText(settingsOptionManager.getUpdateInterval().getUpdateIntervalName(this));
        binding.etTempUnit.setText(settingsOptionManager.getTemperatureUnit().getAbbreviation(this));
        binding.etPressureUnit.setText(settingsOptionManager.getPressureUnit().getAbbreviation(this));
        binding.etDistanceUnit.setText(settingsOptionManager.getDistanceUnit().getAbbreviation(this));
        binding.etSpeedUnit.setText(settingsOptionManager.getSpeedUnit().getAbbreviation(this));
        binding.etPrecipUnit.setText(settingsOptionManager.getPrecipitationUnit().getAbbreviation(this));
        binding.etForecastTodayValue.setText(settingsOptionManager.getTodayForecastTime());
        binding.etForecastTomorrowValue.setText(settingsOptionManager.getTomorrowForecastTime());
        binding.activitySettingsTitle.setText(getString(R.string.action_settings));

        binding.switchTempIconEnable.setChecked(settingsOptionManager.isNotificationTemperatureIconEnabled());
        binding.switchCanBeCleared.setChecked(settingsOptionManager.isNotificationCanBeClearedEnabled());
        binding.switchHideNotificationIcon.setChecked(settingsOptionManager.isNotificationHideIconEnabled());
        binding.switchHideInLockScreen.setChecked(settingsOptionManager.isNotificationHideInLockScreenEnabled());
        binding.switchHideBigView.setChecked(settingsOptionManager.isNotificationHideBigViewEnabled());


        clickListeners();
        changeListeners();
    }

    private void enableDisableViews() {
        DisplayUtils.disableEditText(binding.etTempUnit);
        DisplayUtils.disableEditText(binding.etPressureUnit);
        DisplayUtils.disableEditText(binding.etDistanceUnit);
        DisplayUtils.disableEditText(binding.etSpeedUnit);
        DisplayUtils.disableEditText(binding.etPrecipUnit);
        DisplayUtils.disableEditText(binding.etForecastTomorrowValue);
        DisplayUtils.disableEditText(binding.etForecastTodayValue);
        DisplayUtils.disableEditText(binding.etNotificationStyle);


        //  Enable/Diable today forecast
        toggleTodayForecast();

        //  Enable/Diable tomorrow forecast
        toggleTomorrowForecast();

        binding.switchAlertNotification.setChecked(settingsOptionManager.isAlertPushEnabled());
        binding.switchNotification.setChecked(settingsOptionManager.isNotificationEnabled());
        toggleNotificationItems(binding.llNotification);


    }

    private void toggleNotificationItems(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                toggleNotificationItems((ViewGroup) child);
            } else if (child instanceof EditText) {
                if (settingsOptionManager.isNotificationEnabled()) {
                    binding.etNotificationStyle.setTextColor(ContextCompat.getColor(MySettingsActivity.this, R.color.colorSkyBlue));
                    DrawableCompat.setTint(binding.etNotificationStyle.getBackground(), ContextCompat.getColor(this, R.color.colorSkyBlue));
                } else {
                    binding.etNotificationStyle.setTextColor(ContextCompat.getColor(MySettingsActivity.this, R.color.black30));
                    DrawableCompat.setTint(binding.etNotificationStyle.getBackground(), ContextCompat.getColor(this, R.color.black30));
                }
                binding.etNotificationStyle.setText(settingsOptionManager.getNotificationStyle().getNotificationStyleName(this));

            } else if (child instanceof com.google.android.material.switchmaterial.SwitchMaterial) {
                child.setEnabled(settingsOptionManager.isNotificationEnabled());
            } else if (child instanceof TextView) {
                if (settingsOptionManager.isNotificationEnabled()) {
                    ((TextView) child).setTextColor(ContextCompat.getColor(MySettingsActivity.this, R.color.colorTextWhite));
                } else {
                    ((TextView) child).setTextColor(ContextCompat.getColor(MySettingsActivity.this, R.color.black30));
                }
            }
        }
    }

    private void toggleTodayForecast() {
        if (settingsOptionManager.isTodayForecastEnabled()) {
            binding.tvForecastToday.setTextColor(ContextCompat.getColor(MySettingsActivity.this, R.color.colorTextWhite));
            binding.etForecastTodayValue.setTextColor(ContextCompat.getColor(MySettingsActivity.this, R.color.colorSkyBlue));
            DrawableCompat.setTint(binding.etForecastTodayValue.getBackground(), ContextCompat.getColor(this, R.color.colorSkyBlue));


        } else {
            binding.tvForecastTomorrow.setTextColor(ContextCompat.getColor(MySettingsActivity.this, R.color.black30));
            binding.etForecastTodayValue.setTextColor(ContextCompat.getColor(MySettingsActivity.this, R.color.black30));
            DrawableCompat.setTint(binding.etForecastTodayValue.getBackground(), ContextCompat.getColor(this, R.color.black30));
        }
        binding.etForecastTodayValue.setText(settingsOptionManager.getTodayForecastTime());
        binding.switchForecastToday.setChecked(settingsOptionManager.isTodayForecastEnabled());
    }

    private void toggleTomorrowForecast() {
        if (settingsOptionManager.isTomorrowForecastEnabled()) {
            binding.tvForecastTomorrow.setTextColor(ContextCompat.getColor(MySettingsActivity.this, R.color.colorTextWhite));
            binding.etForecastTomorrowValue.setTextColor(ContextCompat.getColor(MySettingsActivity.this, R.color.colorSkyBlue));
            DrawableCompat.setTint(binding.etForecastTomorrowValue.getBackground(), ContextCompat.getColor(this, R.color.colorSkyBlue));
        } else {
            binding.tvForecastTomorrow.setTextColor(ContextCompat.getColor(MySettingsActivity.this, R.color.black30));
            binding.etForecastTomorrowValue.setTextColor(ContextCompat.getColor(MySettingsActivity.this, R.color.black30));
            DrawableCompat.setTint(binding.etForecastTomorrowValue.getBackground(), ContextCompat.getColor(this, R.color.black30));

        }
        binding.etForecastTomorrowValue.setText(settingsOptionManager.getTomorrowForecastTime());
        binding.switchForecastTomorrow.setChecked(settingsOptionManager.isTomorrowForecastEnabled());
    }

    private void changeListeners() {

        binding.switchSendPercip.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setPrecipitationPushEnabled(b);
            }
        });

        binding.switchForecastToday.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setTodayForecastEnabled(b);
                PollingManager.resetNormalBackgroundTask(MySettingsActivity.this, false);
                toggleTodayForecast();
            }
        }));

        binding.switchForecastTomorrow.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setTomorrowForecastEnabled(b);
                PollingManager.resetNormalBackgroundTask(MySettingsActivity.this, false);
                toggleTomorrowForecast();
            }
        }));

        binding.switchAlertNotification.setOnCheckedChangeListener(((compoundButton, b) ->
        {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setAlertPushEnabled(b);
            }
        }));

        binding.switchAlertNotification.setOnCheckedChangeListener(((compoundButton, b) ->
        {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setAlertPushEnabled(b);
            }
        }));

        binding.switchNotification.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setNotificationEnabled(b);
                if (b) { // open notification.
                    PollingManager.resetNormalBackgroundTask(MySettingsActivity.this, true);
                } else { // close notification.
                    NormalNotificationIMP.cancelNotification(MySettingsActivity.this);
                    PollingManager.resetNormalBackgroundTask(MySettingsActivity.this, false);
                }
                toggleNotificationItems(binding.llNotification);
            }
        }));

        binding.switchTempIconEnable.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setNotificationTemperatureIconEnabled(b);
                PollingManager.resetNormalBackgroundTask(MySettingsActivity.this, true);
            }
        }));

        binding.switchHideNotificationIcon.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setNotificationHideIconEnabled(b);
                PollingManager.resetNormalBackgroundTask(MySettingsActivity.this, true);
            }
        }));

        binding.switchHideInLockScreen.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setNotificationHideInLockScreenEnabled(b);
                PollingManager.resetNormalBackgroundTask(MySettingsActivity.this, true);
            }
        }));

        binding.switchHideBigView.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setNotificationHideBigViewEnabled(b);
                PollingManager.resetNormalBackgroundTask(MySettingsActivity.this, true);
            }
        }));

        binding.switchCanBeCleared.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                settingsOptionManager.setNotificationCanBeClearedEnabled(b);
                PollingManager.resetNormalBackgroundTask(MySettingsActivity.this, true);
            }
        }));

    }

    private void clickListeners() {
        binding.imgDarkTheme.setOnClickListener(view -> {
            settingsOptionManager.setDarkMode("dark");
            WeatherFlow.getInstance().resetDayNightMode();
            WeatherFlow.getInstance().recreateAllActivities();

        });

        binding.imgRegularTheme.setOnClickListener(view -> {
            settingsOptionManager.setDarkMode("light");
            WeatherFlow.getInstance().resetDayNightMode();
            WeatherFlow.getInstance().recreateAllActivities();

        });

        binding.llRefreshRateContainer.setOnClickListener(view -> showRefreshRateDialog());

        binding.llTempContainer.setOnClickListener(view -> showTempUnitDialog());

        binding.llPressureContainer.setOnClickListener(view -> showPressureUnitDialog());

        binding.llDistanceContainer.setOnClickListener(view -> showDistanceUnitDialog());

        binding.llPercipitationContainer.setOnClickListener(view -> showPrecipUnitDialog());

        binding.llSpeedContainer.setOnClickListener(view -> showSpeedUnitDialog());

        binding.llTodayForecast.setOnClickListener(view -> {
            if (settingsOptionManager.isTodayForecastEnabled()) {
                showTodayForecastTimeDialog();
            }
        });

        binding.llTomorrowForecast.setOnClickListener(view -> {
            if (settingsOptionManager.isTomorrowForecastEnabled()) {
                showTomorrowForecastTimeDialog();
            }
        });

        binding.llNotificationStyle.setOnClickListener(view -> {
            if (settingsOptionManager.isNotificationEnabled()) {
                showNotificationStyleDialog();
            }
        });

        binding.activitySettingsToolbar.setNavigationOnClickListener(v -> finish());


    }

    private void showTodayForecastTimeDialog() {
        TimeSetterDialog dialog = new TimeSetterDialog();
        dialog.setIsToday(true);
        dialog.setOnTimeChangedListener(() -> {

            if (settingsOptionManager.isTodayForecastEnabled()) {
                PollingManager.resetTodayForecastBackgroundTask(
                        MySettingsActivity.this, false, false);
            }
            toggleTodayForecast();
        });
        dialog.show(getSupportFragmentManager(), null);
    }

    private void showTomorrowForecastTimeDialog() {
        TimeSetterDialog dialog = new TimeSetterDialog();
        dialog.setIsToday(false);
        dialog.setOnTimeChangedListener(() -> {
            if (settingsOptionManager.isTomorrowForecastEnabled()) {
                PollingManager.resetTomorrowForecastBackgroundTask(
                        MySettingsActivity.this, false, false);
            }

            toggleTomorrowForecast();

        });
        dialog.show(getSupportFragmentManager(), null);
    }

    private void showNotificationStyleDialog() {
        String[] unitsTitle = getResources().getStringArray(R.array.notification_styles);
        String[] unitsValues = getResources().getStringArray(R.array.notification_style_values);
        AlertDialog.Builder builder = new AlertDialog.Builder(MySettingsActivity.this);
        builder.setTitle(getString(R.string.settings_title_notification_style));

        builder.setItems(unitsTitle, (dialog, which) -> {
            settingsOptionManager.setNotificationStyle(unitsValues[which]);
            PollingManager.resetNormalBackgroundTask(MySettingsActivity.this, true);
            binding.etNotificationStyle.setText(settingsOptionManager.getNotificationStyle().getNotificationStyleName(this));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showSpeedUnitDialog() {
        String[] unitsTitle = getResources().getStringArray(R.array.speed_units);
        String[] unitsValues = getResources().getStringArray(R.array.speed_unit_values);
        AlertDialog.Builder builder = new AlertDialog.Builder(MySettingsActivity.this);
        builder.setTitle(getString(R.string.settings_title_speed_unit));

        builder.setItems(unitsTitle, (dialog, which) -> {
            settingsOptionManager.setSpeedUnit(unitsValues[which]);
            binding.etSpeedUnit.setText(settingsOptionManager.getSpeedUnit().getAbbreviation(this));

//            SnackbarUtils.showSnackbar(
//                    MySettingsActivity.this, getString(R.string.feedback_refresh_ui_after_refresh));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDistanceUnitDialog() {
        String[] unitsTitle = getResources().getStringArray(R.array.distance_units);
        String[] unitsValues = getResources().getStringArray(R.array.distance_unit_values);
        AlertDialog.Builder builder = new AlertDialog.Builder(MySettingsActivity.this);
        builder.setTitle(getString(R.string.settings_title_distance_unit));

        builder.setItems(unitsTitle, (dialog, which) -> {
            settingsOptionManager.setDistanceUnit(unitsValues[which]);
            binding.etDistanceUnit.setText(settingsOptionManager.getDistanceUnit().getAbbreviation(this));

//            SnackbarUtils.showSnackbar(
//                    MySettingsActivity.this, getString(R.string.feedback_refresh_ui_after_refresh));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showPrecipUnitDialog() {
        String[] unitsTitle = getResources().getStringArray(R.array.precipitation_units);
        String[] unitsValues = getResources().getStringArray(R.array.precipitation_unit_values);
        AlertDialog.Builder builder = new AlertDialog.Builder(MySettingsActivity.this);
        builder.setTitle(getString(R.string.settings_title_precipitation_unit));

        builder.setItems(unitsTitle, (dialog, which) -> {
            settingsOptionManager.setPrecipitationUnit(unitsValues[which]);
            binding.etPrecipUnit.setText(settingsOptionManager.getPrecipitationUnit().getAbbreviation(this));

//            SnackbarUtils.showSnackbar(
//                    MySettingsActivity.this, getString(R.string.feedback_refresh_ui_after_refresh));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showPressureUnitDialog() {
        String[] pressureUnitsTitle = getResources().getStringArray(R.array.pressure_units);
        String[] pressureUnitsValues = getResources().getStringArray(R.array.pressure_unit_values);
        AlertDialog.Builder builder = new AlertDialog.Builder(MySettingsActivity.this);
        builder.setTitle(getString(R.string.settings_title_pressure_unit));

        builder.setItems(pressureUnitsTitle, (dialog, which) -> {
            settingsOptionManager.setPressureUnit(pressureUnitsValues[which]);
            binding.etPressureUnit.setText(settingsOptionManager.getPressureUnit().getAbbreviation(this));

//            SnackbarUtils.showSnackbar(
//                    MySettingsActivity.this, getString(R.string.feedback_refresh_ui_after_refresh));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showTempUnitDialog() {
        String[] tempUnitsTitle = getResources().getStringArray(R.array.temperature_units);
        String[] tempUnitsValues = getResources().getStringArray(R.array.temperature_unit_values);
        AlertDialog.Builder builder = new AlertDialog.Builder(MySettingsActivity.this);
        builder.setTitle(getString(R.string.settings_title_temperature_unit));

        builder.setItems(tempUnitsTitle, (dialog, which) -> {
            settingsOptionManager.setTemperatureUnit(tempUnitsValues[which]);
            PollingManager.resetNormalBackgroundTask(MySettingsActivity.this, false);
            binding.etTempUnit.setText(settingsOptionManager.getTemperatureUnit().getAbbreviation(this));

//            SnackbarUtils.showSnackbar(
//                    MySettingsActivity.this, getString(R.string.feedback_refresh_ui_after_refresh));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showRefreshRateDialog() {
        String[] refreshRatesTitles = getResources().getStringArray(R.array.automatic_refresh_rates);
        String[] refreshRatesValues = getResources().getStringArray(R.array.automatic_refresh_rate_values);
        AlertDialog.Builder builder = new AlertDialog.Builder(MySettingsActivity.this);
        builder.setTitle(getString(R.string.settings_title_refresh_rate));

        builder.setItems(refreshRatesTitles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                settingsOptionManager.setUpdateInterval(refreshRatesValues[which]);
                PollingManager.resetNormalBackgroundTask(MySettingsActivity.this, false);
                binding.tvRefreshRateValue.setText(settingsOptionManager.getUpdateInterval().getUpdateIntervalName(MySettingsActivity.this));

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setThemePreview(Boolean isDarkMode) {
        if (isDarkMode) {
            binding.imgDarkTheme.setImageDrawable(ContextCompat.getDrawable(MySettingsActivity.this, R.drawable.black_theme_select_shape));
            binding.imgRegularTheme.setImageDrawable(ContextCompat.getDrawable(MySettingsActivity.this, R.drawable.cyan_theme_unselect_shape));

        } else {
            binding.imgDarkTheme.setImageDrawable(ContextCompat.getDrawable(MySettingsActivity.this, R.drawable.black_theme_unselect_shape));
            binding.imgRegularTheme.setImageDrawable(ContextCompat.getDrawable(MySettingsActivity.this, R.drawable.cyan_theme_select_shape));
        }
    }
}