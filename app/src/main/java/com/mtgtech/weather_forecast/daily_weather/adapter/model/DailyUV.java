package com.mtgtech.weather_forecast.daily_weather.adapter.model;

import com.mtgtech.weather_forecast.daily_weather.adapter.DailyWeatherAdapter;
import com.mtgtech.weather_forecast.weather_model.model.weather.UV;

public class DailyUV implements DailyWeatherAdapter.ViewModel {

    private UV uv;

    public DailyUV(UV uv) {
        this.uv = uv;
    }

    public static boolean isCode(int code) {
        return code == 8;
    }

    public UV getUv() {
        return uv;
    }

    public void setUv(UV uv) {
        this.uv = uv;
    }

    @Override
    public int getCode() {
        return 8;
    }
}
