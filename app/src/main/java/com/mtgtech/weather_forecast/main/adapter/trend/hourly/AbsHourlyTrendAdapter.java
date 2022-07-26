package com.mtgtech.weather_forecast.main.adapter.trend.hourly;

import androidx.recyclerview.widget.RecyclerView;

import com.mtgtech.weather_forecast.main.dialog.HourlyWeatherDialog;
import com.mtgtech.weather_forecast.utils.manager.ThemeManager;
import com.mtgtech.weather_forecast.view.weather_widget.trend.abs.TrendParent;
import com.mtgtech.weather_forecast.view.weather_widget.trend.abs.TrendRecyclerViewAdapter;
import com.mtgtech.weather_forecast.weather_model.GeoActivity;
import com.mtgtech.weather_forecast.weather_model.model.weather.Weather;

public abstract class AbsHourlyTrendAdapter<VH extends RecyclerView.ViewHolder> extends TrendRecyclerViewAdapter<VH> {

    private GeoActivity activity;
    private Weather weather;

    public AbsHourlyTrendAdapter(GeoActivity activity, TrendParent trendParent, Weather weather) {
        super(trendParent);
        this.activity = activity;
        this.weather = weather;
    }

    protected void onItemClicked(int adapterPosition) {
        if (activity.isForeground()) {
            HourlyWeatherDialog dialog = new HourlyWeatherDialog();
            dialog.setData(weather, adapterPosition, ThemeManager.getInstance(activity).getWeatherThemeColors()[0]);
            dialog.show(activity.getSupportFragmentManager(), null);
        }
    }
}
