package anaxxes.com.weatherFlow.ui.adapter.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.databinding.ItemLocation2Binding;
import anaxxes.com.weatherFlow.databinding.ItemLocationBinding;
import anaxxes.com.weatherFlow.resource.provider.ResourceProvider;
import anaxxes.com.weatherFlow.utils.DisplayUtils;
import anaxxes.com.weatherFlow.utils.manager.ThemeManager;
import anaxxes.com.weatherFlow.utils.manager.TimeManager;

public class LocationHolder extends RecyclerView.ViewHolder {

    private ItemLocation2Binding binding;

    protected LocationModel model;
    private ThemeManager themeManager;
    private int direction;
    private @ColorInt
    int swipeEndColor;

    protected LocationHolder(ItemLocation2Binding binding,
                             LocationAdapter.OnLocationItemClickListener listener) {
        super(binding.container);
        this.binding = binding;
//        this.themeManager = ThemeManager.getInstance(binding.getRoot().getContext());

        binding.container.setOnClickListener(v -> listener.onClick(v, model.location.getFormattedId(),getAdapterPosition()));
    }

    @SuppressLint("SetTextI18n")
    protected void onBindView(Context context, LocationModel model, ResourceProvider resourceProvider) {
        this.model = model;
        direction = 0;
        swipeEndColor = ContextCompat.getColor(context,
                model.location.isCurrentPosition() ? R.color.colorPrimary : R.color.colorTextAlert);

//        if (model.currentPosition) {
//            binding.swipeIconEnd.setImageResource(R.drawable.ic_settings);
//        } else {
//            binding.swipeIconEnd.setImageResource(
//                    model.residentPosition ? R.drawable.ic_tag_off : R.drawable.ic_tag_plus);
//        }

//        binding.item.setBackgroundColor(themeManager.getRootColor(context));

//        binding.residentIcon.setVisibility(model.residentPosition ? View.VISIBLE : View.GONE);

//        if (model.weatherCode != null) {
//            binding.weatherIcon.setVisibility(View.VISIBLE);
//            binding.weatherIcon.setImageDrawable(
//                    resourceProvider.getWeatherIcon(
//                            model.weatherCode,
//                            TimeManager.isDaylight(model.location)
//                    )
//            );
//        } else {
//            binding.weatherIcon.setVisibility(View.GONE);
//        }

//        binding.title.setTextColor(themeManager.getTextTitleColor(context));
        binding.title.setText(model.title);

//        binding.alerts.setTextColor(themeManager.getTextSubtitleColor(context));
//        if (!TextUtils.isEmpty(model.alerts)) {
//            binding.alerts.setVisibility(View.VISIBLE);
//            binding.alerts.setText(model.alerts);
//        } else {
//            binding.alerts.setVisibility(View.GONE);
//        }

//        binding.subtitle.setTextColor(themeManager.getTextContentColor(context));
        binding.geoPosition.setText(model.subtitle);
        String weatherText = "";
        if (model.location.getWeather() == null) {
            binding.temperatureContainer.setVisibility(View.INVISIBLE);

        } else {
            binding.temperatureContainer.setVisibility(View.VISIBLE);
            if (model.location.getWeather().getCurrent() != null){
                weatherText = model.location.getWeather().getCurrent().getWeatherText();
                binding.tvTemperatureLocation.setText(String.valueOf(model.location.getWeather().getCurrent().getTemperature().getTemperature()));

            }
        }
        binding.subtitle.setText(weatherText);

        // binding.geoPosition.setText(model.latitude + ", " + model.longitude
        //         + " - " + model.timeZone.getDisplayName(false, TimeZone.SHORT));

        // source.
//        binding.source.setText("Powered by " + model.weatherSource.getSourceUrl());
//        binding.source.setTextColor(model.weatherSource.getSourceColor());

        drawSwipe(context, 0);
        drawDrag(context, false);
    }

    public void drawDrag(Context context, boolean elevate) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.container.setElevation(DisplayUtils.dpToPx(context, elevate ? 10 : 0));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            int sideMargins = (int) DisplayUtils.dpToPx(context,10);
            params.setMargins(sideMargins,0,sideMargins,sideMargins);

            binding.container.setLayoutParams(params);
        }
    }

    public void drawSwipe(Context context, float dX) {
        if (itemView.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            if (dX < 0 && direction >= 0) {
                direction = -1;
//                binding.container.setBackgroundColor(ContextCompat.getColor(context, R.color.striking_red));
            } else if (dX > 0 && direction <= 0) {
                direction = 1;
//                binding.container.setBackgroundColor(swipeEndColor);
            }

//            binding.container.setTranslationX(0);
//            binding.item.setTranslationX(dX);
//            binding.swipeIconStart.setTranslationX(
//                    (float) Math.max(0.5 * (dX + binding.swipeIconEnd.getMeasuredWidth()), 0));
//            binding.swipeIconEnd.setTranslationX(
//                    (float) Math.min(0.5 * (dX - binding.swipeIconStart.getMeasuredWidth()), 0));
        } else {
            if (dX < 0 && direction >= 0) {
                direction = -1;
//                binding.container.setBackgroundColor(swipeEndColor);
            } else if (dX > 0 && direction <= 0) {
                direction = 1;
//                binding.container.setBackgroundColor(ContextCompat.getColor(context, R.color.striking_red));
            }

//            binding.container.setTranslationX(0);
//            binding.item.setTranslationX(dX);
//            binding.swipeIconStart.setTranslationX(
//                    (float) Math.min(0.5 * (dX - binding.swipeIconStart.getMeasuredWidth()), 0));
//            binding.swipeIconEnd.setTranslationX(
//                    (float) Math.max(0.5 * (dX + binding.swipeIconEnd.getMeasuredWidth()), 0));
        }
    }
}
