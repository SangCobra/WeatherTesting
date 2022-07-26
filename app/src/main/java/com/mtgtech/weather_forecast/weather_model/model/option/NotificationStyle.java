package com.mtgtech.weather_forecast.weather_model.model.option;

import android.content.Context;

import androidx.annotation.Nullable;

import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.weather_model.model.option.utils.OptionMapper;

public enum NotificationStyle {
    NATIVE("native"),
    CUSTOM("geometric");

    private String styleId;

    NotificationStyle(String styleId) {
        this.styleId = styleId;
    }

    @Nullable
    public String getNotificationStyleName(Context context) {
        return OptionMapper.getNameByValue(
                context,
                styleId,
                R.array.notification_styles,
                R.array.notification_style_values
        );
    }
}
