package anaxxes.com.weatherFlow.basic.model.option;

import android.content.Context;

import androidx.annotation.Nullable;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.option.utils.OptionMapper;

public enum WidgetWeekIconMode {
    AUTO("auto"),
    DAY("day"),
    NIGHT("night");

    private String modeId;

    WidgetWeekIconMode(String modeId) {
        this.modeId = modeId;
    }

    @Nullable
    public String getWidgetWeekIconModeName(Context context) {
        return OptionMapper.getNameByValue(
                context,
                modeId,
                R.array.week_icon_modes,
                R.array.week_icon_mode_values
        );
    }
}
