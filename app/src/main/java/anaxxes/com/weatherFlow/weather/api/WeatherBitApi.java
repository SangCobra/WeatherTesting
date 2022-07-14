package anaxxes.com.weatherFlow.weather.api;


import anaxxes.com.weatherFlow.weather.json.accu.CurrentCondition;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherBitApi {
    @GET("current/airquality")
    Observable<CurrentCondition> getCurrentAQI(@Query("lat") double lat,
                                               @Query("lon") double lon,
                                               @Query("key") String key);
}
