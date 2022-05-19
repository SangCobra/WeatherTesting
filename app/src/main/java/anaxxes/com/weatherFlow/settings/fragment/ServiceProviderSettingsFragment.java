package anaxxes.com.weatherFlow.settings.fragment;

import android.os.Bundle;

import androidx.preference.Preference;

import java.util.List;

import anaxxes.com.weatherFlow.WeatherFlow;
import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.GeoActivity;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.basic.model.option.provider.WeatherSource;
import anaxxes.com.weatherFlow.db.DatabaseHelper;
import anaxxes.com.weatherFlow.basic.model.option.utils.OptionMapper;
import anaxxes.com.weatherFlow.utils.SnackbarUtils;

/**
 * Service provider settings fragment.
 * */

public class ServiceProviderSettingsFragment extends AbstractSettingsFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.perference_service_provider);
        initPreferences();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // do nothing.
    }

    private void initPreferences() {
        // weather source.
        Preference chineseSource = findPreference(getString(R.string.key_weather_source));
        chineseSource.setSummary(getSettingsOptionManager().getWeatherSource().getSourceName(getActivity()));
        chineseSource.setOnPreferenceChangeListener((preference, newValue) -> {
            WeatherSource source = OptionMapper.getWeatherSource((String) newValue);

            getSettingsOptionManager().setWeatherSource(source);
            preference.setSummary(source.getSourceName(getActivity()));

            List<Location> locationList = DatabaseHelper.getInstance(requireActivity()).readLocationList();
            for (int i = 0; i < locationList.size(); i ++) {
                if (locationList.get(i).isCurrentPosition()) {
                    locationList.get(i).setWeatherSource(source);
                    break;
                }
            }
            DatabaseHelper.getInstance(requireActivity()).writeLocationList(locationList);
            return true;
        });

        // location source.
        Preference locationService = findPreference(getString(R.string.key_location_service));
        locationService.setSummary(getSettingsOptionManager().getLocationProvider().getProviderName(getActivity()));
        locationService.setOnPreferenceChangeListener((preference, newValue) -> {
            getSettingsOptionManager().setLocationProvider(OptionMapper.getLocationProvider((String) newValue));
            preference.setSummary(getSettingsOptionManager().getLocationProvider().getProviderName(getActivity()));
            SnackbarUtils.showSnackbar(
                    (GeoActivity) requireActivity(),
                    getString(R.string.feedback_restart),
                    getString(R.string.restart),
                    v -> WeatherFlow.getInstance().recreateAllActivities()
            );
            return true;
        });
    }
}