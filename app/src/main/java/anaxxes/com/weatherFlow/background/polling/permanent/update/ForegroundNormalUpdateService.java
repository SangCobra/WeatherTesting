package anaxxes.com.weatherFlow.background.polling.permanent.update;

import android.content.Context;

import java.util.List;

import anaxxes.com.weatherFlow.WeatherFlow;
import anaxxes.com.weatherFlow.background.polling.basic.ForegroundUpdateService;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.background.polling.permanent.PermanentServiceHelper;
import anaxxes.com.weatherFlow.remoteviews.NotificationUtils;
import anaxxes.com.weatherFlow.remoteviews.WidgetUtils;

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
