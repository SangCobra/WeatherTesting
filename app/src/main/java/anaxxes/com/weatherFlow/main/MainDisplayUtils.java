package anaxxes.com.weatherFlow.main;

import android.content.Context;

import anaxxes.com.weatherFlow.utils.DisplayUtils;

public class MainDisplayUtils {

    public static boolean isMultiFragmentEnabled(Context context) {
        return DisplayUtils.isTabletDevice(context) && DisplayUtils.isLandscape(context);
    }
}
