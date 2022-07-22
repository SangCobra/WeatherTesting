package mtgtech.com.weather_forecast.weather_forecast.converter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import mtgtech.com.weather_forecast.weather_model.model.location.Location;
import mtgtech.com.weather_forecast.weather_model.model.option.provider.WeatherSource;
import mtgtech.com.weather_forecast.weather_model.model.weather.AirQuality;
import mtgtech.com.weather_forecast.weather_model.model.weather.Alert;
import mtgtech.com.weather_forecast.weather_model.model.weather.Astro;
import mtgtech.com.weather_forecast.weather_model.model.weather.Base;
import mtgtech.com.weather_forecast.weather_model.model.weather.Current;
import mtgtech.com.weather_forecast.weather_model.model.weather.Daily;
import mtgtech.com.weather_forecast.weather_model.model.weather.HalfDay;
import mtgtech.com.weather_forecast.weather_model.model.weather.History;
import mtgtech.com.weather_forecast.weather_model.model.weather.Hourly;
import mtgtech.com.weather_forecast.weather_model.model.weather.Minutely;
import mtgtech.com.weather_forecast.weather_model.model.weather.MoonPhase;
import mtgtech.com.weather_forecast.weather_model.model.weather.Pollen;
import mtgtech.com.weather_forecast.weather_model.model.weather.Precipitation;
import mtgtech.com.weather_forecast.weather_model.model.weather.PrecipitationDuration;
import mtgtech.com.weather_forecast.weather_model.model.weather.PrecipitationProbability;
import mtgtech.com.weather_forecast.weather_model.model.weather.Temperature;
import mtgtech.com.weather_forecast.weather_model.model.weather.UV;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;
import mtgtech.com.weather_forecast.weather_model.model.weather.WeatherCode;
import mtgtech.com.weather_forecast.weather_model.model.weather.Wind;
import mtgtech.com.weather_forecast.weather_model.model.weather.WindDegree;
import mtgtech.com.weather_forecast.weather_model.model.weather.WindGust;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.AccuAlertResult;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.AccuDailyResult;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.AccuHourlyResult;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.AccuLocationResult;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.AccuMinuteResult;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.AccuCurrentResult;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.CurrentCondition;

public class AccuResultConverter {

    public static Location convert(@Nullable Location location, AccuLocationResult result,
                                   @Nullable String zipCode) {
        if (location != null
                && !TextUtils.isEmpty(location.getProvince())
                && !TextUtils.isEmpty(location.getCity())
                && !TextUtils.isEmpty(location.getDistrict())) {
            return new Location(
                    result.Key,
                    (float) result.GeoPosition.Latitude,
                    (float) result.GeoPosition.Longitude,
                    TimeZone.getTimeZone(result.TimeZone.Name),
                    result.Country.EnglishName,
                    location.getProvince(),
                    result.EnglishName,
                    location.getDistrict() + (zipCode == null ? "" : (" (" + zipCode + ")")),
                    null,
                    WeatherSource.ACCU,
                    false,
                    false,
                    !TextUtils.isEmpty(result.Country.ID)
                            && (result.Country.ID.equals("CN")
                            || result.Country.ID.equals("cn")
                            || result.Country.ID.equals("HK")
                            || result.Country.ID.equals("hk")
                            || result.Country.ID.equals("TW")
                            || result.Country.ID.equals("tw"))
            );
        } else {
            return new Location(
                    result.Key,
                    (float) result.GeoPosition.Latitude,
                    (float) result.GeoPosition.Longitude,
                    TimeZone.getTimeZone(result.TimeZone.Name),
                    result.Country.EnglishName,
                    result.AdministrativeArea == null ? "" : result.AdministrativeArea.LocalizedName,
                    result.EnglishName,
                    "",
                    null,
                    WeatherSource.ACCU,
                    false,
                    false,
                    !TextUtils.isEmpty(result.Country.ID)
                            && (result.Country.ID.equals("CN")
                            || result.Country.ID.equals("cn")
                            || result.Country.ID.equals("HK")
                            || result.Country.ID.equals("hk")
                            || result.Country.ID.equals("TW")
                            || result.Country.ID.equals("tw"))
            );
        }
    }

    public static Weather convert(Context context,
                                  Location location,
                                  AccuCurrentResult currentResult,
                                  AccuDailyResult dailyResult,
                                  List<AccuHourlyResult> hourlyResultList,
                                  @Nullable AccuMinuteResult minuteResult,
                                  CurrentCondition aqiResult,
                                  List<AccuAlertResult> alertResultList) {
        try {
            return new Weather(
                    new Base(
                            location.getCityId(),
                            System.currentTimeMillis(),
                            new Date(currentResult.EpochTime * 1000),
                            currentResult.EpochTime * 1000,
                            new Date(),
                            System.currentTimeMillis()
                    ),
                    new Current(
                            currentResult.WeatherText,
                            getWeatherCode(currentResult.WeatherIcon),
                            new Temperature(
                                    toInt(currentResult.Temperature.Metric.Value),
                                    toInt(currentResult.RealFeelTemperature.Metric.Value),
                                    toInt(currentResult.RealFeelTemperatureShade.Metric.Value),
                                    toInt(currentResult.ApparentTemperature.Metric.Value),
                                    toInt(currentResult.WindChillTemperature.Metric.Value),
                                    toInt(currentResult.WetBulbTemperature.Metric.Value),
                                    null
                            ),
                            new Precipitation(
                                    (float) currentResult.Precip1hr.Metric.Value,
                                    null,
                                    null,
                                    null,
                                    null
                            ),
                            new PrecipitationProbability(
                                    null,
                                    null,
                                    null,
                                    null,
                                    null
                            ),
                            new Wind(
                                    currentResult.Wind.Direction.Localized,
                                    new WindDegree(currentResult.Wind.Direction.Degrees, false),
                                    (float) currentResult.Wind.Speed.Metric.Value,
                                    CommonConverter.getWindLevel(context, currentResult.Wind.Speed.Metric.Value)
                            ),
                            new UV(currentResult.UVIndex, currentResult.UVIndexText, null),
                            aqiResult == null ? new AirQuality(
                                    null, null, null, null,
                                    null, null, null, null
                            ) : new AirQuality(
                                    CommonConverter.getAqiQuality(context, toInt(aqiResult.getData().get(0).getAqi())),
                                    (toInt(aqiResult.getData().get(0).getAqi())),
                                    (float) toInt(aqiResult.getData().get(0).getPm25()),
                                    (float) toInt(aqiResult.getData().get(0).getPm10()),
                                    (float) toInt(aqiResult.getData().get(0).getSo2()),
                                    (float) toInt(aqiResult.getData().get(0).getNo2()),
                                    (float) toInt(aqiResult.getData().get(0).getO3()),
                                    (float) toInt(aqiResult.getData().get(0).getCo())
                            ),
                            (float) currentResult.RelativeHumidity,
                            (float) currentResult.Pressure.Metric.Value,
                            (float) currentResult.Visibility.Metric.Value,
                            toInt(currentResult.DewPoint.Metric.Value),
                            currentResult.CloudCover,
                            (float) (currentResult.Ceiling.Metric.Value / 1000.0),
                            dailyResult.Headline.Text,
                            minuteResult != null ? minuteResult.Summary.LongPhrase : null
                    ),
                    new History(
                            new Date((currentResult.EpochTime - 24 * 60 * 60) * 1000),
                            (currentResult.EpochTime - 24 * 60 * 60) * 1000,
                            toInt(currentResult.TemperatureSummary.Past24HourRange.Maximum.Metric.Value),
                            toInt(currentResult.TemperatureSummary.Past24HourRange.Minimum.Metric.Value)
                    ),
                    getDailyList(context, dailyResult),
                    getHourlyList(context,hourlyResultList),
                    getMinutelyList(
                            dailyResult.DailyForecasts.get(0).Sun.Rise,
                            dailyResult.DailyForecasts.get(0).Sun.Set,
                            minuteResult
                    ),
                    getAlertList(alertResultList)
            );
        } catch (Exception ignored) {
            return null;
        }
    }

    private static List<Daily> getDailyList(Context context, AccuDailyResult dailyResult) {
        List<Daily> dailyList = new ArrayList<>(dailyResult.DailyForecasts.size());

        for (AccuDailyResult.DailyForecasts forecasts : dailyResult.DailyForecasts) {
            dailyList.add(
                    new Daily(
                            forecasts.Date,
                            forecasts.EpochDate * 1000,
                            new HalfDay(
                                    forecasts.Day.LongPhrase,
                                    forecasts.Day.ShortPhrase,
                                    getWeatherCode(forecasts.Day.Icon),
                                    new Temperature(
                                            toInt(forecasts.Temperature.Maximum.Value),
                                            toInt(forecasts.RealFeelTemperature.Maximum.Value),
                                            toInt(forecasts.RealFeelTemperatureShade.Maximum.Value),
                                            null,
                                            null,
                                            null,
                                            toInt(forecasts.DegreeDaySummary.Heating.Value)
                                    ),
                                    new Precipitation(
                                            (float) forecasts.Day.TotalLiquid.Value,
                                            null,
                                            (float) forecasts.Day.Rain.Value,
                                            (float) (forecasts.Day.Snow.Value * 10),
                                            (float) forecasts.Day.Ice.Value
                                    ),
                                    new PrecipitationProbability(
                                            (float) forecasts.Day.PrecipitationProbability,
                                            (float) forecasts.Day.ThunderstormProbability,
                                            (float) forecasts.Day.RainProbability,
                                            (float) forecasts.Day.SnowProbability,
                                            (float) forecasts.Day.IceProbability
                                    ),
                                    new PrecipitationDuration(
                                            (float) forecasts.Day.HoursOfPrecipitation,
                                            null,
                                            (float) forecasts.Day.HoursOfRain,
                                            (float) forecasts.Day.HoursOfSnow,
                                            (float) forecasts.Day.HoursOfIce
                                    ),
                                    new Wind(
                                            forecasts.Day.Wind.Direction.Localized,
                                            new WindDegree(forecasts.Day.Wind.Direction.Degrees, false),
                                            (float) forecasts.Day.Wind.Speed.Value,
                                            CommonConverter.getWindLevel(context, forecasts.Day.Wind.Speed.Value)
                                    ),
                                    forecasts.Day.CloudCover
                            ),
                            new HalfDay(
                                    forecasts.Night.LongPhrase,
                                    forecasts.Night.ShortPhrase,
                                    getWeatherCode(forecasts.Night.Icon),
                                    new Temperature(
                                            toInt(forecasts.Temperature.Minimum.Value),
                                            toInt(forecasts.RealFeelTemperature.Minimum.Value),
                                            toInt(forecasts.RealFeelTemperatureShade.Minimum.Value),
                                            null,
                                            null,
                                            null,
                                            toInt(forecasts.DegreeDaySummary.Cooling.Value)
                                    ),
                                    new Precipitation(
                                            (float) forecasts.Night.TotalLiquid.Value,
                                            null,
                                            (float) forecasts.Night.Rain.Value,
                                            (float) (forecasts.Day.Snow.Value * 10),
                                            (float) forecasts.Night.Ice.Value
                                    ),
                                    new PrecipitationProbability(
                                            (float) forecasts.Night.PrecipitationProbability,
                                            (float) forecasts.Night.ThunderstormProbability,
                                            (float) forecasts.Night.RainProbability,
                                            (float) forecasts.Night.SnowProbability,
                                            (float) forecasts.Night.IceProbability
                                    ),
                                    new PrecipitationDuration(
                                            (float) forecasts.Night.HoursOfPrecipitation,
                                            null,
                                            (float) forecasts.Night.HoursOfRain,
                                            (float) forecasts.Night.HoursOfSnow,
                                            (float) forecasts.Night.HoursOfIce
                                    ),
                                    new Wind(
                                            forecasts.Night.Wind.Direction.Localized,
                                            new WindDegree(forecasts.Night.Wind.Direction.Degrees, false),
                                            (float) forecasts.Night.Wind.Speed.Value,
                                            CommonConverter.getWindLevel(context, forecasts.Night.Wind.Speed.Value)
                                    ),
                                    forecasts.Night.CloudCover
                            ),
                            new Astro(forecasts.Sun.Rise, forecasts.Sun.Set),
                            new Astro(forecasts.Moon.Rise, forecasts.Moon.Set),
                            new MoonPhase(
                                    CommonConverter.getMoonPhaseAngle(forecasts.Moon.Phase),
                                    forecasts.Moon.Phase
                            ),
                            getDailyAirQuality(context, forecasts.AirAndPollen),
                            getDailyPollen(forecasts.AirAndPollen),
                            getDailyUV(forecasts.AirAndPollen),
                            (float) forecasts.HoursOfSun
                    )
            );
        }
        return dailyList;
    }

    private static AirQuality getDailyAirQuality(Context context,
                                                 List<AccuDailyResult.DailyForecasts.AirAndPollen> list) {
        AccuDailyResult.DailyForecasts.AirAndPollen aqi = getAirAndPollen(list, "AirQuality");
        Integer index = aqi == null ? null : aqi.Value;
        if (index != null && index == 0) {
            index = null;
        }
        return new AirQuality(
                CommonConverter.getAqiQuality(context, index),
                index,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private static Pollen getDailyPollen(List<AccuDailyResult.DailyForecasts.AirAndPollen> list) {
        AccuDailyResult.DailyForecasts.AirAndPollen grass = getAirAndPollen(list, "Grass");
        AccuDailyResult.DailyForecasts.AirAndPollen mold = getAirAndPollen(list, "Mold");
        AccuDailyResult.DailyForecasts.AirAndPollen ragweed = getAirAndPollen(list, "Ragweed");
        AccuDailyResult.DailyForecasts.AirAndPollen tree = getAirAndPollen(list, "Tree");
        return new Pollen(
                grass == null ? null : grass.Value,
                grass == null ? null : grass.CategoryValue,
                grass == null ? null : grass.Category,
                mold == null ? null : mold.Value,
                mold == null ? null : mold.CategoryValue,
                mold == null ? null : mold.Category,
                ragweed == null ? null : ragweed.Value,
                ragweed == null ? null : ragweed.CategoryValue,
                ragweed == null ? null : ragweed.Category,
                tree == null ? null : tree.Value,
                tree == null ? null : tree.CategoryValue,
                tree == null ? null : tree.Category
        );
    }

    private static UV getDailyUV(List<AccuDailyResult.DailyForecasts.AirAndPollen> list) {
        AccuDailyResult.DailyForecasts.AirAndPollen uv = getAirAndPollen(list, "UVIndex");
        return new UV(
                uv == null ? null : uv.Value,
                uv == null ? null : uv.Category,
                null
        );
    }

    @Nullable
    private static AccuDailyResult.DailyForecasts.AirAndPollen getAirAndPollen(
            List<AccuDailyResult.DailyForecasts.AirAndPollen> list, String name) {
        for (AccuDailyResult.DailyForecasts.AirAndPollen item : list) {
            if (item.Name.equals(name)) {
                return item;
            }
        }
        return null;
    }

    private static List<Hourly> getHourlyList(Context context,List<AccuHourlyResult> resultList) {
        List<Hourly> hourlyList = new ArrayList<>(resultList.size());
        for (AccuHourlyResult result : resultList) {
            hourlyList.add(
                    new Hourly(
                            result.DateTime,
                            result.EpochDateTime * 1000,
                            result.IsDaylight,
                            result.IconPhrase,
                            getWeatherCode(result.WeatherIcon),
                            new Temperature(
                                    toInt(result.Temperature.Value),
                                    toInt(result.RealFeelTemperature.Value),
                                    null,
                                    null,
                                    null,
                                    null,
                                    null
                            ),
                            new Precipitation(
                                    null, null,
                                    (float) result.Rain.Value,
                                    (float) result.Snow.Value,
                                    (float) result.Ice.Value
                            ),
                            new WindGust((float) result.WindGust.Speed.Value),
                            new Wind(
                                    result.Wind.Direction.Localized,
                                    new WindDegree(
                                            (float)result.Wind.Direction.Degrees,
                                            false
                                    ),
                                    (float) result.Wind.Speed.Value,
                                    CommonConverter.getWindLevel(context, result.Wind.Speed.Value)
                            ),
                            (float) result.Visibility.Value,(int)result.DewPoint.Value,(int) result.CloudCover,(float) result.Ceiling.Value,
                            new UV(result.UVIndex,result.UVIndexText,null),
                            new PrecipitationProbability(
                                    (float) result.PrecipitationProbability,
                                    null,
                                    (float) result.RainProbability,
                                    (float) result.SnowProbability,
                                    (float) result.IceProbability
                            ),
                            result.RelativeHumidity,
                            result.IndoorRelativeHumidity
                    )
            );
        }
        return hourlyList;
    }

    private static List<Minutely> getMinutelyList(Date sunrise, Date sunset,
                                                  @Nullable AccuMinuteResult minuteResult) {
        if (minuteResult == null) {
            return new ArrayList<>();
        }
        List<Minutely> minutelyList = new ArrayList<>(minuteResult.Intervals.size());
        for (AccuMinuteResult.IntervalsBean interval : minuteResult.Intervals) {
            minutelyList.add(
                    new Minutely(
                            interval.StartDateTime,
                            interval.StartEpochDateTime,
                            CommonConverter.isDaylight(sunrise, sunset, interval.StartDateTime),
                            interval.ShortPhrase,
                            getWeatherCode(interval.IconCode),
                            interval.Minute,
                            toInt(interval.Dbz),
                            interval.CloudCover
                    )
            );
        }
        return minutelyList;
    }

    private static List<Alert> getAlertList(List<AccuAlertResult> resultList) {
        List<Alert> alertList = new ArrayList<>(resultList.size());
        for (AccuAlertResult result : resultList) {
            alertList.add(
                    new Alert(
                            result.AlertID,
                            result.Area.get(0).StartTime,
                            result.Area.get(0).EpochStartTime * 1000,
                            result.Description.Localized,
                            result.Area.get(0).Text,
                            result.TypeID,
                            result.Priority,
                            Color.rgb(result.Color.Red, result.Color.Green, result.Color.Blue)
                    )
            );
        }
        Alert.deduplication(alertList);
        return alertList;
    }

    private static int toInt(double value) {
        return (int) (value + 0.5);
    }

    private static WeatherCode getWeatherCode(int icon) {
        if (icon == 1 || icon == 2 || icon == 3 ||  icon == 30
                || icon == 33 || icon == 34) {
            return WeatherCode.CLEAR;
        } else if (icon == 4 || icon == 6 || icon == 7
                || icon == 35 || icon == 36 || icon == 38) {
            return WeatherCode.PARTLY_CLOUDY;
        } else if (icon == 5 || icon == 37) {
            return WeatherCode.HAZE;
        } else if (icon == 8) {
            return WeatherCode.CLOUDY;
        } else if (icon == 11) {
            return WeatherCode.FOG;
        } else if (icon == 12 || icon == 13 || icon == 14 || icon == 18
                || icon == 39 || icon == 40) {
            return WeatherCode.RAIN;
        } else if (icon == 15 || icon == 16 || icon == 17 || icon == 41 || icon == 42) {
            return WeatherCode.THUNDERSTORM;
        } else if (icon == 19 || icon == 20 || icon == 21 || icon == 22 || icon == 23 || icon == 24
                || icon == 31 || icon == 43 || icon == 44) {
            return WeatherCode.SNOW;
        } else if (icon == 25) {
            return WeatherCode.HAIL;
        } else if (icon == 26 || icon == 29) {
            return WeatherCode.SLEET;
        } else if (icon == 32) {
            return WeatherCode.WIND;
        } else {
            return WeatherCode.CLOUDY;
        }
    }
}
