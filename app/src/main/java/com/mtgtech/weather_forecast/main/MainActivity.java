package com.mtgtech.weather_forecast.main;

import android.Manifest;
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
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.common.control.dialog.RateAppDialog;
import com.common.control.interfaces.AdCallback;
import com.common.control.interfaces.PermissionCallback;
import com.common.control.interfaces.RateCallback;
import com.common.control.manager.AdmobManager;
import com.common.control.manager.AppOpenManager;
import com.common.control.utils.CommonUtils;
import com.common.control.utils.PermissionUtils;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import com.mtgtech.weather_forecast.AdCache;
import com.mtgtech.weather_forecast.BuildConfig;
import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.background.polling.PollingManager;
import com.mtgtech.weather_forecast.databinding.ActivityMainBinding;
import com.mtgtech.weather_forecast.db.DatabaseHelper;
import com.mtgtech.weather_forecast.main.adapter.MainPagerAdapter;
import com.mtgtech.weather_forecast.main.adapter.main.MainAdapter;
import com.mtgtech.weather_forecast.main.adapter.trend.HourlyTrendAdapter;
import com.mtgtech.weather_forecast.main.dialog.DialogPer1;
import com.mtgtech.weather_forecast.main.dialog.DialogPer2;
import com.mtgtech.weather_forecast.main.layout.TrendHorizontalLinearLayoutManager;
import com.mtgtech.weather_forecast.models.AQIGasModel;
import com.mtgtech.weather_forecast.models.TodayForecastModel;
import com.mtgtech.weather_forecast.remoteviews.NotificationUtils;
import com.mtgtech.weather_forecast.remoteviews.WidgetUtils;
import com.mtgtech.weather_forecast.resource.provider.ResourceProvider;
import com.mtgtech.weather_forecast.resource.provider.ResourcesProviderFactory;
import com.mtgtech.weather_forecast.settings.SettingsOptionManager;
import com.mtgtech.weather_forecast.settings.SharePreferenceUtils;
import com.mtgtech.weather_forecast.settings.dialog.SettingNavDialog;
import com.mtgtech.weather_forecast.utils.CmUtils;
import com.mtgtech.weather_forecast.utils.DisplayUtils;
import com.mtgtech.weather_forecast.utils.MyUtils;
import com.mtgtech.weather_forecast.utils.NavigationView;
import com.mtgtech.weather_forecast.utils.SunMoonUtils;
import com.mtgtech.weather_forecast.utils.helpter.IntentHelper;
import com.mtgtech.weather_forecast.utils.manager.ShortcutsManager;
import com.mtgtech.weather_forecast.utils.manager.ThemeManager;
import com.mtgtech.weather_forecast.utils.manager.ThreadManager;
import com.mtgtech.weather_forecast.utils.manager.TimeManager;
import com.mtgtech.weather_forecast.view.adapter.DailyForecastAdapter;
import com.mtgtech.weather_forecast.view.adapter.TodayForecastAdapter;
import com.mtgtech.weather_forecast.view.fragment.LocationManageFragment;
import com.mtgtech.weather_forecast.view.weather_widget.weatherView.WeatherView;
import com.mtgtech.weather_forecast.weather_model.GeoActivity;
import com.mtgtech.weather_forecast.weather_model.model.location.Location;
import com.mtgtech.weather_forecast.weather_model.model.option.DarkMode;
import com.mtgtech.weather_forecast.weather_model.model.option.provider.WeatherSource;
import com.mtgtech.weather_forecast.weather_model.model.option.unit.CloudCoverUnit;
import com.mtgtech.weather_forecast.weather_model.model.option.unit.RelativeHumidityUnit;
import com.mtgtech.weather_forecast.weather_model.model.option.unit.SpeedUnit;
import com.mtgtech.weather_forecast.weather_model.model.resource.Resource;
import com.mtgtech.weather_forecast.weather_model.model.weather.AirQuality;
import com.mtgtech.weather_forecast.weather_model.model.weather.Base;
import com.mtgtech.weather_forecast.weather_model.model.weather.Daily;
import com.mtgtech.weather_forecast.weather_model.model.weather.Weather;


/**
 * Main activity.
 */

public class MainActivity extends GeoActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    public static final int SETTINGS_ACTIVITY = 1;
    public static final int MANAGE_ACTIVITY = 2;
    public static final int CARD_MANAGE_ACTIVITY = 3;


//    private BillingProcessor bp;
    public static final int SEARCH_ACTIVITY = 4;
    public static final int SELECT_PROVIDER_ACTIVITY = 5;
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
    public static final String KEY_RELOAD_WEATHER = "RELOAD_WEATHER";
    private static final long INVALID_CURRENT_WEATHER_TIME_STAMP = -1;
    public static boolean isShowAds;
    public static boolean isGotoSettings;
    public static boolean isStartAgain;
    @Nullable
    public String currentLocationFormattedId;
    public LoadLocation loadLocation;
    public List<Location> listBeforeGotoManageLocation;
    private MainActivityViewModel viewModel;
    private ActivityMainBinding binding;
    private final BroadcastReceiver backgroundUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String formattedId = intent.getStringExtra(KEY_LOCATION_FORMATTED_ID);
            viewModel.updateLocationFromBackground(MainActivity.this, formattedId);
            if (isForeground()) {
                getSnackBarContainer().postDelayed(() -> {
                    if (isForeground()
                            && formattedId != null
                            && formattedId.equals(viewModel.getCurrentLocationFormattedId())) {
//                        SnackbarUtils.showSnackbar(
//                                MainActivity.this, getString(R.string.feedback_updated_in_background));
                    }
                }, 1200);
            }
        }
    };
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
    private WeatherSource currentWeatherSource;
    private long currentWeatherTimeStamp;
    private SettingsOptionManager settingsOptionManager;
    private int CLICK_ACTION_THRESHOLD = 200;
    private float startX;
    private float startY;
    private String CHANNEL_ID = "CHANNEL_ID";
    private boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pendingIntentAction(getIntent());
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        isGotoSettings = false;
        Log.d("android_log", "onCreate: ");
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        showNotify();

        loadLocation = formattedId -> viewModel.init(MainActivity.this, formattedId);
        setPermissionCallback(new PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                if (PermissionUtils.permissionGranted(
                        MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )) {
                    isGotoSettings = true;
                    isStartAgain = true;
                }
            }

            @Override
            public void onPermissionDenied() {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    if (!PermissionUtils.permissionGranted(
                            MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ) && !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                    ) {
                        DialogPer2.start(MainActivity.this);
                        DialogPer2.listener = () -> {
                            gotoSettings(MainActivity.this);
                        };
                    } else if (!PermissionUtils.permissionGranted(
                            MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    )) {
                        isGotoSettings = false;
                        isStartAgain = false;
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MyUtils.requestCode);
                    }
                }
            }
        });

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

        listBeforeGotoManageLocation = DatabaseHelper.getInstance(this).readLocationList();
        registerReceiver(
                backgroundUpdateReceiver,
                new IntentFilter(ACTION_UPDATE_WEATHER_IN_BACKGROUND)
        );
        refreshBackgroundViews(true, viewModel.getLocationList(),
                false, true);
        binding.background.mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                binding.background.mainPager.setCurrentItem(index);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.pendingIntentAction(intent);
        resetUIUpdateFlag();
        viewModel.init(this, getLocationId(intent));
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("reload");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (Objects.equals(action, "reload")) {
                    viewModel.updateWeather(MainActivity.this);
                }
            }
        }, intentFilter);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
//        }
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("android_log", "onActivityResult: ");

        switch (requestCode) {
            case SETTINGS_ACTIVITY:
//                if (settingsOptionManager.isFirstTime()){
//                    DialogSettingBegin.start(this, (key, data1) -> {
//                        if (Objects.equals(key, "done")){
//                            viewModel.reset(MainActivity.this);
//                            settingsOptionManager.setFirstTime(false);
//                        }
//                    }, this);
//
//                }
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
                onRefresh();
                break;

            case MANAGE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String formattedId = getLocationId(data);
                    if (TextUtils.isEmpty(formattedId)) {
                        formattedId = viewModel.getCurrentLocationFormattedId();
                    }
                    viewModel.init(this, formattedId);
                    if (data != null) {
                        index = data.getIntExtra(MainActivity.KEY_LOCATION_INDEX, 0);
                    } else {
                        index = viewModel.getLocationList().size() - 1;
                    }
                    binding.background.mainPager.setCurrentItem(index);
                }
                onRefresh();
                break;

            case CARD_MANAGE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    resetUIUpdateFlag();
                    viewModel.reset(this);
                    onRefresh();
                }
                break;

            case SEARCH_ACTIVITY:
                if (resultCode == RESULT_OK && manageFragment != null) {
                    manageFragment.addLocation();
                    onRefresh();
                }
                break;

            case SELECT_PROVIDER_ACTIVITY:
                if (manageFragment != null) {
                    manageFragment.resetLocationList();
                    onRefresh();
                }
                break;
        }

    }

    public void resetNavLocation(String formattedId) {
        viewModel.updateWeather(this);
        index = 0;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("android_log", "onStart: ");
        if (PermissionUtils.permissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            AppOpenManager.getInstance().enableAppResume();
        }
//        weatherView.setDrawable(true);
    }

    @Override
    protected void onStop() {
        isStartAgain = true;
        super.onStop();
        Log.d("android_log", "onStop: ");

//        weatherView.setDrawable(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("android_log", "onPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isStartAgain = true;
//        if (bp != null) {
//            bp.release();
//        }
        AdCache.getInstance().setInterstitialAd(null);
        Log.d("android_log", "onDestroy: ");
        unregisterReceiver(backgroundUpdateReceiver);
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

    public void loadIntersAdDailyDetails() {
        if (AdCache.getInstance().getInterstitialAdDailyDetails() == null) {
            AdmobManager.getInstance().loadInterAds(this, BuildConfig.inter_detail_daily, new AdCallback() {
                @Override
                public void onResultInterstitialAd(InterstitialAd interstitialAd) {
                    super.onResultInterstitialAd(interstitialAd);
                    AdCache.getInstance().setInterstitialAdDailyDetails(interstitialAd);
                }
            });
        }

    }

    // init.

    @Override
    public View getSnackBarContainer() {
        return binding.background.background;
    }

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

    @SuppressLint({"ClickableViewAccessibility", "ObsoleteSdkInt"})
    private void initView() {

        new NavigationView().setUp(this, binding);
        todayForecastAdapter = new TodayForecastAdapter(this);
        settingsOptionManager = SettingsOptionManager.getInstance(this);
        settingsOptionManager.setNotificationTemperatureIconEnabled(true);


        hourlyTrendAdapter = new HourlyTrendAdapter();
//        binding.background.todayForecastList.setAdapter(todayForecastAdapter);
//        binding.background.todayForecastList.setLayoutManager(new GridLayoutManager(this, 3));


        dailyForecastAdapter = new DailyForecastAdapter(this, null);
//        binding.background.dailyForecastList.setAdapter(dailyForecastAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        binding.background.dailyForecastList.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MainActivity.this,
                layoutManager.getOrientation());
//        binding.background.dailyForecastList.addItemDecoration(dividerItemDecoration);

//        DisplayUtils.disableEditText(binding.background.tv24Hours);
//        DisplayUtils.disableEditText(binding.background.tv25Days);
//        DisplayUtils.disableEditText(binding.background.tvSeeMoreRadar);

        binding.background.scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) binding.background.scrollView.getChildAt(binding.background.scrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (binding.background.scrollView.getHeight() + binding.background.scrollView
                        .getScrollY()));

                if (diff == 0) {
                    if (sunMoonUtils != null)
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
//        bp = new BillingProcessor(this, Config.LICENCE_KEY_FROM_GOOGLE_PLAY_CONSOLE, new BillingProcessor.IBillingHandler() {
//            @Override
//            public void onProductPurchased(String productId, TransactionDetails details) {
//                PurchaseUtils.setIsPurchased(MainActivity.this);
//            }
//
//            @Override
//            public void onPurchaseHistoryRestored() {
//
//            }
//
//            @Override
//            public void onBillingError(int errorCode, Throwable error) {
//
//            }
//
//            @Override
//            public void onBillingInitialized() {
//
//            }
//        });
//        bp.initialize();
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

//
//        binding.navLayout.tvSettings.setOnClickListener(view -> {
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//            IntentHelper.startMySettingsActivityForResult(this, SETTINGS_ACTIVITY);
//        });
//
//        binding.navLayout.llRemoveAds.setOnClickListener(view -> {
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//            bp.purchase(MainActivity.this, Config.PRODUCT_ID);
//        });

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

    // control.

    public void toggleDrawerLayout() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (SharePreferenceUtils.shouldShowRatePopup(this)) {
            rate(true);
        }
        executeBack();
    }

    private void executeBack() {
        if (doubleBackToExitPressedOnce) {
            SharePreferenceUtils.increaseCountRate(this);
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, getString(R.string.click_back_again), Toast.LENGTH_SHORT).show()
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

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

    public void redrawAfterNightInfo() {
        resetUI(location);
    }

    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
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


            setWeatherImage(location.getWeather(), binding.background.imgWeather, settingsOptionManager.isWeatherBgEnabled());


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


            sunMoonUtils = new SunMoonUtils(binding.background.sunMoonView, location, resourceProvider, false);
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


            binding.background.radarWebView.loadData(html, "text/html; charset=UTF-8", null);


            RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setDuration(5000);
            rotateAnimation.setRepeatCount(Animation.INFINITE);

            binding.background.imgWindFan.startAnimation(rotateAnimation);


            String windDirection = "N/A", windSpeed = "N/A", guageText = "N/A";
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


            binding.background.tvPressureValue.setText(settingsOptionManager.getPressureUnit().getPressureText(MainActivity.this, location.getWeather().getCurrent().getPressure()));


            MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), viewModel.getLocationList(), loadLocation);
            binding.background.mainPager.setAdapter(mainPagerAdapter);
            int size = viewModel.getLocationList().size();
            binding.background.mainPager.setOffscreenPageLimit(viewModel.getLocationList().size() - 1);
            binding.background.mainPager.setCurrentItem(index);
            binding.background.frAd.setVisibility(View.VISIBLE);
            AdmobManager.getInstance().loadBanner(this, BuildConfig.banner_main);

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


    public void setWeatherImage(Weather weather, ImageView imageView, boolean isBgEnabled) {
        switch (weather.getCurrent().getWeatherCode()) {
            case CLEAR:
                if (MyUtils.isNight()) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_moon));
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.clear_n));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.night));

                    }

                } else {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.clear));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.day));

                    }
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_sun_cloudy));

                }
                break;
            case PARTLY_CLOUDY:
            case CLOUDY:

                if (MyUtils.isNight()) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_cloudy_moon));
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.bg_t_cloudy));
                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.night));
                    }

                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_sun_cloudy));
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.bg_s_cloudy));
                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.day));
                    }


                }
                break;
            case RAIN:
                if (MyUtils.isNight()) {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.rain_n));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.night));
                    }
                } else {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.bg_t_rain));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.day));
                    }
                }
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_rain));
                break;
            case SNOW:
            case SLEET:
                if (MyUtils.isNight()) {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.snow_n));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.night));
                    }
                } else {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.bg_s_snow));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.day));
                    }
                }
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_snow));
                break;
            case HAIL:
                if (MyUtils.isNight()) {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.bg_s_hail));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.night));
                    }
                } else {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.bg_s_hail));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.day));
                    }
                }
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_fog));
                break;
            case WIND:
                if (MyUtils.isNight()) {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.bg_t_wind));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.night));
                    }
                } else {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.bg_s_wind));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.day));
                    }
                }
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.wind));
                break;
            case FOG:
            case HAZE:
                if (MyUtils.isNight()) {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.bg_t_haze));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.night));
                    }
                } else {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.bg_s_haze));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.day));
                    }
                }
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_fog));
                break;

            case THUNDER:
            case THUNDERSTORM:
                if (MyUtils.isNight()) {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.bg_t_thunder));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.night));
                    }
                } else {
                    if (isBgEnabled) {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.bg_s_thunder));

                    } else {
                        binding.getRoot().setBackground(ContextCompat.getDrawable(this, R.drawable.day));
                    }
                }
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.img_thunder_rain));

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
                if (locationList != null && locationList.size() > 0) {
                    ShortcutsManager.refreshShortcutsInNewThread(this, locationList);

                }
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


    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        loadIntersAd();
        loadIntersAdDailyDetails();
        Log.d("android_log", "onResume: ");
        if (isGotoSettings) {
            if (!PermissionUtils.permissionGranted(
                    MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            )) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    DialogPer1.start(this);
                    DialogPer1.listener = () -> {
                        gotoSettings(this);
                    };
                } else {
                    DialogPer2.start(this);
                    DialogPer2.listener = () -> {
                        gotoSettings(this);
                    };
                }
                return;
            }
            getCurrentLocation();
        } else if (!isLocationEnabled()) {
            if (!PermissionUtils.permissionGranted(
                    MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            )) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, MyUtils.requestCode);
                return;
            }
        }
        if (isStartAgain && location != null) {
            viewModel.updateWeather(this);
            isStartAgain = false;
        }

        if (isLocationEnabled()) {
            getCurrentLocation();
        } else {
            if (isGotoSettings) {
                buildAlertMessageNoGps();
            }
        }

    }

    private void getCurrentLocation() {
        viewModel.getCurrentLocation().observe(this, resource -> {
            boolean updateInBackground = resource.consumeUpdatedInBackground();

            setRefreshing(resource.status == Resource.Status.LOADING);
            drawUI(resource.data, resource.isDefaultLocation(), updateInBackground);

            consumeIntentAction();
            refreshBackgroundViews(true, viewModel.getLocationList(), false, false);
        });
    }

    @Override
    public void onRefresh() {
        if (!isLocationEnabled()) {
            buildAlertMessageNoGps();
        } else {
            viewModel.updateWeather(this);
        }
    }

    public void showDialogSettingUnit() {
        SettingNavDialog.Companion.start(this, (key, data) -> {
            if (Objects.equals(key, "done")) onRefresh();
        });
    }

    public void goToMap() {
        if (location != null) {
            sendToRadar(location);
        }
    }

    public void rate(boolean isFinish) {
        RateAppDialog rateAppDialog = new RateAppDialog(this);
        rateAppDialog.show();
        rateAppDialog.setCallback(new RateCallback() {
            @Override
            public void onMaybeLater() {
                if (isFinish) {
                    SharePreferenceUtils.increaseCountRate(MainActivity.this);
                    finishAffinity();
                }
            }

            @Override
            public void onSubmit(String review) {
                SharePreferenceUtils.setRated(MainActivity.this);
                if (isFinish) {
                    finishAffinity();
                }
            }

            @Override
            public void onRate() {
                CommonUtils.getInstance().rateApp(MainActivity.this);
                SharePreferenceUtils.setRated(MainActivity.this);
            }
        });
    }

    public void share() {
        CommonUtils.getInstance().shareApp(this, "Share your app");
    }

    public void feedBack(String email) {
        CommonUtils.getInstance().support(this, "Feedback to us", email);
    }

    public void privacy() {
        CommonUtils.getInstance().showPolicy(this, CmUtils.POLICY_URL);
    }

    public void showNotify() {
        refreshBackgroundViews(true, viewModel.getLocationList(), true, true);
    }

    // on scroll changed listener.

    public void setCityName(String cityName) {
        binding.background.tvCityName.setText(cityName);
    }


//    public void showSpeedUnitDialog() {
//        String[] unitsTitle = getResources().getStringArray(R.array.speed_units);
//        String[] unitsValues = getResources().getStringArray(R.array.speed_unit_values);
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle(getString(R.string.settings_title_speed_unit));
//
//        builder.setItems(unitsTitle, (dialog, which) -> {
//            settingsOptionManager.setSpeedUnit(unitsValues[which]);
//            binding.navLayout.etNavSpeedUnit.setText(settingsOptionManager.getSpeedUnit().getAbbreviation(this));
//
//            SnackbarUtils.showSnackbar(
//                    MainActivity.this, getString(R.string.feedback_refresh_ui_after_refresh));
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//    public void showDistanceUnitDialog() {
//        String[] unitsTitle = getResources().getStringArray(R.array.distance_units);
//        String[] unitsValues = getResources().getStringArray(R.array.distance_unit_values);
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle(getString(R.string.settings_title_distance_unit));
//
//        builder.setItems(unitsTitle, (dialog, which) -> {
//            settingsOptionManager.setDistanceUnit(unitsValues[which]);
//            binding.navLayout.etNavDistanceUnit.setText(settingsOptionManager.getDistanceUnit().getAbbreviation(this));
//
//            SnackbarUtils.showSnackbar(
//                    MainActivity.this, getString(R.string.feedback_refresh_ui_after_refresh));
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//    public void showPrecipUnitDialog() {
//        String[] unitsTitle = getResources().getStringArray(R.array.precipitation_units);
//        String[] unitsValues = getResources().getStringArray(R.array.precipitation_unit_values);
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle(getString(R.string.settings_title_precipitation_unit));
//
//        builder.setItems(unitsTitle, (dialog, which) -> {
//            settingsOptionManager.setPrecipitationUnit(unitsValues[which]);
//            binding.navLayout.etNavPrecipUnit.setText(settingsOptionManager.getPrecipitationUnit().getAbbreviation(this));
//
//            SnackbarUtils.showSnackbar(
//                    MainActivity.this, getString(R.string.feedback_refresh_ui_after_refresh));
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//    public void showPressureUnitDialog() {
//        String[] pressureUnitsTitle = getResources().getStringArray(R.array.pressure_units);
//        String[] pressureUnitsValues = getResources().getStringArray(R.array.pressure_unit_values);
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle(getString(R.string.settings_title_pressure_unit));
//
//        builder.setItems(pressureUnitsTitle, (dialog, which) -> {
//            settingsOptionManager.setPressureUnit(pressureUnitsValues[which]);
//            binding.navLayout.etNavPressureUnit.setText(settingsOptionManager.getPressureUnit().getAbbreviation(this));
//
//            SnackbarUtils.showSnackbar(
//                    MainActivity.this, getString(R.string.feedback_refresh_ui_after_refresh));
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//    public void showTempUnitDialog() {
//        String[] tempUnitsTitle = getResources().getStringArray(R.array.temperature_units);
//        String[] tempUnitsValues = getResources().getStringArray(R.array.temperature_unit_values);
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle(getString(R.string.settings_title_temperature_unit));
//
//        builder.setItems(tempUnitsTitle, (dialog, which) -> {
//            settingsOptionManager.setTemperatureUnit(tempUnitsValues[which]);
//            PollingManager.resetNormalBackgroundTask(MainActivity.this, false);
//            binding.navLayout.etNavTempUnit.setText(settingsOptionManager.getTemperatureUnit().getAbbreviation(this));
//
//            SnackbarUtils.showSnackbar(
//                    MainActivity.this, getString(R.string.feedback_refresh_ui_after_refresh));
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//    public void showRefreshRateDialog() {
//        String[] refreshRatesTitles = getResources().getStringArray(R.array.automatic_refresh_rates);
//        String[] refreshRatesValues = getResources().getStringArray(R.array.automatic_refresh_rate_values);
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle(getString(R.string.settings_title_refresh_rate));
//
//        builder.setItems(refreshRatesTitles, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                settingsOptionManager.setUpdateInterval(refreshRatesValues[which]);
//                PollingManager.resetNormalBackgroundTask(MainActivity.this, false);
//                binding.navLayout.etNavRefreshRate.setText(settingsOptionManager.getUpdateInterval().getUpdateIntervalName(MainActivity.this));
//
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//

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

    public interface LoadLocation {
        void load(String formattedId);
    }

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
}