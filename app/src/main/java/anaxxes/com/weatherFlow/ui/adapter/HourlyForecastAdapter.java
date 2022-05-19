package anaxxes.com.weatherFlow.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.basic.model.option.unit.CloudCoverUnit;
import anaxxes.com.weatherFlow.basic.model.option.unit.RelativeHumidityUnit;
import anaxxes.com.weatherFlow.basic.model.option.unit.SpeedUnit;
import anaxxes.com.weatherFlow.basic.model.weather.Hourly;
import anaxxes.com.weatherFlow.models.DailyDetailsModel;
import anaxxes.com.weatherFlow.models.TodayForecastModel;
import anaxxes.com.weatherFlow.settings.SettingsOptionManager;
import anaxxes.com.weatherFlow.utils.MyUtils;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder> {

    private Context context;
    private ArrayList<Hourly> list = new ArrayList<>();
    private SettingsOptionManager settingsOptionManager = SettingsOptionManager.getInstance(context);

    public HourlyForecastAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HourlyForecastAdapter.HourlyForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HourlyForecastAdapter.HourlyForecastViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly, parent, false));
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

        private ImageView hourylImage;
        private TextView tvWeatherText, tvHourlyTime, tvTemp;
        private RecyclerView hourlyDetailsList;


        public HourlyForecastViewHolder(@NonNull View itemView) {
            super(itemView);

            hourylImage = itemView.findViewById(R.id.hourlyImage);
            tvWeatherText = itemView.findViewById(R.id.tvHourlyWeatherText);
            tvHourlyTime = itemView.findViewById(R.id.tvHourlyTime);
            hourlyDetailsList = itemView.findViewById(R.id.hourlyDetailsList);
            tvTemp = itemView.findViewById(R.id.tvHourlyTemp);
        }

        public void setData(Hourly model) {

            switch (model.getWeatherCode()) {
                case CLEAR:
                    if (MyUtils.isNight()) {
                        hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_night_clear));
                    } else {
                        hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.weather_clear_day));

                    }
                    break;
                case PARTLY_CLOUDY:
                    if (MyUtils.isNight()) {
                        hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.weather_partly_cloudy_night));
                    } else {
                        hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.weather_partly_cloudy_day));
                    }
                    break;
                case CLOUDY:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.weather_partly_cloudy_day));

                    break;
                case RAIN:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.weather_rain));

                    break;
                case SNOW:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.weather_snow));

                    break;
                case WIND:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.weather_wind));

                    break;
                case FOG:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.weather_fog));

                    break;
                case HAZE:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.weather_haze));

                    break;
                case SLEET:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.weather_sleet));

                    break;
                case HAIL:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.weather_hail));

                    break;
                case THUNDER:
                case THUNDERSTORM:
                    hourylImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.weather_thunder));


                    break;

            }

            tvWeatherText.setText(model.getWeatherText());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            tvHourlyTime.setText(sdf.format(new Date(model.getTime())));
            tvTemp.setText(model.getTemperature().getTemperature(context, SettingsOptionManager.getInstance(context).getTemperatureUnit()));


            TodayForecastAdapter adapter = new TodayForecastAdapter(context);
            hourlyDetailsList.setAdapter(adapter);
            hourlyDetailsList.setLayoutManager(new GridLayoutManager(context,3));

            adapter.updateData(getHourlyForecastList(model));

        }
    }

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
