package mtgtech.com.weather_forecast.weather_model.model.weather;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Humidity implements Serializable {
    @NonNull private Integer relativeHumidity;
    @NonNull private Integer indoorHumidity;

    public Humidity(@NonNull Integer relativeHumidity, @NonNull Integer indoorHumidity) {
        this.relativeHumidity = relativeHumidity;
        this.indoorHumidity = indoorHumidity;
    }

    @NonNull
    public Integer getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(@NonNull Integer relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    @NonNull
    public Integer getIndoorHumidity() {
        return indoorHumidity;
    }

    public void setIndoorHumidity(@NonNull Integer indoorHumidity) {
        this.indoorHumidity = indoorHumidity;
    }
}
