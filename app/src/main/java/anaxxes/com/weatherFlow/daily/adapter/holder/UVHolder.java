package anaxxes.com.weatherFlow.daily.adapter.holder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.weather.UV;
import anaxxes.com.weatherFlow.daily.adapter.DailyWeatherAdapter;
import anaxxes.com.weatherFlow.daily.adapter.model.DailyUV;

public class UVHolder extends DailyWeatherAdapter.ViewHolder {

    private AppCompatImageView icon;
    private TextView title;

    public UVHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_daily_uv, parent, false));
        icon = itemView.findViewById(R.id.item_weather_daily_uv_icon);
        title = itemView.findViewById(R.id.item_weather_daily_uv_title);
    }

    @Override
    public void onBindView(DailyWeatherAdapter.ViewModel model, int position) {
        Context context = itemView.getContext();
        UV uv = ((DailyUV) model).getUv();

        icon.setSupportImageTintList(ColorStateList.valueOf(uv.getUVColor(context)));
        title.setText(uv.getUVDescription());
    }
}