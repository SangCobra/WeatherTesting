package anaxxes.com.weatherFlow.main.adapter.trend.daily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.weather.Daily;
import anaxxes.com.weatherFlow.settings.SettingsOptionManager;
import anaxxes.com.weatherFlow.ui.adapter.DailyForecastAdapter;
import anaxxes.com.weatherFlow.utils.DisplayUtils;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Daily> list;
    private final SettingsOptionManager settingsOptionManager;
    private DailyForecastAdapter.DailyForecastClickListener listener;

    public DailyAdapter(Context context, DailyForecastAdapter.DailyForecastClickListener listener) {
        this.context = context;
        this.listener = listener;
        settingsOptionManager = SettingsOptionManager.getInstance(context);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Daily> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_main, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Daily daily = list.get(position);
        String day = "";
        if (daily.getDate().toString().startsWith("Mon")){
            day = "Mon";
        }
        else if (daily.getDate().toString().startsWith("Tue")) day = "Tue";
        else if (daily.getDate().toString().startsWith("Wed")) day = "Wed";
        else if (daily.getDate().toString().startsWith("Thu")) day = "Thu";
        else if (daily.getDate().toString().startsWith("Fri")) day = "Fri";
        else if (daily.getDate().toString().startsWith("Sat")) day = "Sat";
        else if (daily.getDate().toString().startsWith("Sun")) day = "Sun";
        holder.dayName.setText(day);

        switch (daily.day().getWeatherCode()) {
            case CLEAR:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_clear);
                holder.iconRain.setImageResource(R.drawable.ic_wi_umbrella_yellow);
                break;
            case PARTLY_CLOUDY:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_partly_cloudy);
                holder.iconRain.setImageResource(R.drawable.ic_wi_umbrella_yellow);
                break;
            case CLOUDY:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_sun_cloudy);
                holder.iconRain.setImageResource(R.drawable.ic_wi_umbrella_yellow);
                break;
            case RAIN:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_rain);
                holder.iconRain.setImageResource(R.drawable.ic_wi_umbrella_yellow);

                break;
            case SNOW:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_snow);
                holder.iconRain.setImageResource(R.drawable.ic_snow);

                break;
            case WIND:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_weather_wind);
                holder.iconRain.setImageResource(R.drawable.ic_wi_umbrella_yellow);
                break;
            case FOG:
            case HAZE:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_fog);
                holder.iconRain.setImageResource(R.drawable.ic_wi_umbrella_yellow);
                break;

            case SLEET:
                holder.iconWeatherDaily.setImageResource(R.drawable.weather_sleet);
                holder.iconRain.setImageResource(R.drawable.ic_wi_umbrella_yellow);
                break;
            case HAIL:
                holder.iconWeatherDaily.setImageResource(R.drawable.weather_hail);
                holder.iconRain.setImageResource(R.drawable.ic_wi_umbrella_yellow);
                break;
            case THUNDER:
            case THUNDERSTORM:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_thunder_rain);
                holder.iconRain.setImageResource(R.drawable.ic_wi_umbrella_yellow);
                break;

        }
        holder.percentRain.setText(Math.round(daily.day().getPrecipitationProbability().getTotal()) + "%");
        holder.maxTerm.setText(settingsOptionManager.getTemperatureUnit().getShortTemperatureText(context, daily.day().getTemperature().getTemperature()));
        holder.minTerm.setText(settingsOptionManager.getTemperatureUnit().getShortTemperatureText(context, daily.night().getTemperature().getTemperature()));
        int dif = daily.day().getTemperature().getTemperature() - daily.night().getTemperature().getTemperature();
        ViewGroup.LayoutParams layoutParams = holder.length.getLayoutParams();
        layoutParams.height =(dif*100/getListIndexBiggest());
        layoutParams.width = 25;
        holder.dayName.setOnClickListener(v -> {
            this.listener.clickDaily(position);
        });
    }

    private int getListIndexBiggest() {
        int i = list.get(0).day().getTemperature().getTemperature() - list.get(0).night().getTemperature().getTemperature();
        for (int j = 1; j < list.size(); j++){
            if ((list.get(j).day().getTemperature().getTemperature() - list.get(j).night().getTemperature().getTemperature()) > i){
                i = list.get(j).day().getTemperature().getTemperature() - list.get(j).night().getTemperature().getTemperature();
            }
        }
        return i;
    }

    @Override
    public int getItemCount() {
        return Math.min(list.size(), 7);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dayName, percentRain, maxTerm, minTerm;
        private ImageView iconWeatherDaily, iconRain;
        private FrameLayout length;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.time_in_daily);
            percentRain = itemView.findViewById(R.id.percent_rain_down_in_daily);
            maxTerm = itemView.findViewById(R.id.percent_in_daily_up);
            minTerm = itemView.findViewById(R.id.percent_in_daily_down);
            iconWeatherDaily = itemView.findViewById(R.id.icon_weather_in_daily);
            iconRain = itemView.findViewById(R.id.icon_daily_trend);
            length = itemView.findViewById(R.id.length_to_percent);
        }
    }
}
