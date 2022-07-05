package anaxxes.com.weatherFlow.main.adapter.trend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.weather.Hourly;
import anaxxes.com.weatherFlow.settings.SettingsOptionManager;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Hourly> list;
    private final SettingsOptionManager settingsOptionManager;

    public HourlyAdapter(Context context) {
        this.context = context;
        settingsOptionManager = SettingsOptionManager.getInstance(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Hourly> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_houly_main, parent, false));
    }

    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hourly hourly = list.get(position);
        if (position == 0){
            holder.tvHour.setText("NOW");
            holder.tvHour.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            holder.tvHour.setBackgroundResource(R.drawable.shape_border_text);
            holder.tvHour.setTextColor(Color.BLACK);
        }
        else {
            holder.tvHour.setText(hourly.getHour(context));
            holder.tvHour.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
            holder.tvHour.setBackgroundResource(0);
            holder.tvHour.setTextColor(Color.WHITE);
        }
        holder.tvPercentRain.setText(Objects.requireNonNull(hourly.getPrecipitationProbability().getTotal()).toString() + "%");
        switch (hourly.getWeatherCode()) {
            case CLEAR:
                if (!hourly.isDaylight()) {
                    holder.iconWeather.setImageResource(R.drawable.img_moon);
                } else {
                    holder.iconWeather.setImageResource(R.drawable.img_clear);

                }
                break;
            case PARTLY_CLOUDY:
                if (!hourly.isDaylight()) {
                    holder.iconWeather.setImageResource(R.drawable.img_cloudy_moon);
                } else {
                    holder.iconWeather.setImageResource(R.drawable.img_partly_cloudy);
                }
                break;
            case CLOUDY:
                holder.iconWeather.setImageResource(R.drawable.img_sun_cloudy);

                break;
            case RAIN:
                holder.iconWeather.setImageResource(R.drawable.img_rain);

                break;
            case SNOW:
                holder.iconWeather.setImageResource(R.drawable.img_snow);

                break;
            case WIND:
                holder.iconWeather.setImageResource(R.drawable.img_weather_wind);

                break;
            case FOG:
                holder.iconWeather.setImageResource(R.drawable.img_fog);

                break;
            case HAZE:
                holder.iconWeather.setImageResource(R.drawable.img_fog);

                break;
            case SLEET:
                holder.iconWeather.setImageResource(R.drawable.weather_sleet);

                break;
            case HAIL:
                holder.iconWeather.setImageResource(R.drawable.weather_hail);

                break;
            case THUNDER:
            case THUNDERSTORM:
                holder.iconWeather.setImageResource(R.drawable.img_thunder);


                break;

        }

        holder.tvTerm.setText(settingsOptionManager.getTemperatureUnit().getTemperatureText(context, hourly.getTemperature().getTemperature()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHour, tvPercentRain, tvTerm;
        private ImageView iconWeather;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHour = itemView.findViewById(R.id.time_in_day);
            tvPercentRain = itemView.findViewById(R.id.percent_rain_down_in_day);
            tvTerm = itemView.findViewById(R.id.percent_in_hours);
            iconWeather = itemView.findViewById(R.id.icon_weather_in_day);
        }
    }
}
