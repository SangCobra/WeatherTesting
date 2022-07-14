package anaxxes.com.weatherFlow.daily.adapter.holder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.weather.Daily;
import anaxxes.com.weatherFlow.settings.SettingsOptionManager;
import anaxxes.com.weatherFlow.ui.adapter.DailyForecastAdapter;

public class DailyListAdapter extends RecyclerView.Adapter<DailyListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Daily> list;
    private SettingsOptionManager settingsOptionManager;
    private DailyForecastAdapter.DailyForecastClickListener listener;

    public DailyListAdapter(Context context, DailyForecastAdapter.DailyForecastClickListener listener) {
        this.context = context;
        this.listener = listener;
        settingsOptionManager = SettingsOptionManager.getInstance(context);
    }

    public void setList(ArrayList<Daily> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_daily_list_adapter, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Daily daily = list.get(position);
//        if (daily.isExpand()){
//            holder.moreInfo.setVisibility(View.VISIBLE);
//            holder.expand.setImageResource(R.drawable.ic_back_up);
//        }else {
//            holder.moreInfo.setVisibility(View.GONE);
//            holder.expand.setImageResource(R.drawable.ic_back);
//        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy\nEEEE");
        holder.time.setText(dateFormat.format(daily.getDate()));
        holder.precipitationProb.setText(Math.round(daily.day().getPrecipitationProbability().getTotal()) + "%");
        holder.maxTerm.setText(settingsOptionManager.getTemperatureUnit().getShortTemperatureText(context, daily.day().getTemperature().getTemperature()));
        holder.minTerm.setText(settingsOptionManager.getTemperatureUnit().getShortTemperatureText(context, daily.night().getTemperature().getTemperature()));
        holder.statusWeather.setText(daily.day().getWeatherPhase());
//        holder.precipitation.setText("Precipitation: " + settingsOptionManager.getPrecipitationUnit().getPrecipitationText(context, daily.day().getPrecipitation().getTotal()));
//        holder.windChill.setText("Wind chill: " + daily.day().getWind().getLevel());
//        holder.cloudCover.setText("Cloud cover: " + daily.day().getCloudCover());
//        holder.moonPhrase.setText("Moon phrase: " + daily.getMoonPhase().getMoonPhase(context));
//        holder.windSpeed.setText("Wind speed: " + settingsOptionManager.getSpeedUnit().getSpeedText(context, daily.day().getWind().getSpeed()));
//        holder.uvIndex.setText("Ultraviolet index: " + daily.getUV().getIndex());
//        holder.windDir.setText("Wind direction: " + daily.day().getWind().getDirection());
        switch (daily.day().getWeatherCode()) {
            case CLEAR:
                holder.iconWeather.setImageResource(R.drawable.img_clear);
                break;
            case PARTLY_CLOUDY:
                holder.iconWeather.setImageResource(R.drawable.img_partly_cloudy);

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
                holder.iconWeather.setImageResource(R.drawable.img_thunder_rain);


                break;

        }
        holder.itemView.setOnClickListener(v -> {
//            if (daily.isExpand()){
//                holder.moreInfo.setVisibility(View.GONE);
//                daily.setExpand(false);
//                holder.expand.setImageResource(R.drawable.ic_back);
//            }
//            else {
//                holder.moreInfo.setVisibility(View.VISIBLE);
//                daily.setExpand(true);
//                holder.expand.setImageResource(R.drawable.ic_back_up);
//            }
            this.listener.clickDaily(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView time, precipitationProb, maxTerm, minTerm, statusWeather, precipitation, windChill, cloudCover, moonPhrase, windSpeed, uvIndex, windDir;
        private ImageView iconWeather, expand;
        private LinearLayout moreInfo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time_or_date_daily);
            precipitationProb = itemView.findViewById(R.id.percent_rain_down_daily);
            maxTerm = itemView.findViewById(R.id.term_max);
            minTerm = itemView.findViewById(R.id.term_min);
            statusWeather = itemView.findViewById(R.id.daily_weather_text);
//            precipitation = itemView.findViewById(R.id.precipitation_daily);
//            windChill = itemView.findViewById(R.id.wind_chill_daily);
//            cloudCover = itemView.findViewById(R.id.cloud_cover_daily);
//            moonPhrase = itemView.findViewById(R.id.moon_phrase_daily);
//            windSpeed = itemView.findViewById(R.id.wind_speed_daily);
//            uvIndex = itemView.findViewById(R.id.ultraviolet_index_daily);
//            windDir = itemView.findViewById(R.id.wind_dir_daily);
            iconWeather = itemView.findViewById(R.id.icon_status_weather_daily);
            expand = itemView.findViewById(R.id.icon_expand);
        }
    }
}
