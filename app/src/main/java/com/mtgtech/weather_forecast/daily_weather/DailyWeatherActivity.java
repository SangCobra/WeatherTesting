package com.mtgtech.weather_forecast.daily_weather;

import static com.mtgtech.weather_forecast.main.MainActivity.isStartAgain;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.common.control.manager.AdmobManager;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.mtgtech.weather_forecast.BuildConfig;
import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.db.DatabaseHelper;
import com.mtgtech.weather_forecast.models.DailyDetailsModel;
import com.mtgtech.weather_forecast.settings.SettingsOptionManager;
import com.mtgtech.weather_forecast.utils.DisplayUtils;
import com.mtgtech.weather_forecast.view.weather_widget.insets.FitBottomSystemBarViewPager;
import com.mtgtech.weather_forecast.weather_model.GeoActivity;
import com.mtgtech.weather_forecast.weather_model.model.location.Location;
import com.mtgtech.weather_forecast.weather_model.model.option.unit.SpeedUnit;
import com.mtgtech.weather_forecast.weather_model.model.weather.Daily;
import com.mtgtech.weather_forecast.weather_model.model.weather.HalfDay;
import com.mtgtech.weather_forecast.weather_model.model.weather.Weather;

/**
 * Daily weather activity.
 */

public class DailyWeatherActivity extends GeoActivity {

    public static final String KEY_FORMATTED_LOCATION_ID = "FORMATTED_LOCATION_ID";
    public static final String KEY_CURRENT_DAILY_INDEX = "CURRENT_DAILY_INDEX";
    public static final String KEY_WEATHER_FORMATTED = "KEY_WEATHER_FORMATTED";
    private CoordinatorLayout container;
    private TextView title;
    //    private TextView subtitle;
    private TextView indicator;
    private FrameLayout frAd;
    private @Nullable
    Weather weather;
    private @Nullable
    TimeZone timeZone;
    private Location location;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_daily);
        initData();
        initWidget();
        AdmobManager.getInstance().loadNative(this, BuildConfig.native_detail_daily_weather, frAd, R.layout.custom_native_app);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public View getSnackBarContainer() {
        return null;
    }

    private void initData() {
        location = (Location) getIntent().getSerializableExtra(KEY_FORMATTED_LOCATION_ID);
        position = getIntent().getIntExtra(KEY_CURRENT_DAILY_INDEX, 0);
        if (location != null) {
            weather = DatabaseHelper.getInstance(DailyWeatherActivity.this).readWeather(location);
            timeZone = location.getTimeZone();
        }


    }

    private void initWidget() {
        SettingsOptionManager settingsOptionManager = SettingsOptionManager.getInstance(this);
        container = findViewById(R.id.activity_weather_daily_container);

        Toolbar toolbar = findViewById(R.id.activity_weather_daily_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            isStartAgain = false;
            finish();
        });

        title = findViewById(R.id.activity_weather_daily_title);
//        subtitle = findViewById(R.id.activity_weather_daily_subtitle);
        indicator = findViewById(R.id.activity_weather_daily_indicator);
        indicator.setTextColor(Color.parseColor("#ffffff"));
//        if (!SettingsOptionManager.getInstance(this).getLanguage().isChinese()){
//            subtitle.setVisibility(View.GONE);
//        }

        frAd = findViewById(R.id.fr_ad_native);
        frAd.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3B3B3B")));


        title.setText(location.getCityName(this));
        title.setTextColor(Color.WHITE);
        FitBottomSystemBarViewPager pager = findViewById(R.id.activity_weather_daily_pager);
        if (weather != null) {

            selectPage(
                    weather.getDailyForecast().get(position),
                    position,
                    weather.getDailyForecast().size()
            );

            List<View> viewList = new ArrayList<>(7);
            List<String> titleList = new ArrayList<>(weather.getDailyForecast().size());


            for (int i = 0; i < weather.getHourlyForecast().size(); i++) {
                Daily d = weather.getDailyForecast().get(i);
                titleList.add(d.getDate("EEE\nd/M"));
            }


            pager.setAdapter(new FitBottomSystemBarViewPager.FitBottomSystemBarPagerAdapter(this, pager, location, weather, titleList));
            pager.setPageMargin((int) DisplayUtils.dpToPx(this, 1));
//        pager.setPageMarginDrawable(new ColorDrawable(ThemeManager.getInstance(this).getLineColor(this)));
            pager.setCurrentItem(position);
            pager.setOffscreenPageLimit(30);
            pager.clearOnPageChangeListeners();
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    // do nothing.
                }

                @Override
                public void onPageSelected(int position) {
                    selectPage(
                            weather.getDailyForecast().get(position),
                            position,
                            weather.getDailyForecast().size()
                    );

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    // do nothing.
                }
            });


            TabLayout tabs = findViewById(R.id.dailyTabs);
            tabs.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
            tabs.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
            tabs.setupWithViewPager(pager);
        }
    }

    private void setWeatherIcon(Daily d, com.mtgtech.weather_forecast.databinding.FragmentDailyDetailsBinding dayDetailsBinding) {
        switch (d.day().getWeatherCode()) {
            case CLEAR:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_clear));

                break;
            case PARTLY_CLOUDY:
            case CLOUDY:

                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_partly_cloudy));

                break;
            case RAIN:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_rain));
                break;
            case SNOW:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_snow));
                break;
            case WIND:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_weather_wind));
                break;
            case FOG:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_fog));
                break;
            case HAZE:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_haze));
                break;
            case SLEET:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_sleet));
                break;
            case HAIL:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_hail));
                break;
            case THUNDER:
            case THUNDERSTORM:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_thunder_rain));
                break;

        }
        switch (d.night().getWeatherCode()) {
            case CLEAR:
                dayDetailsBinding.imgNightIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_moon));

                break;
            case PARTLY_CLOUDY:
            case CLOUDY:

                dayDetailsBinding.imgNightIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_cloudy_moon));

                break;
            case RAIN:
                dayDetailsBinding.imgNightIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_night_rain));
                break;
            case SNOW:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_snow));
                break;
            case WIND:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_weather_wind));
                break;
            case FOG:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_fog));
                break;
            case HAZE:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_haze));
                break;
            case SLEET:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_sleet));
                break;
            case HAIL:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_hail));
                break;
            case THUNDER:
            case THUNDERSTORM:
                dayDetailsBinding.imgNightIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_thunder_night));
                break;

        }
    }

    @NotNull
    private ArrayList<DailyDetailsModel> getDailyDetailsModels(HalfDay d, SettingsOptionManager settingsOptionManager) {
        ArrayList<DailyDetailsModel> list = new ArrayList<>();
        list.add(new DailyDetailsModel(R.drawable.ic_temperature, getString(R.string.real_feel_temperature), String.valueOf(d.getTemperature().getShortTemperature(this, settingsOptionManager.getTemperatureUnit())), true));
        if (d.getPrecipitation().getTotal() != null)
            list.add(new DailyDetailsModel(R.drawable.ic_drop, getString(R.string.precipitation), settingsOptionManager.getPrecipitationUnit().getPrecipitationText(this, d.getPrecipitation().getTotal()), true));
        else {
            list.add(new DailyDetailsModel(R.drawable.ic_drop, getString(R.string.precipitation), "0.0", true));
        }

        if (d.getPrecipitation().getThunderstorm() != null)
            list.add(new DailyDetailsModel(R.drawable.ic_thunder, getString(R.string.thunderstorm), settingsOptionManager.getPrecipitationUnit().getPrecipitationText(this, d.getPrecipitation().getThunderstorm()), true));
        else {
            list.add(new DailyDetailsModel(R.drawable.ic_thunder, getString(R.string.thunderstorm), "0.0", true));
        }
        String windDirection = "", windSpeed = "", guageText = "";
        Boolean speedVisible = false;
        SpeedUnit unit = SettingsOptionManager.getInstance(this).getSpeedUnit();

        if (d.getWind().getDegree().isNoDirection() || d.getWind().getDegree().getDegree() % 45 == 0) {
            windDirection = d.getWind().getDirection();
        } else {
            windDirection = d.getWind().getDirection()
                    + " (" + (int) (d.getWind().getDegree().getDegree() % 360) + "°)";
        }

        if (d.getWind().getSpeed() != null && d.getWind().getSpeed() > 0) {
            speedVisible = true;
            windSpeed = unit.getSpeedText(this, unit.getSpeed(d.getWind().getSpeed()));
        } else {
            speedVisible = false;
        }

        guageText = d.getWind().getLevel();

        list.add(new DailyDetailsModel(R.drawable.ic_wind_direction, getString(R.string.wind_direction), windDirection, true));
        list.add(new DailyDetailsModel(R.drawable.ic_windspeed, getString(R.string.wind_speed), windSpeed, speedVisible));
        list.add(new DailyDetailsModel(R.drawable.ic_windgauge, getString(R.string.wind_level), guageText, true));
        return list;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isStartAgain = false;
    }

    @SuppressLint("SetTextI18n")
    private void selectPage(Daily daily, int position, int size) {

//        subtitle.setText(daily.getLunar());
        if (timeZone != null && daily.isToday(timeZone)) {
            indicator.setText(getString(R.string.today));
        } else {
            indicator.setText((position + 1) + "/" + size);
        }
    }
}
