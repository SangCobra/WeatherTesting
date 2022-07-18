package mtgtech.com.weather_forecast.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.UnitUtils;
import mtgtech.com.weather_forecast.weather_model.model.weather.AQIObject;
import mtgtech.com.weather_forecast.settings.SettingsOptionManager;

public class AQIGasAdapter extends RecyclerView.Adapter<AQIGasAdapter.AQIViewHolder> {

    private Context context;
    private ArrayList<AQIObject> list = new ArrayList<>();
    private SettingsOptionManager settingsOptionManager = SettingsOptionManager.getInstance(context);
    private UnitUtils unitUtils;

    public AQIGasAdapter(Context context) {

        this.context = context;
        unitUtils = new UnitUtils();
    }

    @NonNull
    @Override
    public AQIGasAdapter.AQIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AQIGasAdapter.AQIViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aqi_gas, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AQIGasAdapter.AQIViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AQIViewHolder extends RecyclerView.ViewHolder {

        private TextView tvHeading, tvValue;

        public AQIViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHeading = itemView.findViewById(R.id.tvGasName);
            tvValue = itemView.findViewById(R.id.tvGasValue);
        }

        public void setData(AQIObject model) {
            if (model.getValue() != null){
                tvHeading.setText(model.getName());
                tvValue.setText(UnitUtils.formatFloat(model.getValue(), 0));
            }
        }
    }

    public void updateData(ArrayList<AQIObject> newList) {
        list = newList;
        notifyDataSetChanged();
    }
}
