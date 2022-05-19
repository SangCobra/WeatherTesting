package anaxxes.com.weatherFlow.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Calendar;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.GeoActivity;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.basic.model.option.unit.CloudCoverUnit;
import anaxxes.com.weatherFlow.basic.model.option.unit.RelativeHumidityUnit;
import anaxxes.com.weatherFlow.basic.model.option.unit.SpeedUnit;
import anaxxes.com.weatherFlow.basic.model.weather.AirQuality;
import anaxxes.com.weatherFlow.basic.model.weather.Base;
import anaxxes.com.weatherFlow.basic.model.weather.Daily;
import anaxxes.com.weatherFlow.basic.model.weather.Weather;
import anaxxes.com.weatherFlow.databinding.FragmentHomeBinding;
import anaxxes.com.weatherFlow.main.MainActivity;
import anaxxes.com.weatherFlow.main.RadarActivity;
import anaxxes.com.weatherFlow.main.adapter.MainPagerAdapter;
import anaxxes.com.weatherFlow.main.adapter.trend.HourlyTrendAdapter;
import anaxxes.com.weatherFlow.main.layout.TrendHorizontalLinearLayoutManager;
import anaxxes.com.weatherFlow.models.TodayForecastModel;
import anaxxes.com.weatherFlow.resource.provider.ResourceProvider;
import anaxxes.com.weatherFlow.resource.provider.ResourcesProviderFactory;
import anaxxes.com.weatherFlow.settings.SettingsOptionManager;
import anaxxes.com.weatherFlow.ui.adapter.DailyDayNightAdapter;
import anaxxes.com.weatherFlow.ui.adapter.DailyForecastAdapter;
import anaxxes.com.weatherFlow.ui.adapter.TodayForecastAdapter;
import anaxxes.com.weatherFlow.utils.DisplayUtils;
import anaxxes.com.weatherFlow.utils.MyUtils;
import anaxxes.com.weatherFlow.utils.SunMoonUtils;
import anaxxes.com.weatherFlow.utils.helpter.IntentHelper;

import static anaxxes.com.weatherFlow.main.MainActivity.MANAGE_ACTIVITY;


public class HomeFragment extends Fragment {

    private Location location;
    private FragmentHomeBinding binding;
    private ResourceProvider resourceProvider;


    private TodayForecastAdapter todayForecastAdapter;
    private DailyForecastAdapter dailyForecastAdapter;
    private DailyDayNightAdapter dailyDayNightAdapter;
    private HourlyTrendAdapter hourlyTrendAdapter;
    private SunMoonUtils sunMoonUtils;
    private SettingsOptionManager settingsOptionManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for requireActivity() fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ensureResourceProvider();
        location = getArguments().getParcelable("location");



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        prefs.registerOnSharedPreferenceChangeListener(
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                       if(key.equals(getString(R.string.key_weather_background))){
                               setWeatherImage(location.getWeather(),binding.imgWeather,settingsOptionManager.isWeatherBgEnabled());
                       }
                    }
                });


        todayForecastAdapter = new TodayForecastAdapter(requireActivity());
        dailyForecastAdapter = new DailyForecastAdapter(requireActivity(), new DailyForecastAdapter.DailyForecastClickListener() {
            @Override
            public void clickDaily(int index) {
                IntentHelper.startDailyWeatherActivity(
                        requireActivity(), location.getFormattedId(), index);
            }
        });
        dailyDayNightAdapter = new DailyDayNightAdapter(requireActivity(), index -> IntentHelper.startDailyWeatherActivity(
                requireActivity(), location.getFormattedId(), index));
        settingsOptionManager = SettingsOptionManager.getInstance(requireActivity());
        hourlyTrendAdapter = new HourlyTrendAdapter();
        binding.todayForecastList.setAdapter(todayForecastAdapter);
        binding.todayForecastList.setLayoutManager(new GridLayoutManager(requireActivity(), 3));


        if (settingsOptionManager.isShowNightInfoEnabled()) {
            binding.dailyForecastList.setAdapter(dailyDayNightAdapter);
        } else {
            binding.dailyForecastList.setAdapter(dailyForecastAdapter);
        }



        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        binding.dailyForecastList.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireActivity(),
                layoutManager.getOrientation());
        binding.dailyForecastList.addItemDecoration(dividerItemDecoration);

        DisplayUtils.disableEditText(binding.tv24Hours);
        DisplayUtils.disableEditText(binding.tv25Days);
        DisplayUtils.disableEditText(binding.tvSeeMoreRadar);

        clickListeners();

        sunMoonUtils = new SunMoonUtils(binding.sunMoonView, location, resourceProvider,MyUtils.isNight());
        sunMoonUtils.ensureTime(location.getWeather().getDailyForecast().get(0),
                location.getWeather().getDailyForecast().get(1),
                location.getTimeZone());


        sunMoonUtils.setMoonDrawable();
        sunMoonUtils.setSunDrawable();


        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) binding.scrollView.getChildAt(binding.scrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (binding.scrollView.getHeight() + binding.scrollView
                        .getScrollY()));

                if (diff == 0) {
                    sunMoonUtils.onEnterScreen();
                }
            }
        });

        resetUI(location);

    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) requireActivity()).setCityName(location.getCityName(requireActivity()));


    }

    private void resetUI(Location location) {

        SettingsOptionManager settingsOptionManager = SettingsOptionManager.getInstance(requireActivity());
        if (location.getWeather() != null) {
            setWeatherImage(location.getWeather(), binding.imgWeather,settingsOptionManager.isWeatherBgEnabled());
            String currentTemperature = location.getWeather().getCurrent().getTemperature().getTemperatureWithoutDegree(requireActivity(), settingsOptionManager.getTemperatureUnit());
            binding.tvTemperature.setText(String.valueOf(currentTemperature));
            binding.tvRealFeelTemp.setText(location.getWeather().getCurrent().getTemperature().getShortRealFeeTemperature(requireActivity(), settingsOptionManager.getTemperatureUnit()));
            binding.tvTempStatus.setText(location.getWeather().getCurrent().getWeatherText());
            binding.tvLowTemp.setText(location.getWeather().getYesterday().getNighttimeTemperature(requireActivity(), settingsOptionManager.getTemperatureUnit()) +"\u00B0");
            binding.tvMaxTemp.setText(location.getWeather().getYesterday().getDaytimeTemperature(requireActivity(), settingsOptionManager.getTemperatureUnit()) +"\u00B0");
            binding.tvRecentRefresh.setText(
                    String.format("%s %s", getString(R.string.refresh_at), Base.getTime(requireActivity(), location.getWeather().getBase().getUpdateDate()))
            );




            switch (settingsOptionManager.getTemperatureUnit()) {
                case C:
                    binding.tvTempScale.setText("C");
                    break;
                case F:
                    binding.tvTempScale.setText("F");
                    break;
                case K:
                    binding.tvTempScale.setText("K");
                    break;
            }


            todayForecastAdapter.updateData(getTodayForecastList(location));

            ArrayList<Daily> dailyList = new ArrayList<>();

            for(int i =0; i<5;i++){
                dailyList.add(location.getWeather().getDailyForecast().get(i));
            }
            if (settingsOptionManager.isShowNightInfoEnabled()) {
                dailyDayNightAdapter.updateData((ArrayList<Daily>) dailyList);

            } else {
                dailyForecastAdapter.updateData((ArrayList<Daily>) dailyList);

            }

            //Hourly Trend RecyclerView
            binding.containerMainHourlyTrendCardTrendRecyclerView.setHasFixedSize(true);
            binding.containerMainHourlyTrendCardTrendRecyclerView.setLayoutManager(
                    new TrendHorizontalLinearLayoutManager(
                            requireActivity(),
                            DisplayUtils.isLandscape(requireActivity()) ? 7 : 5
                    )
            );
            binding.containerMainHourlyTrendCardTrendRecyclerView.setAdapter(hourlyTrendAdapter);
            binding.containerMainHourlyTrendCardTrendRecyclerView.setKeyLineVisibility(
                    SettingsOptionManager.getInstance(requireActivity()).isTrendHorizontalLinesEnabled());
            hourlyTrendAdapter.temperature(
                    (GeoActivity) requireActivity(), binding.containerMainHourlyTrendCardTrendRecyclerView,
                    location.getWeather(),
                    resourceProvider,
                    SettingsOptionManager.getInstance(requireActivity()).getTemperatureUnit()
            );

            binding.tvDailyStatus.setText(location.getWeather().getCurrent().getWeatherText());


            sunMoonUtils.ensureTime(location.getWeather().getDailyForecast().get(0),
                    location.getWeather().getDailyForecast().get(1),
                    location.getTimeZone());


            sunMoonUtils.setMoonDrawable();
            sunMoonUtils.setSunDrawable();


            Calendar cal = Calendar.getInstance(location.getTimeZone());
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            boolean isNight = hour < 6 || hour > 18;

            if(isNight) {
                binding.tvStartTime.setText(location.getWeather().getDailyForecast().get(0).moon().getRiseTime(requireActivity()));
                binding.tvEndTime.setText(location.getWeather().getDailyForecast().get(0).moon().getSetTime(requireActivity()));
            }else{
                binding.tvStartTime.setText(location.getWeather().getDailyForecast().get(0).sun().getRiseTime(requireActivity()));
                binding.tvEndTime.setText(location.getWeather().getDailyForecast().get(0).sun().getSetTime(requireActivity()));
            }


            //AQI
            AirQuality aqi = location.getWeather().getDailyForecast().get(0).getAirQuality();
            binding.imgAQI.setColorFilter(aqi.getAqiColor(requireActivity()));
            binding.tvAQI.setText(String.valueOf(aqi.getAqiIndex()));
            binding.tvAQIText.setText(String.valueOf(aqi.getAqiText()));


            //Radar iFrame

            Display display = requireActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            String html = "<html><head><style>" +

                    "iframe { " +
                    "position: absolute; " +
                    "top:0; " +
                    "left: 0; " +
                    "width: 100%; " +
                    "height: 100%; " +
                    "}" +
                    "</style></head><body  style=\"padding: 0; margin: 0;\">" +
                    "<div ><iframe src=\"https://embed.windy.com/embed2.html?" +
                    "lat="+location.getLatitude()+
                    "&lon="+location.getLongitude()+
                    "&detailLat="+location.getLatitude()+"" +
                    "&detailLon="+location.getLongitude()+
                    "&zoom=8&level=surface&overlay=wind&product=ecmwf&menu=&message=true&marker=&calendar=now&pressure=&type=map&location=coordinates&detail=&metricWind=default&metricTemp=default&radarRange=-1\"" +
                    " frameborder=\"0\"></iframe></div></body></html>";


//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)DisplayUtils.dpToPx(requireActivity(),300),(int)DisplayUtils.dpToPx(requireActivity(),100));
//            binding.cardWebView.setLayoutParams(layoutParams);
            binding.radarWebView.getSettings().setJavaScriptEnabled(true);

            binding.tvSeeMoreRadar.setOnClickListener(view -> {
                sendToRadar(location);
            });


            binding.radarWebView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startX = event.getX();
                            startY = event.getY();
                            break;
                        case MotionEvent.ACTION_UP:
                            float endX = event.getX();
                            float endY = event.getY();
                            if (isAClick(startX, endX, startY, endY)) {
                                sendToRadar(location);
                            }
                            break;
                    }
                    return true;
                }
            });

            binding.radarWebView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                }
            });
            binding.radarWebView.loadData(html, "text/html", null);


            RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setDuration(5000);
            rotateAnimation.setRepeatCount(Animation.INFINITE);

            binding.imgWindFan.startAnimation(rotateAnimation);


            String windDirection = "N/A", windSpeed = "0.0", guageText = "N/A";
            Boolean speedVisible = false;
            SpeedUnit unit = SettingsOptionManager.getInstance(requireActivity()).getSpeedUnit();

            if (location.getWeather().getCurrent().getWind().getDegree().isNoDirection() || location.getWeather().getCurrent().getWind().getDegree().getDegree() % 45 == 0) {
                windDirection = location.getWeather().getCurrent().getWind().getDirection();
            } else {
                windDirection = location.getWeather().getCurrent().getWind().getDirection()
                        + " (" + (int) (location.getWeather().getCurrent().getWind().getDegree().getDegree() % 360) + "°)";
            }

            if (location.getWeather().getCurrent().getWind().getSpeed() != null && location.getWeather().getCurrent().getWind().getSpeed() > 0) {

                windSpeed = unit.getSpeedText(requireActivity(), location.getWeather().getCurrent().getWind().getSpeed());
                binding.tvWindSpeed.setText(windSpeed);

            }

            guageText = location.getWeather().getCurrent().getWind().getLevel();

            binding.tvWindSpeed.setText(windSpeed);
            binding.tvWindDirection.setText(windDirection);
            binding.tvWindGauge.setText(guageText);


            Log.d("laksjdhfasdf",location.getCityId() + "");
            binding.tvPressureValue.setText(settingsOptionManager.getPressureUnit().getPressureText(requireActivity(), location.getWeather().getCurrent().getPressure()));

            binding.tvCityName.setText(location.getCityName(requireActivity()));


//            AQIGasAdapter aqiGasAdapter = new AQIGasAdapter(requireActivity());
//            binding.aqiList.setAdapter(aqiGasAdapter);
//            binding.aqiList.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
//            aqiGasAdapter.updateData(getAqiGasList(aqi));

        }


//        if (weatherView.getWeatherKind() == WeatherView.WEATHER_KING_NULL
//                && location.getWeather() == null) {
//            WeatherViewController.setWeatherCode(
//                    weatherView, null, themeManager.isLightTheme(), resourceProvider);
//            binding.refreshLayout.setColorSchemeColors(
//                    weatherView.getThemeColors(themeManager.isLightTheme())[0]);
//            binding.refreshLayout.setProgressBackgroundColorSchemeColor(
//                    themeManager.getRootColor(requireActivity()));
//        }
//        weatherView.setGravitySensorEnabled(
//                SettingsOptionManager.getInstance(requireActivity()).isGravitySensorEnabled());
//
//        binding.toolbar.setTitle(location.getCityName(requireActivity()));
//
//        binding.switchLayout.reset();
//
//        if (recyclerViewAnimator != null) {
//            recyclerViewAnimator.cancel();
//            recyclerViewAnimator = null;
//        }
//        if (adapter != null) {
//            adapter.setNullWeather();
//            adapter.notifyDataSetChanged();
//        }
    }

    private int CLICK_ACTION_THRESHOLD = 200;
    private float startX;
    private float startY;

    private void sendToRadar(Location location) {
        Intent intent = new Intent(requireActivity(), RadarActivity.class);
        intent.putExtra("latitude", location.getLatitude());
        intent.putExtra("longitude", location.getLongitude());
        startActivity(intent);
    }

    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        return !(differenceX > CLICK_ACTION_THRESHOLD/* =5 */ || differenceY > CLICK_ACTION_THRESHOLD);
    }


    private ArrayList<TodayForecastModel> getTodayForecastList(Location location) {
        return new ArrayList<TodayForecastModel>() {
            {
                add(new TodayForecastModel(R.drawable.ic_humidity, "Humidity", RelativeHumidityUnit.PERCENT.getRelativeHumidityText(
                        location.getWeather().getCurrent().getRelativeHumidity())));
                add(new TodayForecastModel(R.drawable.ic_uv, "UV Index", location.getWeather().getCurrent().getUV().getUVDescription()));
                add(new TodayForecastModel(R.drawable.ic_visibilty, "Visibility", settingsOptionManager.getDistanceUnit().getDistanceText(requireActivity(), location.getWeather().getCurrent().getVisibility())));
                add(new TodayForecastModel(R.drawable.ic_dew_points, "Dew Points", settingsOptionManager.getTemperatureUnit().getTemperatureText(requireActivity(),
                        location.getWeather().getCurrent().getDewPoint())));
                add(new TodayForecastModel(R.drawable.elevation, "Elevation", settingsOptionManager.getDistanceUnit().getDistanceText(
                        requireActivity(),
                        location.getWeather().getCurrent().getCeiling())));
                add(new TodayForecastModel(R.drawable.ic_cloud_cover, "Cloud Covert", CloudCoverUnit.PERCENT.getCloudCoverText(
                        location.getWeather().getCurrent().getCloudCover()
                )));
            }
        };
    }


    private void clickListeners() {
        binding.tv24Hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.startHourlyWeatherActivity(requireActivity(), location.getFormattedId(), 0);
            }
        });
//
        binding.tv25Days.setOnClickListener(view -> {

            IntentHelper.startDailyListActivity(
                    requireActivity(), location.getFormattedId(), 0);
        });

//        binding.refreshLayout.setOnRefreshListener(requireActivity());

        binding.imgMenu.setOnClickListener(view -> {
            ((MainActivity) requireActivity()).toggleDrawerLayout();
        });

        binding.addCity.setOnClickListener(view -> IntentHelper.startManageActivityForResult(requireActivity(), MANAGE_ACTIVITY)
        );

    }


    private void ensureResourceProvider() {
        String iconProvider = SettingsOptionManager.getInstance(requireActivity()).getIconProvider();
        if (resourceProvider == null
                || !resourceProvider.getPackageName().equals(iconProvider)) {
            resourceProvider = ResourcesProviderFactory.getNewInstance();
        }
    }

    public void setWeatherImage(Weather weather, ImageView imageView,boolean isBgEnabled) {
        switch (weather.getCurrent().getWeatherCode()) {
            case CLEAR:
                if (MyUtils.isNight()) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.moon));
                    if(isBgEnabled){
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_clear_night));

                    }else{
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_clear));

                    }

                } else {
                    if(isBgEnabled){
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_clear_day));

                    }else{
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_clear));

                    }
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.sun));

                }
                break;
            case PARTLY_CLOUDY:

                if (MyUtils.isNight()) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.moon_partialy_cloudy));
                    if(isBgEnabled){
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_partly_cloudy_night));
                    }else{
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_clear));
                    }

                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.sun_clouds));
                    if(isBgEnabled){
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_partly_cloudy_day));
                    }else{
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_clear));
                    }


                }
                break;
            case CLOUDY:
                binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_cloudy));
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.clouds));
                break;
            case RAIN:
                if (MyUtils.isNight()) {
                    if(isBgEnabled){
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_rainy_night));

                    }else{
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_cloudy));
                    }
                }else{
                    if(isBgEnabled){
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_raining_day));

                    }else{
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_cloudy));
                    }
                }
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.rain));
                break;
            case SNOW:
            case HAIL:
            case SLEET:
                binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_cloudy));

                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.snow));
                break;
            case WIND:
                binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_cloudy));

                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.wind));
                break;
            case FOG:
            case HAZE:
                binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_cloudy));

                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.fog));
                break;

            case THUNDER:
            case THUNDERSTORM:
                binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_cloudy));
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.lighting));

                break;

        }
    }


}