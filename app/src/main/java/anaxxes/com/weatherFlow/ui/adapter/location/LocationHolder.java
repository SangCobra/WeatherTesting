package anaxxes.com.weatherFlow.ui.adapter.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import anaxxes.com.weatherFlow.OnActionCallback;
import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.databinding.ItemLocation2Binding;
import anaxxes.com.weatherFlow.databinding.ItemLocationBinding;
import anaxxes.com.weatherFlow.db.DatabaseHelper;
import anaxxes.com.weatherFlow.main.dialog.DeleteDialog;
import anaxxes.com.weatherFlow.resource.provider.ResourceProvider;
import anaxxes.com.weatherFlow.utils.DisplayUtils;
import anaxxes.com.weatherFlow.utils.manager.ThemeManager;
import anaxxes.com.weatherFlow.utils.manager.TimeManager;

public class LocationHolder extends RecyclerView.ViewHolder {

    private ItemLocation2Binding binding;
    protected LocationModel model;
    private ThemeManager themeManager;
    private int direction;
    private @ColorInt
    int swipeEndColor;

    protected LocationHolder(ItemLocation2Binding binding,
                             LocationAdapter.OnLocationItemClickListener listener) {
        super(binding.container);
        this.binding = binding;
        binding.container.setOnClickListener(v -> listener.onClick(v, model.location.getFormattedId(),getAdapterPosition()));
    }

    @SuppressLint("SetTextI18n")
    protected void onBindView(Context context, LocationModel model, ResourceProvider resourceProvider, boolean isLocationActivity) {
        this.model = model;
        direction = 0;
        swipeEndColor = ContextCompat.getColor(context,
                model.location.isCurrentPosition() ? R.color.colorPrimary : R.color.colorTextAlert);

        binding.title.setText(model.title);
        String weatherText = "";
        if (model.location.getWeather() == null) {
            binding.temperatureContainer.setVisibility(View.INVISIBLE);

        } else {
            binding.temperatureContainer.setVisibility(View.VISIBLE);
            weatherText = model.location.getWeather().getCurrent().getWeatherText();
            switch (model.location.getWeather().getCurrent().getWeatherCode()) {
                case CLEAR:
                    binding.imgCityLocation.setImageResource(R.drawable.img_clear);
                    binding.geoPosition.setText("Clear");

                    break;
                case PARTLY_CLOUDY:
                    binding.imgCityLocation.setImageResource(R.drawable.img_partly_cloudy);
                    binding.geoPosition.setText("Partly cloudy");

                    break;
                case CLOUDY:
                    binding.imgCityLocation.setImageResource(R.drawable.img_sun_cloudy);
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
                    binding.imgCityLocation.setImageResource(R.drawable.img_thunder_rain);
                    binding.geoPosition.setText("Thunderstorm");
                    break;

            }


        }

        drawSwipe(context, 0);
        drawDrag(context, false);
    }

    @SuppressLint({"NotifyDataSetChanged", "NewApi"})
    public void deleteLocation(LocationModel model, LocationAdapter adapter, List<Location> locationList, LocationTouchCallback.OnLocationListChangedListener listenerChange) {
        binding.deleteLocation.setOnClickListener(v -> {
            DeleteDialog.Companion.start(itemView.getContext(), (key, data) -> {
                if (Objects.equals(key, "delete")){
                    listenerChange.onLocationRemoved(locationList, model.location);
                    adapter.update(DatabaseHelper.getInstance(itemView.getContext()).readLocationList());
                }
            });
        });

    }

    public void drawDrag(Context context, boolean elevate) {
        binding.container.setElevation(DisplayUtils.dpToPx(context, elevate ? 10 : 0));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        int sideMargins = (int) DisplayUtils.dpToPx(context,10);
        params.setMargins(0,0,0,sideMargins);

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
