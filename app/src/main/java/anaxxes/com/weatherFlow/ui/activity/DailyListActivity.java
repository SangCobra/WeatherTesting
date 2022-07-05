package anaxxes.com.weatherFlow.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TimeZone;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.GeoActivity;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.basic.model.weather.Daily;
import anaxxes.com.weatherFlow.basic.model.weather.Weather;
import anaxxes.com.weatherFlow.daily.adapter.holder.DailyListAdapter;
import anaxxes.com.weatherFlow.databinding.ActivityDailyListBinding;
import anaxxes.com.weatherFlow.databinding.ActivityMainBinding;
import anaxxes.com.weatherFlow.db.DatabaseHelper;
import anaxxes.com.weatherFlow.settings.SettingsOptionManager;
import anaxxes.com.weatherFlow.ui.adapter.DailyDayNightAdapter;
import anaxxes.com.weatherFlow.ui.adapter.DailyForecastAdapter;
import anaxxes.com.weatherFlow.ui.adapter.DailyPagerAdapter;
import anaxxes.com.weatherFlow.utils.helpter.IntentHelper;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityDailyListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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

        dailyListAdapter = new DailyListAdapter(this);

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
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                layoutManager.getOrientation());
        binding.dailyForecastList.addItemDecoration(dividerItemDecoration);
        binding.dailyForecastList.setAdapter(dailyListAdapter);
        dailyListAdapter.setList((ArrayList<Daily>) weather.getDailyForecast());

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