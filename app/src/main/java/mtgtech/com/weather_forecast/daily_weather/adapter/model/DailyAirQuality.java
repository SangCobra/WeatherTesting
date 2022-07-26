package mtgtech.com.weather_forecast.daily_weather.adapter.model;

import mtgtech.com.weather_forecast.daily_weather.adapter.DailyWeatherAdapter;
import mtgtech.com.weather_forecast.weather_model.model.weather.AirQuality;

public class DailyAirQuality implements DailyWeatherAdapter.ViewModel {

    private AirQuality airQuality;

    public DailyAirQuality(AirQuality airQuality) {
        this.airQuality = airQuality;
    }

    public static boolean isCode(int code) {
        return code == 5;
    }

    public AirQuality getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(AirQuality airQuality) {
        this.airQuality = airQuality;
    }

    @Override
    public int getCode() {
        return 5;
    }
}
