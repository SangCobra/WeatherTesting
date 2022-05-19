package anaxxes.com.weatherFlow.daily.adapter.model;

import anaxxes.com.weatherFlow.basic.model.weather.UV;
import anaxxes.com.weatherFlow.daily.adapter.DailyWeatherAdapter;

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
