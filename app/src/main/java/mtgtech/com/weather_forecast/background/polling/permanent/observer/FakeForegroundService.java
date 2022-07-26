package mtgtech.com.weather_forecast.background.polling.permanent.observer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.WeatherFlow;

public class FakeForegroundService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    WeatherFlow.NOTIFICATION_CHANNEL_ID_BACKGROUND,
                    WeatherFlow.getNotificationChannelName(
                            this, WeatherFlow.NOTIFICATION_CHANNEL_ID_BACKGROUND),
                    NotificationManager.IMPORTANCE_LOW);
            channel.setShowBadge(false);
            channel.setLightColor(ContextCompat.getColor(this, R.color.colorPrimary));

            NotificationManagerCompat.from(this).createNotificationChannel(channel);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            startForeground(
                    WeatherFlow.NOTIFICATION_ID_RUNNING_IN_BACKGROUND,
                    TimeObserverService.getForegroundNotification(this, false));
        } else {
            startForeground(
                    WeatherFlow.NOTIFICATION_ID_RUNNING_IN_BACKGROUND,
                    TimeObserverService.getForegroundNotification(this, true));
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
