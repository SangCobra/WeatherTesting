package mtgtech.com.weather_forecast.background.polling.permanent.update;

import android.content.Context;

import androidx.core.app.NotificationCompat;

import java.util.List;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.WeatherFlow;
import mtgtech.com.weather_forecast.background.polling.basic.ForegroundUpdateService;
import mtgtech.com.weather_forecast.remoteviews.presenter.notification.ForecastNotificationIMP;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;

/**
 * Foreground Today forecast update service.
 */

public class ForegroundTomorrowForecastUpdateService extends ForegroundUpdateService {

    @Override
    public void updateView(Context context, Location location) {
        if (ForecastNotificationIMP.isEnable(this, false)) {
            ForecastNotificationIMP.buildForecastAndSendIt(context, location, false);
        }
    }

    @Override
    public void updateView(Context context, List<Location> locationList) {
    }

    @Override
    public void handlePollingResult(boolean failed) {
        // do nothing.
    }

    @Override
    public NotificationCompat.Builder getForegroundNotification(int index, int total) {
        return super.getForegroundNotification(index, total)
                .setContentTitle(getString(R.string.weather_flow) + " " + getString(R.string.forecast));
    }

    @Override
    public int getForegroundNotificationId() {
        return WeatherFlow.NOTIFICATION_ID_UPDATING_TOMORROW_FORECAST;
    }
}
