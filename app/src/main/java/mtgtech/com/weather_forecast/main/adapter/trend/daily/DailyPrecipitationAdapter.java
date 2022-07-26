package mtgtech.com.weather_forecast.main.adapter.trend.daily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.resource.ResourceHelper;
import mtgtech.com.weather_forecast.resource.provider.ResourceProvider;
import mtgtech.com.weather_forecast.utils.manager.ThemeManager;
import mtgtech.com.weather_forecast.view.weather_widget.trend.TrendRecyclerView;
import mtgtech.com.weather_forecast.view.weather_widget.trend.chart.DoubleHistogramView;
import mtgtech.com.weather_forecast.view.weather_widget.trend.item.DailyTrendItemView;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.PrecipitationUnit;
import mtgtech.com.weather_forecast.weather_model.model.weather.Daily;
import mtgtech.com.weather_forecast.weather_model.model.weather.Precipitation;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;

/**
 * Daily precipitation adapter.
 */
public class DailyPrecipitationAdapter extends AbsDailyTrendAdapter<DailyPrecipitationAdapter.ViewHolder> {

    private Weather weather;
    private TimeZone timeZone;
    private ResourceProvider provider;
    private ThemeManager themeManager;
    private PrecipitationUnit unit;

    private float highestPrecipitation;

    @SuppressLint("SimpleDateFormat")
    public DailyPrecipitationAdapter(GeoActivity activity, TrendRecyclerView parent,
                                     String formattedId, @NonNull Weather weather, @NonNull TimeZone timeZone,
                                     ResourceProvider provider, PrecipitationUnit unit) {
        super(activity, parent, formattedId);

        this.weather = weather;
        this.timeZone = timeZone;
        this.provider = provider;
        this.themeManager = ThemeManager.getInstance(activity);
        this.unit = unit;

        highestPrecipitation = Integer.MIN_VALUE;
        Float daytimePrecipitation;
        Float nighttimePrecipitation;
        for (int i = weather.getDailyForecast().size() - 1; i >= 0; i--) {
            daytimePrecipitation = weather.getDailyForecast().get(i).day().getPrecipitation().getTotal();
            nighttimePrecipitation = weather.getDailyForecast().get(i).night().getPrecipitation().getTotal();
            if (daytimePrecipitation != null && daytimePrecipitation > highestPrecipitation) {
                highestPrecipitation = daytimePrecipitation;
            }
            if (nighttimePrecipitation != null && nighttimePrecipitation > highestPrecipitation) {
                highestPrecipitation = nighttimePrecipitation;
            }
        }
        if (highestPrecipitation == 0) {
            highestPrecipitation = Precipitation.PRECIPITATION_HEAVY;
        }

        List<TrendRecyclerView.KeyLine> keyLineList = new ArrayList<>();
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        Precipitation.PRECIPITATION_LIGHT,
                        unit.getPrecipitationTextWithoutUnit(Precipitation.PRECIPITATION_LIGHT),
                        activity.getString(R.string.precipitation_light),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        Precipitation.PRECIPITATION_HEAVY,
                        unit.getPrecipitationTextWithoutUnit(Precipitation.PRECIPITATION_HEAVY),
                        activity.getString(R.string.precipitation_heavy),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        -Precipitation.PRECIPITATION_LIGHT,
                        unit.getPrecipitationTextWithoutUnit(Precipitation.PRECIPITATION_LIGHT),
                        activity.getString(R.string.precipitation_light),
                        TrendRecyclerView.KeyLine.ContentPosition.BELOW_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        -Precipitation.PRECIPITATION_HEAVY,
                        unit.getPrecipitationTextWithoutUnit(Precipitation.PRECIPITATION_HEAVY),
                        activity.getString(R.string.precipitation_heavy),
                        TrendRecyclerView.KeyLine.ContentPosition.BELOW_LINE
                )
        );
        parent.setLineColor(themeManager.getLineColor(activity));
        parent.setData(keyLineList, highestPrecipitation, -highestPrecipitation);
    }

    @NonNull
    @Override
    public DailyPrecipitationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trend_daily, parent, false);
        return new DailyPrecipitationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyPrecipitationAdapter.ViewHolder holder, int position) {
        holder.onBindView(position);
    }

    @Override
    public int getItemCount() {
        return weather.getDailyForecast().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private DailyTrendItemView dailyItem;
        private DoubleHistogramView doubleHistogramView;

        ViewHolder(View itemView) {
            super(itemView);
            dailyItem = itemView.findViewById(R.id.item_trend_daily);
            dailyItem.setParent(getTrendParent());

            doubleHistogramView = new DoubleHistogramView(itemView.getContext());
            dailyItem.setChartItemView(doubleHistogramView);
        }

        @SuppressLint("SetTextI18n, InflateParams")
        void onBindView(int position) {
            Context context = itemView.getContext();
            Daily daily = weather.getDailyForecast().get(position);

            if (daily.isToday(timeZone)) {
                dailyItem.setWeekText(context.getString(R.string.today));
            } else {
                dailyItem.setWeekText(daily.getWeek(context));
            }

            dailyItem.setDateText(daily.getShortDate(context));

            dailyItem.setTextColor(
                    themeManager.getTextContentColor(context),
                    themeManager.getTextSubtitleColor(context)
            );

            dailyItem.setDayIconDrawable(
                    ResourceHelper.getWeatherIcon(provider, daily.day().getWeatherCode(), true));

            Float daytimePrecipitation = weather.getDailyForecast().get(position).day().getPrecipitation().getTotal();
            Float nighttimePrecipitation = weather.getDailyForecast().get(position).night().getPrecipitation().getTotal();
            doubleHistogramView.setData(
                    weather.getDailyForecast().get(position).day().getPrecipitation().getTotal(),
                    weather.getDailyForecast().get(position).night().getPrecipitation().getTotal(),
                    unit.getPrecipitationTextWithoutUnit(daytimePrecipitation == null ? 0 : daytimePrecipitation),
                    unit.getPrecipitationTextWithoutUnit(nighttimePrecipitation == null ? 0 : nighttimePrecipitation),
                    highestPrecipitation
            );
            doubleHistogramView.setLineColors(
                    daily.day().getPrecipitation().getPrecipitationColor(context),
                    daily.night().getPrecipitation().getPrecipitationColor(context),
                    themeManager.getLineColor(context)
            );
            doubleHistogramView.setTextColors(themeManager.getTextContentColor(context));
            doubleHistogramView.setHistogramAlphas(1f, 0.5f);

            dailyItem.setNightIconDrawable(
                    ResourceHelper.getWeatherIcon(provider, daily.night().getWeatherCode(), false));

//            dailyItem.setOnClickListener(v ->  WeatherFlow.adUtil().showInterstitialAd((isLoaded, interstitial) -> {
//                onItemClicked(getAdapterPosition());
//            }));
        }
    }
}