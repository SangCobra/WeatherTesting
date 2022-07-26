package com.mtgtech.weather_forecast.main;

import android.content.Context;

import com.mtgtech.weather_forecast.utils.manager.ThemeManager;
import com.mtgtech.weather_forecast.view.decotarion.ListDecoration;

/**
 * Main list decoration.
 */

public class MainListDecoration extends ListDecoration {

    public MainListDecoration(Context context) {
        super(
                context,
                ThemeManager.getInstance(context).getLineColor(context)
        );
    }
}
