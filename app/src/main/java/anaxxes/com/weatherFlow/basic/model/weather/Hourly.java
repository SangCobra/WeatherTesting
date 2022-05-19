package anaxxes.com.weatherFlow.basic.model.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.BidiFormatter;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.utils.DisplayUtils;
import anaxxes.com.weatherFlow.utils.manager.TimeManager;

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

    public Hourly(Date date, long time, boolean daylight,
                  String weatherText, WeatherCode weatherCode,
                  Temperature temperature,
                  Precipitation precipitation, Wind wind,
                  @Nullable Float visibility,
                  @Nullable Integer dewPoint,
                  @Nullable Integer cloudCover,
                  @Nullable Float ceiling,
                  @Nullable UV uv,
                  PrecipitationProbability precipitationProbability) {
        this.date = date;
        this.time = time;
        this.daylight = daylight;
        this.weatherText = weatherText;
        this.weatherCode = weatherCode;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.precipitationProbability = precipitationProbability;
        this.wind = wind;
        this.visibility = visibility;
        this.dewPoint = dewPoint;
        this.cloudCover = cloudCover;
        this.ceiling = ceiling;
        this.uv = uv;
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
