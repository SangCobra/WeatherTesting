package com.mtgtech.weather_forecast.main;

import android.content.Context;

import com.mtgtech.weather_forecast.utils.DisplayUtils;

public class MainDisplayUtils {

    public static boolean isMultiFragmentEnabled(Context context) {
        return DisplayUtils.isTabletDevice(context) && DisplayUtils.isLandscape(context);
    }
}
