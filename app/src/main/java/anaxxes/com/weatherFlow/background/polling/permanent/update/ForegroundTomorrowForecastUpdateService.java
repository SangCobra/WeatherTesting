package anaxxes.com.weatherFlow.background.polling.permanent.update;

import android.content.Context;

import androidx.core.app.NotificationCompat;

import java.util.List;

import anaxxes.com.weatherFlow.WeatherFlow;
import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.background.polling.basic.ForegroundUpdateService;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.remoteviews.presenter.notification.ForecastNotificationIMP;

/**
 * Foreground Today forecast update service.
 * */

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
