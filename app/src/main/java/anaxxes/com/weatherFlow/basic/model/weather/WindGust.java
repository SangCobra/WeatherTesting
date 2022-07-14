package anaxxes.com.weatherFlow.basic.model.weather;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class WindGust implements Serializable {
    @NonNull private Float speed;

    public WindGust(@NonNull Float speed) {
        this.speed = speed;
    }

    @NonNull
    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(@NonNull Float speed) {
        this.speed = speed;
    }
}
