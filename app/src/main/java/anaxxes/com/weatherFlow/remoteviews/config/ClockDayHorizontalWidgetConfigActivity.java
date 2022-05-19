package anaxxes.com.weatherFlow.remoteviews.config;

import android.view.View;
import android.widget.RemoteViews;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.remoteviews.presenter.ClockDayHorizontalWidgetIMP;

/**
 * Clock day horizontal widget config activity.
 * */

public class ClockDayHorizontalWidgetConfigActivity extends AbstractWidgetConfigActivity {

    @Override
    public void initData() {
        super.initData();

        String[] clockFonts = getResources().getStringArray(R.array.clock_font);
        String[] clockFontValues = getResources().getStringArray(R.array.clock_font_values);

        this.clockFontValueNow = "light";
        this.clockFonts = new String[] {clockFonts[0], clockFonts[1], clockFonts[2]};
        this.clockFontValues = new String[] {clockFontValues[0], clockFontValues[1], clockFontValues[2]};
    }

    @Override
    public void initView() {
        super.initView();
        viewTypeContainer.setVisibility(View.GONE);
        hideSubtitleContainer.setVisibility(View.GONE);
        subtitleDataContainer.setVisibility(View.GONE);
    }

    @Override
    public RemoteViews getRemoteViews() {
        return ClockDayHorizontalWidgetIMP.getRemoteViews(
                this,
                getLocationNow(),
                cardStyleValueNow, cardAlpha,
                textColorValueNow, textSize, clockFontValueNow, hideLunar
        );
    }

    @Override
    public String getSharedPreferencesName() {
        return getString(R.string.sp_widget_clock_day_horizontal_setting);
    }
}