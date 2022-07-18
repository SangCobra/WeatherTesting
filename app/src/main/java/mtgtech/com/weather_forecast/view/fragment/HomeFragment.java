package mtgtech.com.weather_forecast.view.fragment;

import static mtgtech.com.weather_forecast.main.MainActivity.MANAGE_ACTIVITY;
import static mtgtech.com.weather_forecast.main.MainActivity.isShowAds;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.common.control.interfaces.AdCallback;
import com.common.control.manager.AdmobManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import mtgtech.com.weather_forecast.AdCache;
import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.CloudCoverUnit;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.RelativeHumidityUnit;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.SpeedUnit;
import mtgtech.com.weather_forecast.weather_model.model.weather.AQIObject;
import mtgtech.com.weather_forecast.weather_model.model.weather.AirQuality;
import mtgtech.com.weather_forecast.weather_model.model.weather.Daily;
import mtgtech.com.weather_forecast.weather_model.model.weather.Hourly;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;
import mtgtech.com.weather_forecast.databinding.FragmentHomeBinding;
import mtgtech.com.weather_forecast.main.AirQualityActivity;
import mtgtech.com.weather_forecast.main.MainActivity;
import mtgtech.com.weather_forecast.main.RadarActivity;
import mtgtech.com.weather_forecast.main.adapter.trend.HourlyAdapter;
import mtgtech.com.weather_forecast.main.adapter.trend.HourlyTrendAdapter;
import mtgtech.com.weather_forecast.main.adapter.trend.daily.DailyAdapter;
import mtgtech.com.weather_forecast.models.TodayForecastModel;
import mtgtech.com.weather_forecast.resource.provider.ResourceProvider;
import mtgtech.com.weather_forecast.resource.provider.ResourcesProviderFactory;
import mtgtech.com.weather_forecast.settings.SettingsOptionManager;
import mtgtech.com.weather_forecast.view.adapter.AQIGasAdapter;
import mtgtech.com.weather_forecast.view.adapter.DailyDayNightAdapter;
import mtgtech.com.weather_forecast.view.adapter.DailyForecastAdapter;
import mtgtech.com.weather_forecast.view.adapter.TodayForecastAdapter;
import mtgtech.com.weather_forecast.utils.SunMoonUtils;
import mtgtech.com.weather_forecast.utils.helpter.IntentHelper;


public class HomeFragment extends Fragment {

    private static final long TIME_LOAD_INTERS = 18000;
    private Location location;
    private FragmentHomeBinding binding;
    private ResourceProvider resourceProvider;


    private TodayForecastAdapter todayForecastAdapter;
    private DailyForecastAdapter dailyForecastAdapter;
    private DailyDayNightAdapter dailyDayNightAdapter;
    private HourlyTrendAdapter hourlyTrendAdapter;
    private HourlyAdapter hourlyAdapter;
    private DailyAdapter dailyAdapter;
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
        AdmobManager admobManager = AdmobManager.getInstance();
//        admobManager.loadNative(requireContext(), AdIdUtils.idNative, binding.nativeAd, R.layout.custom_native_1);

        ensureResourceProvider();
        if (getArguments() != null) {
            location = getArguments().getParcelable("location");
        }


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());
//        prefs.registerOnSharedPreferenceChangeListener(
//                (prefs1, key) -> {
//                    if (key.equals(getString(R.string.key_weather_background))) {
//                        assert location.getWeather() != null;
//                        setWeatherImage(location.getWeather(), binding.imgWeather, settingsOptionManager.isWeatherBgEnabled());
//                    }
//                });


//        todayForecastAdapter = new TodayForecastAdapter(requireActivity());
        dailyForecastAdapter = new DailyForecastAdapter(requireActivity(), index -> IntentHelper.startDailyWeatherActivity(
                requireActivity(), location.getFormattedId(), index));
        dailyDayNightAdapter = new DailyDayNightAdapter(requireActivity(), index -> IntentHelper.startDailyWeatherActivity(
                requireActivity(), location.getFormattedId(), index));
        settingsOptionManager = SettingsOptionManager.getInstance(requireActivity());
        hourlyTrendAdapter = new HourlyTrendAdapter();
        hourlyAdapter = new HourlyAdapter(requireContext());
        dailyAdapter = new DailyAdapter(requireContext(), index -> IntentHelper.startDailyWeatherActivity(requireActivity(), location.getFormattedId(), index));

//        binding.todayForecastList.setAdapter(todayForecastAdapter);
//        binding.todayForecastList.setLayoutManager(new GridLayoutManager(requireActivity(), 3));


//        if (settingsOptionManager.isShowNightInfoEnabled()) {
//            binding.dailyForecastList.setAdapter(dailyDayNightAdapter);
//        } else {
//            binding.dailyForecastList.setAdapter(dailyForecastAdapter);
//        }
//
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
//        binding.dailyForecastList.setLayoutManager(layoutManager);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireActivity(),
//                layoutManager.getOrientation());
//        binding.dailyForecastList.addItemDecoration(dividerItemDecoration);

//        DisplayUtils.disableEditText(binding.tv24Hours);
//        DisplayUtils.disableEditText(binding.tvSeeMoreRadar);

        clickListeners();



        try {
            if (location.getWeather() != null) {
                sunMoonUtils = new SunMoonUtils(binding.sunMoonView, location, resourceProvider, !location.getWeather().getHourlyForecast().get(0).isDaylight());
            }
            if (location.getWeather() != null) {
                sunMoonUtils.ensureTime(location.getWeather().getDailyForecast().get(0),
                        location.getWeather().getDailyForecast().get(1),
                        location.getTimeZone());
            }


            sunMoonUtils.setMoonDrawable();
            sunMoonUtils.setSunDrawable();

            binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
                View view1 = (View) binding.scrollView.getChildAt(binding.scrollView.getChildCount() - 1);

                int diff = (view1.getBottom() - (binding.scrollView.getHeight() + binding.scrollView
                        .getScrollY()));

                if (diff == 0) {
                    sunMoonUtils.onEnterScreen();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        resetUI(location);

    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) requireActivity()).setCityName(location.getCityName(requireActivity()));


    }

    @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled", "ClickableViewAccessibility"})
    private void resetUI(Location location) {

        SettingsOptionManager settingsOptionManager = SettingsOptionManager.getInstance(requireActivity());
        if (location.getWeather() != null) {
            setWeatherImage(location.getWeather(), binding.imgWeather, settingsOptionManager.isWeatherBgEnabled());

            binding.tvTemperature.setText(settingsOptionManager.getTemperatureUnit().getShortTemperatureText(requireContext(), location.getWeather().getCurrent().getTemperature().getTemperature()));
            binding.tvRealFeelTemp.setText("Feels like: " + location.getWeather().getCurrent().getTemperature().getShortRealFeeTemperature(requireActivity(), settingsOptionManager.getTemperatureUnit()));
            binding.tvTempStatus.setText(location.getWeather().getCurrent().getWeatherText());
            if (location.getWeather().getYesterday() != null) {
                binding.tvLowTemp.setText(location.getWeather().getYesterday().getNighttimeTemperature(requireActivity(), settingsOptionManager.getTemperatureUnit()) + "\u00B0");
            }
            binding.tvMaxTemp.setText(location.getWeather().getYesterday().getDaytimeTemperature(requireActivity(), settingsOptionManager.getTemperatureUnit()) + "\u00B0");
//            binding.tvRecentRefresh.setText(
//                    String.format("%s %s", getString(R.string.refresh_at), Base.getTime(requireActivity(), location.getWeather().getBase().getUpdateDate()))
//            );

            binding.humidityCurrent.setText(RelativeHumidityUnit.PERCENT.getRelativeHumidityText(
                    location.getWeather().getCurrent().getRelativeHumidity()));
            binding.realFeelCurrent.setText(settingsOptionManager.getTemperatureUnit().getShortTemperatureText(requireContext(), location.getWeather().getCurrent().getTemperature().getTemperature()));
            if (location.getWeather().getHourlyForecast().get(0).getPrecipitationProbability().getRain() != null) {
                binding.percentRainCurrent.setText(Math.round(location.getWeather().getHourlyForecast().get(0).getPrecipitation().getRain()) + "%");
            }
            if (location.getWeather().getCurrent().getWind().getSpeed() != null)

                binding.windSpeedCurrent.setText(settingsOptionManager.getSpeedUnit().getSpeedText(requireActivity(), settingsOptionManager.getSpeedUnit().getSpeed(location.getWeather().getCurrent().getWind().getSpeed())));
            if (location.getWeather().getCurrent().getVisibility() != null) {
                binding.visibilityCurrent.setText(settingsOptionManager.getDistanceUnit().getDistanceText(requireActivity(), settingsOptionManager.getDistanceUnit().getDistance(location.getWeather().getCurrent().getVisibility())));
            }
            if (location.getWeather().getCurrent().getPressure() != null) {
                binding.pressureCurrent.setText(settingsOptionManager.getPressureUnit().getPressureText(requireActivity(), Math.round(settingsOptionManager.getPressureUnit().getPressure(location.getWeather().getCurrent().getPressure()))));
            }
            if (location.getWeather().getCurrent().getUV().getIndex() != null) {
                binding.uvCurrent.setText(location.getWeather().getCurrent().getUV().getUVDescription());
            }
            if (location.getWeather().getCurrent().getPrecipitation().getTotal() != null) {
                binding.precipitationCurrent.setText(settingsOptionManager.getPrecipitationUnit().getPrecipitationText(requireActivity(), settingsOptionManager.getPrecipitationUnit().getPrecipitation(location.getWeather().getCurrent().getPrecipitation().getTotal())));
            }


//            todayForecastAdapter.updateData(getTodayForecastList(location));

            ArrayList<Daily> dailyList = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                dailyList.add(location.getWeather().getDailyForecast().get(i));
            }
            if (settingsOptionManager.isShowNightInfoEnabled()) {
                dailyDayNightAdapter.updateData((ArrayList<Daily>) dailyList);

            } else {
                dailyForecastAdapter.updateData((ArrayList<Daily>) dailyList);

            }

            //Hourly Trend RecyclerView
            hourlyAdapter.updateList((ArrayList<Hourly>) location.getWeather().getHourlyForecast());
            dailyAdapter.updateList((ArrayList<Daily>) location.getWeather().getDailyForecast());
            binding.rcvHourlyMain.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
            binding.rcvHourlyMain.setAdapter(hourlyAdapter);
            binding.recyclerViewMainDaily.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
            binding.recyclerViewMainDaily.setAdapter(dailyAdapter);

            //            binding.containerMainHourlyTrendCardTrendRecyclerView.setKeyLineVisibility(
//                    SettingsOptionManager.getInstance(requireActivity()).isTrendHorizontalLinesEnabled());
//            hourlyTrendAdapter.temperature(
//                    (GeoActivity) requireActivity(), binding.containerMainHourlyTrendCardTrendRecyclerView,
//                    location.getWeather(),
//                    resourceProvider,
//                    SettingsOptionManager.getInstance(requireActivity()).getTemperatureUnit()
//            );


//            binding.tvDailyStatus.setText(location.getWeather().getCurrent().getWeatherText());


            sunMoonUtils.ensureTime(location.getWeather().getDailyForecast().get(0),
                    location.getWeather().getDailyForecast().get(1),
                    location.getTimeZone());


            sunMoonUtils.setMoonDrawable();
            sunMoonUtils.setSunDrawable();


            Calendar cal = Calendar.getInstance(location.getTimeZone());
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            boolean isNight = hour < 6 || hour > 18;

            if (isNight) {
                binding.tvStartTime.setText(location.getWeather().getDailyForecast().get(0).moon().getRiseTime(requireActivity()));
                binding.tvEndTime.setText(location.getWeather().getDailyForecast().get(0).moon().getSetTime(requireActivity()));
            } else {
                binding.tvStartTime.setText(location.getWeather().getDailyForecast().get(0).sun().getRiseTime(requireActivity()));
                binding.tvEndTime.setText(location.getWeather().getDailyForecast().get(0).sun().getSetTime(requireActivity()));
            }


            //AQI
            AirQuality aqi = location.getWeather().getCurrent().getAirQuality();
            binding.aqiHead.setText("AQI: " + aqi.getAqiIndex());
            binding.aqiHead.setBackgroundTintList(ColorStateList.valueOf(aqi.getAqiColor(requireActivity())));
            binding.imgAQI.setColorFilter(aqi.getAqiColor(requireActivity()));
            binding.tvAQI.setText(String.valueOf(aqi.getAqiIndex()));
            binding.tvAQIText.setText(String.valueOf(aqi.getAqiText()));


            //Radar iFrame

            Display display = requireActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);


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
                    "lat=" + location.getLatitude() +
                    "&lon=" + location.getLongitude() +
                    "&detailLat=" + location.getLatitude() + "" +
                    "&detailLon=" + location.getLongitude() +
                    "&zoom=8&level=surface&overlay=wind&product=ecmwf&menu=&message=true&marker=&calendar=now&pressure=&type=map&location=coordinates&detail=&metricWind=default&metricTemp=default&radarRange=-1\"" +
                    " frameborder=\"0\"></iframe></div></body></html>";


            binding.radarWebView.getSettings().setJavaScriptEnabled(true);

            binding.tvSeeMoreRadar.setOnClickListener(view -> {
                sendToRadar(location);
                if (!isShowAds) {
                    isShowAds = true;
                    showInterAd();
                }
            });


            binding.radarWebView.setOnTouchListener((v, event) -> {

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
            });

            binding.radarWebView.setOnClickListener(view -> {

            });
            binding.radarWebView.loadData(html, "text/html", null);


            RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setDuration(5000);
            rotateAnimation.setRepeatCount(Animation.INFINITE);

//            binding.imgWindFan.startAnimation(rotateAnimation);


            String windDirection = "N/A", windSpeed = "0.0", guageText = "N/A";
            SpeedUnit unit = SettingsOptionManager.getInstance(requireActivity()).getSpeedUnit();

            if (location.getWeather().getCurrent().getWind().getDegree().isNoDirection() || location.getWeather().getCurrent().getWind().getDegree().getDegree() % 45 == 0) {
                windDirection = location.getWeather().getCurrent().getWind().getDirection();
            } else {
                windDirection = location.getWeather().getCurrent().getWind().getDirection()
                        + " (" + (int) (location.getWeather().getCurrent().getWind().getDegree().getDegree() % 360) + "°)";
            }

            if (location.getWeather().getCurrent().getWind().getSpeed() != null && location.getWeather().getCurrent().getWind().getSpeed() > 0) {

                windSpeed = unit.getSpeedText(requireActivity(), location.getWeather().getCurrent().getWind().getSpeed());
//                binding.tvWindSpeed.setText(windSpeed);

            }

            guageText = location.getWeather().getCurrent().getWind().getLevel();

//            binding.tvWindSpeed.setText(windSpeed);
//            binding.tvWindDirection.setText(windDirection);
//            binding.tvWindGauge.setText(guageText);


//            binding.tvPressureValue.setText(settingsOptionManager.getPressureUnit().getPressureText(requireActivity(), location.getWeather().getCurrent().getPressure()));

            binding.tvCityName.setText(location.getCityName(requireActivity()) + ", " + location.getCountry());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fommater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.ENGLISH);
            try {
//                Date parsed = fommater.parse(location.getWeather().getDailyForecast().get(0).getDate().toString());
                fommater.setTimeZone(location.getTimeZone());
//                assert parsed != null;
                @SuppressLint("SimpleDateFormat") String outDay = new SimpleDateFormat("EEE").format(location.getWeather().getDailyForecast().get(0).getDate());
                @SuppressLint("SimpleDateFormat") String outMon = new SimpleDateFormat("MMM dd, yyyy").format(location.getWeather().getDailyForecast().get(0).getDate());
                binding.tvCurrentTime.setText(outDay + " ");
                binding.dayCurrent.setText(outMon);
            } catch (Exception e) {
                e.printStackTrace();
            }


            AQIGasAdapter aqiGasAdapter = new AQIGasAdapter(requireActivity());
            binding.aqiList.setAdapter(aqiGasAdapter);
            binding.aqiList.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
            aqiGasAdapter.updateData(getAqiGasList(aqi));

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

    private ArrayList<AQIObject> getAqiGasList(AirQuality aqi) {
        ArrayList<AQIObject> list = new ArrayList<>();
        list.add(new AQIObject("PM2.5", aqi.getPM25()));
        list.add(new AQIObject("PM10", aqi.getPM10()));
        list.add(new AQIObject("N02", aqi.getNO2()));
        list.add(new AQIObject("O3", aqi.getO3()));
        list.add(new AQIObject("S02", aqi.getSO2()));
        list.add(new AQIObject("CO", aqi.getCO()));
        return list;
    }

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
        int CLICK_ACTION_THRESHOLD = 200;
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
        binding.tv24Hours.setOnClickListener(view -> {
            IntentHelper.startHourlyWeatherActivity(requireActivity(), location.getFormattedId(), 0);
            if (!isShowAds) {
                isShowAds = true;
                showInterAd();
            }
        });
//
        binding.tv25Days.setOnClickListener(view -> {
            IntentHelper.startDailyListActivity(
                    requireActivity(), location.getFormattedId(), 0);
            if (!isShowAds) {
                isShowAds = true;
                showInterAd();
            }
        });

//        binding.refreshLayout.setOnRefreshListener(requireActivity());

        binding.imgMenu.setOnClickListener(view -> {
            ((MainActivity) requireActivity()).toggleDrawerLayout();
        });
        binding.tvAirQuality.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AirQualityActivity.class));
            if (!isShowAds) {
                isShowAds = true;
                showInterAd();
            }
        });

        binding.addCity.setOnClickListener(view -> {
            IntentHelper.startManageActivityForResult(requireActivity(), MANAGE_ACTIVITY);
            if (!isShowAds) {
                isShowAds = true;
                showInterAd();
            }
        });

    }

    private void showInterAd() {
        AdmobManager.getInstance().showInterstitial(requireActivity(), AdCache.getInstance().getInterstitialAd(), new AdCallback() {
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


    private void ensureResourceProvider() {
        String iconProvider = SettingsOptionManager.getInstance(requireActivity()).getIconProvider();
        if (resourceProvider == null
                || !resourceProvider.getPackageName().equals(iconProvider)) {
            resourceProvider = ResourcesProviderFactory.getNewInstance();
        }
    }

    public void setWeatherImage(Weather weather, ImageView imageView, boolean isBgEnabled) {
        switch (weather.getCurrent().getWeatherCode()) {
            case CLEAR:
                if (!weather.getHourlyForecast().get(0).isDaylight()) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_moon));
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.clear_n));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.night));

                    }

                } else {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.clear));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.day));

                    }
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_clear));

                }
                break;
            case PARTLY_CLOUDY:
            case CLOUDY:

                if (!weather.getHourlyForecast().get(0).isDaylight()) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_cloudy_moon));
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_t_cloudy));
                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.night));
                    }

                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_sun_cloudy));
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_s_cloudy));
                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.day));
                    }


                }
                break;
            case RAIN:
                if (!weather.getHourlyForecast().get(0).isDaylight()) {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.rain_n));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.night));
                    }
                } else {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_s_rain));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.day));
                    }
                }
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_rain));
                break;
            case SNOW:
            case SLEET:
                if (!weather.getHourlyForecast().get(0).isDaylight()) {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.snow_n));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.night));
                    }
                } else {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_s_snow));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.day));
                    }
                }
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_snow));
                break;
            case HAIL:
                if (!weather.getHourlyForecast().get(0).isDaylight()) {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_s_hail));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.night));
                    }
                } else {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_s_hail));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.day));
                    }
                }
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_fog));
                break;
            case WIND:
                if (!weather.getHourlyForecast().get(0).isDaylight()) {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_t_wind));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.night));
                    }
                } else {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_s_wind));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.day));
                    }
                }
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.wind));
                break;
            case FOG:
            case HAZE:
                if (!weather.getHourlyForecast().get(0).isDaylight()) {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_t_haze));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.night));
                    }
                } else {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_s_haze));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.day));
                    }
                }
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_fog));
                break;

            case THUNDER:
            case THUNDERSTORM:
                if (!weather.getHourlyForecast().get(0).isDaylight()) {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_t_thunder));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.night));
                    }
                } else {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_s_thunder));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.day));
                    }
                }
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_thunder_rain));

                break;

        }
    }


}