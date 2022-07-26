package com.mtgtech.weather_forecast.view.weather_widget.trend.abs;

import androidx.annotation.NonNull;

public interface TrendChild {

    void setParent(@NonNull TrendParent parent);

    ChartItemView getChartItemView();

    void setChartItemView(ChartItemView t);
}
