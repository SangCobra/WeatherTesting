package mtgtech.com.weather_forecast.daily_weather.adapter.model;

import mtgtech.com.weather_forecast.weather_model.model.weather.Pollen;
import mtgtech.com.weather_forecast.daily_weather.adapter.DailyWeatherAdapter;

public class DailyPollen implements DailyWeatherAdapter.ViewModel {

    private Pollen pollen;

    public DailyPollen(Pollen pollen) {
        this.pollen = pollen;
    }

    public Pollen getPollen() {
        return pollen;
    }

    public void setPollen(Pollen pollen) {
        this.pollen = pollen;
    }

    public static boolean isCode(int code) {
        return code == 6;
    }

    @Override
    public int getCode() {
        return 6;
    }
}
