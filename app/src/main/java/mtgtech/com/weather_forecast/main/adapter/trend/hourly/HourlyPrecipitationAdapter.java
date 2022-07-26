package mtgtech.com.weather_forecast.main.adapter.trend.hourly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.resource.ResourceHelper;
import mtgtech.com.weather_forecast.resource.provider.ResourceProvider;
import mtgtech.com.weather_forecast.utils.manager.ThemeManager;
import mtgtech.com.weather_forecast.view.weather_widget.trend.TrendRecyclerView;
import mtgtech.com.weather_forecast.view.weather_widget.trend.chart.PolylineAndHistogramView;
import mtgtech.com.weather_forecast.view.weather_widget.trend.item.HourlyTrendItemView;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.PrecipitationUnit;
import mtgtech.com.weather_forecast.weather_model.model.weather.Hourly;
import mtgtech.com.weather_forecast.weather_model.model.weather.Precipitation;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;

/**
 * Hourly precipitation adapter.
 */

public class HourlyPrecipitationAdapter extends AbsHourlyTrendAdapter<HourlyPrecipitationAdapter.ViewHolder> {

    private Weather weather;
    private ResourceProvider provider;
    private ThemeManager themeManager;
    private PrecipitationUnit unit;

    private float highestPrecipitation;

    public HourlyPrecipitationAdapter(GeoActivity activity, TrendRecyclerView parent, @NonNull Weather weather,
                                      ResourceProvider provider, PrecipitationUnit unit) {
        super(activity, parent, weather);

        this.weather = weather;
        this.provider = provider;
        this.themeManager = ThemeManager.getInstance(activity);
        this.unit = unit;

        highestPrecipitation = Integer.MIN_VALUE;
        Float precipitation;
        for (int i = weather.getHourlyForecast().size() - 1; i >= 0; i--) {
            precipitation = weather.getHourlyForecast().get(i).getPrecipitation().getTotal();
            if (precipitation != null && precipitation > highestPrecipitation) {
                highestPrecipitation = precipitation;
            }
        }
        if (highestPrecipitation == 0) {
            highestPrecipitation = Precipitation.PRECIPITATION_HEAVY;
        }

        List<TrendRecyclerView.KeyLine> keyLineList = new ArrayList<>();
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        Precipitation.PRECIPITATION_LIGHT,
                        activity.getString(R.string.precipitation_light),
                        unit.getPrecipitationTextWithoutUnit(Precipitation.PRECIPITATION_LIGHT),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        Precipitation.PRECIPITATION_HEAVY,
                        activity.getString(R.string.precipitation_heavy),
                        unit.getPrecipitationTextWithoutUnit(Precipitation.PRECIPITATION_HEAVY),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        parent.setLineColor(themeManager.getLineColor(activity));
        parent.setData(keyLineList, highestPrecipitation, 0f);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trend_hourly, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindView(position);
    }

    @Override
    public int getItemCount() {
        return weather.getHourlyForecast().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private HourlyTrendItemView hourlyItem;
        private PolylineAndHistogramView polylineAndHistogramView;

        ViewHolder(View itemView) {
            super(itemView);
            hourlyItem = itemView.findViewById(R.id.item_trend_hourly);
            hourlyItem.setParent(getTrendParent());

            polylineAndHistogramView = new PolylineAndHistogramView(itemView.getContext());
            hourlyItem.setChartItemView(polylineAndHistogramView);
        }

        void onBindView(int position) {

            Context context = itemView.getContext();
            Hourly hourly = weather.getHourlyForecast().get(position);

            hourlyItem.setHourText(hourly.getHour(context));

            hourlyItem.setTextColor(themeManager.getTextContentColor(context));

            hourlyItem.setIconDrawable(
                    ResourceHelper.getWeatherIcon(provider, hourly.getWeatherCode(), hourly.isDaylight())
            );

            Float precipitation = weather.getHourlyForecast().get(position).getPrecipitation().getTotal();
            polylineAndHistogramView.setData(
                    null, null,
                    null, null,
                    null, null,
                    precipitation,
                    unit.getPrecipitationTextWithoutUnit(precipitation == null ? 0 : precipitation),
                    highestPrecipitation,
                    0f
            );
            polylineAndHistogramView.setLineColors(
                    hourly.getPrecipitation().getPrecipitationColor(context),
                    hourly.getPrecipitation().getPrecipitationColor(context),
                    themeManager.getLineColor(context)
            );
            int[] themeColors = themeManager.getWeatherThemeColors();
            polylineAndHistogramView.setShadowColors(
                    themeColors[themeManager.isLightTheme() ? 1 : 2], themeColors[2], themeManager.isLightTheme());
            polylineAndHistogramView.setTextColors(
                    themeManager.getTextContentColor(context),
                    themeManager.getTextSubtitleColor(context)
            );
            polylineAndHistogramView.setHistogramAlpha(themeManager.isLightTheme() ? 1f : 0.5f);

//            hourlyItem.setOnClickListener(v ->  WeatherFlow.adUtil().showInterstitialAd((isLoaded, interstitial) -> {
//                onItemClicked(getAdapterPosition());
//            }));
        }
    }
}