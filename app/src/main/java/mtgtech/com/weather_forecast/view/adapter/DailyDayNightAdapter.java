package mtgtech.com.weather_forecast.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.model.weather.Daily;
import mtgtech.com.weather_forecast.databinding.ItemDailyDayNightBinding;
import mtgtech.com.weather_forecast.settings.SettingsOptionManager;

public class DailyDayNightAdapter extends RecyclerView.Adapter<DailyDayNightAdapter.DailyDayNightViewHolder> {

    private Context context;
    private ArrayList<Daily> list = new ArrayList<>();
    private DailyForecastAdapter.DailyForecastClickListener listener;

    public DailyDayNightAdapter(Context context, DailyForecastAdapter.DailyForecastClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DailyDayNightAdapter.DailyDayNightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DailyDayNightAdapter.DailyDayNightViewHolder(ItemDailyDayNightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DailyDayNightAdapter.DailyDayNightViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DailyDayNightViewHolder extends RecyclerView.ViewHolder {

        private ItemDailyDayNightBinding binding;

        public DailyDayNightViewHolder(ItemDailyDayNightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

        public void setData(Daily model) {
            String minMaxWeather = model.day().getTemperature().getTemperatureWithoutDegree(context, SettingsOptionManager.getInstance(context).getTemperatureUnit())
                    + "/" +
                    model.night().getTemperature().getTemperature(context, SettingsOptionManager.getInstance(context).getTemperatureUnit());
            binding.tvDayTemp.setText(model.day().getTemperature().getTemperature(context, SettingsOptionManager.getInstance(context).getTemperatureUnit()));
            binding.tvNightTemp.setText(model.night().getTemperature().getTemperature(context, SettingsOptionManager.getInstance(context).getTemperatureUnit()));
            SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM");
            binding.tvDayAndDate.setText(sdf.format(model.getDate()));

            binding.tvDailyDayWeather.setText(model.day().getWeatherText());
            binding.tvDailyNightWeather.setText(model.night().getWeatherText());


            binding.getRoot().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    listener.clickDaily(getAdapterPosition());
                }
            });

            switch (model.day().getWeatherCode()) {
                case CLEAR:

                    binding.imgDailyDay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_clear_day));

                    break;
                case PARTLY_CLOUDY:
                    binding.imgDailyDay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_partly_cloudy_day));

                    break;
                case CLOUDY:
                    binding.imgDailyDay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_cloudy));
                    break;
                case RAIN:
                    binding.imgDailyDay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_rain));
                    break;
                case SNOW:
                    binding.imgDailyDay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_snow));
                    break;
                case WIND:
                    binding.imgDailyDay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_weather_wind));
                    break;
                case FOG:
                    binding.imgDailyDay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_fog));
                    break;
                case HAZE:
                    binding.imgDailyDay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_haze));
                    break;
                case SLEET:
                    binding.imgDailyDay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_sleet));
                    break;
                case HAIL:
                    binding.imgDailyDay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_hail));
                    break;
                case THUNDER:
                case THUNDERSTORM:
                    binding.imgDailyDay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_thunder));

                    break;

            }

            switch (model.night().getWeatherCode()) {
                case CLEAR:

                    binding.imgDailyNight.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_clear_night));

                    break;
                case PARTLY_CLOUDY:
                    binding.imgDailyNight.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_partly_cloudy_night));

                    break;
                case CLOUDY:
                    binding.imgDailyNight.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_cloudy));
                    break;
                case RAIN:
                    binding.imgDailyNight.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_rain));
                    break;
                case SNOW:
                    binding.imgDailyNight.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_snow));
                    break;
                case WIND:
                    binding.imgDailyNight.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_weather_wind));
                    break;
                case FOG:
                    binding.imgDailyNight.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_fog));
                    break;
                case HAZE:
                    binding.imgDailyNight.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_haze));
                    break;
                case SLEET:
                    binding.imgDailyNight.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_sleet));
                    break;
                case HAIL:
                    binding.imgDailyNight.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_hail));
                    break;
                case THUNDER:
                case THUNDERSTORM:
                    binding.imgDailyNight.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_thunder));

                    break;

            }

        }
    }

    public void updateData(ArrayList<Daily> newList) {
        list = newList;
        notifyDataSetChanged();
    }
}
