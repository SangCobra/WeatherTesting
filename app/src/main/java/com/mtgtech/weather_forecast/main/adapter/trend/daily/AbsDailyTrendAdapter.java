package com.mtgtech.weather_forecast.main.adapter.trend.daily;

import androidx.recyclerview.widget.RecyclerView;

import com.mtgtech.weather_forecast.view.weather_widget.trend.abs.TrendParent;
import com.mtgtech.weather_forecast.view.weather_widget.trend.abs.TrendRecyclerViewAdapter;
import com.mtgtech.weather_forecast.weather_model.GeoActivity;

public abstract class AbsDailyTrendAdapter<VH extends RecyclerView.ViewHolder> extends TrendRecyclerViewAdapter<VH> {

    private GeoActivity activity;
    private String formattedId;

    public AbsDailyTrendAdapter(GeoActivity activity, TrendParent trendParent, String formattedId) {
        super(trendParent);
        this.activity = activity;
        this.formattedId = formattedId;
    }

    protected void onItemClicked(int adapterPosition) {
        if (activity.isForeground()) {
//            IntentHelper.startDailyWeatherActivity(activity, formattedId, adapterPosition);
        }
    }
}
