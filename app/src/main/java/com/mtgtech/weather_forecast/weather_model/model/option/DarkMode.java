package com.mtgtech.weather_forecast.weather_model.model.option;

import android.content.Context;

import androidx.annotation.Nullable;

import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.weather_model.model.option.utils.OptionMapper;

public enum DarkMode {
    AUTO("auto"),
    LIGHT("light"),
    DARK("dark");

    private String modeId;

    DarkMode(String modeId) {
        this.modeId = modeId;
    }

    @Nullable
    public String getDarkModeName(Context context) {
        return OptionMapper.getNameByValue(
                context,
                modeId,
                R.array.dark_modes,
                R.array.dark_mode_values
        );
    }
}
