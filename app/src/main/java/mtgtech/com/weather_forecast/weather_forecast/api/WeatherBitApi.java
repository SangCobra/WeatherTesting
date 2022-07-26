package mtgtech.com.weather_forecast.weather_forecast.api;


import io.reactivex.Observable;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.CurrentCondition;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherBitApi {
    @GET("current/airquality")
    Observable<CurrentCondition> getCurrentAQI(@Query("lat") double lat,
                                               @Query("lon") double lon,
                                               @Query("key") String key);
}
