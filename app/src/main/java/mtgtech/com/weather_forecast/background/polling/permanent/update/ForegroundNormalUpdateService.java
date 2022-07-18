package mtgtech.com.weather_forecast.background.polling.permanent.update;

import android.content.Context;

import java.util.List;

import mtgtech.com.weather_forecast.WeatherFlow;
import mtgtech.com.weather_forecast.background.polling.basic.ForegroundUpdateService;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;
import mtgtech.com.weather_forecast.background.polling.permanent.PermanentServiceHelper;
import mtgtech.com.weather_forecast.remoteviews.NotificationUtils;
import mtgtech.com.weather_forecast.remoteviews.WidgetUtils;

/**
 * Foreground normal update service.
 * */
public class ForegroundNormalUpdateService extends ForegroundUpdateService {

    @Override
    public void updateView(Context context, Location location) {
        WidgetUtils.updateWidgetIfNecessary(context, location);
        NotificationUtils.updateNotificationIfNecessary(context, location);
    }

    @Override
    public void updateView(Context context, List<Location> locationList) {
        WidgetUtils.updateWidgetIfNecessary(context, locationList);
    }

    @Override
    public void handlePollingResult(boolean failed) {
        PermanentServiceHelper.updatePollingService(this, failed);
    }

    @Override
    public int getForegroundNotificationId() {
        return WeatherFlow.NOTIFICATION_ID_UPDATING_NORMALLY;
    }
}
