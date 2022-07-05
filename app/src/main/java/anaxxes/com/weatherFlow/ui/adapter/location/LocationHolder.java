package anaxxes.com.weatherFlow.ui.adapter.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import anaxxes.com.weatherFlow.OnActionCallback;
import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.databinding.ItemLocation2Binding;
import anaxxes.com.weatherFlow.databinding.ItemLocationBinding;
import anaxxes.com.weatherFlow.db.DatabaseHelper;
import anaxxes.com.weatherFlow.main.dialog.DeleteDialog;
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
        binding.container.setOnClickListener(v -> listener.onClick(v, model.location.getFormattedId(),getAdapterPosition()));
    }

    @SuppressLint("SetTextI18n")
    protected void onBindView(Context context, LocationModel model, ResourceProvider resourceProvider) {
        this.model = model;
        direction = 0;
        swipeEndColor = ContextCompat.getColor(context,
                model.location.isCurrentPosition() ? R.color.colorPrimary : R.color.colorTextAlert);
        binding.title.setText(model.title);
        binding.geoPosition.setText(model.subtitle);
        String weatherText = "";
        if (model.location.getWeather() == null) {
            binding.temperatureContainer.setVisibility(View.INVISIBLE);

        } else {
            binding.temperatureContainer.setVisibility(View.VISIBLE);
            weatherText = model.location.getWeather().getCurrent().getWeatherText();

        }
        drawSwipe(context, 0);
        drawDrag(context, false);
    }

    @SuppressLint({"NotifyDataSetChanged", "NewApi"})
    public void deleteLocation(LocationModel model, LocationAdapter adapter) {
        binding.deleteLocation.setOnClickListener(v -> {
            DeleteDialog.Companion.start(itemView.getContext(), (key, data) -> {
                if (Objects.equals(key, "delete")){
                    DatabaseHelper.getInstance(itemView.getContext()).deleteLocation(model.location);
                    DatabaseHelper.getInstance(itemView.getContext()).deleteWeather(model.location);
                    adapter.update(DatabaseHelper.getInstance(itemView.getContext()).readLocationList());
                    DatabaseHelper.getInstance(itemView.getContext()).readLocationList().forEach(location -> {
                        DatabaseHelper.getInstance(itemView.getContext()).readWeather(location);
                    });
                }
            });
        });

    }

    public void drawDrag(Context context, boolean elevate) {
        binding.container.setElevation(DisplayUtils.dpToPx(context, elevate ? 10 : 0));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        int sideMargins = (int) DisplayUtils.dpToPx(context,10);
        params.setMargins(sideMargins,0,sideMargins,sideMargins);

        binding.container.setLayoutParams(params);
    }

    public void drawSwipe(Context context, float dX) {
        if (itemView.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            if (dX < 0 && direction >= 0) {
                direction = -1;
                binding.container.setBackgroundColor(ContextCompat.getColor(context, R.color.striking_red));
            } else if (dX > 0 && direction <= 0) {
                direction = 1;
            }
        } else {
            if (dX < 0 && direction >= 0) {
                direction = -1;
            } else if (dX > 0 && direction <= 0) {
                direction = 1;
            }
        }
        binding.container.setTranslationX(0);
    }
}
