package anaxxes.com.weatherFlow.background.polling.work.worker;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;

import java.util.List;

import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.remoteviews.NotificationUtils;
import anaxxes.com.weatherFlow.remoteviews.WidgetUtils;

public class NormalUpdateWorker extends AsyncUpdateWorker {

    public NormalUpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public void updateView(Context context, Location location) {
        WidgetUtils.updateWidgetIfNecessary(context, location);
        NotificationUtils.updateNotificationIfNecessary(context, location);
    }

    @Override
    public void updateView(Context context, List<Location> locationList) {
        WidgetUtils.updateWidgetIfNecessary(context, locationList);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void handleUpdateResult(SettableFuture<Result> future, boolean failed) {
        future.set(failed ? Result.retry() : Result.success());
    }
}
