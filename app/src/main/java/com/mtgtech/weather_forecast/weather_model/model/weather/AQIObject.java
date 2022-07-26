package com.mtgtech.weather_forecast.weather_model.model.weather;

public class AQIObject {
    private String name;
    private Float value;

    public AQIObject() {
    }

    public AQIObject(String name, Float value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
