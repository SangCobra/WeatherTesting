package anaxxes.com.weatherFlow.resource.provider;

import android.animation.Animator;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;

import anaxxes.com.weatherFlow.WeatherFlow;
import anaxxes.com.weatherFlow.basic.model.weather.WeatherCode;
import anaxxes.com.weatherFlow.resource.Constants;
import anaxxes.com.weatherFlow.resource.ResourceUtils;
import anaxxes.com.weatherFlow.ui.image.pixel.PixelMoonDrawable;
import anaxxes.com.weatherFlow.ui.image.pixel.PixelSunDrawable;

public class PixelResourcesProvider extends IconPackResourcesProvider {

    PixelResourcesProvider(@NonNull ResourceProvider defaultProvider) {
        super(
                WeatherFlow.getInstance(),
                WeatherFlow.getInstance().getPackageName(),
                defaultProvider
        );
    }

    static boolean isPixelIconProvider(@NonNull String packageName) {
        return packageName.equals(
                WeatherFlow.getInstance().getPackageName() + ".Pixel"
        );
    }

    @NonNull
    protected Uri getDrawableUri(String resName) {
        return ResourceUtils.getDrawableUri(super.getPackageName(), "drawable", resName);
    }

    @Override
    public String getPackageName() {
        return super.getPackageName() + ".Pixel";
    }

    @Override
    public String getProviderName() {
        return "Pixel";
    }

    @Override
    public Drawable getProviderIcon() {
        return getWeatherIcon(WeatherCode.PARTLY_CLOUDY, true);
    }

    // weather icon.

    @Override
    @Size(3)
    public Drawable[] getWeatherIcons(WeatherCode code, boolean dayTime) {
        return new Drawable[] {getWeatherIcon(code, dayTime), null, null};
    }

    @Override
    String getWeatherIconName(WeatherCode code, boolean daytime) {
        return super.getWeatherIconName(code, daytime) + Constants.SEPARATOR + "pixel";
    }

    @Override
    String getWeatherIconName(WeatherCode code, boolean daytime,
                                     @IntRange(from = 1, to = 3) int index) {
        if (index == 1) {
            return getWeatherIconName(code, daytime);
        } else {
            return null;
        }
    }

    // animator.

    @Override
    @Size(3)
    public Animator[] getWeatherAnimators(WeatherCode code, boolean dayTime) {
        return new Animator[] {null, null, null};
    }

    @Override
    String getWeatherAnimatorName(WeatherCode code, boolean daytime,
                                  @IntRange(from = 1, to = 3) int index) {
        return null;
    }

    // sun and moon.

    @NonNull
    public Drawable getSunDrawable() {
        return new PixelSunDrawable();
    }

    @NonNull
    public Drawable getMoonDrawable() {
        return new PixelMoonDrawable();
    }

    @Override
    @Nullable
    String getSunDrawableClassName() {
        return PixelSunDrawable.class.toString();
    }

    @Override
    @Nullable
    String getMoonDrawableClassName() {
        return PixelMoonDrawable.class.toString();
    }
}
