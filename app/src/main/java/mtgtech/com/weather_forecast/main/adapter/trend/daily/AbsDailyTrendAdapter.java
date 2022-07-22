package mtgtech.com.weather_forecast.main.adapter.trend.daily;

import androidx.recyclerview.widget.RecyclerView;

import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.view.weather_widget.trend.abs.TrendParent;
import mtgtech.com.weather_forecast.view.weather_widget.trend.abs.TrendRecyclerViewAdapter;
import mtgtech.com.weather_forecast.utils.helpter.IntentHelper;

public abstract class AbsDailyTrendAdapter<VH extends RecyclerView.ViewHolder> extends TrendRecyclerViewAdapter<VH>  {

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
