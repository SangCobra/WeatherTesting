package com.mtgtech.weather_forecast.background.polling.basic;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.WeatherFlow;
import com.mtgtech.weather_forecast.db.DatabaseHelper;
import com.mtgtech.weather_forecast.weather_model.model.location.Location;
import com.mtgtech.weather_forecast.weather_model.model.weather.Weather;

/**
 * Foreground update service.
 */

public abstract class ForegroundUpdateService extends UpdateService {

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    WeatherFlow.NOTIFICATION_CHANNEL_ID_BACKGROUND,
                    WeatherFlow.getNotificationChannelName(
                            this, WeatherFlow.NOTIFICATION_CHANNEL_ID_BACKGROUND),
                    NotificationManager.IMPORTANCE_MIN
            );
            channel.setShowBadge(false);
            channel.setLightColor(ContextCompat.getColor(this, R.color.colorPrimary));
            NotificationManagerCompat.from(this).createNotificationChannel(channel);
        }

        // version O.
        startForeground(
                getForegroundNotificationId(),
                getForegroundNotification(
                        1,
                        DatabaseHelper.getInstance(this).countLocation()
                ).build()
        );

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // version O.
        stopForeground(true);
        NotificationManagerCompat.from(this).cancel(getForegroundNotificationId());
    }

    @Override
    public void stopService(boolean updateFailed) {
        stopForeground(true);
        NotificationManagerCompat.from(this).cancel(getForegroundNotificationId());
        super.stopService(updateFailed);
    }

    public NotificationCompat.Builder getForegroundNotification(int index, int total) {
        return new NotificationCompat.Builder(this, WeatherFlow.NOTIFICATION_CHANNEL_ID_BACKGROUND)
                .setSmallIcon(R.drawable.icon_app)
                .setContentTitle(getString(R.string.weather_flow))
                .setContentText(getString(R.string.feedback_updating_weather_data) + " (" + index + "/" + total + ")")
                .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setProgress(0, 0, true)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setAutoCancel(false)
                .setOngoing(false);
    }

    public abstract int getForegroundNotificationId();

    @Override
    public void onUpdateCompleted(@NonNull Location location, @Nullable Weather old,
                                  boolean succeed, int index, int total) {
        super.onUpdateCompleted(location, old, succeed, index, total);
        if (index + 1 != total) {
            NotificationManagerCompat.from(this).notify(
                    getForegroundNotificationId(),
                    getForegroundNotification(index + 2, total).build()
            );
        }
    }
}
