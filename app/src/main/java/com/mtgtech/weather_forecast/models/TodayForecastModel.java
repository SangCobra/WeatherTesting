package com.mtgtech.weather_forecast.models;

public class TodayForecastModel {

    private int todayForecastImage;
    private String todayForecastHeading;
    private String todayForecastValue;

    public TodayForecastModel(int todayForecastImage, String todayForecastHeading, String todayForecastValue) {
        this.todayForecastImage = todayForecastImage;
        this.todayForecastHeading = todayForecastHeading;
        this.todayForecastValue = todayForecastValue;
    }

    public int getTodayForecastImage() {
        return todayForecastImage;
    }

    public void setTodayForecastImage(int todayForecastImage) {
        this.todayForecastImage = todayForecastImage;
    }

    public String getTodayForecastHeading() {
        return todayForecastHeading;
    }

    public void setTodayForecastHeading(String todayForecastHeading) {
        this.todayForecastHeading = todayForecastHeading;
    }

    public String getTodayForecastValue() {
        return todayForecastValue;
    }

    public void setTodayForecastValue(String todayForecastValue) {
        this.todayForecastValue = todayForecastValue;
    }
}
