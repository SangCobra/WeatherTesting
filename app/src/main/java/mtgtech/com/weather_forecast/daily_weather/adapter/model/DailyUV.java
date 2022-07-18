package mtgtech.com.weather_forecast.daily_weather.adapter.model;

import mtgtech.com.weather_forecast.weather_model.model.weather.UV;
import mtgtech.com.weather_forecast.daily_weather.adapter.DailyWeatherAdapter;

public class DailyUV implements DailyWeatherAdapter.ViewModel {

    private UV uv;

    public DailyUV(UV uv) {
        this.uv = uv;
    }

    public UV getUv() {
        return uv;
    }

    public void setUv(UV uv) {
        this.uv = uv;
    }

    public static boolean isCode(int code) {
        return code == 8;
    }

    @Override
    public int getCode() {
        return 8;
    }
}
