package com.mtgtech.weather_forecast.weather_model.model.option.provider;

import android.content.Context;

import androidx.annotation.Nullable;

import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.weather_model.model.option.utils.OptionMapper;

public enum LocationProvider {

    NATIVE("native");

    private String providerId;

    LocationProvider(String id) {
        providerId = id;
    }

    public String getProviderId() {
        return providerId;
    }

    @Nullable
    public String getProviderName(Context context) {
        return OptionMapper.getNameByValue(
                context,
                providerId,
                R.array.location_services,
                R.array.location_service_values
        );
    }
}
