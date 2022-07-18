package mtgtech.com.weather_forecast.weather_forecast.json.accu;

import java.util.Date;

/**
 * Accu hourly result.
 * */

public class AccuHourlyResult {

    /**
     * DateTime : 2016-12-22T10:00:00+08:00
     * EpochDateTime : 1482372000
     * WeatherIcon : 6
     * IconPhrase : 多云转阴
     * IsDaylight : true
     * Temperature : {"Value":4.1,"Unit":"C","UnitType":17}
     * PrecipitationProbability : 7
     * MobileLink : http://m.accuweather.com/zh/cn/qingdao/106573/hourly-weather-forecast/106573?day=1&hbhhour=10&unit=c&lang=zh-cn
     * Link : http://www.accuweather.com/zh/cn/qingdao/106573/hourly-weather-forecast/106573?day=1&hbhhour=10&unit=c&lang=zh-cn
     */

    public Date DateTime;
    public long EpochDateTime;
    public int WeatherIcon;
    public String IconPhrase;
    public boolean IsDaylight;
    /**
     * Value : 4.1
     * Unit : C
     * UnitType : 17
     */

    public Temperature Temperature;
    public RealFeelTemperature RealFeelTemperature;
    public DewPoint DewPoint;
    public Visibility Visibility;
    public Ceiling Ceiling;
    public Wind Wind;
    public WindGust WindGust;
    public int PrecipitationProbability;
    public int RainProbability;
    public int SnowProbability;
    public int IceProbability;
    public Rain Rain;
    public Snow Snow;
    public Ice Ice;
    public String MobileLink;
    public String Link;
    public int UVIndex;
    public int CloudCover;
    public String UVIndexText;
    public int RelativeHumidity;
    public int IndoorRelativeHumidity;

    public static class Temperature {
        public double Value;
        public String Unit;
        public int UnitType;
    }
    public static class RealFeelTemperature {
        public double Value;
        public String Unit;
        public int UnitType;

    }

    public static class DewPoint {
        public double Value;
        public String Unit;
        public int UnitType;
    }
    public static class Visibility {
        public double Value;
        public String Unit;
        public int UnitType;
    }


    public static class Ceiling {
        public double Value;
        public String Unit;
        public int UnitType;
    }

    public static class Speed {
        public double Value;
        public String Unit;
        public int UnitType;
    }


    public static class Direction {
        public double Degrees;
        public String Localized;
    }


    public static class Wind {
        public Speed Speed;
        public Direction Direction;

    }


    public static class WindGust {
        public Speed Speed;
    }
    public static class Rain{
        public double Value;
        public String Unit;
        public int UnitType;
    }
    public static class Snow{
        public double Value;
        public String Unit;
        public int UnitType;
    }
    public static class Ice{
        public double Value;
        public String Unit;
        public int UnitType;
    }
}
