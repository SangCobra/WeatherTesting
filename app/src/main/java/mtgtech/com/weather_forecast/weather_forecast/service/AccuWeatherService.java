package mtgtech.com.weather_forecast.weather_forecast.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import mtgtech.com.weather_forecast.BuildConfig;
import mtgtech.com.weather_forecast.WeatherFlow;
import mtgtech.com.weather_forecast.weather_forecast.SchedulerTransformer;
import mtgtech.com.weather_forecast.weather_forecast.api.AccuWeatherApi;
import mtgtech.com.weather_forecast.weather_forecast.api.WeatherBitApi;
import mtgtech.com.weather_forecast.weather_forecast.converter.AccuResultConverter;
import mtgtech.com.weather_forecast.weather_forecast.interceptor.GzipInterceptor;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.AccuAlertResult;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.AccuAqiResult;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.AccuCurrentResult;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.AccuDailyResult;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.AccuHourlyResult;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.AccuLocationResult;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.AccuMinuteResult;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.CurrentCondition;
import mtgtech.com.weather_forecast.weather_forecast.observer.BaseObserver;
import mtgtech.com.weather_forecast.weather_forecast.observer.ObserverContainer;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;
import retrofit2.Retrofit;

/**
 * Accu weather service.
 */

public class AccuWeatherService extends WeatherService {

    private static final String PREFERENCE_LOCAL = "LOCAL_PREFERENCE";
    private static final String KEY_OLD_DISTRICT = "OLD_DISTRICT";
    private static final String KEY_OLD_CITY = "OLD_CITY";
    private static final String KEY_OLD_PROVINCE = "OLD_PROVINCE";
    private static final String KEY_OLD_KEY = "OLD_KEY";
    String languageCode = "en";
    private AccuWeatherApi api;
    private WeatherBitApi bitApi;
    private CompositeDisposable compositeDisposable;

    public AccuWeatherService() {
        api = new Retrofit.Builder()
                .baseUrl(BuildConfig.ACCU_WEATHER_BASE_URL)
                .client(
                        WeatherFlow.getInstance()
                                .getOkHttpClient()
                                .newBuilder()
                                .addInterceptor(new GzipInterceptor())
                                .build()
                ).addConverterFactory(WeatherFlow.getInstance().getGsonConverterFactory())
                .addCallAdapterFactory(WeatherFlow.getInstance().getRxJava2CallAdapterFactory())
                .build()
                .create((AccuWeatherApi.class));
        compositeDisposable = new CompositeDisposable();
        bitApi = new Retrofit.Builder()
                .baseUrl(BuildConfig.ACCU_WEATHERBIT_BASE_URL)
                .client(
                        WeatherFlow.getInstance()
                                .getOkHttpClient()
                                .newBuilder()
                                .addInterceptor(new GzipInterceptor())
                                .build()
                ).addConverterFactory(WeatherFlow.getInstance().getGsonConverterFactory())
                .addCallAdapterFactory(WeatherFlow.getInstance().getRxJava2CallAdapterFactory())
                .build()
                .create((WeatherBitApi.class));
    }

    public AccuWeatherApi getApi() {
        return api;
    }

    public void setApi(AccuWeatherApi api) {
        this.api = api;
    }

    @Override
    public void requestWeather(Context context, Location location, @NonNull RequestWeatherCallback callback) {
//        String languageCode = SettingsOptionManager.getInstance(context).getLanguage().getCode();

        Observable<List<AccuCurrentResult>> realtime = api.getCurrent(
                location.getCityId(), BuildConfig.ACCU_CURRENT_KEY, languageCode, true);

        Observable<AccuDailyResult> daily = api.getDaily(
                location.getCityId(), BuildConfig.ACCU_WEATHER_KEY, languageCode, true, true);

        Observable<List<AccuHourlyResult>> hourly = api.getHourly(
                location.getCityId(), BuildConfig.ACCU_WEATHER_KEY, languageCode, true, true);

        Observable<AccuMinuteResult> minute = api.getMinutely(
                BuildConfig.ACCU_WEATHER_KEY,
                languageCode,
                true,
                location.getLatitude() + "," + location.getLongitude()
        ).onExceptionResumeNext(
                Observable.create(emitter -> emitter.onNext(new EmptyMinuteResult()))
        );

        Observable<List<AccuAlertResult>> alert = api.getAlert(
                location.getCityId(), BuildConfig.ACCU_WEATHER_KEY, languageCode, true);

//        Observable<AccuAqiResult> aqi = api.getAirQuality(
//                location.getCityId(),
//                BuildConfig.ACCU_AQI_KEY
//        ).onExceptionResumeNext(
//                Observable.create(emitter -> emitter.onNext(new EmptyAqiResult()))
//        );
        Observable<CurrentCondition> aqi = bitApi.getCurrentAQI(location.getLatitude(), location.getLongitude(), BuildConfig.ACCU_WEATHERBIT_KEY
        ).onExceptionResumeNext(
                Observable.create(emitter -> emitter.onNext(new EmptyAqiResultBit()))
        );


        Observable.zip(realtime, daily, hourly, minute, alert, aqi,
                (accuRealtimeResults,
                 accuDailyResult, accuHourlyResults, accuMinuteResult,
                 accuAlertResults,
                 accuAqiResult) -> AccuResultConverter.convert(
                        context,
                        location,
                        accuRealtimeResults.get(0),
                        accuDailyResult,
                        accuHourlyResults,
                        accuMinuteResult instanceof EmptyMinuteResult ? null : accuMinuteResult,
                        accuAqiResult instanceof EmptyAqiResultBit ? null : accuAqiResult,
                        accuAlertResults
                )
        ).compose(SchedulerTransformer.create())
                .subscribe(new ObserverContainer<>(compositeDisposable, new BaseObserver<Weather>() {
                    @Override
                    public void onSucceed(Weather weather) {
                        if (weather != null) {
                            location.setWeather(weather);
                            callback.requestWeatherSuccess(location);
                        } else {
                            onFailed();
                        }
                    }

                    @Override
                    public void onFailed() {
                        callback.requestWeatherFailed(location);
                    }
                }));
    }

    @Override
    @NonNull
    public List<Location> requestLocation(Context context, String query) {
//        String languageCode = SettingsOptionManager.getInstance(context).getLanguage().getCode();
        List<AccuLocationResult> resultList = null;
        try {
            resultList = api.callWeatherLocation(
                    "Always",
                    BuildConfig.ACCU_WEATHER_KEY,
                    query,
                    languageCode
            ).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String zipCode = query.matches("[a-zA-Z0-9]*") ? query : null;

        List<Location> locationList = new ArrayList<>();
        if (resultList != null && resultList.size() != 0) {
            for (AccuLocationResult r : resultList) {
                locationList.add(AccuResultConverter.convert(null, r, zipCode));
            }
        }
        return locationList;
    }

    @Override
    public void requestLocation(Context context, Location location,
                                @NonNull RequestLocationCallback callback) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_LOCAL,
                Context.MODE_PRIVATE
        );
        String oldDistrict = sharedPreferences.getString(KEY_OLD_DISTRICT, "");
        String oldCity = sharedPreferences.getString(KEY_OLD_CITY, "");
        String oldProvince = sharedPreferences.getString(KEY_OLD_PROVINCE, "");
        String oldKey = sharedPreferences.getString(KEY_OLD_KEY, "");

        if (location.hasGeocodeInformation()
                && queryEqualsIgnoreEmpty(location.getDistrict(), oldDistrict)
                && queryEquals(location.getCity(), oldCity)
                && queryEquals(location.getProvince(), oldProvince)
                && queryEquals(location.getCityId(), oldKey)) {
            List<Location> locationList = new ArrayList<>();
            locationList.add(location);
            callback.requestLocationSuccess(
                    location.getLatitude() + "," + location.getLongitude(),
                    locationList
            );
            return;
        }

        sharedPreferences.edit()
                .putString(KEY_OLD_DISTRICT, location.getDistrict())
                .putString(KEY_OLD_CITY, location.getCity())
                .putString(KEY_OLD_PROVINCE, location.getProvince())
                .apply();

//        String languageCode = SettingsOptionManager.getInstance(context).getLanguage().getCode();
        final CacheLocationRequestCallback finalCallback = new CacheLocationRequestCallback(context, callback);

        api.getWeatherLocationByGeoPosition(
                "Always",
                BuildConfig.ACCU_WEATHER_KEY,
                location.getLatitude() + "," + location.getLongitude(),
                languageCode
        ).compose(SchedulerTransformer.create())
                .subscribe(new ObserverContainer<>(compositeDisposable, new BaseObserver<AccuLocationResult>() {
                    @Override
                    public void onSucceed(AccuLocationResult accuLocationResult) {
                        if (accuLocationResult != null) {
                            List<Location> locationList = new ArrayList<>();
                            locationList.add(AccuResultConverter.convert(location, accuLocationResult, null));
                            finalCallback.requestLocationSuccess(
                                    location.getLatitude() + "," + location.getLongitude(), locationList);
                        } else {
                            onFailed();
                        }
                    }

                    @Override
                    public void onFailed() {
                        finalCallback.requestLocationFailed(
                                location.getLatitude() + "," + location.getLongitude());
                    }
                }));
    }

    public void requestLocation(Context context, String query,
                                @NonNull RequestLocationCallback callback) {
//        String languageCode = SettingsOptionManager.getInstance(context).getLanguage().getCode();
        String zipCode = query.matches("[a-zA-Z0-9]") ? query : null;

        api.getWeatherLocation("Always", BuildConfig.ACCU_WEATHER_KEY, query, languageCode)
                .compose(SchedulerTransformer.create())
                .subscribe(new ObserverContainer<>(compositeDisposable, new BaseObserver<List<AccuLocationResult>>() {
                    @Override
                    public void onSucceed(List<AccuLocationResult> accuLocationResults) {
                        if (accuLocationResults != null && accuLocationResults.size() != 0) {
                            List<Location> locationList = new ArrayList<>();
                            for (AccuLocationResult r : accuLocationResults) {
                                locationList.add(AccuResultConverter.convert(null, r, zipCode));
                            }
                            callback.requestLocationSuccess(query, locationList);
                        } else {
                            callback.requestLocationFailed(query);
                        }
                    }

                    @Override
                    public void onFailed() {
                        callback.requestLocationFailed(query);
                    }
                }));
    }

    @Override
    public void cancel() {
        compositeDisposable.clear();
    }

    private boolean queryEquals(String a, String b) {
        if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(b)) {
            return a.equals(b);
        }
        return false;
    }

    private boolean queryEqualsIgnoreEmpty(String a, String b) {
        if (TextUtils.isEmpty(a) && TextUtils.isEmpty(b)) {
            return true;
        }
        if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(b)) {
            return a.equals(b);
        }
        return false;
    }

    private class CacheLocationRequestCallback implements RequestLocationCallback {

        private Context context;
        @NonNull
        private RequestLocationCallback callback;

        CacheLocationRequestCallback(Context context, @NonNull RequestLocationCallback callback) {
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void requestLocationSuccess(String query, List<Location> locationList) {
            if (!TextUtils.isEmpty(locationList.get(0).getCityId())) {
                context.getSharedPreferences(PREFERENCE_LOCAL, Context.MODE_PRIVATE)
                        .edit()
                        .putString(KEY_OLD_KEY, locationList.get(0).getCityId())
                        .apply();
            }
            callback.requestLocationSuccess(query, locationList);
        }

        @Override
        public void requestLocationFailed(String query) {
            context.getSharedPreferences(PREFERENCE_LOCAL, Context.MODE_PRIVATE)
                    .edit()
                    .putString(KEY_OLD_DISTRICT, "")
                    .putString(KEY_OLD_CITY, "")
                    .putString(KEY_OLD_PROVINCE, "")
                    .putString(KEY_OLD_KEY, "")
                    .apply();
            callback.requestLocationFailed(query);
        }
    }

    private class EmptyMinuteResult extends AccuMinuteResult {
    }

    private class EmptyAqiResult extends AccuAqiResult {
    }

    private class EmptyAqiResultBit extends CurrentCondition {
    }
}