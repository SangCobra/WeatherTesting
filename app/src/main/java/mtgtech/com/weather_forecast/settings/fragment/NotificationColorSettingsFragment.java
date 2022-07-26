package mtgtech.com.weather_forecast.settings.fragment;

import android.os.Bundle;

import androidx.preference.ListPreference;

import com.jaredrummler.android.colorpicker.ColorPreferenceCompat;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.background.polling.PollingManager;

/**
 * Notification color settings fragment.
 */

public class NotificationColorSettingsFragment extends AbstractSettingsFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.perference_notification_color);
        initNotificationPart();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // do nothing.
    }

    private void initNotificationPart() {
        // notification custom color.
        findPreference(getString(R.string.key_notification_custom_color)).setOnPreferenceChangeListener((p, newValue) -> {
            getSettingsOptionManager().setNotificationCustomColorEnabled((Boolean) newValue);
            initNotificationPart();
            PollingManager.resetNormalBackgroundTask(getActivity(), true);
            return true;
        });

        // notification background.
        ColorPreferenceCompat notificationBackgroundColor = findPreference(getString(R.string.key_notification_background_color));
        notificationBackgroundColor.setEnabled(getSettingsOptionManager().isNotificationCustomColorEnabled());
        notificationBackgroundColor.setOnPreferenceChangeListener((preference, newValue) -> {
            getSettingsOptionManager().setNotificationBackgroundColor((Integer) newValue);
            PollingManager.resetNormalBackgroundTask(getActivity(), true);
            return true;
        });

        // notification text color.
        ListPreference notificationTextColor = findPreference(getString(R.string.key_notification_text_color));
        notificationTextColor.setSummary(
                getSettingsOptionManager().getNotificationTextColor().getNotificationTextColorName(
                        getActivity()
                )
        );
        notificationTextColor.setOnPreferenceChangeListener((preference, newValue) -> {
            PollingManager.resetNormalBackgroundTask(getActivity(), true);
            preference.setSummary(
                    getSettingsOptionManager().getNotificationTextColor().getNotificationTextColorName(
                            getActivity()
                    )
            );
            return true;
        });
    }
}