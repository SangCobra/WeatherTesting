package mtgtech.com.weather_forecast.view.adapter.location;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.util.List;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;
import mtgtech.com.weather_forecast.weather_model.model.option.provider.WeatherSource;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.TemperatureUnit;
import mtgtech.com.weather_forecast.weather_model.model.weather.Alert;
import mtgtech.com.weather_forecast.weather_model.model.weather.WeatherCode;

public class LocationModel {

    public @NonNull
    Location location;

    public @Nullable
    WeatherCode weatherCode;
    public @NonNull
    WeatherSource weatherSource;
    public boolean currentPosition;
    public boolean residentPosition;
    private TemperatureUnit temperatureUnit;

    public @NonNull
    String title;
    public @NonNull
    String subtitle;

    // public float latitude;
    // public float longitude;
    // public TimeZone timeZone;

    public @Nullable
    String alerts;

    public boolean lightTheme;
    private boolean forceUpdate;

    public LocationModel(Context context, Location location, TemperatureUnit unit, WeatherSource defaultSource,
                         boolean lightTheme, boolean forceUpdate) {
        this.location = location;
        this.temperatureUnit = unit;
        if (location.getWeather() != null) {
            this.weatherCode = location.getWeather().getCurrent().getWeatherCode();
        } else {
            this.weatherCode = null;
        }

        this.weatherSource = location.isCurrentPosition()
                ? defaultSource
                : location.getWeatherSource();

        this.currentPosition = location.isCurrentPosition();
        this.residentPosition = location.isResidentPosition();

        StringBuilder builder = new StringBuilder(location.isCurrentPosition()
                ? context.getString(R.string.current_location)
                : location.getCityName(context));
//        if (location.getWeather() != null) {
//            builder.append(", ").append(
//                    location.getWeather().getCurrent().getTemperature().getTemperature(context, unit)
//            );
//        }
        title = builder.toString();

        if (!location.isCurrentPosition() || location.isUsable()) {
            builder = new StringBuilder(location.getCountry() + " " + location.getProvince());
            if (!location.getProvince().equals(location.getCity())
                    && !TextUtils.isEmpty(location.getCity())) {
                builder.append(" ").append(location.getCity());
            }
            if (!location.getCity().equals(location.getDistrict())
                    && !TextUtils.isEmpty(location.getDistrict())) {
                builder.append(" ").append(location.getDistrict());
            }
            subtitle = builder.toString();
        } else {
            subtitle = context.getString(R.string.feedback_not_yet_location);
        }

        // latitude = location.getLatitude();
        // longitude = location.getLongitude();
        // timeZone = location.getTimeZone();

        if (location.getWeather() != null) {
            List<Alert> alertList = location.getWeather().getAlertList();
            if (alertList.size() > 0) {
                builder = new StringBuilder();
                for (int i = 0; i < alertList.size(); i++) {
                    builder.append(alertList.get(i).getDescription())
                            .append(", ")
                            .append(
                                    DateFormat.getDateTimeInstance(
                                            DateFormat.SHORT,
                                            DateFormat.SHORT
                                    ).format(alertList.get(i).getDate())
                            );
                    if (i != alertList.size() - 1) {
                        builder.append("\n");
                    }
                }
                alerts = builder.toString();
            } else if (!TextUtils.isEmpty(location.getWeather().getCurrent().getDailyForecast())) {
                alerts = location.getWeather().getCurrent().getDailyForecast();
            } else if (!TextUtils.isEmpty(location.getWeather().getCurrent().getHourlyForecast())) {
                alerts = location.getWeather().getCurrent().getHourlyForecast();
            } else {
                alerts = null;
            }
        } else {
            alerts = null;
        }

        this.lightTheme = lightTheme;
        this.forceUpdate = forceUpdate;
    }

    public boolean areItemsTheSame(@NonNull LocationModel newItem) {
        return location.equals(newItem.location);
    }

    public boolean areContentsTheSame(@NonNull LocationModel newItem) {
        return weatherCode == newItem.weatherCode
                && weatherSource == newItem.weatherSource
                && currentPosition == newItem.currentPosition
                && residentPosition == newItem.residentPosition
                && isSameString(title, newItem.title)
                && isSameString(subtitle, newItem.subtitle)
                && isSameString(alerts, newItem.alerts)
                // && latitude == newItem.latitude
                // && longitude == newItem.longitude
                // && timeZone.getID().equals(newItem.timeZone.getID())
                && lightTheme == newItem.lightTheme
                && !newItem.forceUpdate;
    }

    private static boolean isSameString(String a, String b) {
        if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(b)) {
            return a.equals(b);
        } else {
            return TextUtils.isEmpty(a) && TextUtils.isEmpty(b);
        }
    }

    public TemperatureUnit getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(TemperatureUnit temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }
}
