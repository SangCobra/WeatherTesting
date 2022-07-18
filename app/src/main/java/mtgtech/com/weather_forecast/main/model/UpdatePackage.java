package mtgtech.com.weather_forecast.main.model;

import mtgtech.com.weather_forecast.weather_model.model.location.Location;

public class UpdatePackage {

    public Location location;
    public Indicator indicator;

    public UpdatePackage(Location location, Indicator indicator) {
        this.location = location;
        this.indicator = indicator;
    }
}
