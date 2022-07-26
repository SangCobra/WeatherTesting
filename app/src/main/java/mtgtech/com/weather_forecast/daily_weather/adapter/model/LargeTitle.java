package mtgtech.com.weather_forecast.daily_weather.adapter.model;

import mtgtech.com.weather_forecast.daily_weather.adapter.DailyWeatherAdapter;

public class LargeTitle implements DailyWeatherAdapter.ViewModel {

    private String title;

    public LargeTitle(String title) {
        this.title = title;
    }

    public static boolean isCode(int code) {
        return code == 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getCode() {
        return 0;
    }
}
