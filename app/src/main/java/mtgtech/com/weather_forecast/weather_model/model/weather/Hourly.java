package mtgtech.com.weather_forecast.weather_model.model.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.BidiFormatter;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.utils.DisplayUtils;
import mtgtech.com.weather_forecast.utils.manager.TimeManager;

/**
 * Hourly.
 * <p>
 * All properties are {@link androidx.annotation.NonNull}.
 */
public class Hourly implements Serializable {

    private Date date;
    private long time;
    private boolean daylight;

    private String weatherText;
    private WeatherCode weatherCode;

    private Temperature temperature;
    private Precipitation precipitation;
    private WindGust windGust;
    private Wind wind;
    @Nullable
    private Float visibility;


    @Nullable
    private Integer dewPoint;
    @Nullable
    private Integer cloudCover;
    @Nullable
    private Float ceiling;
    @Nullable
    private UV uv;
    private PrecipitationProbability precipitationProbability;
    private Integer relativeHumidity;
    private Integer indoorRelativeHumidity;
    private boolean isExpand;

    public Hourly() {

    }

    public Hourly(Date date, long time, boolean daylight,
                  String weatherText, WeatherCode weatherCode,
                  Temperature temperature,
                  Precipitation precipitation, WindGust windGust, Wind wind,
                  @Nullable Float visibility,
                  @Nullable Integer dewPoint,
                  @Nullable Integer cloudCover,
                  @Nullable Float ceiling,
                  @Nullable UV uv,
                  PrecipitationProbability precipitationProbability,
                  Integer relativeHumidity,
                  Integer indoorRelativeHumidity) {
        this.date = date;
        this.time = time;
        this.daylight = daylight;
        this.weatherText = weatherText;
        this.weatherCode = weatherCode;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.precipitationProbability = precipitationProbability;
        this.windGust = windGust;
        this.wind = wind;
        this.visibility = visibility;
        this.dewPoint = dewPoint;
        this.cloudCover = cloudCover;
        this.ceiling = ceiling;
        this.uv = uv;
        this.relativeHumidity = relativeHumidity;
        this.indoorRelativeHumidity = indoorRelativeHumidity;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public Integer getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(Integer relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public Integer getIndoorRelativeHumidity() {
        return indoorRelativeHumidity;
    }

    public void setIndoorRelativeHumidity(Integer indoorRelativeHumidity) {
        this.indoorRelativeHumidity = indoorRelativeHumidity;
    }

    public WindGust getWindGust() {
        return windGust;
    }

    public void setWindGust(WindGust windGust) {
        this.windGust = windGust;
    }

    public Date getDate() {
        return date;
    }

    public long getTime() {
        return time;
    }

    public boolean isDaylight() {
        return daylight;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public WeatherCode getWeatherCode() {
        return weatherCode;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public Precipitation getPrecipitation() {
        return precipitation;
    }

    public PrecipitationProbability getPrecipitationProbability() {
        return precipitationProbability;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }


    @Nullable
    public Float getVisibility() {
        return visibility;
    }

    @Nullable
    public Integer getDewPoint() {
        return dewPoint;
    }

    @Nullable
    public Integer getCloudCover() {
        return cloudCover;
    }

    @Nullable
    public Float getCeiling() {
        return ceiling;
    }

    @Nullable
    public UV getUv() {
        return uv;
    }

    @SuppressLint("DefaultLocale")
    public String getHour(Context c) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hour;
        if (TimeManager.is12Hour(c)) {
            hour = calendar.get(Calendar.HOUR);
            if (hour == 0) {
                hour = 12;
            }
        } else {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
        }

        if (DisplayUtils.isRtl(c)) {
            return BidiFormatter.getInstance().unicodeWrap(String.format("%d", hour))
                    + c.getString(R.string.of_clock);
        } else {
            return hour + c.getString(R.string.of_clock);
        }
    }


}
