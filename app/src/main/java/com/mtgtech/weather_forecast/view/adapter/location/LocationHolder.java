package com.mtgtech.weather_forecast.view.adapter.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.databinding.ItemLocation2Binding;
import com.mtgtech.weather_forecast.db.DatabaseHelper;
import com.mtgtech.weather_forecast.main.dialog.DeleteDialog;
import com.mtgtech.weather_forecast.resource.provider.ResourceProvider;
import com.mtgtech.weather_forecast.utils.DisplayUtils;
import com.mtgtech.weather_forecast.utils.manager.ThemeManager;
import com.mtgtech.weather_forecast.weather_model.model.location.Location;

public class LocationHolder extends RecyclerView.ViewHolder {

    protected LocationModel model;
    private ItemLocation2Binding binding;
    private ThemeManager themeManager;
    private int direction;
    private @ColorInt
    int swipeEndColor;

    protected LocationHolder(ItemLocation2Binding binding,
                             LocationAdapter.OnLocationItemClickListener listener) {
        super(binding.container);
        this.binding = binding;
        binding.container.setOnClickListener(v -> listener.onClick(v, model.location.getFormattedId(), getAdapterPosition()));
    }

    @SuppressLint("SetTextI18n")
    protected void onBindView(Context context, LocationModel model, ResourceProvider resourceProvider, boolean isLocationActivity) {
        this.model = model;
        direction = 0;
        swipeEndColor = ContextCompat.getColor(context,
                model.location.isCurrentPosition() ? R.color.colorPrimary : R.color.colorTextAlert);
        if (getAdapterPosition() == 0) {
            binding.deleteLocation.setVisibility(View.GONE);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int sideMargins = (int) DisplayUtils.dpToPx(context, 10);
        params.setMargins(0, 0, 0, sideMargins);

        binding.container.setLayoutParams(params);

        binding.title.setText(model.title);
        String weatherText = "";
        if (model.location.getWeather() == null) {
            binding.temperatureContainer.setVisibility(View.INVISIBLE);

        } else {
            binding.temperatureContainer.setVisibility(View.VISIBLE);
            weatherText = model.location.getWeather().getCurrent().getWeatherText();
            switch (model.location.getWeather().getCurrent().getWeatherCode()) {
                case CLEAR:
                    if (model.location.getWeather().getHourlyForecast().get(0).isDaylight()) {
                        binding.imgCityLocation.setImageResource(R.drawable.img_clear);
                    } else {
                        binding.imgCityLocation.setImageResource(R.drawable.img_moon);
                    }
                    binding.geoPosition.setText("Clear");

                    break;
                case PARTLY_CLOUDY:
                    if (model.location.getWeather().getHourlyForecast().get(0).isDaylight()) {
                        binding.imgCityLocation.setImageResource(R.drawable.img_partly_cloudy);
                    } else {
                        binding.imgCityLocation.setImageResource(R.drawable.img_cloudy_moon);

                    }
                    binding.geoPosition.setText("Partly cloudy");

                    break;
                case CLOUDY:
                    if (model.location.getWeather().getHourlyForecast().get(0).isDaylight()) {
                        binding.imgCityLocation.setImageResource(R.drawable.img_sun_cloudy);
                    } else {
                        binding.imgCityLocation.setImageResource(R.drawable.img_cloudy_moon);
                    }
                    binding.geoPosition.setText("Cloudy");

                    break;
                case RAIN:
                    binding.imgCityLocation.setImageResource(R.drawable.img_rain);
                    binding.geoPosition.setText("Rain");

                    break;
                case SNOW:
                    binding.imgCityLocation.setImageResource(R.drawable.img_snow);
                    binding.geoPosition.setText("Snow");

                    break;
                case WIND:
                    binding.imgCityLocation.setImageResource(R.drawable.img_weather_wind);
                    binding.geoPosition.setText("Wind");

                    break;
                case FOG:
                    binding.imgCityLocation.setImageResource(R.drawable.img_fog);
                    binding.geoPosition.setText("Fog");

                    break;
                case HAZE:
                    binding.imgCityLocation.setImageResource(R.drawable.img_fog);
                    binding.geoPosition.setText("Haze");

                    break;
                case SLEET:
                    binding.imgCityLocation.setImageResource(R.drawable.weather_sleet);
                    binding.geoPosition.setText("Sleet");

                    break;
                case HAIL:
                    binding.imgCityLocation.setImageResource(R.drawable.weather_hail);
                    binding.geoPosition.setText("Hail");

                    break;
                case THUNDER:
                case THUNDERSTORM:
                    if (model.location.getWeather().getHourlyForecast().get(0).isDaylight()) {
                        binding.imgCityLocation.setImageResource(R.drawable.img_thunder_rain);
                    } else {
                        binding.imgCityLocation.setImageResource(R.drawable.img_thunder_night);

                    }
                    binding.geoPosition.setText("Thunderstorm");
                    break;

            }


        }

//        drawSwipe(context, 0);
//        drawDrag(context, false);
    }

    @SuppressLint({"NotifyDataSetChanged", "NewApi"})
    public void deleteLocation(LocationModel model, LocationAdapter adapter, List<Location> locationList, LocationTouchCallback.OnLocationListChangedListener listenerChange) {
        binding.deleteLocation.setOnClickListener(v -> {
            DeleteDialog.Companion.start(itemView.getContext(), (key, data) -> {
                if (Objects.equals(key, "delete")) {
                    List<Location> list = adapter.getLocationList();
                    Location location = list.remove(getAdapterPosition());
                    location.setWeather(DatabaseHelper.getInstance(itemView.getContext()).readWeather(location));
//                    DatabaseHelper.getInstance(itemView.getContext()).deleteWeather(model.location);
                    adapter.update(list, location.getFormattedId());
                    listenerChange.onLocationRemoved(locationList, model.location);
                }
            });
        });

    }

    public void drawDrag(Context context, boolean elevate) {
        binding.container.setElevation(DisplayUtils.dpToPx(context, elevate ? 10 : 0));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int sideMargins = (int) DisplayUtils.dpToPx(context, 10);
        params.setMargins(0, 0, 0, sideMargins);

        binding.container.setLayoutParams(params);
    }

    public void drawSwipe(Context context, float dX) {
        if (itemView.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            if (dX < 0 && direction >= 0) {
                direction = -1;
                binding.container.setBackgroundColor(ContextCompat.getColor(context, R.color.striking_red));
            } else if (dX > 0 && direction <= 0) {
                direction = 1;
            }
        } else {
            if (dX < 0 && direction >= 0) {
                direction = -1;
            } else if (dX > 0 && direction <= 0) {
                direction = 1;
            }
        }
        binding.container.setTranslationX(0);
    }
}
