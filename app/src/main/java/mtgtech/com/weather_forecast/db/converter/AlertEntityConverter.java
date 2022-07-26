package mtgtech.com.weather_forecast.db.converter;

import java.util.ArrayList;
import java.util.List;

import mtgtech.com.weather_forecast.db.entity.AlertEntity;
import mtgtech.com.weather_forecast.db.propertyConverter.WeatherSourceConverter;
import mtgtech.com.weather_forecast.weather_model.model.option.provider.WeatherSource;
import mtgtech.com.weather_forecast.weather_model.model.weather.Alert;

public class AlertEntityConverter {

    public static AlertEntity convertToEntity(String cityId, WeatherSource source, Alert alert) {
        AlertEntity entity = new AlertEntity();

        entity.cityId = cityId;
        entity.weatherSource = new WeatherSourceConverter().convertToDatabaseValue(source);

        entity.alertId = alert.getAlertId();
        entity.date = alert.getDate();
        entity.time = alert.getTime();

        entity.description = alert.getDescription();
        entity.content = alert.getContent();

        entity.type = alert.getType();
        entity.priority = alert.getPriority();
        entity.color = alert.getColor();

        return entity;
    }

    public static List<AlertEntity> convertToEntityList(String cityId, WeatherSource source,
                                                        List<Alert> alertList) {
        List<AlertEntity> entityList = new ArrayList<>(alertList.size());
        for (Alert alert : alertList) {
            entityList.add(convertToEntity(cityId, source, alert));
        }
        return entityList;
    }

    public static Alert convertToModule(AlertEntity entity) {
        return new Alert(
                entity.alertId,
                entity.date,
                entity.time,
                entity.description,
                entity.content,
                entity.type,
                entity.priority,
                entity.color
        );
    }

    public static List<Alert> convertToModuleList(List<AlertEntity> entityList) {
        List<Alert> dailyList = new ArrayList<>(entityList.size());
        for (AlertEntity entity : entityList) {
            dailyList.add(convertToModule(entity));
        }
        return dailyList;
    }
}
