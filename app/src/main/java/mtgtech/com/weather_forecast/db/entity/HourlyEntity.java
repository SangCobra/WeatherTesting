package mtgtech.com.weather_forecast.db.entity;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

import mtgtech.com.weather_forecast.weather_model.model.weather.WeatherCode;
import mtgtech.com.weather_forecast.weather_model.model.weather.WindDegree;
import mtgtech.com.weather_forecast.db.propertyConverter.WeatherCodeConverter;
import mtgtech.com.weather_forecast.db.propertyConverter.WindDegreeConverter;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Hourly entity.
 * <p>
 * {@link mtgtech.com.weather_forecast.weather_model.model.weather.Hourly}.
 */
@Entity
public class HourlyEntity {

    @Id
    public Long id;
    public String cityId;
    public String weatherSource;

    public Date date;
    public long time;
    public boolean daylight;

    public String weatherText;
    @Convert(converter = WeatherCodeConverter.class, columnType = String.class)
    public WeatherCode weatherCode;

    public int temperature;
    public Integer realFeelTemperature;
    public Integer realFeelShaderTemperature;
    public Integer apparentTemperature;
    public Integer windChillTemperature;
    public Integer wetBulbTemperature;
    public Integer degreeDayTemperature;

    public Float totalPrecipitation;
    public Float thunderstormPrecipitation;
    public Float rainPrecipitation;
    public Float snowPrecipitation;
    public Float icePrecipitation;

    public Float totalPrecipitationProbability;
    public Float thunderstormPrecipitationProbability;
    public Float rainPrecipitationProbability;
    public Float snowPrecipitationProbability;
    public Float icePrecipitationProbability;


    public Float visibility;


    public Integer dewPoint;
    public Integer cloudCover;
    public Float ceiling;

    //Wind
    public String windDirection;
    @Convert(converter = WindDegreeConverter.class, columnType = Float.class)
    public WindDegree windDegree;
    public Float windSpeed;
    public String windLevel;

    //WindGust
    public Float windGustSpeed;

    //UV
    public Integer uvIndex;
    public String uvLevel;
    public String uvDescription;
    //Humidity
    public Integer relativeHumidity;
    public Integer indoorHumidity;

    @Generated(hash = 892631300)
    public HourlyEntity(Long id, String cityId, String weatherSource, Date date, long time,
            boolean daylight, String weatherText, WeatherCode weatherCode, int temperature,
            Integer realFeelTemperature, Integer realFeelShaderTemperature,
            Integer apparentTemperature, Integer windChillTemperature,
            Integer wetBulbTemperature, Integer degreeDayTemperature,
            Float totalPrecipitation, Float thunderstormPrecipitation,
            Float rainPrecipitation, Float snowPrecipitation, Float icePrecipitation,
            Float totalPrecipitationProbability, Float thunderstormPrecipitationProbability,
            Float rainPrecipitationProbability, Float snowPrecipitationProbability,
            Float icePrecipitationProbability, Float visibility, Integer dewPoint,
            Integer cloudCover, Float ceiling, String windDirection, WindDegree windDegree,
            Float windSpeed, String windLevel, Float windGustSpeed, Integer uvIndex,
            String uvLevel, String uvDescription, Integer relativeHumidity,
            Integer indoorHumidity) {
        this.id = id;
        this.cityId = cityId;
        this.weatherSource = weatherSource;
        this.date = date;
        this.time = time;
        this.daylight = daylight;
        this.weatherText = weatherText;
        this.weatherCode = weatherCode;
        this.temperature = temperature;
        this.realFeelTemperature = realFeelTemperature;
        this.realFeelShaderTemperature = realFeelShaderTemperature;
        this.apparentTemperature = apparentTemperature;
        this.windChillTemperature = windChillTemperature;
        this.wetBulbTemperature = wetBulbTemperature;
        this.degreeDayTemperature = degreeDayTemperature;
        this.totalPrecipitation = totalPrecipitation;
        this.thunderstormPrecipitation = thunderstormPrecipitation;
        this.rainPrecipitation = rainPrecipitation;
        this.snowPrecipitation = snowPrecipitation;
        this.icePrecipitation = icePrecipitation;
        this.totalPrecipitationProbability = totalPrecipitationProbability;
        this.thunderstormPrecipitationProbability = thunderstormPrecipitationProbability;
        this.rainPrecipitationProbability = rainPrecipitationProbability;
        this.snowPrecipitationProbability = snowPrecipitationProbability;
        this.icePrecipitationProbability = icePrecipitationProbability;
        this.visibility = visibility;
        this.dewPoint = dewPoint;
        this.cloudCover = cloudCover;
        this.ceiling = ceiling;
        this.windDirection = windDirection;
        this.windDegree = windDegree;
        this.windSpeed = windSpeed;
        this.windLevel = windLevel;
        this.windGustSpeed = windGustSpeed;
        this.uvIndex = uvIndex;
        this.uvLevel = uvLevel;
        this.uvDescription = uvDescription;
        this.relativeHumidity = relativeHumidity;
        this.indoorHumidity = indoorHumidity;
    }


    @Generated(hash = 617074574)
    public HourlyEntity() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityId() {
        return this.cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getWeatherSource() {
        return this.weatherSource;
    }

    public void setWeatherSource(String weatherSource) {
        this.weatherSource = weatherSource;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean getDaylight() {
        return this.daylight;
    }

    public void setDaylight(boolean daylight) {
        this.daylight = daylight;
    }

    public String getWeatherText() {
        return this.weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }

    public WeatherCode getWeatherCode() {
        return this.weatherCode;
    }

    public void setWeatherCode(WeatherCode weatherCode) {
        this.weatherCode = weatherCode;
    }

    public int getTemperature() {
        return this.temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public Integer getRealFeelTemperature() {
        return this.realFeelTemperature;
    }

    public void setRealFeelTemperature(Integer realFeelTemperature) {
        this.realFeelTemperature = realFeelTemperature;
    }

    public Integer getRealFeelShaderTemperature() {
        return this.realFeelShaderTemperature;
    }

    public void setRealFeelShaderTemperature(Integer realFeelShaderTemperature) {
        this.realFeelShaderTemperature = realFeelShaderTemperature;
    }

    public Integer getApparentTemperature() {
        return this.apparentTemperature;
    }

    public void setApparentTemperature(Integer apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
    }

    public Integer getWindChillTemperature() {
        return this.windChillTemperature;
    }

    public void setWindChillTemperature(Integer windChillTemperature) {
        this.windChillTemperature = windChillTemperature;
    }

    public Integer getWetBulbTemperature() {
        return this.wetBulbTemperature;
    }

    public void setWetBulbTemperature(Integer wetBulbTemperature) {
        this.wetBulbTemperature = wetBulbTemperature;
    }

    public Integer getDegreeDayTemperature() {
        return this.degreeDayTemperature;
    }

    public void setDegreeDayTemperature(Integer degreeDayTemperature) {
        this.degreeDayTemperature = degreeDayTemperature;
    }

    public Float getTotalPrecipitation() {
        return this.totalPrecipitation;
    }

    public void setTotalPrecipitation(Float totalPrecipitation) {
        this.totalPrecipitation = totalPrecipitation;
    }

    public Float getThunderstormPrecipitation() {
        return this.thunderstormPrecipitation;
    }

    public void setThunderstormPrecipitation(Float thunderstormPrecipitation) {
        this.thunderstormPrecipitation = thunderstormPrecipitation;
    }

    public Float getRainPrecipitation() {
        return this.rainPrecipitation;
    }

    public void setRainPrecipitation(Float rainPrecipitation) {
        this.rainPrecipitation = rainPrecipitation;
    }

    public Float getSnowPrecipitation() {
        return this.snowPrecipitation;
    }

    public void setSnowPrecipitation(Float snowPrecipitation) {
        this.snowPrecipitation = snowPrecipitation;
    }

    public Float getIcePrecipitation() {
        return this.icePrecipitation;
    }

    public void setIcePrecipitation(Float icePrecipitation) {
        this.icePrecipitation = icePrecipitation;
    }

    public Float getTotalPrecipitationProbability() {
        return this.totalPrecipitationProbability;
    }

    public void setTotalPrecipitationProbability(
            Float totalPrecipitationProbability) {
        this.totalPrecipitationProbability = totalPrecipitationProbability;
    }

    public Float getThunderstormPrecipitationProbability() {
        return this.thunderstormPrecipitationProbability;
    }

    public void setThunderstormPrecipitationProbability(
            Float thunderstormPrecipitationProbability) {
        this.thunderstormPrecipitationProbability = thunderstormPrecipitationProbability;
    }

    public Float getRainPrecipitationProbability() {
        return this.rainPrecipitationProbability;
    }

    public void setRainPrecipitationProbability(
            Float rainPrecipitationProbability) {
        this.rainPrecipitationProbability = rainPrecipitationProbability;
    }

    public Float getSnowPrecipitationProbability() {
        return this.snowPrecipitationProbability;
    }

    public void setSnowPrecipitationProbability(
            Float snowPrecipitationProbability) {
        this.snowPrecipitationProbability = snowPrecipitationProbability;
    }

    public Float getIcePrecipitationProbability() {
        return this.icePrecipitationProbability;
    }

    public void setIcePrecipitationProbability(Float icePrecipitationProbability) {
        this.icePrecipitationProbability = icePrecipitationProbability;
    }


    public Float getVisibility() {
        return visibility;
    }

    public void setVisibility(Float visibility) {
        this.visibility = visibility;
    }

    public Integer getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(Integer dewPoint) {
        this.dewPoint = dewPoint;
    }

    public Integer getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(Integer cloudCover) {
        this.cloudCover = cloudCover;
    }

    public Float getCeiling() {
        return ceiling;
    }

    public void setCeiling(Float ceiling) {
        this.ceiling = ceiling;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public WindDegree getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(WindDegree windDegree) {
        this.windDegree = windDegree;
    }

    public Float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindLevel() {
        return windLevel;
    }

    public void setWindLevel(String windLevel) {
        this.windLevel = windLevel;
    }

    public Integer getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(Integer uvIndex) {
        this.uvIndex = uvIndex;
    }

    public String getUvLevel() {
        return uvLevel;
    }

    public void setUvLevel(String uvLevel) {
        this.uvLevel = uvLevel;
    }

    public String getUvDescription() {
        return uvDescription;
    }

    public void setUvDescription(String uvDescription) {
        this.uvDescription = uvDescription;
    }


    public Float getWindGustSpeed() {
        return this.windGustSpeed;
    }


    public void setWindGustSpeed(Float windGustSpeed) {
        this.windGustSpeed = windGustSpeed;
    }


    public Integer getRelativeHumidity() {
        return this.relativeHumidity;
    }


    public void setRelativeHumidity(Integer relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }


    public Integer getIndoorHumidity() {
        return this.indoorHumidity;
    }


    public void setIndoorHumidity(Integer indoorHumidity) {
        this.indoorHumidity = indoorHumidity;
    }

}
