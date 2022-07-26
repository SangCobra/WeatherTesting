package com.mtgtech.weather_forecast.weather_forecast;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import com.mtgtech.weather_forecast.db.DatabaseHelper;
import com.mtgtech.weather_forecast.weather_forecast.observer.BaseObserver;
import com.mtgtech.weather_forecast.weather_forecast.observer.ObserverContainer;
import com.mtgtech.weather_forecast.weather_forecast.service.AccuWeatherService;
import com.mtgtech.weather_forecast.weather_forecast.service.WeatherService;
import com.mtgtech.weather_forecast.weather_model.model.location.Location;
import com.mtgtech.weather_forecast.weather_model.model.option.provider.WeatherSource;
import com.mtgtech.weather_forecast.weather_model.model.weather.Weather;

/**
 * Weather helper.
 */

public class WeatherHelper {

    @Nullable
    private WeatherService weatherService;

    @Nullable
    private WeatherService[] searchServices;
    private CompositeDisposable compositeDisposable;

    public WeatherHelper() {
        weatherService = null;
        searchServices = null;
        compositeDisposable = new CompositeDisposable();
    }

    @NonNull
    private static WeatherService getWeatherService(WeatherSource source) {
        switch (source) {
            default: // ACCU.
                return new AccuWeatherService();
        }
    }

    public void requestWeather(Context c, Location location, @NonNull final OnRequestWeatherListener l) {
        weatherService = getWeatherService(location.getWeatherSource());
        weatherService.requestWeather(c, location, new WeatherService.RequestWeatherCallback() {

            @Override
            public void requestWeatherSuccess(@NonNull Location requestLocation) {
                Weather weather = requestLocation.getWeather();
                if (weather != null) {
                    DatabaseHelper.getInstance(c).writeWeather(requestLocation, weather);
                    if (weather.getYesterday() == null) {
                        weather.setYesterday(
                                DatabaseHelper.getInstance(c).readHistory(requestLocation, weather));
                    }
                    l.requestWeatherSuccess(requestLocation);
                } else {
                    requestWeatherFailed(requestLocation);
                }
            }

            @Override
            public void requestWeatherFailed(@NonNull Location requestLocation) {
                requestLocation.setWeather(DatabaseHelper.getInstance(c).readWeather(requestLocation));
                l.requestWeatherFailed(requestLocation);
            }
        });
    }

    public void requestLocation(Context context, String query, @NonNull final OnRequestLocationListener l) {
        searchServices = new WeatherService[]{
                getWeatherService(WeatherSource.ACCU)
        };

        Observable<List<Location>> accu = Observable.create(emitter ->
                emitter.onNext(searchServices[0].requestLocation(context, query)));

        accu.compose(SchedulerTransformer.create())
                .subscribe(new ObserverContainer<>(compositeDisposable, new BaseObserver<List<Location>>() {
                    @Override
                    public void onSucceed(List<Location> locationList) {
                        if (locationList != null && locationList.size() != 0) {
                            l.requestLocationSuccess(query, locationList);
                        } else {
                            onFailed();
                        }
                    }

                    @Override
                    public void onFailed() {
                        l.requestLocationFailed(query);
                    }
                }));
    }

    public void cancel() {
        if (weatherService != null) {
            weatherService.cancel();
        }
        if (searchServices != null) {
            for (WeatherService s : searchServices) {
                if (s != null) {
                    s.cancel();
                }
            }
        }
        compositeDisposable.clear();
    }

    // interface.

    public interface OnRequestWeatherListener {
        void requestWeatherSuccess(@NonNull Location requestLocation);

        void requestWeatherFailed(@NonNull Location requestLocation);
    }

    public interface OnRequestLocationListener {
        void requestLocationSuccess(String query, List<Location> locationList);

        void requestLocationFailed(String query);
    }
}
