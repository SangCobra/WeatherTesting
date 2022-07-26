package mtgtech.com.weather_forecast.remoteviews.presenter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.content.ContextCompat;

import java.util.Date;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.WeatherFlow;
import mtgtech.com.weather_forecast.background.receiver.widget.WidgetClockDayDetailsProvider;
import mtgtech.com.weather_forecast.remoteviews.WidgetUtils;
import mtgtech.com.weather_forecast.resource.ResourceHelper;
import mtgtech.com.weather_forecast.resource.provider.ResourceProvider;
import mtgtech.com.weather_forecast.resource.provider.ResourcesProviderFactory;
import mtgtech.com.weather_forecast.settings.SettingsOptionManager;
import mtgtech.com.weather_forecast.utils.helpter.LunarHelper;
import mtgtech.com.weather_forecast.utils.manager.TimeManager;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.RelativeHumidityUnit;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.TemperatureUnit;
import mtgtech.com.weather_forecast.weather_model.model.weather.Temperature;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;

public class ClockDayDetailsWidgetIMP extends AbstractRemoteViewsPresenter {

    public static void updateWidgetView(Context context, Location location) {
        WidgetConfig config = getWidgetConfig(
                context,
                context.getString(R.string.sp_widget_clock_day_details_setting)
        );

        RemoteViews views = getRemoteViews(
                context, location,
                config.cardStyle, config.cardAlpha, config.textColor, config.textSize, config.clockFont,
                config.hideLunar
        );

        AppWidgetManager.getInstance(context).updateAppWidget(
                new ComponentName(context, WidgetClockDayDetailsProvider.class),
                views
        );
    }

    public static RemoteViews getRemoteViews(Context context,
                                             Location location,
                                             String cardStyle, int cardAlpha,
                                             String textColor, int textSize, String clockFont,
                                             boolean hideLunar) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_clock_day_details);
        Weather weather = location.getWeather();
        if (weather == null) {
            return views;
        }

        ResourceProvider provider = ResourcesProviderFactory.getNewInstance();

        boolean dayTime = TimeManager.isDaylight(location);

        SettingsOptionManager settings = SettingsOptionManager.getInstance(context);
        TemperatureUnit temperatureUnit = settings.getTemperatureUnit();
        boolean minimalIcon = settings.isWidgetMinimalIconEnabled();
        boolean touchToRefresh = settings.isWidgetClickToRefreshEnabled();

        WidgetColor color = new WidgetColor(context, dayTime, cardStyle, textColor);

        int textColorInt;
        if (color.darkText) {
            textColorInt = ContextCompat.getColor(context, R.color.colorTextDark);
        } else {
            textColorInt = ContextCompat.getColor(context, R.color.colorTextLight);
        }

        views.setImageViewUri(
                R.id.widget_clock_day_icon,
                ResourceHelper.getWidgetNotificationIconUri(
                        provider,
                        weather.getCurrent().getWeatherCode(),
                        dayTime,
                        minimalIcon,
                        color.darkText
                )
        );

        views.setTextViewText(
                R.id.widget_clock_day_lunar,
                settings.getLanguage().isChinese() && !hideLunar
                        ? (" - " + LunarHelper.getLunarDate(new Date()))
                        : ""
        );

        views.setTextViewText(
                R.id.widget_clock_day_subtitle,
                location.getCityName(context)
                        + " "
                        + weather.getCurrent().getTemperature().getTemperature(context, temperatureUnit)
        );

        views.setTextViewText(
                R.id.widget_clock_day_todayTemp,
                context.getString(R.string.today) + " " + Temperature.getTrendTemperature(
                        context,
                        weather.getDailyForecast().get(0).night().getTemperature().getTemperature(),
                        weather.getDailyForecast().get(0).day().getTemperature().getTemperature(),
                        temperatureUnit
                )
        );

        views.setTextViewText(
                R.id.widget_clock_day_sensibleTemp,
                context.getString(R.string.feels_like)
                        + " "
                        + weather.getCurrent().getTemperature().getRealFeelTemperature(context, temperatureUnit)
        );

        views.setTextViewText(
                R.id.widget_clock_day_aqiHumidity,
                getAQIHumidityTempText(context, weather)
        );

        views.setTextViewText(
                R.id.widget_clock_day_wind,
                context.getString(R.string.wind) + " " + weather.getCurrent().getWind().getLevel()
        );

        views.setTextColor(R.id.widget_clock_day_clock_light, textColorInt);
        views.setTextColor(R.id.widget_clock_day_clock_normal, textColorInt);
        views.setTextColor(R.id.widget_clock_day_clock_black, textColorInt);
        views.setTextColor(R.id.widget_clock_day_clock_aa_light, textColorInt);
        views.setTextColor(R.id.widget_clock_day_clock_aa_normal, textColorInt);
        views.setTextColor(R.id.widget_clock_day_clock_aa_black, textColorInt);
        views.setTextColor(R.id.widget_clock_day_title, textColorInt);
        views.setTextColor(R.id.widget_clock_day_lunar, textColorInt);
        views.setTextColor(R.id.widget_clock_day_subtitle, textColorInt);
        views.setTextColor(R.id.widget_clock_day_todayTemp, textColorInt);
        views.setTextColor(R.id.widget_clock_day_sensibleTemp, textColorInt);
        views.setTextColor(R.id.widget_clock_day_aqiHumidity, textColorInt);
        views.setTextColor(R.id.widget_clock_day_wind, textColorInt);

        if (textSize != 100) {
            float clockSize = context.getResources().getDimensionPixelSize(R.dimen.widget_current_weather_icon_size)
                    * textSize / 100f;
            float clockAASize = context.getResources().getDimensionPixelSize(R.dimen.widget_aa_text_size)
                    * textSize / 100f;
            float contentSize = context.getResources().getDimensionPixelSize(R.dimen.widget_content_text_size)
                    * textSize / 100f;

            views.setTextViewTextSize(R.id.widget_clock_day_clock_light, TypedValue.COMPLEX_UNIT_PX, clockSize);
            views.setTextViewTextSize(R.id.widget_clock_day_clock_normal, TypedValue.COMPLEX_UNIT_PX, clockSize);
            views.setTextViewTextSize(R.id.widget_clock_day_clock_black, TypedValue.COMPLEX_UNIT_PX, clockSize);
            views.setTextViewTextSize(R.id.widget_clock_day_clock_aa_light, TypedValue.COMPLEX_UNIT_PX, clockAASize);
            views.setTextViewTextSize(R.id.widget_clock_day_clock_aa_normal, TypedValue.COMPLEX_UNIT_PX, clockAASize);
            views.setTextViewTextSize(R.id.widget_clock_day_clock_aa_black, TypedValue.COMPLEX_UNIT_PX, clockAASize);
            views.setTextViewTextSize(R.id.widget_clock_day_title, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_clock_day_lunar, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_clock_day_subtitle, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_clock_day_todayTemp, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_clock_day_sensibleTemp, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_clock_day_aqiHumidity, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_clock_day_wind, TypedValue.COMPLEX_UNIT_PX, contentSize);
        }

        if (color.showCard) {
            views.setImageViewResource(
                    R.id.widget_clock_day_card,
                    getCardBackgroundId(context, color.darkCard, cardAlpha)
            );
            views.setViewVisibility(R.id.widget_clock_day_card, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.widget_clock_day_card, View.GONE);
        }

        if (clockFont == null) {
            clockFont = "light";
        }
        switch (clockFont) {
            case "light":
                views.setViewVisibility(R.id.widget_clock_day_clock_lightContainer, View.VISIBLE);
                views.setViewVisibility(R.id.widget_clock_day_clock_normalContainer, View.GONE);
                views.setViewVisibility(R.id.widget_clock_day_clock_blackContainer, View.GONE);
                break;

            case "normal":
                views.setViewVisibility(R.id.widget_clock_day_clock_lightContainer, View.GONE);
                views.setViewVisibility(R.id.widget_clock_day_clock_normalContainer, View.VISIBLE);
                views.setViewVisibility(R.id.widget_clock_day_clock_blackContainer, View.GONE);
                break;

            case "black":
                views.setViewVisibility(R.id.widget_clock_day_clock_lightContainer, View.GONE);
                views.setViewVisibility(R.id.widget_clock_day_clock_normalContainer, View.GONE);
                views.setViewVisibility(R.id.widget_clock_day_clock_blackContainer, View.VISIBLE);
                break;
        }

        setOnClickPendingIntent(context, views, location, touchToRefresh);

        return views;
    }

    public static boolean isEnable(Context context) {
        int[] widgetIds = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(
                        new ComponentName(context, WidgetClockDayDetailsProvider.class)
                );
        return widgetIds != null && widgetIds.length > 0;
    }

    private static String getAQIHumidityTempText(Context context, Weather weather) {
        if (weather.getCurrent().getAirQuality().getAqiIndex() != null
                && weather.getCurrent().getAirQuality().getAqiText() != null) {
            return "AQI "
                    + weather.getCurrent().getAirQuality().getAqiIndex()
                    + " ("
                    + weather.getCurrent().getAirQuality().getAqiText()
                    + ")";
        } else {
            return context.getString(R.string.humidity) + " " + RelativeHumidityUnit.PERCENT.getRelativeHumidityText(
                    WidgetUtils.getNonNullValue(weather.getCurrent().getRelativeHumidity(), 0));
        }
    }

    private static void setOnClickPendingIntent(Context context, RemoteViews views, Location location,
                                                boolean touchToRefresh) {
        // weather.
        if (touchToRefresh) {
            views.setOnClickPendingIntent(
                    R.id.widget_clock_day_weather,
                    getRefreshPendingIntent(
                            context,
                            WeatherFlow.WIDGET_CLOCK_DAY_DETAILS_PENDING_INTENT_CODE_REFRESH
                    )
            );
        } else {
            views.setOnClickPendingIntent(
                    R.id.widget_clock_day_weather,
                    getWeatherPendingIntent(
                            context,
                            location,
                            WeatherFlow.WIDGET_CLOCK_DAY_DETAILS_PENDING_INTENT_CODE_WEATHER
                    )
            );
        }

        // clock.
        views.setOnClickPendingIntent(
                R.id.widget_clock_day_clock_light,
                getAlarmPendingIntent(
                        context,
                        WeatherFlow.WIDGET_CLOCK_DAY_DETAILS_PENDING_INTENT_CODE_CLOCK_LIGHT
                )
        );
        views.setOnClickPendingIntent(
                R.id.widget_clock_day_clock_normal,
                getAlarmPendingIntent(
                        context,
                        WeatherFlow.WIDGET_CLOCK_DAY_DETAILS_PENDING_INTENT_CODE_CLOCK_NORMAL
                )
        );
        views.setOnClickPendingIntent(
                R.id.widget_clock_day_clock_black,
                getAlarmPendingIntent(
                        context,
                        WeatherFlow.WIDGET_CLOCK_DAY_DETAILS_PENDING_INTENT_CODE_CLOCK_BLACK
                )
        );

        // title.
        views.setOnClickPendingIntent(
                R.id.widget_clock_day_title,
                getCalendarPendingIntent(
                        context,
                        WeatherFlow.WIDGET_CLOCK_DAY_DETAILS_PENDING_INTENT_CODE_CALENDAR
                )
        );
    }
}
