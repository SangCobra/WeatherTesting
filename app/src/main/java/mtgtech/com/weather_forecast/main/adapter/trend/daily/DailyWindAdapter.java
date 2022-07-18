package mtgtech.com.weather_forecast.main.adapter.trend.daily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import mtgtech.com.weather_forecast.WeatherFlow;
import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.SpeedUnit;
import mtgtech.com.weather_forecast.weather_model.model.weather.Daily;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;
import mtgtech.com.weather_forecast.weather_model.model.weather.Wind;
import mtgtech.com.weather_forecast.view.image.RotateDrawable;
import mtgtech.com.weather_forecast.view.weather_widget.trend.TrendRecyclerView;
import mtgtech.com.weather_forecast.view.weather_widget.trend.chart.DoubleHistogramView;
import mtgtech.com.weather_forecast.view.weather_widget.trend.item.DailyTrendItemView;
import mtgtech.com.weather_forecast.utils.manager.ThemeManager;

/**
 * Daily wind adapter.
 * */
public class DailyWindAdapter extends AbsDailyTrendAdapter<DailyWindAdapter.ViewHolder> {

    private Weather weather;
    private TimeZone timeZone;
    private ThemeManager themeManager;
    private SpeedUnit unit;

    private float highestWindSpeed;

    private int size;

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

            int daytimeWindColor = daily.day().getWind().getWindColor(context);
            int nighttimeWindColor = daily.night().getWind().getWindColor(context);

            RotateDrawable dayIcon = daily.day().getWind().isValidSpeed()
                    ? new RotateDrawable(ContextCompat.getDrawable(context, R.drawable.ic_navigation))
                    : new RotateDrawable(ContextCompat.getDrawable(context, R.drawable.ic_circle_medium));
            dayIcon.rotate(daily.day().getWind().getDegree().getDegree() + 180);
            dayIcon.setColorFilter(new PorterDuffColorFilter(daytimeWindColor, PorterDuff.Mode.SRC_ATOP));
            dailyItem.setDayIconDrawable(dayIcon);

            Float daytimeWindSpeed = weather.getDailyForecast().get(position).day().getWind().getSpeed();
            Float nighttimeWindSpeed = weather.getDailyForecast().get(position).night().getWind().getSpeed();
            doubleHistogramView.setData(
                    weather.getDailyForecast().get(position).day().getWind().getSpeed(),
                    weather.getDailyForecast().get(position).night().getWind().getSpeed(),
                    unit.getSpeedTextWithoutUnit(daytimeWindSpeed == null ? 0 : daytimeWindSpeed),
                    unit.getSpeedTextWithoutUnit(nighttimeWindSpeed == null ? 0 : nighttimeWindSpeed),
                    highestWindSpeed
            );
            doubleHistogramView.setLineColors(daytimeWindColor, nighttimeWindColor, themeManager.getLineColor(context));
            doubleHistogramView.setTextColors(themeManager.getTextContentColor(context));
            doubleHistogramView.setHistogramAlphas(1f, 0.5f);

            RotateDrawable nightIcon = daily.night().getWind().isValidSpeed()
                    ? new RotateDrawable(ContextCompat.getDrawable(context, R.drawable.ic_navigation))
                    : new RotateDrawable(ContextCompat.getDrawable(context, R.drawable.ic_circle_medium));
            nightIcon.rotate(daily.night().getWind().getDegree().getDegree() + 180);
            nightIcon.setColorFilter(new PorterDuffColorFilter(nighttimeWindColor, PorterDuff.Mode.SRC_ATOP));
            dailyItem.setNightIconDrawable(nightIcon);

//            dailyItem.setOnClickListener(v -> WeatherFlow.adUtil().showInterstitialAd((isLoaded, interstitial) -> {
//                onItemClicked(getAdapterPosition());
//            }));
        }
    }

    @SuppressLint("SimpleDateFormat")
    public DailyWindAdapter(GeoActivity activity, TrendRecyclerView parent, String formattedId,
                            @NonNull Weather weather, @NonNull TimeZone timeZone, SpeedUnit unit) {
        super(activity, parent, formattedId);

        this.weather = weather;
        this.timeZone = timeZone;
        this.themeManager = ThemeManager.getInstance(activity);
        this.unit = unit;

        highestWindSpeed = Integer.MIN_VALUE;
        Float daytimeWindSpeed;
        Float nighttimeWindSpeed;
        boolean valid = false;
        for (int i = weather.getDailyForecast().size() - 1; i >= 0; i --) {
            daytimeWindSpeed = weather.getDailyForecast().get(i).day().getWind().getSpeed();
            nighttimeWindSpeed = weather.getDailyForecast().get(i).night().getWind().getSpeed();
            if (daytimeWindSpeed != null && daytimeWindSpeed > highestWindSpeed) {
                highestWindSpeed = daytimeWindSpeed;
            }
            if (nighttimeWindSpeed != null && nighttimeWindSpeed > highestWindSpeed) {
                highestWindSpeed = nighttimeWindSpeed;
            }
            if ((daytimeWindSpeed != null && daytimeWindSpeed != 0)
                    || (nighttimeWindSpeed != null && nighttimeWindSpeed != 0)
                    || valid) {
                valid = true;
                size ++;
            }
        }
        if (highestWindSpeed == 0) {
            highestWindSpeed = Wind.WIND_SPEED_11;
        }

        List<TrendRecyclerView.KeyLine> keyLineList = new ArrayList<>();
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        Wind.WIND_SPEED_3,
                        unit.getSpeedTextWithoutUnit(Wind.WIND_SPEED_3),
                        activity.getString(R.string.wind_3),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        Wind.WIND_SPEED_7,
                        unit.getSpeedTextWithoutUnit(Wind.WIND_SPEED_7),
                        activity.getString(R.string.wind_7),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        -Wind.WIND_SPEED_3,
                        unit.getSpeedTextWithoutUnit(Wind.WIND_SPEED_3),
                        activity.getString(R.string.wind_3),
                        TrendRecyclerView.KeyLine.ContentPosition.BELOW_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        -Wind.WIND_SPEED_7,
                        unit.getSpeedTextWithoutUnit(Wind.WIND_SPEED_7),
                        activity.getString(R.string.wind_7),
                        TrendRecyclerView.KeyLine.ContentPosition.BELOW_LINE
                )
        );
        parent.setLineColor(themeManager.getLineColor(activity));
        parent.setData(keyLineList, highestWindSpeed, -highestWindSpeed);
    }

    @NonNull
    @Override
    public DailyWindAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trend_daily, parent, false);
        return new DailyWindAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyWindAdapter.ViewHolder holder, int position) {
        holder.onBindView(position);
    }

    @Override
    public int getItemCount() {
        return size;
    }
}