package mtgtech.com.weather_forecast.main;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.TimeZone;

import mtgtech.com.weather_forecast.AdCache;
import mtgtech.com.weather_forecast.BuildConfig;
import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;
import mtgtech.com.weather_forecast.weather_model.model.weather.Hourly;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;
import mtgtech.com.weather_forecast.databinding.ActivityHourlyListBinding;
import mtgtech.com.weather_forecast.db.DatabaseHelper;
import mtgtech.com.weather_forecast.view.adapter.HourlyForecastAdapter;

import static mtgtech.com.weather_forecast.daily_weather.DailyWeatherActivity.KEY_CURRENT_DAILY_INDEX;
import static mtgtech.com.weather_forecast.daily_weather.DailyWeatherActivity.KEY_FORMATTED_LOCATION_ID;
import static mtgtech.com.weather_forecast.main.MainActivity.isShowAds;
import static mtgtech.com.weather_forecast.main.MainActivity.isStartAgain;
import static mtgtech.com.weather_forecast.view.fragment.HomeFragment.TIME_LOAD_INTERS;

import com.common.control.interfaces.AdCallback;
import com.common.control.manager.AdmobManager;
import com.google.android.gms.ads.interstitial.InterstitialAd;

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
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityHourlyListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AdmobManager.getInstance().loadBanner(this, BuildConfig.banner_hourly_weather);
        initData();
        initViews();

        adapter = new HourlyForecastAdapter(this);
        binding.hourlyList.setAdapter(adapter);
        binding.hourlyList.setLayoutManager(new LinearLayoutManager(this));
//        List<Hourly> listHourly1 = weather.getHourlyForecast().subList(0, 3);
//        List<Hourly> listHourly2 = weather.getHourlyForecast().subList(3, weather.getHourlyForecast().size());
//        ArrayList<Hourly> listAdapter = new ArrayList<>();
//        listAdapter.addAll(listHourly1);
//        listAdapter.add(null);
//        listAdapter.addAll(listHourly2);
        ArrayList<Hourly> listAdapter = (ArrayList<Hourly>) weather.getHourlyForecast();

        listAdapter.add(2, null);
        adapter.updateData(listAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isStartAgain = false;
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

    private void initViews() {
//        binding.activityWeatherHourlyTitle.setText(location.getCityName(this));
        Toolbar toolbar = findViewById(R.id.activity_weather_hourly_toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        toolbar.setNavigationOnClickListener(v -> {
            isStartAgain = false;
            finish();
        });
    }
    public void showInterAd() {
        AdmobManager.getInstance().showInterstitial(this, AdCache.getInstance().getInterstitialAd(), new AdCallback() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                AdCache.getInstance().setInterstitialAd(null);
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_LOAD_INTERS);
                        isShowAds = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
    }
    public void loadIntersAd() {
        if (AdCache.getInstance().getInterstitialAd() == null) {
            AdmobManager.getInstance().loadInterAds(this, BuildConfig.inter_move_screen, new AdCallback() {
                @Override
                public void onResultInterstitialAd(InterstitialAd interstitialAd) {
                    super.onResultInterstitialAd(interstitialAd);
                    AdCache.getInstance().setInterstitialAd(interstitialAd);
                }
            });
        }

    }

}