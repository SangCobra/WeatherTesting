package com.mtgtech.weather_forecast.utils;

import java.util.Calendar;

public class MyUtils {
    public static int requestCode;

    public static Boolean isNight() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return hour < 6 || hour > 18;
    }

// switch (model.getWeatherCode()){
//        case CLEAR:
//
//            break;
//        case PARTLY_CLOUDY:
//
//            break;
//        case CLOUDY:
//
//            break;
//        case RAIN:
//
//            break;
//        case SNOW:
//
//            break;
//        case WIND:
//
//            break;
//        case FOG:
//
//            break;
//        case HAZE:
//
//            break;
//        case SLEET:
//
//            break;
//        case HAIL:
//
//            break;
//        case THUNDER:
//
//
//            break;
//        case THUNDERSTORM:
//
//
//            break;
}
