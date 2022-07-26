package mtgtech.com.weather_forecast.db.converter;

import androidx.annotation.Nullable;

import mtgtech.com.weather_forecast.db.entity.HistoryEntity;
import mtgtech.com.weather_forecast.db.propertyConverter.WeatherSourceConverter;
import mtgtech.com.weather_forecast.weather_model.model.option.provider.WeatherSource;
import mtgtech.com.weather_forecast.weather_model.model.weather.History;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;

public class HistoryEntityConverter {

    public static HistoryEntity convert(String cityId, WeatherSource source, History history) {
        HistoryEntity entity = new HistoryEntity();
        entity.cityId = cityId;
        entity.weatherSource = new WeatherSourceConverter().convertToDatabaseValue(source);
        entity.date = history.getDate();
        entity.time = history.getTime();
        entity.daytimeTemperature = history.getDaytimeTemperature();
        entity.nighttimeTemperature = history.getNighttimeTemperature();
        return entity;
    }

    public static HistoryEntity convert(String cityId, WeatherSource source, Weather weather) {
        HistoryEntity entity = new HistoryEntity();
        entity.cityId = cityId;
        entity.weatherSource = new WeatherSourceConverter().convertToDatabaseValue(source);
        entity.date = weather.getBase().getPublishDate();
        entity.time = weather.getBase().getPublishTime();
        entity.daytimeTemperature = weather.getDailyForecast().get(0).day().getTemperature().getTemperature();
        entity.nighttimeTemperature = weather.getDailyForecast().get(0).night().getTemperature().getTemperature();
        return entity;
    }

    public static History convert(@Nullable HistoryEntity entity) {
        if (entity == null) {
            return null;
        }
        return new History(
                entity.date,
                entity.time,
                entity.daytimeTemperature,
                entity.nighttimeTemperature
        );
    }
}
