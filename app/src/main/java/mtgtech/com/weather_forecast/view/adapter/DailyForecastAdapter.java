package mtgtech.com.weather_forecast.view.adapter;

import android.annotation.SuppressLint;
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
import mtgtech.com.weather_forecast.databinding.ItemDailyForecastBinding;
import mtgtech.com.weather_forecast.settings.SettingsOptionManager;
import mtgtech.com.weather_forecast.utils.MyUtils;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.DailyForecastViewHolder> {

    private Context context;
    private ArrayList<Daily> list = new ArrayList<>();
    private DailyForecastClickListener listener;

    public DailyForecastAdapter(Context context,DailyForecastClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DailyForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DailyForecastViewHolder(ItemDailyForecastBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DailyForecastViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DailyForecastViewHolder extends RecyclerView.ViewHolder {

        private ItemDailyForecastBinding binding;

        public DailyForecastViewHolder(ItemDailyForecastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

        public void setData(Daily model) {
            String minMaxWeather = model.day().getTemperature().getTemperatureWithoutDegree(context, SettingsOptionManager.getInstance(context).getTemperatureUnit())
                    + "/" +
                    model.night().getTemperature().getShortTemperature(context, SettingsOptionManager.getInstance(context).getTemperatureUnit());
            binding.tvMinMaxTemp.setText(minMaxWeather);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat daySdf = new SimpleDateFormat("EEE");
            binding.tvDate.setText(sdf.format(model.getDate()));
            if (getAdapterPosition() != 0)
                binding.tvDay.setText(daySdf.format(model.getDate()));
            binding.tvDailyForecastStatus.setText(model.day().getWeatherText());

//            if(getAdapterPosition() +1 == list.size()){
//                binding.divider.setVisibility(View.GONE);
//            }else{
//                binding.divider.setVisibility(View.VISIBLE);
//            }

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View view) {
                                                         listener.clickDaily(getAdapterPosition());
                                                     }
                                                 }
            );

                switch (model.day().getWeatherCode()) {
                    case CLEAR:
                        if(MyUtils.isNight()){
                            binding.imgDailyForecast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_clear_night));

                        }else{
                            binding.imgDailyForecast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_clear_day));

                        }
                        break;
                    case PARTLY_CLOUDY:
                        if(MyUtils.isNight())
                        {
                            binding.imgDailyForecast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_partly_cloudy_night));

                        }else{
                            binding.imgDailyForecast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_partly_cloudy_day));

                        }
                        break;
                    case CLOUDY:
                        binding.imgDailyForecast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_cloudy));
                        break;
                    case RAIN:
                        binding.imgDailyForecast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_rain));
                        break;
                    case SNOW:
                        binding.imgDailyForecast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_snow));
                        break;
                    case WIND:
                        binding.imgDailyForecast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_weather_wind));
                        break;
                    case FOG:
                        binding.imgDailyForecast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_fog));
                        break;
                    case HAZE:
                        binding.imgDailyForecast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_haze));
                        break;
                    case SLEET:
                        binding.imgDailyForecast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_sleet));
                        break;
                    case HAIL:
                        binding.imgDailyForecast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_hail));
                        break;
                    case THUNDER:
                    case THUNDERSTORM:
                        binding.imgDailyForecast.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_thunder));

                        break;

                }

        }
    }

    public void updateData(ArrayList<Daily> newList) {
            list = newList;
        notifyDataSetChanged();
    }

    public interface DailyForecastClickListener{
        void clickDaily(int index);
    }
}
