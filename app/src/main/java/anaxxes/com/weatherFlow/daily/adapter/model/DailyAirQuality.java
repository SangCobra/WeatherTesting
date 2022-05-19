package anaxxes.com.weatherFlow.daily.adapter.model;

import anaxxes.com.weatherFlow.basic.model.weather.AirQuality;
import anaxxes.com.weatherFlow.daily.adapter.DailyWeatherAdapter;

public class DailyAirQuality implements DailyWeatherAdapter.ViewModel {

    private AirQuality airQuality;

    public DailyAirQuality(AirQuality airQuality) {
        this.airQuality = airQuality;
    }

    public AirQuality getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(AirQuality airQuality) {
        this.airQuality = airQuality;
    }

    public static boolean isCode(int code) {
        return code == 5;
    }

    @Override
    public int getCode() {
        return 5;
    }
}
