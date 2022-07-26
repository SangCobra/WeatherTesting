package mtgtech.com.weather_forecast.models;

public class AirQualityModel {
    private int color;
    private String adjustAir, numberRange;

    public AirQualityModel() {
    }

    public AirQualityModel(int color, String adjustAir, String numberRange) {
        this.color = color;
        this.adjustAir = adjustAir;
        this.numberRange = numberRange;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getAdjustAir() {
        return adjustAir;
    }

    public void setAdjustAir(String adjustAir) {
        this.adjustAir = adjustAir;
    }

    public String getNumberRange() {
        return numberRange;
    }

    public void setNumberRange(String numberRange) {
        this.numberRange = numberRange;
    }
}
