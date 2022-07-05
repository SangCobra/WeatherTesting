package anaxxes.com.weatherFlow.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.option.unit.CloudCoverUnit;
import anaxxes.com.weatherFlow.basic.model.option.unit.SpeedUnit;
import anaxxes.com.weatherFlow.basic.model.weather.Hourly;
import anaxxes.com.weatherFlow.models.TodayForecastModel;
import anaxxes.com.weatherFlow.settings.SettingsOptionManager;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder> {

    private Context context;
    private ArrayList<Hourly> list = new ArrayList<>();
    private final SettingsOptionManager settingsOptionManager = SettingsOptionManager.getInstance(context);

    public HourlyForecastAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HourlyForecastAdapter.HourlyForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HourlyForecastAdapter.HourlyForecastViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_infomation, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyForecastAdapter.HourlyForecastViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HourlyForecastViewHolder extends RecyclerView.ViewHolder {

        private ImageView hourylImage, iconBack;
        private TextView tvWeatherText, tvHourlyTime, tvTemp, precipitation, windChill, cloudCover, windSpeed, humidity, dewPoint, ultravioletIndex, windDirection;
        private LinearLayout moreInfo, expand;
        private RecyclerView hourlyDetailsList;


        public HourlyForecastViewHolder(@NonNull View itemView) {
            super(itemView);

            hourylImage = itemView.findViewById(R.id.hourlyImage);
            tvWeatherText = itemView.findViewById(R.id.tvHourlyWeatherText);
            tvHourlyTime = itemView.findViewById(R.id.tvHourlyTime);
//            hourlyDetailsList = itemView.findViewById(R.id.hourlyDetailsList);
            tvTemp = itemView.findViewById(R.id.tvHourlyTemp);
            precipitation = itemView.findViewById(R.id.precipitation_hours);
            windChill = itemView.findViewById(R.id.wind_chill_hours);
            cloudCover = itemView.findViewById(R.id.cloud_cover_hours);
            windSpeed = itemView.findViewById(R.id.wind_speed_hour);
            humidity = itemView.findViewById(R.id.humidity_hours);
            dewPoint = itemView.findViewById(R.id.dew_point_hours);
            ultravioletIndex = itemView.findViewById(R.id.ultraviolet_index_hours);
            windDirection = itemView.findViewById(R.id.wind_dir_hours);
            moreInfo = itemView.findViewById(R.id.more_info_hourly);
            iconBack = itemView.findViewById(R.id.icon_expand_hourly);
            expand = itemView.findViewById(R.id.mostly_cloudy);
        }

        @SuppressLint("SetTextI18n")
        public void setData(Hourly model) {
            model.setExpand(false);
            moreInfo.setVisibility(View.GONE);
            switch (model.getWeatherCode()) {
                case CLEAR:
                    if (!model.isDaylight()) {
                        hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.img_moon));
                    } else {
                        hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.img_clear));

                    }
                    break;
                case PARTLY_CLOUDY:
                    if (!model.isDaylight()) {
                        hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.img_cloudy_moon));
                    } else {
                        hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.img_partly_cloudy));
                    }
                    break;
                case CLOUDY:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.img_sun_cloudy));

                    break;
                case RAIN:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.img_rain));

                    break;
                case SNOW:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.img_snow));

                    break;
                case WIND:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.img_weather_wind));

                    break;
                case FOG:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.img_fog));

                    break;
                case HAZE:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.img_fog));

                    break;
                case SLEET:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.weather_sleet));

                    break;
                case HAIL:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.weather_hail));

                    break;
                case THUNDER:
                case THUNDERSTORM:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.img_thunder));


                    break;

            }

            tvWeatherText.setText(model.getWeatherText());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            tvHourlyTime.setText(model.getHour(context));
            tvTemp.setText(model.getTemperature().getTemperature(context, SettingsOptionManager.getInstance(context).getTemperatureUnit()));
            precipitation.setText("Precipitation: " + settingsOptionManager.getPrecipitationUnit().getPrecipitationText(
                    context,
                    model.getPrecipitationProbability().getTotal()));
            windChill.setText("Wind chill: " + model.getWind().getLevel());
            cloudCover.setText("Cloud cover: " + CloudCoverUnit.PERCENT.getCloudCoverText(
                    model.getCloudCover()
            ));
            windSpeed.setText("Wind speed: " + settingsOptionManager.getSpeedUnit().getSpeedText(context, model.getWind().getSpeed()));
            humidity.setText("Humidity:");
            dewPoint.setText("Dew point: " + settingsOptionManager.getTemperatureUnit().getTemperatureText(context,
                    model.getDewPoint()));
            ultravioletIndex.setText("Ultraviolet index: " + model.getUv().getIndex() + " " + model.getUv().getLevel());
            windDirection.setText("Wind direction: " + model.getWind().getDirection());
            expand.setOnClickListener(v -> {
                if (model.isExpand()){
                    iconBack.setImageResource(R.drawable.ic_back);
                    moreInfo.setVisibility(View.GONE);
                    model.setExpand(false);
                }
                else {
                    iconBack.setImageResource(R.drawable.ic_back_up);
                    moreInfo.setVisibility(View.VISIBLE);
                    model.setExpand(true);
                }
            });


            TodayForecastAdapter adapter = new TodayForecastAdapter(context);
//            hourlyDetailsList.setAdapter(adapter);
//            hourlyDetailsList.setLayoutManager(new GridLayoutManager(context,3));

            adapter.updateData(getHourlyForecastList(model));

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(ArrayList<Hourly> newList) {
        list = newList;
        notifyDataSetChanged();
    }


    private ArrayList<TodayForecastModel> getHourlyForecastList(Hourly hourly) {

        SpeedUnit unit = SettingsOptionManager.getInstance(context).getSpeedUnit();
        return new ArrayList<TodayForecastModel>() {
            {
                add(new TodayForecastModel(R.drawable.ic_real_feel, context.getString(R.string.real_feel_temperature), hourly.getTemperature().getRealFeelTemperature(context, SettingsOptionManager.getInstance(context).getTemperatureUnit())));
                add(new TodayForecastModel(R.drawable.ic_dew_points, context.getString(R.string.dew_point), settingsOptionManager.getTemperatureUnit().getTemperatureText(context,
                        hourly.getDewPoint())));
                add(new TodayForecastModel(R.drawable.ic_visibilty, context.getString(R.string.visibility), settingsOptionManager.getDistanceUnit().getDistanceText(context,hourly.getVisibility())));
                add(new TodayForecastModel(R.drawable.ic_wind_direction, context.getString(R.string.wind_direction), hourly.getWind().getDirection()));


                add(new TodayForecastModel(R.drawable.ic_wind_speed, context.getString(R.string.wind_speed),unit.getSpeedText(context, hourly.getWind().getSpeed())));
                add(new TodayForecastModel(R.drawable.ic_wind_gauge, context.getString(R.string.wind_level), hourly.getWind().getLevel()));

                add(new TodayForecastModel(R.drawable.ic_uv, context.getString(R.string.uv_index),hourly.getUv().getIndex() + " " + hourly.getUv().getLevel()));
                add(new TodayForecastModel(R.drawable.ic_cloud_cover, context.getString(R.string.cloud_cover), CloudCoverUnit.PERCENT.getCloudCoverText(
                        hourly.getCloudCover()
                )));
                add(new TodayForecastModel(R.drawable.ic_precipitation, context.getString(R.string.precipitation),settingsOptionManager.getPrecipitationUnit().getPrecipitationText(
                        context,
                       hourly.getPrecipitationProbability().getTotal())));

            }
        };
    }

}
