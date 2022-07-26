package com.mtgtech.weather_forecast.view.weather_widget.insets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.daily_weather.adapter.DailyWeatherAdapter;
import com.mtgtech.weather_forecast.databinding.FragmentDailyDetailsBinding;
import com.mtgtech.weather_forecast.models.DailyDetailsModel;
import com.mtgtech.weather_forecast.resource.provider.ResourcesProviderFactory;
import com.mtgtech.weather_forecast.settings.SettingsOptionManager;
import com.mtgtech.weather_forecast.utils.SunMoonUtils;
import com.mtgtech.weather_forecast.view.adapter.DailyDetailsAdapter;
import com.mtgtech.weather_forecast.weather_model.model.location.Location;
import com.mtgtech.weather_forecast.weather_model.model.option.unit.SpeedUnit;
import com.mtgtech.weather_forecast.weather_model.model.weather.Daily;
import com.mtgtech.weather_forecast.weather_model.model.weather.HalfDay;
import com.mtgtech.weather_forecast.weather_model.model.weather.Weather;

public class FitBottomSystemBarViewPager extends ViewPager {

    private Rect windowInsets;


    public FitBottomSystemBarViewPager(@NonNull Context context) {
        super(context);
    }

    public FitBottomSystemBarViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void setOnApplyWindowInsetsListener(OnApplyWindowInsetsListener listener) {
        super.setOnApplyWindowInsetsListener((v, insets) -> {
            Rect waterfull = Utils.getWaterfullInsets(insets);
            fitSystemWindows(
                    new Rect(
                            insets.getSystemWindowInsetLeft() + waterfull.left,
                            insets.getSystemWindowInsetTop() + waterfull.top,
                            insets.getSystemWindowInsetRight() + waterfull.right,
                            insets.getSystemWindowInsetBottom() + waterfull.bottom
                    )
            );
            return listener == null ? insets : listener.onApplyWindowInsets(v, insets);
        });
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        windowInsets = insets;
        return false;
    }

    public Rect getWindowInsets() {
        return windowInsets;
    }

    public static class FitBottomSystemBarPagerAdapter extends PagerAdapter {

        public List<String> titleList;
        private FitBottomSystemBarViewPager pager;
        private View viewList;
        private Weather weather;
        private Location location;
        private Context context;
        private SettingsOptionManager settingsOptionManager;

        public FitBottomSystemBarPagerAdapter(Context context, FitBottomSystemBarViewPager pager,
                                              Location location, Weather weather, List<String> titleList) {
            this.context = context;
            this.pager = pager;
            this.titleList = titleList;
            this.location = location;
            this.weather = weather;
            this.settingsOptionManager = SettingsOptionManager.getInstance(context);
        }

        @Override
        public int getCount() {
            return Math.min(titleList.size(), 7);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
//            setWindowInsetsForViewTree(viewList.get(position), pager.getWindowInsets());
            try {
                Daily d = weather.getDailyForecast().get(position);
                Daily tomorrow = null;

                if (position + 1 < weather.getDailyForecast().size()) {
                    tomorrow = weather.getDailyForecast().get(position + 1);
                }
                FitBottomSystemBarRecyclerView recyclerView = new FitBottomSystemBarRecyclerView(context);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
                DailyWeatherAdapter dailyWeatherAdapter = new DailyWeatherAdapter(context, d, 3);

                gridLayoutManager.setSpanSizeLookup(dailyWeatherAdapter.spanSizeLookup);
                recyclerView.setAdapter(dailyWeatherAdapter);
                recyclerView.setLayoutManager(gridLayoutManager);

                FragmentDailyDetailsBinding dayDetailsBinding = FragmentDailyDetailsBinding.inflate(((Activity) context).getLayoutInflater());


                //Day Time Sun Moon View
                SunMoonUtils daySunMoonUtils = new SunMoonUtils(dayDetailsBinding.daySunMoonView, location, ResourcesProviderFactory.getNewInstance(), false);
                if (tomorrow != null) {
                    daySunMoonUtils.ensureTime(d, tomorrow, location.getTimeZone());
                } else {
                    daySunMoonUtils.ensureTime(d, d, location.getTimeZone());

                }
                daySunMoonUtils.onEnterScreen();
                daySunMoonUtils.setMoonDrawable();
                daySunMoonUtils.setSunDrawable();
                dayDetailsBinding.tvDayStartTime.setText(d.sun().getRiseTime(context));
                dayDetailsBinding.tvDayEndTime.setText(d.sun().getSetTime(context));


                //set weather icon
                setWeatherIcon(d, dayDetailsBinding);


                //Night Time Sun moon View
                SunMoonUtils nightSunMoonUtils = new SunMoonUtils(dayDetailsBinding.nightSunMoonView, location, ResourcesProviderFactory.getNewInstance(), true);
                if (tomorrow != null) {
                    nightSunMoonUtils.ensureTime(d, tomorrow, location.getTimeZone());
                } else {
                    nightSunMoonUtils.ensureTime(d, d, location.getTimeZone());

                }
                nightSunMoonUtils.setMoonDrawable();
                nightSunMoonUtils.setSunDrawable();
                nightSunMoonUtils.onEnterScreen();

                dayDetailsBinding.tvNightStartTime.setText(d.moon().getRiseTime(context));
                dayDetailsBinding.tvNightEndTime.setText(d.moon().getSetTime(context));


                //Day list
                DailyDetailsAdapter dayDetailsAdapter = new DailyDetailsAdapter(context);
                ArrayList<DailyDetailsModel> dayList = getDailyDetailsModels(d.day(), settingsOptionManager);
                if (d.getUV().getIndex() != null) {
                    dayList.add(new DailyDetailsModel(R.drawable.ic_uv, context.getString(R.string.uv_index), d.getUV().getIndex().toString(), true));
                }
                dayDetailsBinding.rcDailyDetails.setAdapter(dayDetailsAdapter);
                dayDetailsBinding.rcDailyDetails.setLayoutManager(new LinearLayoutManager(context));
                dayDetailsAdapter.updateData(dayList);

                //NightList
                ArrayList<DailyDetailsModel> nightList = getDailyDetailsModels(d.night(), settingsOptionManager);
                DailyDetailsAdapter nightDetailsAdapter = new DailyDetailsAdapter(context);
                dayDetailsBinding.rcNightDetails.setAdapter(nightDetailsAdapter);
                dayDetailsBinding.rcNightDetails.setLayoutManager(new LinearLayoutManager(context));
                nightDetailsAdapter.updateData(nightList);


                dayDetailsBinding.tvLowTempDay.setText(d.night().getTemperature().getShortTemperature(context, settingsOptionManager.getTemperatureUnit()));
                dayDetailsBinding.tvLowTempNight.setText(d.night().getTemperature().getShortTemperature(context, settingsOptionManager.getTemperatureUnit()));
                dayDetailsBinding.tvMaxTempDay.setText(d.day().getTemperature().getShortTemperature(context, settingsOptionManager.getTemperatureUnit()));
                dayDetailsBinding.tvMaxTemp.setText(d.day().getTemperature().getShortTemperature(context, settingsOptionManager.getTemperatureUnit()));
                dayDetailsBinding.tvDayDailyStatus.setText(d.day().getWeatherText());
                dayDetailsBinding.tvNightDailyStatus.setText(d.night().getWeatherText());
                viewList = dayDetailsBinding.getRoot();
                container.addView(viewList);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return viewList;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(viewList);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        private void setWindowInsetsForViewTree(View view, Rect insets) {
            setWindowInsets(view, insets);
            if (view instanceof ViewGroup) {
                int count = ((ViewGroup) view).getChildCount();
                for (int i = 0; i < count; i++) {
                    setWindowInsetsForViewTree(((ViewGroup) view).getChildAt(i), insets);
                }
            }
        }

        private void setWindowInsets(View view, Rect insets) {
            if (view instanceof FitBottomSystemBarNestedScrollView) {
                ((FitBottomSystemBarNestedScrollView) view).fitSystemWindows(insets);
            } else if (view instanceof FitBottomSystemBarRecyclerView) {
                ((FitBottomSystemBarRecyclerView) view).fitSystemWindows(insets);
            }
        }

        private void setWeatherIcon(Daily d, com.mtgtech.weather_forecast.databinding.FragmentDailyDetailsBinding dayDetailsBinding) {
            switch (d.day().getWeatherCode()) {
                case CLEAR:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_clear));

                    break;
                case PARTLY_CLOUDY:
                case CLOUDY:

                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_partly_cloudy));

                    break;
                case RAIN:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_rain));
                    break;
                case SNOW:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_snow));
                    break;
                case WIND:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_weather_wind));
                    break;
                case FOG:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_fog));
                    break;
                case HAZE:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_haze));
                    break;
                case SLEET:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_sleet));
                    break;
                case HAIL:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_hail));
                    break;
                case THUNDER:
                case THUNDERSTORM:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_thunder_rain));
                    break;

            }
            switch (d.night().getWeatherCode()) {
                case CLEAR:
                    dayDetailsBinding.imgNightIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_moon));

                    break;
                case PARTLY_CLOUDY:
                case CLOUDY:

                    dayDetailsBinding.imgNightIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_cloudy_moon));

                    break;
                case RAIN:
                    dayDetailsBinding.imgNightIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_night_rain));
                    break;
                case SNOW:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_snow));
                    break;
                case WIND:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_weather_wind));
                    break;
                case FOG:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_fog));
                    break;
                case HAZE:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_haze));
                    break;
                case SLEET:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_sleet));
                    break;
                case HAIL:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.weather_hail));
                    break;
                case THUNDER:
                case THUNDERSTORM:
                    dayDetailsBinding.imgNightIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_thunder_night));
                    break;

            }
        }

        @NotNull
        private ArrayList<DailyDetailsModel> getDailyDetailsModels(HalfDay d, SettingsOptionManager settingsOptionManager) {
            ArrayList<DailyDetailsModel> list = new ArrayList<>();
            list.add(new DailyDetailsModel(R.drawable.ic_temperature, context.getString(R.string.real_feel_temperature), String.valueOf(d.getTemperature().getShortTemperature(context, settingsOptionManager.getTemperatureUnit())), true));
            if (d.getPrecipitation().getTotal() != null)
                list.add(new DailyDetailsModel(R.drawable.ic_drop, context.getString(R.string.precipitation), settingsOptionManager.getPrecipitationUnit().getPrecipitationText(context, d.getPrecipitation().getTotal()), true));
            else {
                list.add(new DailyDetailsModel(R.drawable.ic_drop, context.getString(R.string.precipitation), "0.0", true));
            }

            if (d.getPrecipitation().getThunderstorm() != null)
                list.add(new DailyDetailsModel(R.drawable.ic_thunder, context.getString(R.string.thunderstorm), settingsOptionManager.getPrecipitationUnit().getPrecipitationText(context, d.getPrecipitation().getThunderstorm()), true));
            else {
                list.add(new DailyDetailsModel(R.drawable.ic_thunder, context.getString(R.string.thunderstorm), "0.0", true));
            }
            String windDirection = "", windSpeed = "", guageText = "";
            Boolean speedVisible = false;
            SpeedUnit unit = SettingsOptionManager.getInstance(context).getSpeedUnit();

            if (d.getWind().getDegree().isNoDirection() || d.getWind().getDegree().getDegree() % 45 == 0) {
                windDirection = d.getWind().getDirection();
            } else {
                windDirection = d.getWind().getDirection()
                        + " (" + (int) (d.getWind().getDegree().getDegree() % 360) + "°)";
            }

            if (d.getWind().getSpeed() != null && d.getWind().getSpeed() > 0) {
                speedVisible = true;
                windSpeed = unit.getSpeedText(context, unit.getSpeed(d.getWind().getSpeed()));
            } else {
                speedVisible = false;
            }

            guageText = d.getWind().getLevel();

            list.add(new DailyDetailsModel(R.drawable.ic_wind_direction, context.getString(R.string.wind_direction), windDirection, true));
            list.add(new DailyDetailsModel(R.drawable.ic_windspeed, context.getString(R.string.wind_speed), windSpeed, speedVisible));
            list.add(new DailyDetailsModel(R.drawable.ic_windgauge, context.getString(R.string.wind_level), guageText, true));
            return list;
        }
    }
}
