package com.mtgtech.weather_forecast.daily_weather.adapter.holder;

import static com.mtgtech.weather_forecast.view.adapter.HourlyForecastAdapter.AD_TYPE;
import static com.mtgtech.weather_forecast.view.adapter.HourlyForecastAdapter.NON_AD_TYPE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.common.control.manager.AdmobManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.mtgtech.weather_forecast.BuildConfig;
import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.settings.SettingsOptionManager;
import com.mtgtech.weather_forecast.view.adapter.DailyForecastAdapter;
import com.mtgtech.weather_forecast.weather_model.model.weather.Daily;

public class DailyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null) {
            return AD_TYPE;
        } else return NON_AD_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == AD_TYPE)
            return new AdViewHolder(LayoutInflater.from(context).inflate(R.layout.item_native, parent, false));
        else
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_daily_list_adapter, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.setData(list.get(position));
        }

//        if (daily.isExpand()){
//            moreInfo.setVisibility(View.VISIBLE);
//            expand.setImageResource(R.drawable.ic_back_up);
//        }else {
//            moreInfo.setVisibility(View.GONE);
//            expand.setImageResource(R.drawable.ic_back);
//        }

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

        public void setData(Daily daily) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy\nEEEE");
            time.setText(dateFormat.format(daily.getDate()));
            precipitationProb.setText(Math.round(daily.day().getPrecipitationProbability().getTotal()) + "%");
            maxTerm.setText(settingsOptionManager.getTemperatureUnit().getShortTemperatureText(context, daily.day().getTemperature().getTemperature()));
            minTerm.setText(settingsOptionManager.getTemperatureUnit().getShortTemperatureText(context, daily.night().getTemperature().getTemperature()));
            statusWeather.setText(daily.day().getWeatherPhase());
//        precipitation.setText("Precipitation: " + settingsOptionManager.getPrecipitationUnit().getPrecipitationText(context, daily.day().getPrecipitation().getTotal()));
//        windChill.setText("Wind chill: " + daily.day().getWind().getLevel());
//        cloudCover.setText("Cloud cover: " + daily.day().getCloudCover());
//        moonPhrase.setText("Moon phrase: " + daily.getMoonPhase().getMoonPhase(context));
//        windSpeed.setText("Wind speed: " + settingsOptionManager.getSpeedUnit().getSpeedText(context, daily.day().getWind().getSpeed()));
//        uvIndex.setText("Ultraviolet index: " + daily.getUV().getIndex());
//        windDir.setText("Wind direction: " + daily.day().getWind().getDirection());
            switch (daily.day().getWeatherCode()) {
                case CLEAR:
                    iconWeather.setImageResource(R.drawable.img_clear);
                    break;
                case PARTLY_CLOUDY:
                    iconWeather.setImageResource(R.drawable.img_partly_cloudy);

                    break;
                case CLOUDY:
                    iconWeather.setImageResource(R.drawable.img_sun_cloudy);

                    break;
                case RAIN:
                    iconWeather.setImageResource(R.drawable.img_rain);

                    break;
                case SNOW:
                    iconWeather.setImageResource(R.drawable.img_snow);

                    break;
                case WIND:
                    iconWeather.setImageResource(R.drawable.img_weather_wind);

                    break;
                case FOG:
                case HAZE:
                    iconWeather.setImageResource(R.drawable.img_fog);

                    break;

                case SLEET:
                    iconWeather.setImageResource(R.drawable.weather_sleet);

                    break;
                case HAIL:
                    iconWeather.setImageResource(R.drawable.weather_hail);

                    break;
                case THUNDER:
                case THUNDERSTORM:
                    iconWeather.setImageResource(R.drawable.img_thunder_rain);


                    break;

            }
            itemView.setOnClickListener(v -> {
//            if (daily.isExpand()){
//                moreInfo.setVisibility(View.GONE);
//                daily.setExpand(false);
//                expand.setImageResource(R.drawable.ic_back);
//            }
//            else {
//                moreInfo.setVisibility(View.VISIBLE);
//                daily.setExpand(true);
//                expand.setImageResource(R.drawable.ic_back_up);
//            }
                DailyListAdapter.this.listener.clickDaily(list.indexOf(daily));
            });
        }
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout frAd;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            frAd = itemView.findViewById(R.id.fr_ad_native);
            frAd.setBackgroundColor(Color.parseColor("#3B3B3B"));
            AdmobManager.getInstance().loadNative(context, BuildConfig.native_daily_weather, frAd, R.layout.custom_native_1);
        }
    }
}
