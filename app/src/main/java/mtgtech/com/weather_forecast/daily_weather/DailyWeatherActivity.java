package mtgtech.com.weather_forecast.daily_weather;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.common.control.manager.AdmobManager;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import mtgtech.com.weather_forecast.BuildConfig;
import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.SpeedUnit;
import mtgtech.com.weather_forecast.weather_model.model.weather.Daily;
import mtgtech.com.weather_forecast.weather_model.model.weather.HalfDay;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;
import mtgtech.com.weather_forecast.daily_weather.adapter.DailyWeatherAdapter;
import mtgtech.com.weather_forecast.databinding.FragmentDailyDetailsBinding;
import mtgtech.com.weather_forecast.db.DatabaseHelper;
import mtgtech.com.weather_forecast.models.DailyDetailsModel;
import mtgtech.com.weather_forecast.resource.provider.ResourcesProviderFactory;
import mtgtech.com.weather_forecast.settings.SettingsOptionManager;
import mtgtech.com.weather_forecast.view.adapter.DailyDetailsAdapter;
import mtgtech.com.weather_forecast.view.weather_widget.insets.FitBottomSystemBarRecyclerView;
import mtgtech.com.weather_forecast.view.weather_widget.insets.FitBottomSystemBarViewPager;
import mtgtech.com.weather_forecast.utils.DisplayUtils;
import mtgtech.com.weather_forecast.utils.SunMoonUtils;

/**
 * Daily weather activity.
 */

public class DailyWeatherActivity extends GeoActivity {

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

    public static final String KEY_FORMATTED_LOCATION_ID = "FORMATTED_LOCATION_ID";
    public static final String KEY_CURRENT_DAILY_INDEX = "CURRENT_DAILY_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_daily);
        initData();
        initWidget();
    }

    @Override
    public View getSnackBarContainer() {
        return container;
    }

    private void initData() {
        String formattedId = getIntent().getStringExtra(KEY_FORMATTED_LOCATION_ID);
        if (TextUtils.isEmpty(formattedId)) {
            location = DatabaseHelper.getInstance(this).readLocationList().get(0);
        } else {
            location = DatabaseHelper.getInstance(this).readLocation(formattedId);
        }

        if (location != null) {
            weather = DatabaseHelper.getInstance(this).readWeather(location);
            timeZone = location.getTimeZone();
        }
        position = getIntent().getIntExtra(KEY_CURRENT_DAILY_INDEX, 0);
    }

    private void initWidget() {
        SettingsOptionManager settingsOptionManager = SettingsOptionManager.getInstance(this);
        if (weather == null) {
            finish();
        }

        container = findViewById(R.id.activity_weather_daily_container);

        Toolbar toolbar = findViewById(R.id.activity_weather_daily_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        title = findViewById(R.id.activity_weather_daily_title);
//        subtitle = findViewById(R.id.activity_weather_daily_subtitle);
        indicator = findViewById(R.id.activity_weather_daily_indicator);
        indicator.setTextColor(Color.parseColor("#ffffff"));
//        if (!SettingsOptionManager.getInstance(this).getLanguage().isChinese()){
//            subtitle.setVisibility(View.GONE);
//        }

        frAd = findViewById(R.id.fr_ad_native);
        frAd.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3B3B3B")));
        AdmobManager.getInstance().loadNative(this, BuildConfig.native_detail_daily_weather, frAd);


        title.setText(location.getCityName(this));
        title.setTextColor(Color.WHITE);
        selectPage(
                weather.getDailyForecast().get(position),
                position,
                weather.getDailyForecast().size()
        );

        List<View> viewList = new ArrayList<>(weather.getDailyForecast().size());
        List<String> titleList = new ArrayList<>(weather.getDailyForecast().size());


        for (int i = 0; i < weather.getDailyForecast().size(); i++) {
            Daily d = weather.getDailyForecast().get(i);
            Daily tomorrow = null;

            if (i + 1 < weather.getDailyForecast().size()) {
                tomorrow = weather.getDailyForecast().get(i + 1);
            }

            FitBottomSystemBarRecyclerView recyclerView = new FitBottomSystemBarRecyclerView(this);
            recyclerView.setClipToPadding(false);
            DailyWeatherAdapter dailyWeatherAdapter = new DailyWeatherAdapter(this, d, 3);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            gridLayoutManager.setSpanSizeLookup(dailyWeatherAdapter.spanSizeLookup);
            recyclerView.setAdapter(dailyWeatherAdapter);
            recyclerView.setLayoutManager(gridLayoutManager);


            FragmentDailyDetailsBinding dayDetailsBinding = FragmentDailyDetailsBinding.inflate(getLayoutInflater());


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
            dayDetailsBinding.tvDayStartTime.setText(d.sun().getRiseTime(DailyWeatherActivity.this));
            dayDetailsBinding.tvDayEndTime.setText(d.sun().getSetTime(DailyWeatherActivity.this));


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

            dayDetailsBinding.tvNightStartTime.setText(d.moon().getRiseTime(DailyWeatherActivity.this));
            dayDetailsBinding.tvNightEndTime.setText(d.moon().getSetTime(DailyWeatherActivity.this));


            //Day list
            DailyDetailsAdapter dayDetailsAdapter = new DailyDetailsAdapter(this);
            ArrayList<DailyDetailsModel> dayList = getDailyDetailsModels(d.day(), settingsOptionManager);
            if (d.getUV().getIndex() != null) {
                dayList.add(new DailyDetailsModel(R.drawable.ic_uv, getString(R.string.uv_index), d.getUV().getIndex().toString(), true));
            }
            dayDetailsBinding.rcDailyDetails.setAdapter(dayDetailsAdapter);
            dayDetailsBinding.rcDailyDetails.setLayoutManager(new LinearLayoutManager(this));
            dayDetailsAdapter.updateData(dayList);

            //NightList
            ArrayList<DailyDetailsModel> nightList = getDailyDetailsModels(d.night(), settingsOptionManager);
            DailyDetailsAdapter nightDetailsAdapter = new DailyDetailsAdapter(this);
            dayDetailsBinding.rcNightDetails.setAdapter(nightDetailsAdapter);
            dayDetailsBinding.rcNightDetails.setLayoutManager(new LinearLayoutManager(this));
            nightDetailsAdapter.updateData(nightList);


            dayDetailsBinding.tvLowTempDay.setText(d.night().getTemperature().getShortTemperature(this, settingsOptionManager.getTemperatureUnit()));
            dayDetailsBinding.tvLowTempNight.setText(d.night().getTemperature().getShortTemperature(this, settingsOptionManager.getTemperatureUnit()));
            dayDetailsBinding.tvMaxTempDay.setText(d.day().getTemperature().getShortTemperature(this, settingsOptionManager.getTemperatureUnit()));
            dayDetailsBinding.tvMaxTemp.setText(d.day().getTemperature().getShortTemperature(this, settingsOptionManager.getTemperatureUnit()));
            dayDetailsBinding.tvDayDailyStatus.setText(d.day().getWeatherText());
            dayDetailsBinding.tvNightDailyStatus.setText(d.night().getWeatherText());
            viewList.add(dayDetailsBinding.getRoot());

            titleList.add(d.getDate("EEE\nd/M"));


        }

        FitBottomSystemBarViewPager pager = findViewById(R.id.activity_weather_daily_pager);

        pager.setAdapter(new FitBottomSystemBarViewPager.FitBottomSystemBarPagerAdapter(pager, viewList, titleList));
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

    private void setWeatherIcon(Daily d, mtgtech.com.weather_forecast.databinding.FragmentDailyDetailsBinding dayDetailsBinding) {
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
        switch (d.night().getWeatherCode()){
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
