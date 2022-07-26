package mtgtech.com.weather_forecast.remoteviews.config;

import android.view.View;
import android.widget.RemoteViews;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.remoteviews.presenter.TextWidgetIMP;

/**
 * Text widget config activity.
 */

public class TextWidgetConfigActivity extends AbstractWidgetConfigActivity {

    @Override
    public void initView() {
        super.initView();
        viewTypeContainer.setVisibility(View.GONE);
        cardStyleContainer.setVisibility(View.GONE);
        cardAlphaContainer.setVisibility(View.GONE);
        hideSubtitleContainer.setVisibility(View.GONE);
        subtitleDataContainer.setVisibility(View.GONE);
        clockFontContainer.setVisibility(View.GONE);
        hideLunarContainer.setVisibility(View.GONE);
    }

    @Override
    public RemoteViews getRemoteViews() {
        return TextWidgetIMP.getRemoteViews(this, getLocationNow(), textColorValueNow, textSize);
    }

    @Override
    public String getSharedPreferencesName() {
        return getString(R.string.sp_widget_text_setting);
    }
}