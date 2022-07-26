package mtgtech.com.weather_forecast.weather_model.model.option;

import android.content.Context;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.model.option.utils.OptionMapper;

public enum NotificationTextColor {

    DARK("dark", R.color.colorTextDark, R.color.colorTextDark2nd),
    GREY("grey", R.color.colorTextGrey, R.color.colorTextGrey2nd),
    LIGHT("light", R.color.colorTextLight, R.color.colorTextLight2nd);

    private String colorId;
    @ColorRes
    private int mainTextColorResId;
    @ColorRes
    private int subTextColorResId;

    NotificationTextColor(String colorId, int mainTextColorResId, int subTextColorResId) {
        this.colorId = colorId;
        this.mainTextColorResId = mainTextColorResId;
        this.subTextColorResId = subTextColorResId;
    }

    @Nullable
    public String getNotificationTextColorName(Context context) {
        return OptionMapper.getNameByValue(
                context,
                colorId,
                R.array.notification_text_colors,
                R.array.notification_text_color_values
        );
    }

    @ColorRes
    public int getMainTextColorResId() {
        return mainTextColorResId;
    }

    @ColorRes
    public int getSubTextColorResId() {
        return subTextColorResId;
    }
}
