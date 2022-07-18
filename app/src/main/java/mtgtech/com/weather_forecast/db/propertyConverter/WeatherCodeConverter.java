package mtgtech.com.weather_forecast.db.propertyConverter;

import org.greenrobot.greendao.converter.PropertyConverter;

import mtgtech.com.weather_forecast.weather_model.model.weather.WeatherCode;

public class WeatherCodeConverter implements PropertyConverter<WeatherCode, String> {

    @Override
    public WeatherCode convertToEntityProperty(String databaseValue) {
        return WeatherCode.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(WeatherCode entityProperty) {
        return entityProperty.name();
    }
}
