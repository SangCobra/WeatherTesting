package mtgtech.com.weather_forecast.db.converter;

import java.util.ArrayList;
import java.util.List;

import mtgtech.com.weather_forecast.db.entity.HourlyEntity;
import mtgtech.com.weather_forecast.db.propertyConverter.WeatherSourceConverter;
import mtgtech.com.weather_forecast.weather_model.model.option.provider.WeatherSource;
import mtgtech.com.weather_forecast.weather_model.model.weather.Hourly;
import mtgtech.com.weather_forecast.weather_model.model.weather.Precipitation;
import mtgtech.com.weather_forecast.weather_model.model.weather.PrecipitationProbability;
import mtgtech.com.weather_forecast.weather_model.model.weather.Temperature;
import mtgtech.com.weather_forecast.weather_model.model.weather.UV;
import mtgtech.com.weather_forecast.weather_model.model.weather.Wind;
import mtgtech.com.weather_forecast.weather_model.model.weather.WindGust;

public class HourlyEntityConverter {

    public static HourlyEntity convertToEntity(String cityId, WeatherSource source, Hourly hourly) {
        HourlyEntity entity = new HourlyEntity();

        entity.cityId = cityId;
        entity.weatherSource = new WeatherSourceConverter().convertToDatabaseValue(source);

        entity.date = hourly.getDate();
        entity.time = hourly.getTime();
        entity.daylight = hourly.isDaylight();

        entity.weatherCode = hourly.getWeatherCode();
        entity.weatherText = hourly.getWeatherText();

        entity.temperature = hourly.getTemperature().getTemperature();
        entity.realFeelTemperature = hourly.getTemperature().getRealFeelTemperature();
        entity.realFeelShaderTemperature = hourly.getTemperature().getRealFeelShaderTemperature();
        entity.apparentTemperature = hourly.getTemperature().getApparentTemperature();
        entity.windChillTemperature = hourly.getTemperature().getWindChillTemperature();
        entity.wetBulbTemperature = hourly.getTemperature().getWetBulbTemperature();
        entity.degreeDayTemperature = hourly.getTemperature().getDegreeDayTemperature();

        entity.totalPrecipitation = hourly.getPrecipitation().getTotal();
        entity.thunderstormPrecipitation = hourly.getPrecipitation().getThunderstorm();
        entity.rainPrecipitation = hourly.getPrecipitation().getRain();
        entity.snowPrecipitation = hourly.getPrecipitation().getSnow();
        entity.icePrecipitation = hourly.getPrecipitation().getIce();

        entity.totalPrecipitationProbability = hourly.getPrecipitationProbability().getTotal();
        entity.thunderstormPrecipitationProbability = hourly.getPrecipitationProbability().getThunderstorm();
        entity.rainPrecipitationProbability = hourly.getPrecipitationProbability().getRain();
        entity.snowPrecipitationProbability = hourly.getPrecipitationProbability().getSnow();
        entity.icePrecipitationProbability = hourly.getPrecipitationProbability().getIce();
        entity.visibility = hourly.getVisibility();
        entity.dewPoint = hourly.getDewPoint();
        entity.ceiling = hourly.getCeiling();
        entity.cloudCover = hourly.getCloudCover();
        entity.uvDescription = hourly.getUv().getUVDescription();
        entity.uvIndex = hourly.getUv().getIndex();
        entity.uvLevel = hourly.getUv().getLevel();
        entity.windDegree = hourly.getWind().getDegree();
        entity.windDirection = hourly.getWind().getDirection();
        entity.windLevel = hourly.getWind().getLevel();
        entity.windSpeed = hourly.getWind().getSpeed();
        entity.windGustSpeed = hourly.getWindGust().getSpeed();
        entity.relativeHumidity = hourly.getRelativeHumidity();
        entity.indoorHumidity = hourly.getIndoorRelativeHumidity();

        return entity;
    }

    public static List<HourlyEntity> convertToEntityList(String cityId, WeatherSource source,
                                                         List<Hourly> hourlyList) {
        List<HourlyEntity> entityList = new ArrayList<>(hourlyList.size());
        for (Hourly hourly : hourlyList) {
            entityList.add(convertToEntity(cityId, source, hourly));
        }
        return entityList;
    }

    public static Hourly convertToModule(HourlyEntity entity) {
        return new Hourly(
                entity.date, entity.time, entity.daylight,
                entity.weatherText, entity.weatherCode,
                new Temperature(
                        entity.temperature,
                        entity.realFeelTemperature,
                        entity.realFeelShaderTemperature,
                        entity.apparentTemperature,
                        entity.windChillTemperature,
                        entity.wetBulbTemperature,
                        entity.degreeDayTemperature
                ),
                new Precipitation(
                        entity.totalPrecipitation,
                        entity.thunderstormPrecipitation,
                        entity.rainPrecipitation,
                        entity.snowPrecipitation,
                        entity.icePrecipitation
                ),
                new WindGust(entity.windGustSpeed),
                new Wind(entity.getWindDirection(), entity.getWindDegree(), entity.windSpeed, entity.windLevel),
                entity.visibility, entity.dewPoint, entity.cloudCover, entity.ceiling, new UV(entity.uvIndex, entity.uvLevel, entity.uvDescription),
                new PrecipitationProbability(
                        entity.totalPrecipitationProbability,
                        entity.thunderstormPrecipitationProbability,
                        entity.rainPrecipitationProbability,
                        entity.snowPrecipitationProbability,
                        entity.icePrecipitationProbability
                ),
                entity.relativeHumidity,
                entity.indoorHumidity
        );
    }

    public static List<Hourly> convertToModuleList(List<HourlyEntity> entityList) {
        List<Hourly> dailyList = new ArrayList<>(entityList.size());
        for (HourlyEntity entity : entityList) {
            dailyList.add(convertToModule(entity));
        }
        return dailyList;
    }
}
