package mtgtech.com.weather_forecast.background.receiver.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import mtgtech.com.weather_forecast.utils.helpter.IntentHelper;

/**
 * Abstract widget provider.
 */
public class AbstractWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        IntentHelper.startAwakeForegroundUpdateService(context);
    }
}
