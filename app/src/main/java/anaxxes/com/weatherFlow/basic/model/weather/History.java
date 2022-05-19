package anaxxes.com.weatherFlow.basic.model.weather;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;

import anaxxes.com.weatherFlow.basic.model.option.unit.TemperatureUnit;

/**
 * History.
 *
 * All properties are {@link androidx.annotation.NonNull}.
 * */
public class History implements Serializable {

    private Date date;
    private long time;

    private int daytimeTemperature;
    private int nighttimeTemperature;

    public History(Date date, long time, int daytimeTemperature, int nighttimeTemperature) {
        this.date = date;
        this.time = time;
        this.daytimeTemperature = daytimeTemperature;
        this.nighttimeTemperature = nighttimeTemperature;
    }

    public Date getDate() {
        return date;
    }

    public long getTime() {
        return time;
    }

    public int getDaytimeTemperature() {
        return daytimeTemperature;
    }

    public int getNighttimeTemperature() {
        return nighttimeTemperature;
    }

    public String getDaytimeTemperature(Context context, TemperatureUnit unit) {
        return unit.getTemperatureTextWithoutDegree(context,daytimeTemperature);

    }

    public String getNighttimeTemperature(Context context, TemperatureUnit unit) {
       return unit.getTemperatureTextWithoutDegree(context,nighttimeTemperature);
    }
}
