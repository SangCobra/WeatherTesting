package anaxxes.com.weatherFlow.daily.adapter.model;

import anaxxes.com.weatherFlow.daily.adapter.DailyWeatherAdapter;

public class Margin implements DailyWeatherAdapter.ViewModel {

    public static boolean isCode(int code) {
        return code == -2;
    }

    @Override
    public int getCode() {
        return -2;
    }
}
