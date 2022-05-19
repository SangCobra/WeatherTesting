package anaxxes.com.weatherFlow.main.adapter.trend.hourly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import anaxxes.com.weatherFlow.WeatherFlow;
import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.GeoActivity;
import anaxxes.com.weatherFlow.basic.model.option.unit.ProbabilityUnit;
import anaxxes.com.weatherFlow.basic.model.option.unit.TemperatureUnit;
import anaxxes.com.weatherFlow.basic.model.weather.Hourly;
import anaxxes.com.weatherFlow.basic.model.weather.Temperature;
import anaxxes.com.weatherFlow.basic.model.weather.Weather;
import anaxxes.com.weatherFlow.resource.ResourceHelper;
import anaxxes.com.weatherFlow.resource.provider.ResourceProvider;
import anaxxes.com.weatherFlow.ui.widget.trend.TrendRecyclerView;
import anaxxes.com.weatherFlow.ui.widget.trend.chart.PolylineAndHistogramView;
import anaxxes.com.weatherFlow.ui.widget.trend.item.HourlyTrendItemView;
import anaxxes.com.weatherFlow.utils.manager.ThemeManager;

/**
 * Hourly temperature adapter.
 */

public abstract class HourlyTemperatureAdapter extends AbsHourlyTrendAdapter<HourlyTemperatureAdapter.ViewHolder> {

    private Weather weather;
    private ResourceProvider provider;
    private ThemeManager themeManager;
    private TemperatureUnit unit;

    private float[] temperatures;
    private int highestTemperature;
    private int lowestTemperature;

    private boolean showPrecipitationProbability;

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

            hourlyItem.setTextColor(ContextCompat.getColor(context,R.color.colorTextWhite));

            hourlyItem.setIconDrawable(
                    ResourceHelper.getWeatherIcon(provider, hourly.getWeatherCode(), hourly.isDaylight())
            );

            Float precipitationProbability = hourly.getPrecipitationProbability().getTotal();
            float p = precipitationProbability == null ? 0 : precipitationProbability;
            if (!showPrecipitationProbability) {
                p = 0;
            }
            polylineAndHistogramView.setData(
                    buildTemperatureArrayForItem(temperatures, position),
                    null,
                    getShortTemperatureString(weather, position, unit),
                    null,
                    (float) highestTemperature,
                    (float) lowestTemperature,
                    p < 5 ? null : p,
                    p < 5 ? null : ProbabilityUnit.PERCENT.getProbabilityText(context, p),
                    100f,
                    0f
            );
            int[] themeColors = themeManager.getWeatherThemeColors();
//            polylineAndHistogramView.setLineColors(
//                    themeColors[themeManager.isLightTheme() ? 1 : 2], themeColors[2], themeManager.getLineColor(context));
            polylineAndHistogramView.setLineColors(
                    themeColors[1], themeColors[2], ContextCompat.getColor(context,R.color.gnt_white));

//            polylineAndHistogramView.setShadowColors(
//                    themeColors[themeManager.isLightTheme() ? 1 : 2], themeColors[2], themeManager.isLightTheme());

            polylineAndHistogramView.setShadowColors(
                    ContextCompat.getColor(context, android.R.color.white),
                    ContextCompat.getColor(context,android.R.color.white),
                    false);


            polylineAndHistogramView.setTextColors(
                    ContextCompat.getColor(context, R.color.colorTextWhite),
                    ContextCompat.getColor(context, R.color.colorTextWhite)
            );
            polylineAndHistogramView.setHistogramAlpha(0.5f);

            hourlyItem.setOnClickListener(v -> WeatherFlow.adUtil().showInterstitialAd((isLoaded, interstitial) -> {
                onItemClicked(getAdapterPosition());
            }));
        }

        @Size(3)
        private Float[] buildTemperatureArrayForItem(float[] temps, int adapterPosition) {
            Float[] a = new Float[3];
            a[1] = temps[2 * adapterPosition];
            if (2 * adapterPosition - 1 < 0) {
                a[0] = null;
            } else {
                a[0] = temps[2 * adapterPosition - 1];
            }
            if (2 * adapterPosition + 1 >= temps.length) {
                a[2] = null;
            } else {
                a[2] = temps[2 * adapterPosition + 1];
            }
            return a;
        }
    }

    public HourlyTemperatureAdapter(GeoActivity activity, TrendRecyclerView parent, @NonNull Weather weather,
                                    ResourceProvider provider, TemperatureUnit unit) {
        this(activity, parent, weather, true, provider, unit);
    }

    public HourlyTemperatureAdapter(GeoActivity activity, TrendRecyclerView parent, @NonNull Weather weather,
                                    boolean showPrecipitationProbability,
                                    ResourceProvider provider, TemperatureUnit unit) {
        super(activity, parent, weather);

        this.weather = weather;
        this.provider = provider;
        this.themeManager = ThemeManager.getInstance(activity);
        this.unit = unit;

        this.temperatures = new float[Math.max(0, weather.getHourlyForecast().size() * 2 - 1)];
        for (int i = 0; i < temperatures.length; i += 2) {
            temperatures[i] = getTemperatureC(weather, i / 2);
        }
        for (int i = 1; i < temperatures.length; i += 2) {
            temperatures[i] = (temperatures[i - 1] + temperatures[i + 1]) * 0.5F;
        }

        highestTemperature = weather.getYesterday() == null
                ? Integer.MIN_VALUE
                : weather.getYesterday().getDaytimeTemperature();
        lowestTemperature = weather.getYesterday() == null
                ? Integer.MAX_VALUE
                : weather.getYesterday().getNighttimeTemperature();
        for (int i = 0; i < weather.getHourlyForecast().size(); i++) {
            if (getTemperatureC(weather, i) > highestTemperature) {
                highestTemperature = getTemperatureC(weather, i);
            }
            if (getTemperatureC(weather, i) < lowestTemperature) {
                lowestTemperature = getTemperatureC(weather, i);
            }
        }

        this.showPrecipitationProbability = showPrecipitationProbability;

        parent.setLineColor(ContextCompat.getColor(activity,R.color.gnt_white));
        if (weather.getYesterday() == null) {
            parent.setData(null, 0, 0);
        } else {
            List<TrendRecyclerView.KeyLine> keyLineList = new ArrayList<>();
//            keyLineList.add(
//                    new TrendRecyclerView.KeyLine(
//                            weather.getYesterday().getDaytimeTemperature(),
//                            Temperature.getShortTemperature(
//                                    activity,
//                                    weather.getYesterday().getDaytimeTemperature(),
//                                    unit
//                            ),
//                            activity.getString(R.string.yesterday),
//                            TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
//                    )
//            );
            keyLineList.add(
                    new TrendRecyclerView.KeyLine(
                            weather.getYesterday().getNighttimeTemperature(),
                            Temperature.getShortTemperature(
                                    activity,
                                    weather.getYesterday().getNighttimeTemperature(),
                                    unit
                            ),
                            activity.getString(R.string.yesterday),
                            TrendRecyclerView.KeyLine.ContentPosition.BELOW_LINE
                    )
            );
            parent.setData(keyLineList, highestTemperature, lowestTemperature);
        }
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
        return Math.min(weather.getHourlyForecast().size(), 24);
    }

    protected abstract int getTemperatureC(Weather weather, int index);

    protected abstract int getTemperature(Weather weather, int index, TemperatureUnit unit);

    protected abstract String getTemperatureString(Weather weather, int index, TemperatureUnit unit);

    protected abstract String getShortTemperatureString(Weather weather, int index, TemperatureUnit unit);
}