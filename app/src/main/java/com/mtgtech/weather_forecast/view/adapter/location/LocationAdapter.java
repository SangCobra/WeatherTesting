package com.mtgtech.weather_forecast.view.adapter.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.turingtechnologies.materialscrollbar.ICustomAdapter;

import java.util.ArrayList;
import java.util.List;

import com.mtgtech.weather_forecast.databinding.ItemLocation2Binding;
import com.mtgtech.weather_forecast.resource.provider.ResourceProvider;
import com.mtgtech.weather_forecast.resource.provider.ResourcesProviderFactory;
import com.mtgtech.weather_forecast.settings.SettingsOptionManager;
import com.mtgtech.weather_forecast.utils.manager.ThemeManager;
import com.mtgtech.weather_forecast.weather_model.model.location.Location;
import com.mtgtech.weather_forecast.weather_model.model.option.provider.WeatherSource;
import com.mtgtech.weather_forecast.weather_model.model.option.unit.TemperatureUnit;

/**
 * Location adapter.
 */

public class LocationAdapter extends ListAdapter<LocationModel, RecyclerView.ViewHolder>
        implements ICustomAdapter {

    private final Context context;
    private @NonNull
    final ThemeManager themeManager;
    private @NonNull
    final ResourceProvider resourceProvider;
    private @NonNull
    final WeatherSource defaultSource;
    private @NonNull
    final TemperatureUnit temperatureUnit;
    private LocationTouchCallback.OnLocationListChangedListener listenerChange;
    private OnLocationItemClickListener listener = null;
    private boolean isLocationActivity;
    private List<Location> list;

    public LocationAdapter(Context context, List<Location> locationList, OnLocationItemClickListener l, boolean isLocationActivity) {
        super(new DiffUtil.ItemCallback<LocationModel>() {
            @Override
            public boolean areItemsTheSame(@NonNull LocationModel oldItem, @NonNull LocationModel newItem) {
                return oldItem.areItemsTheSame(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull LocationModel oldItem, @NonNull LocationModel newItem) {
                return oldItem.areContentsTheSame(newItem);
            }
        });
        this.context = context;
        this.themeManager = ThemeManager.getInstance(context);
        this.resourceProvider = ResourcesProviderFactory.getNewInstance();
        this.defaultSource = SettingsOptionManager.getInstance(context).getWeatherSource();
        this.temperatureUnit = SettingsOptionManager.getInstance(context).getTemperatureUnit();
        this.isLocationActivity = isLocationActivity;
        setOnLocationItemClickListener(l);
        list = locationList;
        update(list);

    }

    public void setListenerChange(LocationTouchCallback.OnLocationListChangedListener listenerChange) {
        this.listenerChange = listenerChange;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocationHolder(
                ItemLocation2Binding.inflate(LayoutInflater.from(parent.getContext())),
                listener
        );
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LocationHolder) {
            LocationHolder locationHolder = (LocationHolder) holder;
            locationHolder.onBindView(context, getItem(position), resourceProvider, isLocationActivity);
            locationHolder.deleteLocation(getItem(position), this, getLocationList(getCurrentList()), listenerChange);
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void update(@NonNull List<Location> newList) {
        update(newList, null);
        notifyDataSetChanged();
    }

    public void update(@NonNull List<Location> newList, @Nullable String forceUpdateId) {
        List<LocationModel> modelList = new ArrayList<>(newList.size());
        for (Location l : newList) {
            if (l != null) {
                modelList.add(
                        new LocationModel(
                                context,
                                l,
                                temperatureUnit,
                                defaultSource,
                                themeManager.isLightTheme(),
                                l.getFormattedId().equals(forceUpdateId)
                        )
                );
            }
        }
        submitList(modelList);
    }

    protected List<Location> moveItem(int from, int to) {
        List<LocationModel> modelList = new ArrayList<>(getCurrentList());
        modelList.add(to, modelList.remove(from));
        submitList(modelList);

        return getLocationList(modelList);
    }

    @ColorInt
    public int getItemSourceColor(int position) {
        if (0 <= position && position < getItemCount()) {
            return getItem(position).weatherSource.getSourceColor();
        } else {
            return Color.TRANSPARENT;
        }
    }

    protected List<Location> getLocationList() {
        return getLocationList(getCurrentList());
    }

    protected List<Location> getLocationList(List<LocationModel> modelList) {
        List<Location> locationList = new ArrayList<>(getItemCount());
        for (LocationModel m : modelList) {
            locationList.add(m.location);
        }

        return locationList;
    }

    // interface.

    private void setOnLocationItemClickListener(OnLocationItemClickListener l) {
        this.listener = l;
    }

    @Override
    public String getCustomStringForElement(int element) {
        if (getItemCount() == 0) {
            return "";
        }
        return getItem(element).weatherSource.getSourceUrl();
    }

    // I custom adapter.

    public interface OnLocationItemClickListener {
        void onClick(View view, String formattedId, int index);
    }
}