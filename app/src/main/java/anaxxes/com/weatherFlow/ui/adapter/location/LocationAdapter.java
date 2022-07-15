package anaxxes.com.weatherFlow.ui.adapter.location;

import static anaxxes.com.weatherFlow.ui.adapter.HourlyForecastAdapter.AD_TYPE;
import static anaxxes.com.weatherFlow.ui.adapter.HourlyForecastAdapter.NON_AD_TYPE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turingtechnologies.materialscrollbar.ICustomAdapter;

import java.util.ArrayList;
import java.util.List;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.basic.model.option.provider.WeatherSource;
import anaxxes.com.weatherFlow.basic.model.option.unit.TemperatureUnit;
import anaxxes.com.weatherFlow.databinding.ItemLocation2Binding;
import anaxxes.com.weatherFlow.resource.provider.ResourceProvider;
import anaxxes.com.weatherFlow.resource.provider.ResourcesProviderFactory;
import anaxxes.com.weatherFlow.settings.SettingsOptionManager;
import anaxxes.com.weatherFlow.utils.manager.ThemeManager;

/**
 * Location adapter.
 * */

public class LocationAdapter extends ListAdapter<LocationModel, RecyclerView.ViewHolder>
        implements ICustomAdapter {

    private final Context context;
    private LocationTouchCallback.OnLocationListChangedListener listenerChange;
    private OnLocationItemClickListener listener = null;
    private boolean isLocationActivity;
    private @NonNull
    final ThemeManager themeManager;
    private @NonNull
    final ResourceProvider resourceProvider;
    private @NonNull
    final WeatherSource defaultSource;
    private @NonNull
    final TemperatureUnit temperatureUnit;
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
    public void setListenerChange(LocationTouchCallback.OnLocationListChangedListener listenerChange){
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
        if (holder instanceof LocationHolder){
            LocationHolder locationHolder = (LocationHolder) holder;
            locationHolder.onBindView(context, getItem(position), resourceProvider, isLocationActivity);
            locationHolder.deleteLocation(getItem(position), this, getLocationList(getCurrentList()), listenerChange);
        }

    }

    public void update(@NonNull List<Location> newList) {
        update(newList, null);
    }

    public void update(@NonNull List<Location> newList, @Nullable String forceUpdateId) {
        List<LocationModel> modelList = new ArrayList<>(newList.size());
        for (Location l : newList) {
            if (l != null){
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

    public interface OnLocationItemClickListener {
        void onClick(View view, String formattedId,int index);
    }

    private void setOnLocationItemClickListener(OnLocationItemClickListener l){
        this.listener = l;
    }

    // I custom adapter.

    @Override
    public String getCustomStringForElement(int element) {
        if (getItemCount() == 0) {
            return "";
        }
        return getItem(element).weatherSource.getSourceUrl();
    }
}