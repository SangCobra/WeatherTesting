package com.mtgtech.weather_forecast.background.polling.basic;

import android.content.Context;

import java.util.List;

import com.mtgtech.weather_forecast.WeatherFlow;
import com.mtgtech.weather_forecast.background.polling.PollingManager;
import com.mtgtech.weather_forecast.remoteviews.NotificationUtils;
import com.mtgtech.weather_forecast.remoteviews.WidgetUtils;
import com.mtgtech.weather_forecast.weather_model.model.location.Location;

/**
 * Awake foreground update service.
 */
public class AwakeForegroundUpdateService extends ForegroundUpdateService {

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
        PollingManager.resetAllBackgroundTask(this, false);
    }

    @Override
    public int getForegroundNotificationId() {
        return WeatherFlow.NOTIFICATION_ID_UPDATING_AWAKE;
    }
}
