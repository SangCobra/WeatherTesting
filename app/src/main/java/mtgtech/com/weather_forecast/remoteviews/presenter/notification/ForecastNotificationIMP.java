package mtgtech.com.weather_forecast.remoteviews.presenter.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import mtgtech.com.weather_forecast.WeatherFlow;
import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.TemperatureUnit;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;
import mtgtech.com.weather_forecast.weather_model.model.weather.WeatherCode;
import mtgtech.com.weather_forecast.remoteviews.presenter.AbstractRemoteViewsPresenter;
import mtgtech.com.weather_forecast.resource.ResourceHelper;
import mtgtech.com.weather_forecast.resource.provider.ResourceProvider;
import mtgtech.com.weather_forecast.resource.provider.ResourcesProviderFactory;
import mtgtech.com.weather_forecast.settings.SettingsOptionManager;
import mtgtech.com.weather_forecast.view.weather_widget.weatherView.WeatherViewController;
import mtgtech.com.weather_forecast.utils.LanguageUtils;
import mtgtech.com.weather_forecast.utils.manager.TimeManager;

/**
 * Forecast notification utils.
 * */

public class ForecastNotificationIMP extends AbstractRemoteViewsPresenter {

    public static void buildForecastAndSendIt(Context context, Location location, boolean today) {
        Weather weather = location.getWeather();
        if (weather == null) {
            return;
        }

        ResourceProvider provider = ResourcesProviderFactory.getNewInstance();

        LanguageUtils.setLanguage(
                context,
                SettingsOptionManager.getInstance(context).getLanguage().getLocale()
        );
        
        // create channel.
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    WeatherFlow.NOTIFICATION_CHANNEL_ID_FORECAST,
                    WeatherFlow.getNotificationChannelName(
                            context, WeatherFlow.NOTIFICATION_CHANNEL_ID_FORECAST),
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }

        // get builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context, WeatherFlow.NOTIFICATION_CHANNEL_ID_FORECAST);

        // set notification level.
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        // set notification visibility.
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        WeatherCode weatherCode;
        boolean daytime;
        if (today) {
            daytime = TimeManager.isDaylight(location);
            weatherCode = daytime 
                    ? weather.getDailyForecast().get(0).day().getWeatherCode() 
                    : weather.getDailyForecast().get(0).night().getWeatherCode();
        } else {
            daytime = true;
            weatherCode = weather.getDailyForecast().get(1).day().getWeatherCode() ;
        }

        // set small icon.
        builder.setSmallIcon(
                ResourceHelper.getDefaultMinimalXmlIconId(weatherCode, daytime));

        // large icon.
        builder.setLargeIcon(
                drawableToBitmap(
                        ResourceHelper.getWeatherIcon(provider, weatherCode, daytime)
                )
        );

        // sub text.
        if (today) {
            builder.setSubText(context.getString(R.string.today));
        } else {
            builder.setSubText(context.getString(R.string.tomorrow));
        }

        TemperatureUnit temperatureUnit = SettingsOptionManager.getInstance(context).getTemperatureUnit();

        // title and content.
        if (today) {
            builder.setContentTitle(context.getString(R.string.dayTime)
                    + " " + weather.getDailyForecast().get(0).day().getWeatherText()
                    + " " + weather.getDailyForecast().get(0).day().getTemperature().getTemperature(context, temperatureUnit)
            ).setContentText(context.getString(R.string.nightTime)
                    + " " + weather.getDailyForecast().get(0).night().getWeatherText()
                    + " " + weather.getDailyForecast().get(0).night().getTemperature().getTemperature(context, temperatureUnit)
            );
        } else {
            builder.setContentTitle(context.getString(R.string.dayTime)
                    + " " + weather.getDailyForecast().get(1).day().getWeatherText()
                    + " " + weather.getDailyForecast().get(0).day().getTemperature().getTemperature(context, temperatureUnit)
            ).setContentText(context.getString(R.string.nightTime)
                    + " " + weather.getDailyForecast().get(1).night().getWeatherText()
                    + " " + weather.getDailyForecast().get(1).night().getTemperature().getTemperature(context, temperatureUnit)
            );
        }

        builder.setColor(WeatherViewController.getThemeColors(context, weather, daytime)[0]);

        // set intent.
        builder.setContentIntent(
                getWeatherPendingIntent(
                        context,
                        null,
                        today
                                ? WeatherFlow.NOTIFICATION_ID_TODAY_FORECAST
                                : WeatherFlow.NOTIFICATION_ID_TOMORROW_FORECAST
                )
        );

        // set sound & vibrate.
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setAutoCancel(true);

        // set badge.
        builder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);

        Notification notification = builder.build();
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            try {
                notification.getClass()
                        .getMethod("setSmallIcon", Icon.class)
                        .invoke(
                                notification,
                                ResourceHelper.getMinimalIcon(
                                        provider, weather.getCurrent().getWeatherCode(), daytime)
                        );
            } catch (Exception ignore) {
                // do nothing.
            }
        }

        // commit.
        manager.notify(
                today
                        ? WeatherFlow.NOTIFICATION_ID_TODAY_FORECAST
                        : WeatherFlow.NOTIFICATION_ID_TOMORROW_FORECAST,
                notification
        );
    }

    public static boolean isEnable(Context context, boolean today) {
        if (today) {
            return SettingsOptionManager.getInstance(context).isTodayForecastEnabled();
        } else {
            return SettingsOptionManager.getInstance(context).isTomorrowForecastEnabled();
        }
    }
}
