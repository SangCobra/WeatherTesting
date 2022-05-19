package anaxxes.com.weatherFlow.main.adapter.trend.daily;

import androidx.recyclerview.widget.RecyclerView;

import anaxxes.com.weatherFlow.basic.GeoActivity;
import anaxxes.com.weatherFlow.ui.widget.trend.abs.TrendParent;
import anaxxes.com.weatherFlow.ui.widget.trend.abs.TrendRecyclerViewAdapter;
import anaxxes.com.weatherFlow.utils.helpter.IntentHelper;

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
            IntentHelper.startDailyWeatherActivity(activity, formattedId, adapterPosition);
        }
    }
}
