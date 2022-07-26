package mtgtech.com.weather_forecast.background.polling;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.db.DatabaseHelper;
import mtgtech.com.weather_forecast.location_service.LocationHelper;
import mtgtech.com.weather_forecast.utils.helpter.IntentHelper;
import mtgtech.com.weather_forecast.weather_forecast.WeatherHelper;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;

/**
 * Polling updateRotation helper.
 */

public class PollingUpdateHelper {

    private Context context;

    private LocationHelper locationHelper;
    private WeatherHelper weatherHelper;

    private List<Location> locationList;

    private OnPollingUpdateListener listener;

    public PollingUpdateHelper(Context context, List<Location> locationList) {
        this.context = context;
        this.locationHelper = new LocationHelper(context);
        this.weatherHelper = new WeatherHelper();
        this.locationList = locationList;
    }

    // control.

    public void pollingUpdate() {
        requestData(0, false);
    }

    public void cancel() {
        locationHelper.cancel();
        weatherHelper.cancel();
    }

    private void requestData(int position, boolean located) {
        Weather old = DatabaseHelper.getInstance(context).readWeather(locationList.get(position));
        if (old != null && old.isValid(0.25F)) {
            locationList.get(position).setWeather(old);
            new RequestWeatherCallback(old, position, locationList.size()).requestWeatherSuccess(
                    locationList.get(position));
            return;
        }
        if (locationList.get(position).isCurrentPosition() && !located) {
            locationHelper.requestLocation(context, locationList.get(position), true,
                    new RequestLocationCallback(position, locationList.size()));
        } else {
            weatherHelper.requestWeather(context, locationList.get(position),
                    new RequestWeatherCallback(old, position, locationList.size())
            );
        }
    }

    // interface.

    public void setOnPollingUpdateListener(OnPollingUpdateListener l) {
        this.listener = l;
    }

    public interface OnPollingUpdateListener {
        void onUpdateCompleted(@NonNull Location location, @Nullable Weather old,
                               boolean succeed, int index, int total);

        void onPollingCompleted();
    }

    // on request location listener.

    private class RequestLocationCallback implements LocationHelper.OnRequestLocationListener {

        private int index;
        private int total;

        RequestLocationCallback(int index, int total) {
            this.index = index;
            this.total = total;
        }

        @Override
        public void requestLocationSuccess(Location requestLocation) {
            locationList.set(index, requestLocation);

            if (requestLocation.isUsable()) {
                requestData(index, true);
            } else {
                requestLocationFailed(requestLocation);
                Toast.makeText(
                        context,
                        context.getString(R.string.feedback_not_yet_location),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void requestLocationFailed(Location requestLocation) {
            if (locationList.get(index).isUsable()) {
                requestData(index, true);
            } else {
                new RequestWeatherCallback(null, index, total)
                        .requestWeatherFailed(locationList.get(index));
            }
        }
    }

    // on request weather listener.

    private class RequestWeatherCallback implements WeatherHelper.OnRequestWeatherListener {

        @Nullable
        private Weather old;
        private int index;
        private int total;

        RequestWeatherCallback(@Nullable Weather old, int index, int total) {
            this.old = old;
            this.index = index;
            this.total = total;
        }

        @Override
        public void requestWeatherSuccess(@NonNull Location requestLocation) {
            locationList.set(index, requestLocation);

            Weather weather = requestLocation.getWeather();
            if (weather != null
                    && (old == null
                    || weather.getBase().getTimeStamp() != old.getBase().getTimeStamp())) {
                if (listener != null) {
                    listener.onUpdateCompleted(requestLocation, old, true, index, total);
                }
                IntentHelper.sendBackgroundUpdateBroadcast(context, requestLocation);

                if (index + 1 < total) {
                    requestData(index + 1, false);
                } else if (listener != null) {
                    listener.onPollingCompleted();
                }
            } else {
                requestWeatherFailed(requestLocation);
            }
        }

        @Override
        public void requestWeatherFailed(@NonNull Location requestLocation) {
            locationList.set(index, requestLocation);

            if (listener != null) {
                listener.onUpdateCompleted(requestLocation, old, false, index, total);
            }
            if (index + 1 < total) {
                requestData(index + 1, false);
            } else if (listener != null) {
                listener.onPollingCompleted();
            }
        }
    }
}
