package mtgtech.com.weather_forecast.location_service;

public class LocationException extends Exception {

    public LocationException(int code, String msg) {
        super(msg + "(code = " + code + ")");
    }
}
