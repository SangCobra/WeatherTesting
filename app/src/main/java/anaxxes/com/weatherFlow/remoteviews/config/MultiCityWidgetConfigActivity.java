package anaxxes.com.weatherFlow.remoteviews.config;

import android.view.View;
import android.widget.RemoteViews;

import java.util.List;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.db.DatabaseHelper;
import anaxxes.com.weatherFlow.remoteviews.presenter.MultiCityWidgetIMP;

/**
 * Multi city widget config activity.
 * */
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
