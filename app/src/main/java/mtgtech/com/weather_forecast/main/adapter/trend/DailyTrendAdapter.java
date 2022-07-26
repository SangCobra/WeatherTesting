package mtgtech.com.weather_forecast.main.adapter.trend;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.TimeZone;

import mtgtech.com.weather_forecast.main.adapter.trend.daily.AbsDailyTrendAdapter;
import mtgtech.com.weather_forecast.main.adapter.trend.daily.DailyAirQualityAdapter;
import mtgtech.com.weather_forecast.main.adapter.trend.daily.DailyPrecipitationAdapter;
import mtgtech.com.weather_forecast.main.adapter.trend.daily.DailyUVAdapter;
import mtgtech.com.weather_forecast.main.adapter.trend.daily.DailyWindAdapter;
import mtgtech.com.weather_forecast.resource.provider.ResourceProvider;
import mtgtech.com.weather_forecast.view.weather_widget.trend.TrendRecyclerView;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.PrecipitationUnit;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.SpeedUnit;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.TemperatureUnit;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;

public class DailyTrendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Nullable
    private AbsDailyTrendAdapter adapter;

    public DailyTrendAdapter() {
        adapter = null;
    }

    public void temperature(GeoActivity activity, TrendRecyclerView parent,
                            String formattedId, @NonNull Weather weather, @NonNull TimeZone timeZone,
                            ResourceProvider provider, TemperatureUnit unit) {
        adapter = new DailyTemperatureAdapter(activity, parent, formattedId, weather, timeZone, provider, unit);
    }

    public void airQuality(GeoActivity activity, TrendRecyclerView parent,
                           String formattedId, @NonNull Weather weather, @NonNull TimeZone timeZone) {
        adapter = new DailyAirQualityAdapter(activity, parent, formattedId, weather, timeZone);
    }

    public void wind(GeoActivity activity, TrendRecyclerView parent,
                     String formattedId, @NonNull Weather weather, @NonNull TimeZone timeZone, SpeedUnit unit) {
        adapter = new DailyWindAdapter(activity, parent, formattedId, weather, timeZone, unit);
    }

    public void uv(GeoActivity activity, TrendRecyclerView parent,
                   String formattedId, @NonNull Weather weather, @NonNull TimeZone timeZone) {
        adapter = new DailyUVAdapter(activity, parent, formattedId, weather, timeZone);
    }

    public void precipitation(GeoActivity activity, TrendRecyclerView parent,
                              String formattedId, @NonNull Weather weather, @NonNull TimeZone timeZone,
                              ResourceProvider provider, PrecipitationUnit unit) {
        adapter = new DailyPrecipitationAdapter(activity, parent, formattedId, weather, timeZone, provider, unit);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        assert adapter != null;
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        assert adapter != null;
        adapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return adapter == null ? 0 : adapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (adapter == null) {
            return 0;
        } else if (adapter instanceof DailyTemperatureAdapter) {
            return 1;
        } else if (adapter instanceof DailyAirQualityAdapter) {
            return 2;
        } else if (adapter instanceof DailyWindAdapter) {
            return 3;
        } else if (adapter instanceof DailyUVAdapter) {
            return 4;
        } else if (adapter instanceof DailyPrecipitationAdapter) {
            return 5;
        }
        return -1;
    }
}

class DailyTemperatureAdapter extends mtgtech.com.weather_forecast.main.adapter.trend.daily.DailyTemperatureAdapter {

    private Context c;

    public DailyTemperatureAdapter(GeoActivity activity, TrendRecyclerView parent,
                                   String formattedId, @NonNull Weather weather, @NonNull TimeZone timeZone,
                                   ResourceProvider provider, TemperatureUnit unit) {
        super(activity, parent, formattedId, weather, timeZone, true, provider, unit);
        this.c = activity;
    }

    @Override
    protected int getDaytimeTemperatureC(Weather weather, int index) {
        return weather.getDailyForecast().get(index).day().getTemperature().getTemperature();
    }

    @Override
    protected int getNighttimeTemperatureC(Weather weather, int index) {
        return weather.getDailyForecast().get(index).night().getTemperature().getTemperature();
    }

    @Override
    protected int getDaytimeTemperature(Weather weather, int index, TemperatureUnit unit) {
        return unit.getTemperature(getDaytimeTemperatureC(weather, index));
    }

    @Override
    protected int getNighttimeTemperature(Weather weather, int index, TemperatureUnit unit) {
        return unit.getTemperature(getNighttimeTemperatureC(weather, index));
    }

    @Override
    protected String getDaytimeTemperatureString(Weather weather, int index, TemperatureUnit unit) {
        return weather.getDailyForecast().get(index).day().getTemperature().getTemperature(c, unit);
    }

    @Override
    protected String getNighttimeTemperatureString(Weather weather, int index, TemperatureUnit unit) {
        return weather.getDailyForecast().get(index).night().getTemperature().getTemperature(c, unit);
    }

    @Override
    protected String getShortDaytimeTemperatureString(Weather weather, int index, TemperatureUnit unit) {
        return weather.getDailyForecast().get(index).day().getTemperature().getShortTemperature(c, unit);
    }

    @Override
    protected String getShortNighttimeTemperatureString(Weather weather, int index, TemperatureUnit unit) {
        return weather.getDailyForecast().get(index).night().getTemperature().getShortTemperature(c, unit);
    }
}