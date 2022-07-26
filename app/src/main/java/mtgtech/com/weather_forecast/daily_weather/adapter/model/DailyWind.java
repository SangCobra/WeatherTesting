package mtgtech.com.weather_forecast.daily_weather.adapter.model;

import mtgtech.com.weather_forecast.daily_weather.adapter.DailyWeatherAdapter;
import mtgtech.com.weather_forecast.weather_model.model.weather.Wind;

public class DailyWind implements DailyWeatherAdapter.ViewModel {

    private Wind wind;

    public DailyWind(Wind wind) {
        this.wind = wind;
    }

    public static boolean isCode(int code) {
        return code == 4;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    @Override
    public int getCode() {
        return 4;
    }
}
