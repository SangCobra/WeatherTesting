package com.mtgtech.weather_forecast.background.polling.permanent.update;

import android.content.Context;

import java.util.List;

import com.mtgtech.weather_forecast.WeatherFlow;
import com.mtgtech.weather_forecast.background.polling.basic.ForegroundUpdateService;
import com.mtgtech.weather_forecast.background.polling.permanent.PermanentServiceHelper;
import com.mtgtech.weather_forecast.remoteviews.NotificationUtils;
import com.mtgtech.weather_forecast.remoteviews.WidgetUtils;
import com.mtgtech.weather_forecast.weather_model.model.location.Location;

/**
 * Foreground normal update service.
 */
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
