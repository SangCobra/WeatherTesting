package mtgtech.com.weather_forecast.weather_model.model.option.unit;

import android.annotation.SuppressLint;

public class UnitUtils {

    public static String formatFloat(float value) {
        return formatFloat(value, 0);
    }

    public static String formatFloat(float value, int decimalNumber) {
        return String.format(
                "%." + decimalNumber + "f",
                value
        );
    }

    @SuppressLint("DefaultLocale")
    public static String formatInt(int value) {
        return String.format("%d", value);
    }
}
