package mtgtech.com.weather_forecast.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.common.control.manager.AdmobManager;

import java.util.ArrayList;
import java.util.TimeZone;

import mtgtech.com.weather_forecast.BuildConfig;
import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;
import mtgtech.com.weather_forecast.weather_model.model.weather.Daily;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;
import mtgtech.com.weather_forecast.daily_weather.adapter.holder.DailyListAdapter;
import mtgtech.com.weather_forecast.databinding.ActivityDailyListBinding;
import mtgtech.com.weather_forecast.db.DatabaseHelper;
import mtgtech.com.weather_forecast.settings.SettingsOptionManager;
import mtgtech.com.weather_forecast.view.adapter.DailyDayNightAdapter;
import mtgtech.com.weather_forecast.view.adapter.DailyForecastAdapter;
import mtgtech.com.weather_forecast.utils.helpter.IntentHelper;

public class DailyListActivity extends GeoActivity {

    private ActivityDailyListBinding binding;
    private @Nullable
    Weather weather;
    private @Nullable
    TimeZone timeZone;
    private Location location;
    private DailyForecastAdapter dailyForecastAdapter;
    private DailyDayNightAdapter dailyDayNightAdapter;
    private SettingsOptionManager settingsOptionManager;
    private DailyListAdapter dailyListAdapter;



    public static final String KEY_FORMATTED_LOCATION_ID = "FORMATTED_LOCATION_ID";
    public static final String KEY_CURRENT_DAILY_INDEX = "CURRENT_DAILY_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityDailyListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AdmobManager.getInstance().loadBanner(this, BuildConfig.banner_daily_weather);
        settingsOptionManager = SettingsOptionManager.getInstance(this);


        dailyForecastAdapter = new DailyForecastAdapter(this, new DailyForecastAdapter.DailyForecastClickListener() {
            @Override
            public void clickDaily(int index) {
                IntentHelper.startDailyWeatherActivity(
                        DailyListActivity.this, location.getFormattedId(), index);
            }
        });
        dailyDayNightAdapter = new DailyDayNightAdapter(this, index -> IntentHelper.startDailyWeatherActivity(
                this, location.getFormattedId(), index));

        dailyListAdapter = new DailyListAdapter(this, index -> IntentHelper.startDailyWeatherActivity(
                this, location.getFormattedId(), index));

        initData();
    }

    @Override
    public View getSnackBarContainer() {
        return null;
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


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.dailyForecastList.setLayoutManager(layoutManager);
        binding.dailyForecastList.setAdapter(dailyListAdapter);
        ArrayList<Daily> listAdapter = null;
        if (weather != null) {
            listAdapter = (ArrayList<Daily>) weather.getDailyForecast();
        }
        if (listAdapter != null) {
            listAdapter.add(2, null);
        }
        dailyListAdapter.setList(listAdapter);

//        if (settingsOptionManager.isShowNightInfoEnabled()) {
//            binding.dailyForecastList.setAdapter(dailyDayNightAdapter);
//        } else {
//            binding.dailyForecastList.setAdapter(dailyForecastAdapter);
//        }
//
//
//
//        if (settingsOptionManager.isShowNightInfoEnabled()) {
//            dailyDayNightAdapter.updateData((ArrayList<Daily>) weather.getDailyForecast());
//        } else {
//            dailyForecastAdapter.updateData((ArrayList<Daily>) weather.getDailyForecast());
//        }

//        binding.tvDailyStatus.setText(weather.getCurrent().getWeatherText());

        Toolbar toolbar = findViewById(R.id.activity_weather_daily_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
//
//        TextView title = findViewById(R.id.activity_weather_daily_title);
//        title.setText(location.getCityName(this));


    }

}