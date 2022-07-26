package com.mtgtech.weather_forecast.daily_weather.adapter.holder;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.daily_weather.adapter.DailyWeatherAdapter;
import com.mtgtech.weather_forecast.daily_weather.adapter.model.Overview;
import com.mtgtech.weather_forecast.resource.provider.ResourceProvider;
import com.mtgtech.weather_forecast.resource.provider.ResourcesProviderFactory;
import com.mtgtech.weather_forecast.settings.SettingsOptionManager;
import com.mtgtech.weather_forecast.view.weather_widget.AnimatableIconView;
import com.mtgtech.weather_forecast.weather_model.model.option.unit.TemperatureUnit;

public class OverviewHolder extends DailyWeatherAdapter.ViewHolder {

    private AnimatableIconView icon;
    private TextView title;

    private ResourceProvider provider;
    private TemperatureUnit unit;

    public OverviewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_daily_overview, parent, false));
        itemView.setOnClickListener(v -> icon.startAnimators());

        icon = itemView.findViewById(R.id.item_weather_daily_overview_icon);
        title = itemView.findViewById(R.id.item_weather_daily_overview_text);

        provider = ResourcesProviderFactory.getNewInstance();
        unit = SettingsOptionManager.getInstance(parent.getContext()).getTemperatureUnit();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindView(DailyWeatherAdapter.ViewModel model, int position) {
        Overview overview = (Overview) model;
        icon.setAnimatableIcon(
                provider.getWeatherIcons(overview.getHalfDay().getWeatherCode(), overview.isDaytime()),
                provider.getWeatherAnimators(overview.getHalfDay().getWeatherCode(), overview.isDaytime())
        );
        title.setText(overview.getHalfDay().getWeatherText()
                + " " + overview.getHalfDay().getTemperature().getTemperature(title.getContext(), unit));
    }
}
