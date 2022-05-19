package anaxxes.com.weatherFlow.daily;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.GeoActivity;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.basic.model.option.unit.SpeedUnit;
import anaxxes.com.weatherFlow.basic.model.weather.Daily;
import anaxxes.com.weatherFlow.basic.model.weather.HalfDay;
import anaxxes.com.weatherFlow.basic.model.weather.Weather;
import anaxxes.com.weatherFlow.daily.adapter.DailyWeatherAdapter;
import anaxxes.com.weatherFlow.databinding.FragmentDailyDetailsBinding;
import anaxxes.com.weatherFlow.db.DatabaseHelper;
import anaxxes.com.weatherFlow.models.DailyDetailsModel;
import anaxxes.com.weatherFlow.resource.provider.ResourceProvider;
import anaxxes.com.weatherFlow.resource.provider.ResourcesProviderFactory;
import anaxxes.com.weatherFlow.settings.SettingsOptionManager;
import anaxxes.com.weatherFlow.ui.adapter.DailyDetailsAdapter;
import anaxxes.com.weatherFlow.ui.widget.astro.SunMoonView;
import anaxxes.com.weatherFlow.ui.widget.insets.FitBottomSystemBarRecyclerView;
import anaxxes.com.weatherFlow.ui.widget.insets.FitBottomSystemBarViewPager;
import anaxxes.com.weatherFlow.utils.DisplayUtils;
import anaxxes.com.weatherFlow.utils.MyUtils;
import anaxxes.com.weatherFlow.utils.SunMoonUtils;
import anaxxes.com.weatherFlow.utils.manager.ThemeManager;

import static anaxxes.com.weatherFlow.basic.model.weather.WeatherCode.CLOUDY;
import static anaxxes.com.weatherFlow.basic.model.weather.WeatherCode.RAIN;
import static cyanogenmod.providers.WeatherContract.WeatherColumns.WeatherCode.PARTLY_CLOUDY;

/**
 * Daily weather activity.
 * */

public class DailyWeatherActivity extends GeoActivity {

    private CoordinatorLayout container;
    private TextView title;
//    private TextView subtitle;
    private TextView indicator;

    private @Nullable Weather weather;
    private @Nullable TimeZone timeZone;
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
    public View getSnackbarContainer() {
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
//        if (!SettingsOptionManager.getInstance(this).getLanguage().isChinese()){
//            subtitle.setVisibility(View.GONE);
//        }

        title.setText(location.getCityName(this));
        selectPage(
                weather.getDailyForecast().get(position),
                position,
                weather.getDailyForecast().size()
        );

        List<View> viewList = new ArrayList<>(weather.getDailyForecast().size());
        List<String> titleList = new ArrayList<>(weather.getDailyForecast().size());


        for (int i = 0; i < weather.getDailyForecast().size(); i ++) {
            Daily d = weather.getDailyForecast().get(i);
            Daily tomorrow = null;

            if(i+1 <weather.getDailyForecast().size()){
                tomorrow = weather.getDailyForecast().get(i+1);
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
            SunMoonUtils daySunMoonUtils = new SunMoonUtils(dayDetailsBinding.daySunMoonView,location, ResourcesProviderFactory.getNewInstance(),false);
            if(tomorrow != null){
                daySunMoonUtils.ensureTime(d, tomorrow,location.getTimeZone());
            }else{
                daySunMoonUtils.ensureTime(d, d,location.getTimeZone());

            }
            daySunMoonUtils.onEnterScreen();
            daySunMoonUtils.setMoonDrawable();
            daySunMoonUtils.setSunDrawable();
            dayDetailsBinding.tvDayStartTime.setText(d.sun().getRiseTime(DailyWeatherActivity.this));
            dayDetailsBinding.tvDayEndTime.setText(d.sun().getSetTime(DailyWeatherActivity.this));


            //set weather icon
            setWeatherIcon(d, dayDetailsBinding);


            //Night Time Sun moon View
            SunMoonUtils nightSunMoonUtils = new SunMoonUtils(dayDetailsBinding.nightSunMoonView,location, ResourcesProviderFactory.getNewInstance(),true);
            if(tomorrow != null){
                nightSunMoonUtils.ensureTime(d, tomorrow,location.getTimeZone());
            }else{
                nightSunMoonUtils.ensureTime(d, d,location.getTimeZone());

            }
            nightSunMoonUtils.setMoonDrawable();
            nightSunMoonUtils.setSunDrawable();
            nightSunMoonUtils.onEnterScreen();

            dayDetailsBinding.tvNightStartTime.setText(d.moon().getRiseTime(DailyWeatherActivity.this));
            dayDetailsBinding.tvNightEndTime.setText(d.moon().getSetTime(DailyWeatherActivity.this));



            //Day list
            DailyDetailsAdapter dayDetailsAdapter = new DailyDetailsAdapter(this);
            ArrayList<DailyDetailsModel> dayList = getDailyDetailsModels(d.day(),settingsOptionManager);
            dayList.add(new DailyDetailsModel(R.drawable.ic_uv,getString(R.string.uv_index),String.valueOf(d.getUV().getUVDescription()),true));
            dayDetailsBinding.rcDailyDetails.setAdapter(dayDetailsAdapter);
            dayDetailsBinding.rcDailyDetails.setLayoutManager(new LinearLayoutManager(this));
            dayDetailsAdapter.updateData(dayList);

            //NightList
            ArrayList<DailyDetailsModel> nightList = getDailyDetailsModels(d.night(),settingsOptionManager);
            DailyDetailsAdapter nightDetailsAdapter = new DailyDetailsAdapter(this);
            dayDetailsBinding.rcNightDetails.setAdapter(nightDetailsAdapter);
            dayDetailsBinding.rcNightDetails.setLayoutManager(new LinearLayoutManager(this));
            nightDetailsAdapter.updateData(nightList);


            dayDetailsBinding.tvLowTempDay.setText(d.night().getTemperature().getShortTemperature(this,settingsOptionManager.getTemperatureUnit()));
            dayDetailsBinding.tvLowTempNight.setText(d.night().getTemperature().getShortTemperature(this,settingsOptionManager.getTemperatureUnit()));
            dayDetailsBinding.tvMaxTempDay.setText(d.day().getTemperature().getShortTemperature(this,settingsOptionManager.getTemperatureUnit()));
            dayDetailsBinding.tvMaxTemp.setText(d.day().getTemperature().getShortTemperature(this,settingsOptionManager.getTemperatureUnit()));
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
        tabs.setupWithViewPager(pager);
    }

    private void setWeatherIcon(Daily d, anaxxes.com.weatherFlow.databinding.FragmentDailyDetailsBinding dayDetailsBinding) {
        switch (d.day().getWeatherCode()) {
            case CLEAR:
                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_clear_day));
                dayDetailsBinding.imgNightIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_clear_night));

                break;
            case PARTLY_CLOUDY:

                    dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_partly_cloudy_day));
                dayDetailsBinding.imgNightIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_partly_cloudy_night));

                break;
            case CLOUDY:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_cloudy));
                break;
            case RAIN:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_rain));
                break;
            case SNOW:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_snow));
                break;
            case WIND:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_wind));
                break;
            case FOG:
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_fog));
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
                dayDetailsBinding.imgDailyIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weather_thunder));
                break;

        }
    }

    @NotNull
    private ArrayList<DailyDetailsModel> getDailyDetailsModels(HalfDay d,SettingsOptionManager settingsOptionManager) {
        ArrayList<DailyDetailsModel> list = new ArrayList<>();
        list.add(new DailyDetailsModel(R.drawable.ic_heat,getString(R.string.real_feel_temperature),String.valueOf(d.getTemperature().getTemperature(this,settingsOptionManager.getTemperatureUnit())),true));
        if(d.getPrecipitation().getTotal() != null)
        list.add(new DailyDetailsModel(R.drawable.ic_precipitation,getString(R.string.precipitation),settingsOptionManager.getPrecipitationUnit().getPrecipitationText(this,d.getPrecipitation().getTotal()),true));
        else{
            list.add(new DailyDetailsModel(R.drawable.ic_precipitation,getString(R.string.precipitation),"0.0",true));
        }

        if(d.getPrecipitation().getThunderstorm() != null)
        list.add(new DailyDetailsModel(R.drawable.weather_thunder,getString(R.string.thunderstorm),settingsOptionManager.getPrecipitationUnit().getPrecipitationText(this,d.getPrecipitation().getThunderstorm()),true));
        else{
            list.add(new DailyDetailsModel(R.drawable.weather_thunder,getString(R.string.thunderstorm),"0.0",true));
        }
        String windDirection = "",windSpeed = "",guageText = "";
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
            windSpeed = unit.getSpeedText(this, d.getWind().getSpeed());
        } else {
            speedVisible = false;
        }

        guageText = d.getWind().getLevel();

        list.add(new DailyDetailsModel(R.drawable.ic_wind_direction,getString(R.string.wind_direction),windDirection,true));
        list.add(new DailyDetailsModel(R.drawable.ic_wind_speed,getString(R.string.wind_speed),windSpeed,speedVisible));
        list.add(new DailyDetailsModel(R.drawable.ic_wind_gauge,getString(R.string.wind_level),guageText,true));
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
