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
                break;
            case PARTLY_CLOUDY:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_partly_cloudy);

                break;
            case CLOUDY:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_sun_cloudy);

                break;
            case RAIN:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_rain);

                break;
            case SNOW:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_snow);

                break;
            case WIND:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_weather_wind);

                break;
            case FOG:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_fog);

                break;
            case HAZE:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_fog);

                break;
            case SLEET:
                holder.iconWeatherDaily.setImageResource(R.drawable.weather_sleet);

                break;
            case HAIL:
                holder.iconWeatherDaily.setImageResource(R.drawable.weather_hail);

                break;
            case THUNDER:
            case THUNDERSTORM:
                holder.iconWeatherDaily.setImageResource(R.drawable.img_thunder);


                break;

        }
        holder.percentRain.setText(daily.day().getPrecipitationProbability().getTotal() + "%");
        holder.maxTerm.setText(settingsOptionManager.getTemperatureUnit().getTemperatureText(context, daily.day().getTemperature().getTemperature()));
        holder.minTerm.setText(settingsOptionManager.getTemperatureUnit().getTemperatureText(context, daily.night().getTemperature().getTemperature()));
        int dif = daily.day().getTemperature().getTemperature() - daily.night().getTemperature().getTemperature();
        ViewGroup.LayoutParams layoutParams = holder.length.getLayoutParams();
        layoutParams.height =(dif*100/getListIndexBiggest());
        layoutParams.width = 25;
        holder.itemView.setOnClickListener(v -> {
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
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dayName, percentRain, maxTerm, minTerm;
        private ImageView iconWeatherDaily;
        private FrameLayout length;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.time_in_daily);
            percentRain = itemView.findViewById(R.id.percent_rain_down_in_daily);
            maxTerm = itemView.findViewById(R.id.percent_in_daily_up);
            minTerm = itemView.findViewById(R.id.percent_in_daily_down);
            iconWeatherDaily = itemView.findViewById(R.id.icon_weather_in_daily);
            length = itemView.findViewById(R.id.length_to_percent);
        }
    }
}
