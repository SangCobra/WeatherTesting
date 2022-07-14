package anaxxes.com.weatherFlow.ui.adapter.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.basic.model.option.provider.WeatherSource;
import anaxxes.com.weatherFlow.basic.model.option.unit.TemperatureUnit;
import anaxxes.com.weatherFlow.resource.provider.ResourceProvider;
import anaxxes.com.weatherFlow.resource.provider.ResourcesProviderFactory;
import anaxxes.com.weatherFlow.settings.SettingsOptionManager;
import anaxxes.com.weatherFlow.utils.manager.ThemeManager;

public class SearchLocationAdapter extends RecyclerView.Adapter<SearchLocationAdapter.ViewHolder> {
    private Context context;
    private ArrayList<LocationModel> list;
    private LocationAdapter.OnLocationItemClickListener listener = null;
    private @NonNull
    final ThemeManager themeManager;
    private @NonNull
    final ResourceProvider resourceProvider;
    private @NonNull
    final WeatherSource defaultSource;
    private @NonNull
    final TemperatureUnit temperatureUnit;


    public SearchLocationAdapter(Context context, LocationAdapter.OnLocationItemClickListener l) {
        this.context = context;
        this.listener = l;
        this.themeManager = ThemeManager.getInstance(context);
        this.resourceProvider = ResourcesProviderFactory.getNewInstance();
        this.defaultSource = SettingsOptionManager.getInstance(context).getWeatherSource();
        this.temperatureUnit = SettingsOptionManager.getInstance(context).getTemperatureUnit();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<LocationModel> list) {
        this.list = (ArrayList<LocationModel>) list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_location, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationModel locationModel = list.get(position);
        holder.namePlace.setText(locationModel.location.getCityName(context) + ", " + locationModel.location.getCountry());
        if (position == getItemCount()-1){
            holder.line.setVisibility(View.GONE);
        }
        else holder.line.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(v -> {
            listener.onClick(v, locationModel.location.getFormattedId(), position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView namePlace;
        private View line;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namePlace = itemView.findViewById(R.id.name_place_be_found);
            line = itemView.findViewById(R.id.lineSpace);
        }
    }
    public void update(@NonNull List<Location> newList) {
        update(newList, null);
    }

    public void update(@NonNull List<Location> newList, @Nullable String forceUpdateId) {
        ArrayList<LocationModel> modelList = new ArrayList<>(newList.size());
        for (Location l : newList) {
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
        this.list = modelList;
        notifyDataSetChanged();
    }

//    protected List<Location> moveItem(int from, int to) {
//        List<LocationModel> modelList = new ArrayList<>(getCurrentList());
//        modelList.add(to, modelList.remove(from));
//        submitList(modelList);
//
//        return getLocationList(modelList);
//    }
//
//    @ColorInt
//    public int getItemSourceColor(int position) {
//        if (0 <= position && position < getItemCount()) {
//            return getItem(position).weatherSource.getSourceColor();
//        } else {
//            return Color.TRANSPARENT;
//        }
//    }
//
//    protected List<Location> getLocationList() {
//        return getLocationList(getCurrentList());
//    }
//
//    protected List<Location> getLocationList(List<LocationModel> modelList) {
//        List<Location> locationList = new ArrayList<>(getItemCount());
//        for (LocationModel m : modelList) {
//            locationList.add(m.location);
//        }
//        return locationList;
//    }
}
