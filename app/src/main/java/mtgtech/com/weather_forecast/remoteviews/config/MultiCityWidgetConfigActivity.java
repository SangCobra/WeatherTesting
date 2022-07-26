package mtgtech.com.weather_forecast.remoteviews.config;

import android.view.View;
import android.widget.RemoteViews;

import java.util.List;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.db.DatabaseHelper;
import mtgtech.com.weather_forecast.remoteviews.presenter.MultiCityWidgetIMP;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;

/**
 * Multi city widget config activity.
 */
public class MultiCityWidgetConfigActivity extends AbstractWidgetConfigActivity {

    private List<Location> locationList;

    @Override
    public void initData() {
        super.initData();
        locationList = DatabaseHelper.getInstance(this).readLocationList();
        for (Location l : locationList) {
            l.setWeather(DatabaseHelper.getInstance(this).readWeather(l));
        }
    }

    @Override
    public void initView() {
        super.initView();
        viewTypeContainer.setVisibility(View.GONE);
        hideSubtitleContainer.setVisibility(View.GONE);
        subtitleDataContainer.setVisibility(View.GONE);
        clockFontContainer.setVisibility(View.GONE);
        hideLunarContainer.setVisibility(View.GONE);
    }

    @Override
    public RemoteViews getRemoteViews() {
        return MultiCityWidgetIMP.getRemoteViews(
                this,
                locationList,
                cardStyleValueNow, cardAlpha,
                textColorValueNow, textSize
        );
    }

    @Override
    public String getSharedPreferencesName() {
        return getString(R.string.sp_widget_multi_city);
    }
}
