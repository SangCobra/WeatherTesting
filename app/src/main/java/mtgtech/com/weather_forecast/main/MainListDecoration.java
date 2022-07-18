package mtgtech.com.weather_forecast.main;

import android.content.Context;

import mtgtech.com.weather_forecast.view.decotarion.ListDecoration;
import mtgtech.com.weather_forecast.utils.manager.ThemeManager;

/**
 * Main list decoration.
 * */

public class MainListDecoration extends ListDecoration {

    public MainListDecoration(Context context) {
        super(
                context,
                ThemeManager.getInstance(context).getLineColor(context)
        );
    }
}
