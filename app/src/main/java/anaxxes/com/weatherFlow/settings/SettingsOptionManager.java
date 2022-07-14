package anaxxes.com.weatherFlow.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.util.List;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.option.WidgetWeekIconMode;
import anaxxes.com.weatherFlow.basic.model.option.appearance.DailyTrendDisplay;
import anaxxes.com.weatherFlow.basic.model.option.utils.OptionMapper;
import anaxxes.com.weatherFlow.basic.model.option.appearance.CardDisplay;
import anaxxes.com.weatherFlow.basic.model.option.appearance.Language;
import anaxxes.com.weatherFlow.basic.model.option.NotificationStyle;
import anaxxes.com.weatherFlow.basic.model.option.NotificationTextColor;
import anaxxes.com.weatherFlow.basic.model.option.appearance.UIStyle;
import anaxxes.com.weatherFlow.basic.model.option.provider.LocationProvider;
import anaxxes.com.weatherFlow.basic.model.option.provider.WeatherSource;
import anaxxes.com.weatherFlow.basic.model.option.DarkMode;
import anaxxes.com.weatherFlow.basic.model.option.UpdateInterval;
import anaxxes.com.weatherFlow.basic.model.option.unit.DistanceUnit;
import anaxxes.com.weatherFlow.basic.model.option.unit.PrecipitationUnit;
import anaxxes.com.weatherFlow.basic.model.option.unit.PressureUnit;
import anaxxes.com.weatherFlow.basic.model.option.unit.SpeedUnit;
import anaxxes.com.weatherFlow.basic.model.option.unit.TemperatureUnit;

public class SettingsOptionManager {

    private static volatile SettingsOptionManager instance;

    public static SettingsOptionManager getInstance(Context context) {
        if (instance == null) {
            synchronized (SettingsOptionManager.class) {
                if (instance == null) {
                    instance = new SettingsOptionManager(context);
                }
            }
        }
        return instance;
    }

    // basic.
    private boolean isFirstTime;



    private boolean backgroundFree;
    private boolean alertPushEnabled;
    private boolean precipitationPushEnabled;
    private boolean showNightInfoEnabled;
    private boolean isWeatherBgEnabled;
    private boolean isTermChange;


    private UpdateInterval updateInterval;
    private DarkMode darkMode;

    // service provider.
    private WeatherSource weatherSource;
    private LocationProvider locationProvider;

    // unit.
    private TemperatureUnit temperatureUnit;
    private DistanceUnit distanceUnit;
    private PrecipitationUnit precipitationUnit;
    private PressureUnit pressureUnit;
    private SpeedUnit speedUnit;

    // appearance.
    private UIStyle uiStyle;
    private String iconProvider;
    private List<CardDisplay> cardDisplayList;
    private static final String DEFAULT_CARD_DISPLAY = "daily_overview"
            + "&hourly_overview"
            + "&air_quality"
            + "&allergen"
            + "&sunrise_sunset"
            + "&life_details";
    private List<DailyTrendDisplay> dailyTrendDisplayList;
    private static final String DEFAULT_DAILY_TREND_DISPLAY = "temperature"
            + "&air_quality"
            + "&wind"
            + "&uv_index"
            + "&precipitation";

    private boolean trendHorizontalLinesEnabled;
    private boolean exchangeDayNightTempEnabled;
    private boolean gravitySensorEnabled;
    private boolean listAnimationEnabled;
    private boolean itemAnimationEnabled;
    private Language language;

    // forecast.
    private boolean todayForecastEnabled;
    private String todayForecastTime;
    public static final String DEFAULT_TODAY_FORECAST_TIME = "07:00";

    private boolean tomorrowForecastEnabled;
    private String tomorrowForecastTime;
    public static final String DEFAULT_TOMORROW_FORECAST_TIME = "21:00";

    // widget.
    private WidgetWeekIconMode widgetWeekIconMode;
    private boolean widgetMinimalIconEnabled;
    private boolean widgetClickToRefreshEnabled;

    // notification.
    private boolean notificationEnabled;
    private NotificationStyle notificationStyle;
    private boolean notificationMinimalIconEnabled;
    private boolean notificationTemperatureIconEnabled;
    private boolean notificationCustomColorEnabled;
    @ColorInt private int notificationBackgroundColor;
    private NotificationTextColor notificationTextColor;
    private boolean notificationCanBeClearedEnabled;
    private boolean notificationHideIconEnabled;
    private boolean notificationHideInLockScreenEnabled;
    private boolean notificationHideBigViewEnabled;


    private SharedPreferences sharedPreferences;
    private Context context;

    private SettingsOptionManager(Context context) {
       sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
       this.context = context;

        // basic.
        isFirstTime = sharedPreferences.getBoolean(context.getString(R.string.key_check_time_start_app), true);
        backgroundFree = sharedPreferences.getBoolean(
                context.getString(R.string.key_background_free), true);

        alertPushEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_alert_notification_switch), true);

        precipitationPushEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_precipitation_notification_switch), false);

        showNightInfoEnabled = sharedPreferences.getBoolean(context.getString(R.string.key_show_night_info_switch),false);
        isWeatherBgEnabled = sharedPreferences.getBoolean(context.getString(R.string.key_weather_background),true);
        isTermChange =sharedPreferences.getBoolean(context.getString(R.string.key_term_change), true);

        updateInterval = OptionMapper.getUpdateInterval(
                sharedPreferences.getString(
                        context.getString(R.string.key_refresh_rate), "1:30")
        );

        darkMode = OptionMapper.getDarkMode(
                sharedPreferences.getString(
                        context.getString(R.string.key_dark_mode), "light")
        );

        // service provider.

        weatherSource = OptionMapper.getWeatherSource(
                sharedPreferences.getString(
                        context.getString(R.string.key_weather_source), "accu")
        );

        locationProvider = OptionMapper.getLocationProvider(
                sharedPreferences.getString(
                        context.getString(R.string.key_location_service), "native")
        );

        // unit.

        temperatureUnit = OptionMapper.getTemperatureUnit(
                sharedPreferences.getString(
                        context.getString(R.string.key_temperature_unit), "c")
        );
        distanceUnit = OptionMapper.getDistanceUnit(
                sharedPreferences.getString(
                        context.getString(R.string.key_distance_unit), "km")
        );
        precipitationUnit = OptionMapper.getPrecipitationUnit(
                sharedPreferences.getString(
                        context.getString(R.string.key_precipitation_unit), "mm")
        );
        pressureUnit = OptionMapper.getPressureUnit(
                sharedPreferences.getString(
                        context.getString(R.string.key_pressure_unit), "mb")
        );
        speedUnit = OptionMapper.getSpeedUnit(
                sharedPreferences.getString(
                        context.getString(R.string.key_speed_unit), "mps")
        );

        // appearance.

        uiStyle = OptionMapper.getUIStyle(
                sharedPreferences.getString(
                        context.getString(R.string.key_ui_style), "material")
        );

        iconProvider = sharedPreferences.getString(
                context.getString(R.string.key_icon_provider),
                context.getPackageName()
        );

        cardDisplayList = OptionMapper.getCardDisplayList(
                sharedPreferences.getString(context.getString(R.string.key_card_display), DEFAULT_CARD_DISPLAY)
        );

        dailyTrendDisplayList = OptionMapper.getDailyTrendDisplayList(
                sharedPreferences.getString(
                        context.getString(R.string.key_daily_trend_display),
                        DEFAULT_DAILY_TREND_DISPLAY
                )
        );

        trendHorizontalLinesEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_trend_horizontal_line_switch), true);

        exchangeDayNightTempEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_exchange_day_night_temp_switch), false);

        gravitySensorEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_gravity_sensor_switch), true);

        listAnimationEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_list_animation_switch), true);

        itemAnimationEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_item_animation_switch), true);

        language = OptionMapper.getLanguage(
                sharedPreferences.getString(
                        context.getString(R.string.key_language), "follow_system")
        );

        // forecast.

        todayForecastEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_forecast_today), false);

        todayForecastTime = sharedPreferences.getString(
                context.getString(R.string.key_forecast_today_time), DEFAULT_TODAY_FORECAST_TIME);

        tomorrowForecastEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_forecast_tomorrow), false);

        tomorrowForecastTime = sharedPreferences.getString(
                context.getString(R.string.key_forecast_tomorrow_time), DEFAULT_TOMORROW_FORECAST_TIME);

        // widget.

        widgetWeekIconMode = OptionMapper.getWidgetWeekIconMode(
                sharedPreferences.getString(
                        context.getString(R.string.key_week_icon_mode), "auto")
        );

        widgetMinimalIconEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_widget_minimal_icon), false);

        widgetClickToRefreshEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_click_widget_to_refresh), false);

        // notification.

        notificationEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_notification), false);

        notificationStyle = OptionMapper.getNotificationStyle(
                sharedPreferences.getString(
                        context.getString(R.string.key_notification_style), "geometric")
        );

        notificationMinimalIconEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_notification_minimal_icon), false);

        notificationTemperatureIconEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_notification_temp_icon), false);

        notificationCustomColorEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_notification_custom_color), false);

        notificationBackgroundColor = sharedPreferences.getInt(
                context.getString(R.string.key_notification_background_color),
                ContextCompat.getColor(context, R.color.notification_background_l));

        notificationTextColor = OptionMapper.getNotificationTextColor(
                sharedPreferences.getString(
                        context.getString(R.string.key_notification_text_color), "dark")
        );

        notificationCanBeClearedEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_notification_can_be_cleared), false);

        notificationHideIconEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_notification_hide_icon), false);

        notificationHideInLockScreenEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_notification_hide_in_lockScreen), false);

        notificationHideBigViewEnabled = sharedPreferences.getBoolean(
                context.getString(R.string.key_notification_hide_big_view), false);
    }
    public boolean isFirstTime() {
        return isFirstTime;
    }

    @SuppressLint("CommitPrefEdits")
    public void setFirstTime(boolean firstTime) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.key_check_time_start_app), firstTime).apply();
        isFirstTime = firstTime;
    }
    public boolean isBackgroundFree() {
        return backgroundFree;
    }

    public void setBackgroundFree(boolean backgroundFree) {
        this.backgroundFree = backgroundFree;
    }

    public boolean isAlertPushEnabled() {
        return alertPushEnabled;
    }

    public void setAlertPushEnabled(boolean alertPushEnabled) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.key_alert_notification_switch),alertPushEnabled).apply();
        this.alertPushEnabled = alertPushEnabled;
    }

    public boolean isPrecipitationPushEnabled() {
        return precipitationPushEnabled;
    }

    public void setPrecipitationPushEnabled(boolean precipitationPushEnabled) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.key_precipitation_notification_switch),precipitationPushEnabled).apply();
        this.precipitationPushEnabled = precipitationPushEnabled;

    }

    public boolean isTermChange() {
        return isTermChange;
    }

    public void setTermChange(boolean termChange) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.key_term_change), termChange).apply();
        this.isTermChange = termChange;
        if (termChange){
            this.setTemperatureUnit("c");
        }
        else{
            this.setTemperatureUnit("f");
        }
    }
    public boolean isShowNightInfoEnabled() {
        return showNightInfoEnabled;
    }

    public void setShowNightInfoEnabled(boolean showNightInfoEnabled) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.key_show_night_info_switch),showNightInfoEnabled).apply();
        this.showNightInfoEnabled = showNightInfoEnabled;
    }

    public UpdateInterval getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(String updateIntervalValue) {
        sharedPreferences.edit().putString(context.getString(R.string.key_refresh_rate),updateIntervalValue).apply();

        this.updateInterval = OptionMapper.getUpdateInterval( updateIntervalValue);
    }

    public DarkMode getDarkMode() {
        return darkMode;
    }

    public void setDarkMode(String darkModeValue) {
        sharedPreferences.edit().putString(context.getString(R.string.key_dark_mode),darkModeValue).apply();

        this.darkMode = OptionMapper.getDarkMode((String) darkModeValue);
    }

    public WeatherSource getWeatherSource() {
        return weatherSource;
    }

    public void setWeatherSource(WeatherSource weatherSource) {
        this.weatherSource = weatherSource;
    }

    public LocationProvider getLocationProvider() {
        return locationProvider;
    }

    public void setLocationProvider(LocationProvider locationProvider) {
        this.locationProvider = locationProvider;
    }

    public TemperatureUnit getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnitValue) {
        sharedPreferences.edit().putString(context.getString(R.string.key_temperature_unit),temperatureUnitValue).apply();
        this.temperatureUnit = OptionMapper.getTemperatureUnit(temperatureUnitValue);
    }

    public DistanceUnit getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnitValue) {
        sharedPreferences.edit().putString(context.getString(R.string.key_distance_unit),distanceUnitValue).apply();
        this.distanceUnit = OptionMapper.getDistanceUnit(distanceUnitValue);
    }

    public PrecipitationUnit getPrecipitationUnit() {
        return precipitationUnit;
    }

    public void setPrecipitationUnit(String precipitationUnitValue) {
        sharedPreferences.edit().putString(context.getString(R.string.key_precipitation_unit),precipitationUnitValue).apply();
        this.precipitationUnit = OptionMapper.getPrecipitationUnit(precipitationUnitValue);
    }

    public PressureUnit getPressureUnit() {
        return pressureUnit;
    }

    public void setPressureUnit(String pressureUnitValue) {
        sharedPreferences.edit().putString(context.getString(R.string.key_pressure_unit),pressureUnitValue).apply();
        this.pressureUnit = OptionMapper.getPressureUnit( pressureUnitValue);
    }

    public SpeedUnit getSpeedUnit() {
        return speedUnit;
    }

    public void setSpeedUnit(String speedUnitValue) {
        sharedPreferences.edit().putString(context.getString(R.string.key_speed_unit),speedUnitValue).apply();
        this.speedUnit = OptionMapper.getSpeedUnit( speedUnitValue);
    }

    public UIStyle getUiStyle() {
        return uiStyle;
    }

    public void setUiStyle(UIStyle uiStyle) {
        this.uiStyle = uiStyle;
    }

    public String getIconProvider() {
        return iconProvider;
    }

    public void setIconProvider(String iconProvider) {
        this.iconProvider = iconProvider;
    }

    public List<CardDisplay> getCardDisplayList() {
        return cardDisplayList;
    }

    public void setCardDisplayList(List<CardDisplay> cardDisplayList) {
        this.cardDisplayList = cardDisplayList;
    }

    public List<DailyTrendDisplay> getDailyTrendDisplayList() {
        return dailyTrendDisplayList;
    }

    public void setDailyTrendDisplayList(List<DailyTrendDisplay> dailyTrendDisplayList) {
        this.dailyTrendDisplayList = dailyTrendDisplayList;
    }

    public boolean isTrendHorizontalLinesEnabled() {
        return trendHorizontalLinesEnabled;
    }

    public void setTrendHorizontalLinesEnabled(boolean trendHorizontalLinesEnabled) {
        this.trendHorizontalLinesEnabled = trendHorizontalLinesEnabled;
    }

    public boolean isExchangeDayNightTempEnabled() {
        return exchangeDayNightTempEnabled;
    }

    public void setExchangeDayNightTempEnabled(boolean exchangeDayNightTempEnabled) {
        this.exchangeDayNightTempEnabled = exchangeDayNightTempEnabled;
    }

    public boolean isGravitySensorEnabled() {
        return gravitySensorEnabled;
    }

    public void setGravitySensorEnabled(boolean gravitySensorEnabled) {
        this.gravitySensorEnabled = gravitySensorEnabled;
    }

    public boolean isListAnimationEnabled() {
        return listAnimationEnabled;
    }

    public void setListAnimationEnabled(boolean listAnimationEnabled) {
        this.listAnimationEnabled = listAnimationEnabled;
    }

    public boolean isItemAnimationEnabled() {
        return itemAnimationEnabled;
    }

    public void setItemAnimationEnabled(boolean itemAnimationEnabled) {
        this.itemAnimationEnabled = itemAnimationEnabled;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public boolean isTodayForecastEnabled() {
        return todayForecastEnabled;
    }

    public void setTodayForecastEnabled(boolean todayForecastEnabled) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.key_forecast_today),todayForecastEnabled).apply();
        this.todayForecastEnabled = todayForecastEnabled;
    }

    public String getTodayForecastTime() {
        return todayForecastTime;
    }

    public void setTodayForecastTime(String todayForecastTime) {
        this.todayForecastTime = todayForecastTime;
    }

    public boolean isTomorrowForecastEnabled() {
        return tomorrowForecastEnabled;
    }

    public void setTomorrowForecastEnabled(boolean tomorrowForecastEnabled) {
        this.tomorrowForecastEnabled = tomorrowForecastEnabled;
    }

    public String getTomorrowForecastTime() {
        return tomorrowForecastTime;
    }

    public void setTomorrowForecastTime(String tomorrowForecastTime) {
        this.tomorrowForecastTime = tomorrowForecastTime;
    }

    public WidgetWeekIconMode getWidgetWeekIconMode() {
        return widgetWeekIconMode;
    }

    public void setWidgetWeekIconMode(WidgetWeekIconMode widgetWeekIconMode) {
        this.widgetWeekIconMode = widgetWeekIconMode;
    }

    public boolean isWidgetMinimalIconEnabled() {
        return widgetMinimalIconEnabled;
    }

    public void setWidgetMinimalIconEnabled(boolean widgetMinimalIconEnabled) {
        this.widgetMinimalIconEnabled = widgetMinimalIconEnabled;
    }

    public boolean isWidgetClickToRefreshEnabled() {
        return widgetClickToRefreshEnabled;
    }

    public void setWidgetClickToRefreshEnabled(boolean widgetClickToRefreshEnabled) {
        this.widgetClickToRefreshEnabled = widgetClickToRefreshEnabled;
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.key_notification),notificationEnabled).apply();
        this.notificationEnabled = notificationEnabled;
    }

    public NotificationStyle getNotificationStyle() {
        return notificationStyle;
    }

    public void setNotificationStyle(String notificationStyleValue) {
        sharedPreferences.edit().putString(context.getString(R.string.key_notification_style),notificationStyleValue).apply();
        this.notificationStyle = OptionMapper.getNotificationStyle((String) notificationStyleValue);
    }

    public boolean isNotificationMinimalIconEnabled() {
        return notificationMinimalIconEnabled;
    }

    public void setNotificationMinimalIconEnabled(boolean notificationMinimalIconEnabled) {
        this.notificationMinimalIconEnabled = notificationMinimalIconEnabled;
    }

    public boolean isNotificationTemperatureIconEnabled() {
        return notificationTemperatureIconEnabled;
    }

    public void setNotificationTemperatureIconEnabled(boolean notificationTemperatureIconEnabled) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.key_notification_temp_icon),notificationTemperatureIconEnabled).apply();
        this.notificationTemperatureIconEnabled = notificationTemperatureIconEnabled;
    }

    public boolean isNotificationCustomColorEnabled() {
        return notificationCustomColorEnabled;
    }

    public void setNotificationCustomColorEnabled(boolean notificationCustomColorEnabled) {
        this.notificationCustomColorEnabled = notificationCustomColorEnabled;
    }

    public int getNotificationBackgroundColor() {
        return notificationBackgroundColor;
    }

    public void setNotificationBackgroundColor(int notificationBackgroundColor) {
        this.notificationBackgroundColor = notificationBackgroundColor;
    }

    public NotificationTextColor getNotificationTextColor() {
        return notificationTextColor;
    }

    public void setNotificationTextColor(NotificationTextColor notificationTextColor) {
        this.notificationTextColor = notificationTextColor;
    }

    public boolean isNotificationCanBeClearedEnabled() {
        return notificationCanBeClearedEnabled;
    }

    public void setNotificationCanBeClearedEnabled(boolean notificationCanBeClearedEnabled) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.key_notification_can_be_cleared),notificationCanBeClearedEnabled).apply();
        this.notificationCanBeClearedEnabled = notificationCanBeClearedEnabled;
    }

    public boolean isNotificationHideIconEnabled() {
        return notificationHideIconEnabled;
    }

    public void setNotificationHideIconEnabled(boolean notificationHideIconEnabled) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.key_notification_hide_icon),notificationHideInLockScreenEnabled).apply();
        this.notificationHideIconEnabled = notificationHideIconEnabled;
    }

    public boolean isNotificationHideInLockScreenEnabled() {
        return notificationHideInLockScreenEnabled;
    }

    public void setNotificationHideInLockScreenEnabled(boolean notificationHideInLockScreenEnabled) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.key_notification_hide_in_lockScreen),notificationHideInLockScreenEnabled).apply();
        this.notificationHideInLockScreenEnabled = notificationHideInLockScreenEnabled;
    }

    public boolean isNotificationHideBigViewEnabled() {
        return notificationHideBigViewEnabled;
    }

    public void setNotificationHideBigViewEnabled(boolean notificationHideBigViewEnabled) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.key_notification_hide_big_view),notificationHideBigViewEnabled).apply();
        this.notificationHideBigViewEnabled = notificationHideBigViewEnabled;
    }

    public boolean isWeatherBgEnabled() {
        return isWeatherBgEnabled;
    }

    public void setWeatherBgEnabled(boolean weatherBgEnabled) {
        isWeatherBgEnabled = weatherBgEnabled;
    }
}
