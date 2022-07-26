package com.mtgtech.weather_forecast.view.adapter.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.weather_forecast.json.accu.search.Search;

public class SearchLocationAdapter extends RecyclerView.Adapter<SearchLocationAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Search> list;
    private LocationAdapter.OnLocationItemClickListener l;
    private OnSearchLocationItemClickListener listener;

    public SearchLocationAdapter(Context context, OnSearchLocationItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setList(ArrayList<Search> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public LocationAdapter.OnLocationItemClickListener getL() {
        return l;
    }

    public void setL(LocationAdapter.OnLocationItemClickListener l) {
        this.l = l;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_location, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list.size() > 0) {
            return list.size();
        } else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView locationName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.name_place_be_found);
        }

        @SuppressLint("SetTextI18n")
        public void setData(Search search) {
            locationName.setText(search.getLocalizedName() + ", " + search.getCountry().getLocalizedName());
            itemView.setOnClickListener(v -> {
                listener.onClick(search.getLocalizedName(), getAdapterPosition());
//                Location location = ((LocationHolder) viewHolder).model.location;
//                if (location.isCurrentPosition()) {
//                    adapter.update(
//                            adapter.getLocationList(),
//                            location.getFormattedId()
//                    );
//                    if (listener != null) {
//                        listener.onSelectProviderActivityStarted();
//                    }
//                } else {
//                    List<Location> list = adapter.getLocationList();
//                    location = list.get(viewHolder.getAdapterPosition());
//                    location.setResidentPosition(!location.isResidentPosition());
//                    adapter.update(list, location.getFormattedId());
////                    if (location.isResidentPosition()) {
////                        SnackbarUtils.showSnackbar(
////                                activity,
////                                activity.getString(R.string.feedback_resident_location),
////                                activity.getString(R.string.learn_more),
////                                v -> new LearnMoreAboutResidentLocationDialog().show(
////                                        activity.getSupportFragmentManager(), null)
////                        );
////                    }
//
//                    if (listener != null) {
//                        listener.onLocationChanged(list, location);
//                    }
//                }
            });
        }
    }
}
