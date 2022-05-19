package anaxxes.com.weatherFlow.main.adapter.trend.hourly;

import androidx.recyclerview.widget.RecyclerView;

import anaxxes.com.weatherFlow.basic.GeoActivity;
import anaxxes.com.weatherFlow.basic.model.weather.Weather;
import anaxxes.com.weatherFlow.main.dialog.HourlyWeatherDialog;
import anaxxes.com.weatherFlow.ui.widget.trend.abs.TrendParent;
import anaxxes.com.weatherFlow.ui.widget.trend.abs.TrendRecyclerViewAdapter;
import anaxxes.com.weatherFlow.utils.helpter.IntentHelper;
import anaxxes.com.weatherFlow.utils.manager.ThemeManager;

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
