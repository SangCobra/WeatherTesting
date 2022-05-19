package anaxxes.com.weatherFlow.basic.model.option.appearance;

import android.content.Context;

import androidx.annotation.Nullable;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.option.utils.OptionMapper;

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
