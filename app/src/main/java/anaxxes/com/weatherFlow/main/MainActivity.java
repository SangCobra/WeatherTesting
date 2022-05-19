package anaxxes.com.weatherFlow.main;

import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import anaxxes.com.weatherFlow.BuildConfig;
import anaxxes.com.weatherFlow.basic.model.option.unit.CloudCoverUnit;
import anaxxes.com.weatherFlow.basic.model.option.unit.RelativeHumidityUnit;
import anaxxes.com.weatherFlow.basic.model.option.unit.SpeedUnit;
import anaxxes.com.weatherFlow.basic.model.weather.AirQuality;
import anaxxes.com.weatherFlow.basic.model.weather.Base;
import anaxxes.com.weatherFlow.basic.model.weather.Daily;
import anaxxes.com.weatherFlow.main.adapter.MainPagerAdapter;
import anaxxes.com.weatherFlow.main.adapter.trend.HourlyTrendAdapter;
import anaxxes.com.weatherFlow.main.layout.TrendHorizontalLinearLayoutManager;
import anaxxes.com.weatherFlow.models.AQIGasModel;
import anaxxes.com.weatherFlow.models.TodayForecastModel;
import anaxxes.com.weatherFlow.ui.adapter.AQIGasAdapter;
import anaxxes.com.weatherFlow.ui.adapter.DailyForecastAdapter;
import anaxxes.com.weatherFlow.ui.adapter.TodayForecastAdapter;
import anaxxes.com.weatherFlow.utils.MyUtils;
import anaxxes.com.weatherFlow.utils.NavigationView;
import anaxxes.com.weatherFlow.utils.SunMoonUtils;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import anaxxes.com.weatherFlow.admob.Config;
import anaxxes.com.weatherFlow.basic.GeoActivity;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.option.DarkMode;
import anaxxes.com.weatherFlow.basic.model.option.provider.WeatherSource;
import anaxxes.com.weatherFlow.basic.model.resource.Resource;
import anaxxes.com.weatherFlow.basic.model.weather.Weather;
import anaxxes.com.weatherFlow.databinding.ActivityMainBinding;
import anaxxes.com.weatherFlow.main.adapter.main.MainAdapter;
import anaxxes.com.weatherFlow.main.dialog.LocationHelpDialog;
import anaxxes.com.weatherFlow.ui.fragment.LocationManageFragment;
import anaxxes.com.weatherFlow.resource.provider.ResourceProvider;
import anaxxes.com.weatherFlow.resource.provider.ResourcesProviderFactory;
import anaxxes.com.weatherFlow.settings.SettingsOptionManager;
import anaxxes.com.weatherFlow.ui.widget.weatherView.WeatherView;
import anaxxes.com.weatherFlow.remoteviews.NotificationUtils;
import anaxxes.com.weatherFlow.remoteviews.WidgetUtils;
import anaxxes.com.weatherFlow.utils.PurchaseUtils;
import anaxxes.com.weatherFlow.utils.helpter.IntentHelper;
import anaxxes.com.weatherFlow.utils.SnackbarUtils;
import anaxxes.com.weatherFlow.utils.DisplayUtils;
import anaxxes.com.weatherFlow.background.polling.PollingManager;
import anaxxes.com.weatherFlow.utils.manager.ThemeManager;
import anaxxes.com.weatherFlow.utils.manager.ThreadManager;
import anaxxes.com.weatherFlow.utils.manager.TimeManager;
import anaxxes.com.weatherFlow.utils.manager.ShortcutsManager;


/**
 * Main activity.
 */

public class MainActivity extends GeoActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    private MainActivityViewModel viewModel;
    private ActivityMainBinding binding;

    private BillingProcessor bp;

    @Nullable
    private String pendingAction;
    @Nullable
    private HashMap<String, Object> pendingExtraMap;

    @Nullable
    private LocationManageFragment manageFragment;
    private WeatherView weatherView;
    @Nullable
    private MainAdapter adapter;
    @Nullable
    private AnimatorSet recyclerViewAnimator;

    private ResourceProvider resourceProvider;
    private ThemeManager themeManager;

    private TodayForecastAdapter todayForecastAdapter;
    private DailyForecastAdapter dailyForecastAdapter;
    private HourlyTrendAdapter hourlyTrendAdapter;
    private SunMoonUtils sunMoonUtils;

    private int index = 0;
    private Location location = null;

    @Nullable
    public String currentLocationFormattedId;
    @Nullable
    private WeatherSource currentWeatherSource;
    private long currentWeatherTimeStamp;
    private SettingsOptionManager settingsOptionManager;

    public static final int SETTINGS_ACTIVITY = 1;
    public static final int MANAGE_ACTIVITY = 2;
    public static final int CARD_MANAGE_ACTIVITY = 3;
    public static final int SEARCH_ACTIVITY = 4;
    public static final int SELECT_PROVIDER_ACTIVITY = 5;

    private static final long INVALID_CURRENT_WEATHER_TIME_STAMP = -1;

    public static final String ACTION_MAIN = "com.wangdaye.geometricweather.Main";
    public static final String KEY_MAIN_ACTIVITY_LOCATION_FORMATTED_ID
            = "MAIN_ACTIVITY_LOCATION_FORMATTED_ID";

    public static final String ACTION_UPDATE_WEATHER_IN_BACKGROUND
            = "com.wangdaye.geomtricweather.ACTION_UPDATE_WEATHER_IN_BACKGROUND";
    public static final String KEY_LOCATION_FORMATTED_ID = "LOCATION_FORMATTED_ID";

    public static final String ACTION_SHOW_ALERTS
            = "com.wangdaye.geomtricweather.ACTION_SHOW_ALERTS";

    public static final String ACTION_SHOW_DAILY_FORECAST
            = "com.wangdaye.geomtricweather.ACTION_SHOW_DAILY_FORECAST";
    public static final String KEY_DAILY_INDEX = "DAILY_INDEX";
    public static final String KEY_LOCATION_INDEX = "KEY_LOCATION_INDEX";

    private BroadcastReceiver backgroundUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String formattedId = intent.getStringExtra(KEY_LOCATION_FORMATTED_ID);
            viewModel.updateLocationFromBackground(MainActivity.this, formattedId);
            if (isForeground()) {
                getSnackbarContainer().postDelayed(() -> {
                    if (isForeground()
                            && formattedId != null
                            && formattedId.equals(viewModel.getCurrentLocationFormattedId())) {
                        SnackbarUtils.showSnackbar(
                                MainActivity.this, getString(R.string.feedback_updated_in_background));
                    }
                }, 1200);
            }
        }
    };

    private int CLICK_ACTION_THRESHOLD = 200;
    private float startX;
    private float startY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pendingIntentAction(getIntent());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//
//        // attach weather view.
//        switch (SettingsOptionManager.getInstance(this).getUiStyle()) {
//            case MATERIAL:
//                weatherView = new MaterialWeatherView(this);
//                break;
//
//            case CIRCULAR:
//                weatherView = new CircularSkyWeatherView(this);
//                break;
//        }
//
//        ((CoordinatorLayout) binding.switchLayout.getParent()).addView(
//                (View) weatherView,
//                0,
//                new CoordinatorLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT
//                )
//        );
//        weatherView.setSystemBarStyle(MainActivity.this, getWindow(),
//                false, false, true, false);
//
        resetUIUpdateFlag();
        ensureResourceProvider();
//        updateThemeManager();
//
        initModel();
        initView();
        registerReceiver(
                backgroundUpdateReceiver,
                new IntentFilter(ACTION_UPDATE_WEATHER_IN_BACKGROUND)
        );
        refreshBackgroundViews(true, viewModel.getLocationList(),
                false, false);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.pendingIntentAction(intent);
        resetUIUpdateFlag();
        viewModel.init(this, getLocationId(intent));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }

        switch (requestCode) {
            case SETTINGS_ACTIVITY:
                ensureResourceProvider();
//                updateThemeManager();

                Location location = viewModel.getCurrentLocationValue();
                if (location != null) {
                    ThreadManager.getInstance().execute(() ->
                            NotificationUtils.updateNotificationIfNecessary(this, location));
                }
                resetUIUpdateFlag();
                viewModel.reset(this);

                refreshBackgroundViews(true, viewModel.getLocationList(),
                        true, true);
                break;

            case MANAGE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String formattedId = getLocationId(data);
                    if (TextUtils.isEmpty(formattedId)) {
                        formattedId = viewModel.getCurrentLocationFormattedId();
                    }
                    viewModel.init(this, formattedId);
                    index = data.getIntExtra(MainActivity.KEY_LOCATION_INDEX,0);
                    binding.background.mainPager.setCurrentItem(index);
                }
                break;

            case CARD_MANAGE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    resetUIUpdateFlag();
                    viewModel.reset(this);
                }
                break;

            case SEARCH_ACTIVITY:
                if (resultCode == RESULT_OK && manageFragment != null) {
                    manageFragment.addLocation();
                }
                break;

            case SELECT_PROVIDER_ACTIVITY:
                if (manageFragment != null) {
                    manageFragment.resetLocationList();
                }
                break;
        }
    }

    public void resetNavLocation(String formattedId) {
            viewModel.updateWeather(this);
            index = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        weatherView.setDrawable(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        weatherView.setDrawable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bp != null) {
            bp.release();
        }
        unregisterReceiver(backgroundUpdateReceiver);
    }

    @Override
    public View getSnackbarContainer() {
        return binding.background.background;
    }

    // init.

    private void initModel() {
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        if (viewModel.isNewInstance()) {
            viewModel.init(this, getLocationId(getIntent()));
        }

    }

    @Nullable
    private String getLocationId(@Nullable Intent intent) {
        if (intent == null) {
            return null;
        }
        return intent.getStringExtra(KEY_MAIN_ACTIVITY_LOCATION_FORMATTED_ID);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initView() {

        new NavigationView().setUp(this, binding);
        todayForecastAdapter = new TodayForecastAdapter(this);
        settingsOptionManager = SettingsOptionManager.getInstance(this);
        hourlyTrendAdapter = new HourlyTrendAdapter();
        binding.background.todayForecastList.setAdapter(todayForecastAdapter);
        binding.background.todayForecastList.setLayoutManager(new GridLayoutManager(this, 3));


        dailyForecastAdapter = new DailyForecastAdapter(this,null);
        binding.background.dailyForecastList.setAdapter(dailyForecastAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.background.dailyForecastList.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MainActivity.this,
                layoutManager.getOrientation());
        binding.background.dailyForecastList.addItemDecoration(dividerItemDecoration);

        DisplayUtils.disableEditText(binding.background.tv24Hours);
        DisplayUtils.disableEditText(binding.background.tv25Days);
        DisplayUtils.disableEditText(binding.background.tvSeeMoreRadar);

        binding.background.scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) binding.background.scrollView.getChildAt(binding.background.scrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (binding.background.scrollView.getHeight() + binding.background.scrollView
                        .getScrollY()));

                if (diff == 0) {
                    if(sunMoonUtils != null)
                    sunMoonUtils.onEnterScreen();
                }
            }
        });


        clickListeners();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            binding.background.refreshLayout.setOnApplyWindowInsetsListener((v, insets) -> {
                int startPosition = insets.getSystemWindowInsetTop()
                        + getResources().getDimensionPixelSize(R.dimen.normal_margin);
                binding.background.refreshLayout.setProgressViewOffset(
                        false,
                        startPosition,
                        (int) (startPosition + 64 * getResources().getDisplayMetrics().density)
                );
                return insets;
            });
        }
        binding.background.refreshLayout.setOnRefreshListener(this);

        binding.background.tv24Hours.setOnClickListener(view -> {
            IntentHelper.startHourlyWeatherActivity(MainActivity.this, currentLocationFormattedId, 0);
        });
//        binding.recyclerView.setLayoutManager(new MainLayoutManager());
//        binding.recyclerView.setOnTouchListener(indicatorStateListener);
//        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView, (v, insets) -> {
//            v.setPadding(
//                    manageFragment != null ? 0 : insets.getSystemWindowInsetLeft(),
//                    0,
//                    insets.getSystemWindowInsetRight(),
//                    insets.getSystemWindowInsetBottom()
//            );
//            return insets;
//        });
//
//        binding.indicator.setSwitchView(binding.switchLayout);



//
//        viewModel.getIndicator().observe(this, resource -> {
//            binding.switchLayout.setEnabled(resource.total > 1);
//
//            if (binding.switchLayout.getTotalCount() != resource.total
//                    || binding.switchLayout.getPosition() != resource.index) {
//                binding.switchLayout.setData(resource.index, resource.total);
//                binding.indicator.setSwitchView(binding.switchLayout);
//            }
//
//            if (resource.total > 1) {
//                binding.indicator.setVisibility(View.VISIBLE);
//            } else {
//                binding.indicator.setVisibility(View.GONE);
//            }
//        });
//
        bp = new BillingProcessor(this, Config.LICENCE_KEY_FROM_GOOGLE_PLAY_CONSOLE, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(String productId, TransactionDetails details) {
                PurchaseUtils.setIsPurchased(MainActivity.this);
            }

            @Override
            public void onPurchaseHistoryRestored() {

            }

            @Override
            public void onBillingError(int errorCode, Throwable error) {

            }

            @Override
            public void onBillingInitialized() {

            }
        });
        bp.initialize();
    }

    private void clickListeners() {
        binding.background.tv24Hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
//
        binding.background.tv25Days.setOnClickListener(view -> {
            IntentHelper.startDailyWeatherActivity(
                    this, viewModel.getCurrentLocationFormattedId(), 0);
        });


        binding.navLayout.tvSettings.setOnClickListener(view -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            IntentHelper.startMySettingsActivityForResult(this, SETTINGS_ACTIVITY);
        });

        binding.navLayout.llRemoveAds.setOnClickListener(view -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            bp.purchase(MainActivity.this, Config.PRODUCT_ID);
        });

        binding.background.imgMenu.setOnClickListener(view -> {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        binding.background.addCity.setOnClickListener(view -> IntentHelper.startManageActivityForResult(MainActivity.this, MANAGE_ACTIVITY)
        );


    }

    public void toggleDrawerLayout(){
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    // control.

    @SuppressLint("SetTextI18n")
    private void drawUI(Location location, boolean defaultLocation, boolean updatedInBackground) {
        this.location = location;
        if (location.equals(currentLocationFormattedId)
                && location.getWeatherSource() == currentWeatherSource
                && location.getWeather() != null
                && location.getWeather().getBase().getTimeStamp() == currentWeatherTimeStamp) {
            return;
        }

        boolean needToResetUI = !location.equals(currentLocationFormattedId)
                || currentWeatherSource != location.getWeatherSource()
                || currentWeatherTimeStamp != INVALID_CURRENT_WEATHER_TIME_STAMP;

        currentLocationFormattedId = location.getFormattedId();
        currentWeatherSource = location.getWeatherSource();
        currentWeatherTimeStamp = location.getWeather() != null
                ? location.getWeather().getBase().getTimeStamp()
                : INVALID_CURRENT_WEATHER_TIME_STAMP;

        if (location.getWeather() == null) {
            resetUI(location);
            return;
        }

//        if (needToResetUI) {
        resetUI(location);
//        }

        boolean oldDaytime = TimeManager.getInstance(this).isDayTime();
        boolean daytime = TimeManager.getInstance(this)
                .update(this, location)
                .isDayTime();

        setDarkMode(daytime);
        if (oldDaytime != daytime) {
            updateThemeManager();
        }
        if (manageFragment != null) {
            manageFragment.updateView(viewModel.getLocationList());
        }

//        WeatherViewController.setWeatherCode(
//                weatherView, location.getWeather(), daytime, resourceProvider);
//
//        binding.refreshLayout.setColorSchemeColors(weatherView.getThemeColors(themeManager.isLightTheme())[0]);
//        binding.refreshLayout.setProgressBackgroundColorSchemeColor(themeManager.getRootColor(this));
//
//        boolean listAnimationEnabled = SettingsOptionManager.getInstance(this).isListAnimationEnabled();
//        boolean itemAnimationEnabled = SettingsOptionManager.getInstance(this).isItemAnimationEnabled();
//
//        if (adapter == null) {
//            adapter = new MainAdapter(
//                    this, location, resourceProvider, listAnimationEnabled, itemAnimationEnabled);
//            binding.recyclerView.setAdapter(adapter);
//        } else {
//            adapter.reset(
//                    this, location, resourceProvider, listAnimationEnabled, itemAnimationEnabled);
//            adapter.notifyDataSetChanged();
//        }
//
//        OnScrollListener l = new OnScrollListener();
//        binding.recyclerView.clearOnScrollListeners();
//        binding.recyclerView.addOnScrollListener(l);
//        binding.recyclerView.post(() -> l.onScrolled(binding.recyclerView, 0, 0));
//
//        binding.indicator.setCurrentIndicatorColor(themeManager.getAccentColor(this));
//        binding.indicator.setIndicatorColor(themeManager.getTextSubtitleColor(this));
//
//        if (!listAnimationEnabled) {
//            binding.recyclerView.setAlpha(0f);
//            recyclerViewAnimator = new AnimatorSet();
//            recyclerViewAnimator.playTogether(
//                    ObjectAnimator.ofFloat(binding.recyclerView, "alpha", 0f, 1f),
//                    ObjectAnimator.ofFloat(
//                            binding.recyclerView,
//                            "translationY",
//                            DisplayUtils.dpToPx(this, 40), 0f
//                    )
//            );
//            recyclerViewAnimator.setDuration(450);
//            recyclerViewAnimator.setInterpolator(new DecelerateInterpolator(2f));
//            recyclerViewAnimator.setStartDelay(150);
//            recyclerViewAnimator.start();
//        }

        refreshBackgroundViews(false, viewModel.getLocationList(),
                defaultLocation, !updatedInBackground);
    }

    public void redrawAfterNightInfo(){
        resetUI(location);
    }

    private void resetUI(Location location) {


        if (location.getWeather() != null) {

            binding.background.tvCityName.setText(location.getCityName(this));
            String currentTemperature = location.getWeather().getCurrent().getTemperature().getTemperatureWithoutDegree(this, settingsOptionManager.getTemperatureUnit());
            binding.background.tvTemperature.setText(String.valueOf(currentTemperature));
            binding.background.tvTempStatus.setText(location.getWeather().getCurrent().getWeatherText());
            binding.background.tvLowTemp.setText(String.valueOf(location.getWeather().getYesterday().getNighttimeTemperature(this, settingsOptionManager.getTemperatureUnit())));
            binding.background.tvMaxTemp.setText(String.valueOf(location.getWeather().getYesterday().getDaytimeTemperature(this, settingsOptionManager.getTemperatureUnit())));
            binding.background.tvRecentRefresh.setText(
                    String.format("%s %s", getString(R.string.refresh_at), Base.getTime(this, location.getWeather().getBase().getUpdateDate()))
            );


            setWeatherImage(location.getWeather(), binding.background.imgWeather);


            switch (settingsOptionManager.getTemperatureUnit()) {
                case C:
                    binding.background.tvTempScale.setText("C");
                    break;
                case F:
                    binding.background.tvTempScale.setText("F");
                    break;
                case K:
                    binding.background.tvTempScale.setText("K");
                    break;
            }


            todayForecastAdapter.updateData(getTodayForecastList(location));
            dailyForecastAdapter.updateData((ArrayList<Daily>) location.getWeather().getDailyForecast());

            //Hourly Trend RecyclerView
            binding.background.containerMainHourlyTrendCardTrendRecyclerView.setHasFixedSize(true);
            binding.background.containerMainHourlyTrendCardTrendRecyclerView.setLayoutManager(
                    new TrendHorizontalLinearLayoutManager(
                            this,
                            DisplayUtils.isLandscape(this) ? 7 : 5
                    )
            );
            binding.background.containerMainHourlyTrendCardTrendRecyclerView.setAdapter(hourlyTrendAdapter);
            binding.background.containerMainHourlyTrendCardTrendRecyclerView.setKeyLineVisibility(
                    SettingsOptionManager.getInstance(this).isTrendHorizontalLinesEnabled());
            hourlyTrendAdapter.temperature(
                    (GeoActivity) this, binding.background.containerMainHourlyTrendCardTrendRecyclerView,
                    location.getWeather(),
                    resourceProvider,
                    SettingsOptionManager.getInstance(this).getTemperatureUnit()
            );

            binding.background.tvDailyStatus.setText(location.getWeather().getCurrent().getWeatherText());


            sunMoonUtils = new SunMoonUtils(binding.background.sunMoonView, location, resourceProvider,false);
            sunMoonUtils.ensureTime(location.getWeather().getDailyForecast().get(0),
                    location.getWeather().getDailyForecast().get(1),
                    location.getTimeZone());


            sunMoonUtils.setMoonDrawable();
            sunMoonUtils.setSunDrawable();


            //AQI
            AirQuality aqi = location.getWeather().getDailyForecast().get(0).getAirQuality();
            binding.background.imgAQI.setColorFilter(aqi.getAqiColor(this));
            binding.background.tvAQI.setText(String.valueOf(aqi.getAqiIndex()));
            binding.background.tvAQIText.setText(String.valueOf(aqi.getAqiText()));


            //Radar iFrame


            String html = "<html><body style=\"padding: 0; margin: 0;\">" +
                    "<iframe width=\"" + DisplayUtils.dpToPx(this, 133) + "\" " +
                    "height=\"" + DisplayUtils.dpToPx(this, 100) + "\"" +
                    " src=\"https://embed.windy.com/embed2.html?" +
                    "lat=" + location.getLatitude() +
                    "&lon=" + location.getLongitude() +
                    "&detailLat=" + location.getLatitude() + "" +
                    "&detailLon=" + location.getLongitude() +
                    "&width=" + DisplayUtils.dpToPx(this, 133) +
                    "&height=" + DisplayUtils.dpToPx(this, 100) +
                    "&zoom=8&level=surface&overlay=wind&product=ecmwf&menu=&message=true&marker=&calendar=now&pressure=&type=map&location=coordinates&detail=&metricWind=default&metricTemp=default&radarRange=-1\"" +
                    " frameborder=\"0\"></iframe></body></html>";


//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)DisplayUtils.dpToPx(this,300),(int)DisplayUtils.dpToPx(this,100));
//            binding.background.cardWebView.setLayoutParams(layoutParams);
            binding.background.radarWebView.getSettings().setJavaScriptEnabled(true);

            binding.background.tvSeeMoreRadar.setOnClickListener(view -> {
                sendToRadar(location);
            });


            binding.background.radarWebView.setOnTouchListener(new View.OnTouchListener() {
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


            binding.background.radarWebView.loadData(html, "text/html", null);


            RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setDuration(5000);
            rotateAnimation.setRepeatCount(Animation.INFINITE);

            binding.background.imgWindFan.startAnimation(rotateAnimation);



            String windDirection = "N/A",windSpeed = "N/A",guageText = "N/A";
            Boolean speedVisible = false;
            SpeedUnit unit = SettingsOptionManager.getInstance(this).getSpeedUnit();

            if (location.getWeather().getCurrent().getWind().getDegree().isNoDirection() || location.getWeather().getCurrent().getWind().getDegree().getDegree() % 45 == 0) {
                windDirection = location.getWeather().getCurrent().getWind().getDirection();
            } else {
                windDirection = location.getWeather().getCurrent().getWind().getDirection()
                        + " (" + (int) (location.getWeather().getCurrent().getWind().getDegree().getDegree() % 360) + "°)";
            }

            if (location.getWeather().getCurrent().getWind().getSpeed() != null && location.getWeather().getCurrent().getWind().getSpeed() > 0) {

                windSpeed = unit.getSpeedText(this, location.getWeather().getCurrent().getWind().getSpeed());
                binding.background.tvWindSpeed.setText(windSpeed);

            }

            guageText = location.getWeather().getCurrent().getWind().getLevel();

            binding.background.tvWindSpeed.setText(windSpeed);
            binding.background.tvWindDirection.setText(windDirection);
            binding.background.tvWindGauge.setText(guageText);


            binding.background.tvPressureValue.setText(settingsOptionManager.getPressureUnit().getPressureText(MainActivity.this,location.getWeather().getCurrent().getPressure()));


            MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),viewModel.getLocationList());
            binding.background.mainPager.setAdapter(mainPagerAdapter);
            int size = viewModel.getLocationList().size();
            binding.background.mainPager.setOffscreenPageLimit(viewModel.getLocationList().size() -1);
            binding.background.mainPager.setCurrentItem(index);

//            AQIGasAdapter aqiGasAdapter = new AQIGasAdapter(this);
//            binding.background.aqiList.setAdapter(aqiGasAdapter);
//            binding.background.aqiList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//            aqiGasAdapter.updateData(getAqiGasList(aqi));

        }


//        if (weatherView.getWeatherKind() == WeatherView.WEATHER_KING_NULL
//                && location.getWeather() == null) {
//            WeatherViewController.setWeatherCode(
//                    weatherView, null, themeManager.isLightTheme(), resourceProvider);
//            binding.refreshLayout.setColorSchemeColors(
//                    weatherView.getThemeColors(themeManager.isLightTheme())[0]);
//            binding.refreshLayout.setProgressBackgroundColorSchemeColor(
//                    themeManager.getRootColor(this));
//        }
//        weatherView.setGravitySensorEnabled(
//                SettingsOptionManager.getInstance(this).isGravitySensorEnabled());
//
//        binding.toolbar.setTitle(location.getCityName(this));
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

    private void sendToRadar(Location location) {
        Intent intent = new Intent(MainActivity.this, RadarActivity.class);
        intent.putExtra("latitude", location.getLatitude());
        intent.putExtra("longitude", location.getLongitude());
        startActivity(intent);
    }

    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        return !(differenceX > CLICK_ACTION_THRESHOLD/* =5 */ || differenceY > CLICK_ACTION_THRESHOLD);
    }


    public void setWeatherImage(Weather weather, ImageView imageView) {
        switch (weather.getCurrent().getWeatherCode()) {
            case CLEAR:
                if (MyUtils.isNight()) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.moon));
                    binding.drawerLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_clear_night));

                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.sun));
                    binding.drawerLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_clear_day));

                }
                break;
            case PARTLY_CLOUDY:

                if (MyUtils.isNight()) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.moon_partialy_cloudy));
                    binding.drawerLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_partly_cloudy_night));

                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.sun_clouds));
                    binding.drawerLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_partly_cloudy_day));

                }
                break;
            case CLOUDY:
                binding.drawerLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_cloudy));
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.clouds));
                break;
            case RAIN:
                if (MyUtils.isNight()) {
                    binding.drawerLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_rainy_night));
                }else{
                    binding.drawerLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_raining_day));
                }
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.rain));
                break;
            case SNOW:
            case HAIL:
            case SLEET:
                binding.drawerLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_cloudy));

                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.snow));
                break;
            case WIND:
                binding.drawerLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_cloudy));

                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.wind));
                break;
            case FOG:
            case HAZE:
                binding.drawerLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_cloudy));

                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.fog));
                break;

            case THUNDER:
            case THUNDERSTORM:
                binding.drawerLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_cloudy));
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.lighting));

                break;

        }
    }

    private ArrayList<TodayForecastModel> getTodayForecastList(Location location) {
        return new ArrayList<TodayForecastModel>() {
            {
                add(new TodayForecastModel(R.drawable.ic_humidity, "Humidity", RelativeHumidityUnit.PERCENT.getRelativeHumidityText(
                        location.getWeather().getCurrent().getRelativeHumidity())));
                add(new TodayForecastModel(R.drawable.ic_uv, "UV Index", location.getWeather().getCurrent().getUV().getUVDescription()));
                add(new TodayForecastModel(R.drawable.ic_visibilty, "Visibility", settingsOptionManager.getDistanceUnit().getDistanceText(MainActivity.this, location.getWeather().getCurrent().getVisibility())));
                add(new TodayForecastModel(R.drawable.ic_dew_points, "Dew Points", settingsOptionManager.getTemperatureUnit().getTemperatureText(MainActivity.this,
                        location.getWeather().getCurrent().getDewPoint())));
                add(new TodayForecastModel(R.drawable.elevation, "Elevation", settingsOptionManager.getDistanceUnit().getDistanceText(
                        MainActivity.this,
                        location.getWeather().getCurrent().getCeiling())));
                add(new TodayForecastModel(R.drawable.ic_cloud_cover, "Cloud Covert", CloudCoverUnit.PERCENT.getCloudCoverText(
                        location.getWeather().getCurrent().getCloudCover()
                )));
            }
        };
    }

    private ArrayList<AQIGasModel> getAqiGasList(AirQuality airQuality) {
        return new ArrayList<AQIGasModel>() {
            {
                add(new AQIGasModel("PM25", String.valueOf(airQuality.getPM25()), airQuality.getPm25Color(MainActivity.this)));
                add(new AQIGasModel("PM10", String.valueOf(airQuality.getPM10()), airQuality.getPm10Color(MainActivity.this)));
                add(new AQIGasModel("CO", String.valueOf(airQuality.getCO()), airQuality.getCOColor(MainActivity.this)));
                add(new AQIGasModel("NO2", String.valueOf(airQuality.getNO2()), airQuality.getNo2Color(MainActivity.this)));
                add(new AQIGasModel("SO2", String.valueOf(airQuality.getSO2()), airQuality.getSo2Color(MainActivity.this)));
                add(new AQIGasModel("O3", String.valueOf(airQuality.getO3()), airQuality.getO3Color(MainActivity.this)));
            }
        };
    }

    private void resetUIUpdateFlag() {
        currentLocationFormattedId = null;
        currentWeatherSource = null;
        currentWeatherTimeStamp = INVALID_CURRENT_WEATHER_TIME_STAMP;
    }

    private void ensureResourceProvider() {
        String iconProvider = SettingsOptionManager.getInstance(this).getIconProvider();
        if (resourceProvider == null
                || !resourceProvider.getPackageName().equals(iconProvider)) {
            resourceProvider = ResourcesProviderFactory.getNewInstance();
        }
    }

    private void updateThemeManager() {
        if (themeManager == null) {
            themeManager = ThemeManager.getInstance(this);
        }
        themeManager.update(this, weatherView);
    }

    @SuppressLint("RestrictedApi")
    private void setDarkMode(boolean dayTime) {
        if (SettingsOptionManager.getInstance(this).getDarkMode() == DarkMode.AUTO) {
            int mode = dayTime ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES;
            getDelegate().setLocalNightMode(mode);
            AppCompatDelegate.setDefaultNightMode(mode);
        }
    }

    private void setRefreshing(final boolean b) {
        binding.background.refreshLayout.post(() -> binding.background.refreshLayout.setRefreshing(b));
    }

    private void refreshBackgroundViews(boolean resetBackground, List<Location> locationList,
                                        boolean defaultLocationChanged, boolean updateRemoteViews) {
        if (resetBackground) {
            Observable.create(emitter -> PollingManager.resetAllBackgroundTask(this, false))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .delay(1, TimeUnit.SECONDS)
                    .subscribe();
        }

        if (updateRemoteViews) {
            Observable.create(emitter -> {
                if (defaultLocationChanged) {
                    WidgetUtils.updateWidgetIfNecessary(this, locationList.get(0));
                    NotificationUtils.updateNotificationIfNecessary(this, locationList.get(0));
                }
                WidgetUtils.updateWidgetIfNecessary(this, locationList);
            }).subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .delay(1, TimeUnit.SECONDS)
                    .subscribe();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                ShortcutsManager.refreshShortcutsInNewThread(this, locationList);
            }
        }
    }

    private void pendingIntentAction(Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            pendingAction = null;
            pendingExtraMap = null;
            return;
        }

        if (action.equals(ACTION_SHOW_ALERTS)) {
            pendingAction = ACTION_SHOW_ALERTS;
            pendingExtraMap = new HashMap<>();
        } else if (action.equals(ACTION_SHOW_DAILY_FORECAST)) {
            pendingAction = ACTION_SHOW_DAILY_FORECAST;

            pendingExtraMap = new HashMap<>();
            pendingExtraMap.put(KEY_DAILY_INDEX, intent.getIntExtra(KEY_DAILY_INDEX, 0));
        }
    }

    private void consumeIntentAction() {
        String action = pendingAction;
        HashMap<String, Object> extraMap = pendingExtraMap;
        pendingAction = null;
        pendingExtraMap = null;
        if (TextUtils.isEmpty(action) || extraMap == null) {
            return;
        }

        if (action.equals(ACTION_SHOW_ALERTS)) {
            Location location = viewModel.getCurrentLocationValue();
            if (location != null) {
                Weather weather = location.getWeather();
                if (weather != null) {
                    IntentHelper.startAlertActivity(this, weather);
                }
            }
        } else if (action.equals(ACTION_SHOW_DAILY_FORECAST)) {
            String formattedId = viewModel.getCurrentLocationFormattedId();
            Integer index = (Integer) extraMap.get(KEY_DAILY_INDEX);
            if (formattedId != null && index != null) {
                IntentHelper.startDailyWeatherActivity(
                        this, viewModel.getCurrentLocationFormattedId(), index);
            }
        }
    }

    // interface.

    // on touch listener.

//    private View.OnTouchListener indicatorStateListener = new View.OnTouchListener() {
//
//        @SuppressLint("ClickableViewAccessibility")
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_MOVE:
//                    binding.indicator.setDisplayState(true);
//                    break;
//
//                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
//                    binding.indicator.setDisplayState(false);
//                    break;
//            }
//            return false;
//        }
//    };

    // on swipe listener(swipe switch layout).

//    private SwipeSwitchLayout.OnSwitchListener switchListener = new SwipeSwitchLayout.OnSwitchListener() {
//
//        private Location location;
//        private boolean indexSwitched;
//
//        private float lastProgress = 0;
//
//        @Override
//        public void onSwipeProgressChanged(int swipeDirection, float progress) {
//            binding.indicator.setDisplayState(progress != 0);
//
//            indexSwitched = false;
//
//            if (progress >= 1 && lastProgress < 0.5) {
//                indexSwitched = true;
//                location = viewModel.getLocationFromList(swipeDirection == SwipeSwitchLayout.SWIPE_DIRECTION_LEFT ? 1 : -1);
//                lastProgress = 1;
//            } else if (progress < 0.5 && lastProgress >= 1) {
//                indexSwitched = true;
//                location = viewModel.getLocationFromList(0);
//                lastProgress = 0;
//            }
//
//            if (indexSwitched) {
//                binding.toolbar.setTitle(location.getCityName(MainActivity.this));
//                if (location.getWeather() != null) {
//                    WeatherViewController.setWeatherCode(
//                            weatherView,
//                            location.getWeather(),
//                            TimeManager.isDaylight(location),
//                            resourceProvider
//                    );
//                }
//            }
//        }
//
//        @Override
//        public void onSwipeReleased(int swipeDirection, boolean doSwitch) {
//            if (doSwitch) {
//                resetUIUpdateFlag();
//
//                binding.indicator.setDisplayState(false);
//                viewModel.setLocation(
//                        MainActivity.this,
//                        swipeDirection == SwipeSwitchLayout.SWIPE_DIRECTION_LEFT ? 1 : -1
//                );
//            }
//        }
//    };

    // on refresh listener.


    @Override
    protected void onResume() {
        super.onResume();

        if(isLocationEnabled()) {
            viewModel.getCurrentLocation().observe(this, resource -> {
                boolean updateInBackground = resource.consumeUpdatedInBackground();

                setRefreshing(resource.status == Resource.Status.LOADING);
                drawUI(resource.data, resource.isDefaultLocation(), updateInBackground);

                if (resource.isLocateFailed()) {
                    SnackbarUtils.showSnackbar(
                            this,
                            getString(R.string.feedback_location_failed),
                            getString(R.string.help),
                            v -> {
                                if (isForeground()) {
                                    new LocationHelpDialog().show(getSupportFragmentManager(), null);
                                }
                            }
                    );
                } else if (resource.status == Resource.Status.ERROR) {
                    SnackbarUtils.showSnackbar(this, getString(R.string.feedback_get_weather_failed));
                }

                consumeIntentAction();
            });
        }else{
            buildAlertMessageNoGps();
        }
    }

    @Override
    public void onRefresh() {
        viewModel.updateWeather(this);
    }

    // on scroll changed listener.

    private class OnScrollListener extends RecyclerView.OnScrollListener {

        private @Nullable
        Boolean topChanged;
        private boolean topOverlap;

        private int firstCardMarginTop;

        private int scrollY;
        private float lastAppBarTranslationY;

        OnScrollListener() {
            super();

            this.topChanged = null;
            this.topOverlap = false;

            this.firstCardMarginTop = 0;

            this.scrollY = 0;
            this.lastAppBarTranslationY = 0;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//            if (recyclerView.getChildCount() > 0) {
//                firstCardMarginTop = recyclerView.getChildAt(0).getMeasuredHeight();
//            } else {
//                firstCardMarginTop = -1;
//            }
//
//            scrollY = recyclerView.computeVerticalScrollOffset();
//            lastAppBarTranslationY = binding.appBar.getTranslationY();
//
//            weatherView.onScroll(scrollY);
//            if (adapter != null) {
//                adapter.onScroll(recyclerView);
//            }
//
//            // set translation y of toolbar.
//            if (adapter != null && firstCardMarginTop > 0) {
//                if (firstCardMarginTop
//                        >= binding.appBar.getMeasuredHeight() + adapter.getCurrentTemperatureTextHeight(recyclerView)) {
//                    if (scrollY < firstCardMarginTop
//                            - binding.appBar.getMeasuredHeight()
//                            - adapter.getCurrentTemperatureTextHeight(recyclerView)) {
//                        binding.appBar.setTranslationY(0);
//                    } else if (scrollY > firstCardMarginTop - binding.appBar.getY()) {
//                        binding.appBar.setTranslationY(-binding.appBar.getMeasuredHeight());
//                    } else {
//                        binding.appBar.setTranslationY(
//                                firstCardMarginTop
//                                        - adapter.getCurrentTemperatureTextHeight(recyclerView)
//                                        - scrollY
//                                        - binding.appBar.getMeasuredHeight()
//                        );
//                    }
//                } else {
//                    binding.appBar.setTranslationY(-scrollY);
//                }
//            }
//
//            // set system bar style.
//            if (firstCardMarginTop <= 0) {
//                topChanged = true;
//                topOverlap = false;
//            } else {
//                topChanged = (binding.appBar.getTranslationY() != 0) != (lastAppBarTranslationY != 0);
//                topOverlap = binding.appBar.getTranslationY() != 0;
//            }
//
//            if (topChanged) {
//                weatherView.setSystemBarColor(MainActivity.this, getWindow(),
//                        topOverlap, false, true, false);
//            }
//        }
        }
    }


    public void showSpeedUnitDialog() {
        String[] unitsTitle = getResources().getStringArray(R.array.speed_units);
        String[] unitsValues = getResources().getStringArray(R.array.speed_unit_values);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.settings_title_speed_unit));

        builder.setItems(unitsTitle, (dialog, which) -> {
            settingsOptionManager.setSpeedUnit(unitsValues[which]);
            binding.navLayout.etNavSpeedUnit.setText(settingsOptionManager.getSpeedUnit().getAbbreviation(this));

            SnackbarUtils.showSnackbar(
                    MainActivity.this, getString(R.string.feedback_refresh_ui_after_refresh));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showDistanceUnitDialog() {
        String[] unitsTitle = getResources().getStringArray(R.array.distance_units);
        String[] unitsValues = getResources().getStringArray(R.array.distance_unit_values);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.settings_title_distance_unit));

        builder.setItems(unitsTitle, (dialog, which) -> {
            settingsOptionManager.setDistanceUnit(unitsValues[which]);
            binding.navLayout.etNavDistanceUnit.setText(settingsOptionManager.getDistanceUnit().getAbbreviation(this));

            SnackbarUtils.showSnackbar(
                    MainActivity.this, getString(R.string.feedback_refresh_ui_after_refresh));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showPrecipUnitDialog() {
        String[] unitsTitle = getResources().getStringArray(R.array.precipitation_units);
        String[] unitsValues = getResources().getStringArray(R.array.precipitation_unit_values);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.settings_title_precipitation_unit));

        builder.setItems(unitsTitle, (dialog, which) -> {
            settingsOptionManager.setPrecipitationUnit(unitsValues[which]);
            binding.navLayout.etNavPrecipUnit.setText(settingsOptionManager.getPrecipitationUnit().getAbbreviation(this));

            SnackbarUtils.showSnackbar(
                    MainActivity.this, getString(R.string.feedback_refresh_ui_after_refresh));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showPressureUnitDialog() {
        String[] pressureUnitsTitle = getResources().getStringArray(R.array.pressure_units);
        String[] pressureUnitsValues = getResources().getStringArray(R.array.pressure_unit_values);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.settings_title_pressure_unit));

        builder.setItems(pressureUnitsTitle, (dialog, which) -> {
            settingsOptionManager.setPressureUnit(pressureUnitsValues[which]);
            binding.navLayout.etNavPressureUnit.setText(settingsOptionManager.getPressureUnit().getAbbreviation(this));

            SnackbarUtils.showSnackbar(
                    MainActivity.this, getString(R.string.feedback_refresh_ui_after_refresh));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showTempUnitDialog() {
        String[] tempUnitsTitle = getResources().getStringArray(R.array.temperature_units);
        String[] tempUnitsValues = getResources().getStringArray(R.array.temperature_unit_values);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.settings_title_temperature_unit));

        builder.setItems(tempUnitsTitle, (dialog, which) -> {
            settingsOptionManager.setTemperatureUnit(tempUnitsValues[which]);
            PollingManager.resetNormalBackgroundTask(MainActivity.this, false);
            binding.navLayout.etNavTempUnit.setText(settingsOptionManager.getTemperatureUnit().getAbbreviation(this));

            SnackbarUtils.showSnackbar(
                    MainActivity.this, getString(R.string.feedback_refresh_ui_after_refresh));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showRefreshRateDialog() {
        String[] refreshRatesTitles = getResources().getStringArray(R.array.automatic_refresh_rates);
        String[] refreshRatesValues = getResources().getStringArray(R.array.automatic_refresh_rate_values);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.settings_title_refresh_rate));

        builder.setItems(refreshRatesTitles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                settingsOptionManager.setUpdateInterval(refreshRatesValues[which]);
                PollingManager.resetNormalBackgroundTask(MainActivity.this, false);
                binding.navLayout.etNavRefreshRate.setText(settingsOptionManager.getUpdateInterval().getUpdateIntervalName(MainActivity.this));

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void setCityName(String cityName){
        binding.background.tvCityName.setText(cityName);
    }

    public void shareToFriend() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(
                Intent.EXTRA_TEXT,

                BuildConfig.APPLICATION_ID
        );
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public boolean isLocationEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        buildAlertMessageNoGps();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


}