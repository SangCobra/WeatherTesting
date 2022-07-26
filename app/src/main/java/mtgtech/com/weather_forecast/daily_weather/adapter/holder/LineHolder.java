package mtgtech.com.weather_forecast.daily_weather.adapter.holder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.daily_weather.adapter.DailyWeatherAdapter;

public class LineHolder extends DailyWeatherAdapter.ViewHolder {

    public LineHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_daily_line, parent, false));
    }

    @Override
    public void onBindView(DailyWeatherAdapter.ViewModel model, int position) {
        // do nothing.
    }
}
