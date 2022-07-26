package mtgtech.com.weather_forecast.daily_weather.adapter.model;

import mtgtech.com.weather_forecast.daily_weather.adapter.DailyWeatherAdapter;

public class Margin implements DailyWeatherAdapter.ViewModel {

    public static boolean isCode(int code) {
        return code == -2;
    }

    @Override
    public int getCode() {
        return -2;
    }
}
