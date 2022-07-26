package mtgtech.com.weather_forecast.main.adapter.main;

import mtgtech.com.weather_forecast.view.adapter.TagAdapter;

public class MainTag implements TagAdapter.Tag {

    private String name;
    private Type type;

    public MainTag(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public enum Type {TEMPERATURE, WIND, PRECIPITATION, AIR_QUALITY, UV_INDEX}
}
