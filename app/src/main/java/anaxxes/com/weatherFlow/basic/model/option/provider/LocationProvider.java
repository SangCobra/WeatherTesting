package anaxxes.com.weatherFlow.basic.model.option.provider;

import android.content.Context;

import androidx.annotation.Nullable;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.option.utils.OptionMapper;

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
