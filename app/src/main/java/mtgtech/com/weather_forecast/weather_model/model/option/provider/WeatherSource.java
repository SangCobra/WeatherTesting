package mtgtech.com.weather_forecast.weather_model.model.option.provider;

import android.content.Context;

import androidx.annotation.ColorInt;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.model.option.utils.OptionMapper;

public enum WeatherSource {

    ACCU("accu", 0xffef5823, "accuweather.com");

    private String sourceId;
    @ColorInt
    private int sourceColor;
    private String sourceUrl;

    WeatherSource(String id, @ColorInt int color, String url) {
        sourceId = id;
        sourceColor = color;
        sourceUrl = url;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getSourceName(Context context) {
        return OptionMapper.getNameByValue(
                context,
                sourceId,
                R.array.weather_sources,
                R.array.weather_source_values
        );
    }

    public int getSourceColor() {
        return sourceColor;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }
}
