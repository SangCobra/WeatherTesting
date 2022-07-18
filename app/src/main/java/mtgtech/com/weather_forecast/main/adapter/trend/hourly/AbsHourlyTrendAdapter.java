package mtgtech.com.weather_forecast.main.adapter.trend.hourly;

import androidx.recyclerview.widget.RecyclerView;

import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;
import mtgtech.com.weather_forecast.main.dialog.HourlyWeatherDialog;
import mtgtech.com.weather_forecast.view.weather_widget.trend.abs.TrendParent;
import mtgtech.com.weather_forecast.view.weather_widget.trend.abs.TrendRecyclerViewAdapter;
import mtgtech.com.weather_forecast.utils.manager.ThemeManager;

public abstract class AbsHourlyTrendAdapter<VH extends RecyclerView.ViewHolder> extends TrendRecyclerViewAdapter<VH>  {

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
