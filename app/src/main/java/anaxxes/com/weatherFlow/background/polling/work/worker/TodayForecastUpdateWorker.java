package anaxxes.com.weatherFlow.background.polling.work.worker;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;

import java.util.List;

import anaxxes.com.weatherFlow.background.polling.PollingManager;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.remoteviews.presenter.notification.ForecastNotificationIMP;

public class TodayForecastUpdateWorker extends AsyncUpdateWorker {

    public TodayForecastUpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public void updateView(Context context, Location location) {
        if (ForecastNotificationIMP.isEnable(context, true)) {
            ForecastNotificationIMP.buildForecastAndSendIt(context, location, true);
        }
    }

    @Override
    public void updateView(Context context, List<Location> locationList) {
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void handleUpdateResult(SettableFuture<Result> future, boolean failed) {
        future.set(failed ? Result.failure() : Result.success());
        PollingManager.resetTodayForecastBackgroundTask(
                getApplicationContext(), false, true);
    }
}
