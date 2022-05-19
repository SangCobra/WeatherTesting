package anaxxes.com.weatherFlow.daily.adapter.model;

import anaxxes.com.weatherFlow.basic.model.weather.Wind;
import anaxxes.com.weatherFlow.daily.adapter.DailyWeatherAdapter;

public class DailyWind implements DailyWeatherAdapter.ViewModel {

    private Wind wind;

    public DailyWind(Wind wind) {
        this.wind = wind;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public static boolean isCode(int code) {
        return code == 4;
    }

    @Override
    public int getCode() {
        return 4;
    }
}
