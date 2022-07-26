package mtgtech.com.weather_forecast.weather_model.model.option.appearance;

import android.content.Context;

import androidx.annotation.Nullable;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.model.option.utils.OptionMapper;

public enum UIStyle {
    CIRCULAR("circular"),
    MATERIAL("material");

    private String styleId;

    UIStyle(String styleId) {
        this.styleId = styleId;
    }

    @Nullable
    public String getUIStyleName(Context context) {
        return OptionMapper.getNameByValue(
                context,
                styleId,
                R.array.ui_styles,
                R.array.ui_style_values
        );
    }
}
