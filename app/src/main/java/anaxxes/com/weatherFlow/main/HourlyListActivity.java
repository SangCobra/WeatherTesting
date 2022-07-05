package anaxxes.com.weatherFlow.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.TimeZone;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.GeoActivity;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.basic.model.weather.Hourly;
import anaxxes.com.weatherFlow.basic.model.weather.Weather;
import anaxxes.com.weatherFlow.databinding.ActivityHourlyListBinding;
import anaxxes.com.weatherFlow.databinding.ActivityMainBinding;
import anaxxes.com.weatherFlow.db.DatabaseHelper;
import anaxxes.com.weatherFlow.models.DailyDetailsModel;
import anaxxes.com.weatherFlow.ui.adapter.HourlyForecastAdapter;

import static anaxxes.com.weatherFlow.daily.DailyWeatherActivity.KEY_CURRENT_DAILY_INDEX;
import static anaxxes.com.weatherFlow.daily.DailyWeatherActivity.KEY_FORMATTED_LOCATION_ID;

public class HourlyListActivity extends GeoActivity {

    private @Nullable
    Weather weather;
    private @Nullable
    TimeZone timeZone;
    private Location location;
    private int position;
    private HourlyForecastAdapter adapter;

    private ActivityHourlyListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityHourlyListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData();
        initViews();

        adapter = new HourlyForecastAdapter(this);
        binding.hourlyList.setAdapter(adapter);
        binding.hourlyList.setLayoutManager(new LinearLayoutManager(this));
        adapter.updateData((ArrayList<Hourly>) weather.getHourlyForecast());

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
        position = getIntent().getIntExtra(KEY_CURRENT_DAILY_INDEX, 0);
    }

    private void initViews(){
        binding.activityWeatherHourlyTitle.setText(location.getCityName(this));
        Toolbar toolbar = findViewById(R.id.activity_weather_hourly_toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,android.R.color.transparent));
        toolbar.setNavigationOnClickListener(v -> finish());
    }


}