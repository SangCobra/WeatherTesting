package anaxxes.com.weatherFlow.background.receiver.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import anaxxes.com.weatherFlow.utils.helpter.IntentHelper;

/**
 * Abstract widget provider.
 * */
public class AbstractWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        IntentHelper.startAwakeForegroundUpdateService(context);
    }
}
