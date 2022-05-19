package anaxxes.com.weatherFlow.remoteviews.presenter.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.widget.RemoteViews;

import java.util.Date;

import anaxxes.com.weatherFlow.WeatherFlow;
import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.basic.model.option.NotificationStyle;
import anaxxes.com.weatherFlow.basic.model.option.NotificationTextColor;
import anaxxes.com.weatherFlow.basic.model.option.unit.TemperatureUnit;
import anaxxes.com.weatherFlow.basic.model.weather.Base;
import anaxxes.com.weatherFlow.basic.model.weather.Temperature;
import anaxxes.com.weatherFlow.basic.model.weather.Weather;
import anaxxes.com.weatherFlow.remoteviews.presenter.AbstractRemoteViewsPresenter;
import anaxxes.com.weatherFlow.resource.ResourceHelper;
import anaxxes.com.weatherFlow.resource.provider.ResourceProvider;
import anaxxes.com.weatherFlow.resource.provider.ResourcesProviderFactory;
import anaxxes.com.weatherFlow.settings.SettingsOptionManager;
import anaxxes.com.weatherFlow.utils.LanguageUtils;
import anaxxes.com.weatherFlow.utils.helpter.LunarHelper;
import anaxxes.com.weatherFlow.utils.manager.TimeManager;

/**
 * Normal notification utils.
 * */

public class NormalNotificationIMP extends AbstractRemoteViewsPresenter {

    public static void buildNotificationAndSendIt(Context context, @NonNull Location location) {
        Weather weather = location.getWeather();
        if (weather == null) {
            return;
        }

        ResourceProvider provider = ResourcesProviderFactory.getNewInstance();

        LanguageUtils.setLanguage(
                context,
                SettingsOptionManager.getInstance(context).getLanguage().getLocale()
        );

        // get sp & realTimeWeather.
        SettingsOptionManager settings = SettingsOptionManager.getInstance(context);

        TemperatureUnit temperatureUnit = settings.getTemperatureUnit();

        boolean dayTime = TimeManager.isDaylight(location);

        boolean minimalIcon = settings.isNotificationMinimalIconEnabled()
                && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P;

        boolean tempIcon = settings.isNotificationTemperatureIconEnabled();

        boolean customColor = settings.isNotificationCustomColorEnabled();

        boolean hideBigView = settings.isNotificationHideBigViewEnabled();

        boolean hideNotificationIcon = settings.isNotificationHideIconEnabled();

        boolean hideNotificationInLockScreen = settings.isNotificationHideInLockScreenEnabled();

        boolean canBeCleared = settings.isNotificationCanBeClearedEnabled();

        if (settings.getNotificationStyle() == NotificationStyle.NATIVE) {
            NativeNormalNotificationIMP.buildNotificationAndSendIt(context, location, temperatureUnit,
                    dayTime, tempIcon, hideNotificationIcon, hideNotificationInLockScreen, canBeCleared);
            return;
        }

        // background color.
        int backgroundColor = settings.getNotificationBackgroundColor();

        // get text color.
        NotificationTextColor textColor = settings.getNotificationTextColor();

        int mainColor = ContextCompat.getColor(context, textColor.getMainTextColorResId());
        int subColor = ContextCompat.getColor(context, textColor.getSubTextColorResId());

        // build channel.
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    WeatherFlow.NOTIFICATION_CHANNEL_ID_NORMALLY,
                    WeatherFlow.getNotificationChannelName(
                            context,
                            WeatherFlow.NOTIFICATION_CHANNEL_ID_NORMALLY
                    ),
                    hideNotificationIcon
                            ? NotificationManager.IMPORTANCE_MIN
                            : NotificationManager.IMPORTANCE_LOW
            );
            channel.setShowBadge(false);
            channel.setImportance(hideNotificationIcon
                    ? NotificationManager.IMPORTANCE_UNSPECIFIED : NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(hideNotificationInLockScreen
                    ? NotificationCompat.VISIBILITY_SECRET : NotificationCompat.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }

        // get manager & builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context, WeatherFlow.NOTIFICATION_CHANNEL_ID_NORMALLY);

        // set notification level.
        builder.setPriority(hideNotificationIcon
                ? NotificationCompat.PRIORITY_MIN : NotificationCompat.PRIORITY_MAX);

        // set notification visibility.
        builder.setVisibility(hideNotificationInLockScreen
                ? NotificationCompat.VISIBILITY_SECRET : NotificationCompat.VISIBILITY_PUBLIC);

        // set small icon.
        builder.setSmallIcon(
                tempIcon ? ResourceHelper.getTempIconId(
                        context,
                        temperatureUnit.getTemperature(
                                weather.getCurrent().getTemperature().getTemperature()

                        )
                ) : ResourceHelper.getDefaultMinimalXmlIconId(
                        weather.getCurrent().getWeatherCode(),
                        dayTime
                )
        );

        // build base view.
        builder.setContent(
                buildBaseView(
                        context, new RemoteViews(context.getPackageName(), R.layout.notification_base),
                        provider, location,
                        temperatureUnit, dayTime,
                        minimalIcon, textColor,
                        customColor, backgroundColor,
                        mainColor, subColor
                )
        );

        builder.setContentIntent(
                getWeatherPendingIntent(context, null, WeatherFlow.NOTIFICATION_ID_NORMALLY)
        );

        // build big view.
        if (hideBigView) {
            builder.setCustomBigContentView(
                    buildBaseView(
                            context, new RemoteViews(context.getPackageName(), R.layout.notification_base_big),
                            provider, location,
                            temperatureUnit, dayTime,
                            minimalIcon, textColor,
                            customColor, backgroundColor,
                            mainColor, subColor
                    )
            );
        } else {
            builder.setCustomBigContentView(
                    buildBigView(
                            context, new RemoteViews(context.getPackageName(), R.layout.notification_big),
                            provider, location,
                            temperatureUnit, dayTime,
                            minimalIcon, textColor,
                            customColor, backgroundColor,
                            mainColor, subColor
                    )
            );
        }

        // set clear flag
        builder.setOngoing(!canBeCleared);

        // set only alert once.
        builder.setOnlyAlertOnce(true);

        Notification notification = builder.build();
        if (!tempIcon && Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            try {
                notification.getClass()
                        .getMethod("setSmallIcon", Icon.class)
                        .invoke(
                                notification,
                                ResourceHelper.getMinimalIcon(
                                        provider, weather.getCurrent().getWeatherCode(), dayTime)
                        );
            } catch (Exception ignore) {
                // do nothing.
            }
        }

        // commit.
        manager.notify(WeatherFlow.NOTIFICATION_ID_NORMALLY, notification);
    }

    private static RemoteViews buildBaseView(Context context, RemoteViews views,
                                             ResourceProvider provider, Location location,
                                             TemperatureUnit temperatureUnit,
                                             boolean dayTime, boolean minimalIcon,
                                             NotificationTextColor textColor,
                                             boolean customColor, int backgroundColor,
                                             int mainColor, int subColor) {
        Weather weather = location.getWeather();
        if (weather == null) {
            return views;
        }

        views.setImageViewUri(
                R.id.notification_base_icon,
                ResourceHelper.getWidgetNotificationIconUri(
                        provider, weather.getCurrent().getWeatherCode(), dayTime, minimalIcon, textColor
                )
        );

        views.setTextViewText(
                R.id.notification_base_realtimeTemp,
                Temperature.getShortTemperature(
                        context,
                        weather.getCurrent().getTemperature().getTemperature(),
                        temperatureUnit
                )
        );

        if (weather.getCurrent().getAirQuality().isValid()) {
            views.setTextViewText(
                    R.id.notification_base_aqiAndWind,
                    context.getString(R.string.air_quality)
                            + " - "
                            + weather.getCurrent().getAirQuality().getAqiText()
            );
        } else {
            views.setTextViewText(
                    R.id.notification_base_aqiAndWind,
                    context.getString(R.string.wind)
                            + " - "
                            + weather.getCurrent().getWind().getLevel()
            );
        }

        views.setTextViewText(
                R.id.notification_base_weather,
                weather.getCurrent().getWeatherText()
        );

        StringBuilder timeStr = new StringBuilder();
        timeStr.append(location.getCityName(context));
        if (SettingsOptionManager.getInstance(context).getLanguage().isChinese()) {
            timeStr.append(", ")
                    .append(LunarHelper.getLunarDate(new Date()));
        } else {
            timeStr.append(", ")
                    .append(context.getString(R.string.refresh_at))
                    .append(" ")
                    .append(Base.getTime(context, weather.getBase().getUpdateDate()));
        }
        views.setTextViewText(R.id.notification_base_time, timeStr.toString());

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (customColor) {
                views.setInt(R.id.notification_base, "setBackgroundColor", backgroundColor);
            } else {
                views.setInt(R.id.notification_base, "setBackgroundColor", Color.TRANSPARENT);
            }

            views.setTextColor(R.id.notification_base_realtimeTemp, mainColor);
            views.setTextColor(R.id.notification_base_weather, mainColor);
            views.setTextColor(R.id.notification_base_aqiAndWind, subColor);
            views.setTextColor(R.id.notification_base_time, subColor);
        }

        return views;
    }

    private static RemoteViews buildBigView(Context context, RemoteViews views,
                                            ResourceProvider provider, Location location,
                                            TemperatureUnit temperatureUnit,
                                            boolean dayTime, boolean minimalIcon,
                                            NotificationTextColor textColor,
                                            boolean customColor, int backgroundColor,
                                            int mainColor, int subColor) {
        Weather weather = location.getWeather();
        if (weather == null) {
            return views;
        }

        // today
        views = buildBaseView(
                context, views, provider, location,
                temperatureUnit, dayTime,
                minimalIcon, textColor,
                customColor, backgroundColor,
                mainColor, subColor
        );

        // weekly.
        boolean weekIconDaytime = isWeekIconDaytime(
                SettingsOptionManager.getInstance(context).getWidgetWeekIconMode(), dayTime);
        // 1
        views.setTextViewText( // set week 1.
                R.id.notification_big_week_1,
                context.getString(R.string.today)
        );
        views.setTextViewText( // set temps 1.
                R.id.notification_big_temp_1,
                Temperature.getTrendTemperature(
                        context,
                        weather.getDailyForecast().get(0).night().getTemperature().getTemperature(),
                        weather.getDailyForecast().get(0).day().getTemperature().getTemperature(),
                        temperatureUnit
                )
        );
        views.setImageViewUri( // set icon 1.
                R.id.notification_big_icon_1,
                ResourceHelper.getWidgetNotificationIconUri(
                        provider,
                        weekIconDaytime
                                ? weather.getDailyForecast().get(0).day().getWeatherCode()
                                : weather.getDailyForecast().get(0).night().getWeatherCode(),
                        weekIconDaytime, minimalIcon, textColor
                )
        );
        // 2
        views.setTextViewText( // set week 2.
                R.id.notification_big_week_2,
                weather.getDailyForecast().get(1).getWeek(context)
        );
        views.setTextViewText( // set temps 2.
                R.id.notification_big_temp_2,
                Temperature.getTrendTemperature(
                        context,
                        weather.getDailyForecast().get(1).night().getTemperature().getTemperature(),
                        weather.getDailyForecast().get(1).day().getTemperature().getTemperature(),
                        temperatureUnit
                )
        );
        views.setImageViewUri( // set icon 2.
                R.id.notification_big_icon_2,
                ResourceHelper.getWidgetNotificationIconUri( // get icon 2 resource id.
                        provider,
                        weekIconDaytime
                                ? weather.getDailyForecast().get(1).day().getWeatherCode()
                                : weather.getDailyForecast().get(1).night().getWeatherCode(),
                        weekIconDaytime, minimalIcon, textColor
                )
        );
        // 3
        views.setTextViewText( // set week 3.
                R.id.notification_big_week_3,
                weather.getDailyForecast().get(2).getWeek(context)
        );
        views.setTextViewText( // set temps 3.
                R.id.notification_big_temp_3,
                Temperature.getTrendTemperature(
                        context,
                        weather.getDailyForecast().get(2).night().getTemperature().getTemperature(),
                        weather.getDailyForecast().get(2).day().getTemperature().getTemperature(),
                        temperatureUnit
                )
        );
        views.setImageViewUri( // set icon 3.
                R.id.notification_big_icon_3,
                ResourceHelper.getWidgetNotificationIconUri( // get icon 3 resource id.
                        provider,
                        weekIconDaytime
                                ? weather.getDailyForecast().get(2).day().getWeatherCode()
                                : weather.getDailyForecast().get(2).night().getWeatherCode(),
                        weekIconDaytime, minimalIcon, textColor
                )
        );
        // 4
        views.setTextViewText( // set week 4.
                R.id.notification_big_week_4,
                weather.getDailyForecast().get(3).getWeek(context)
        );
        views.setTextViewText( // set temps 4.
                R.id.notification_big_temp_4,
                Temperature.getTrendTemperature(
                        context,
                        weather.getDailyForecast().get(3).night().getTemperature().getTemperature(),
                        weather.getDailyForecast().get(3).day().getTemperature().getTemperature(),
                        temperatureUnit
                )
        );
        views.setImageViewUri( // set icon 4.
                R.id.notification_big_icon_4,
                ResourceHelper.getWidgetNotificationIconUri( // get icon 4 resource id.
                        provider,
                        weekIconDaytime
                                ? weather.getDailyForecast().get(3).day().getWeatherCode()
                                : weather.getDailyForecast().get(3).night().getWeatherCode(),
                        weekIconDaytime, minimalIcon, textColor
                )
        );
        // 5
        views.setTextViewText( // set week 5.
                R.id.notification_big_week_5,
                weather.getDailyForecast().get(4).getWeek(context)
        );
        views.setTextViewText( // set temps 5.
                R.id.notification_big_temp_5,
                Temperature.getTrendTemperature(
                        context,
                        weather.getDailyForecast().get(4).night().getTemperature().getTemperature(),
                        weather.getDailyForecast().get(4).day().getTemperature().getTemperature(),
                        temperatureUnit
                )
        );
        views.setImageViewUri( // set icon 5.
                R.id.notification_big_icon_5,
                ResourceHelper.getWidgetNotificationIconUri( // get icon 5 resource id.
                        provider,
                        weekIconDaytime
                                ? weather.getDailyForecast().get(4).day().getWeatherCode()
                                : weather.getDailyForecast().get(4).night().getWeatherCode(),
                        weekIconDaytime, minimalIcon, textColor
                )
        );

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (customColor) {
                views.setInt(R.id.notification_big, "setBackgroundColor", backgroundColor);
            } else {
                views.setInt(R.id.notification_big, "setBackgroundColor", Color.TRANSPARENT);
            }

            views.setTextColor(R.id.notification_big_week_1, subColor);
            views.setTextColor(R.id.notification_big_week_2, subColor);
            views.setTextColor(R.id.notification_big_week_3, subColor);
            views.setTextColor(R.id.notification_big_week_4, subColor);
            views.setTextColor(R.id.notification_big_week_5, subColor);
            views.setTextColor(R.id.notification_big_temp_1, subColor);
            views.setTextColor(R.id.notification_big_temp_2, subColor);
            views.setTextColor(R.id.notification_big_temp_3, subColor);
            views.setTextColor(R.id.notification_big_temp_4, subColor);
            views.setTextColor(R.id.notification_big_temp_5, subColor);
        }

        return views;
    }

    public static void cancelNotification(Context context) {
        NotificationManagerCompat.from(context).cancel(WeatherFlow.NOTIFICATION_ID_NORMALLY);
    }

    public static boolean isEnable(Context context) {
        return SettingsOptionManager.getInstance(context).isNotificationEnabled();
    }
}
