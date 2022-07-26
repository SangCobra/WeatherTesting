package mtgtech.com.weather_forecast.utils;

import static android.content.Context.MODE_PRIVATE;
import static mtgtech.com.weather_forecast.WeatherFlow.PURCHASE;

import android.content.Context;
import android.content.SharedPreferences;

public class PurchaseUtils {

    public static void setIsPurchased(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PURCHASE, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("isPurchased", true).apply();
    }

    public static Boolean isPurchased(Context context) {
        return context.getSharedPreferences(PURCHASE, MODE_PRIVATE).getBoolean("isPurchased", false);
    }
}
