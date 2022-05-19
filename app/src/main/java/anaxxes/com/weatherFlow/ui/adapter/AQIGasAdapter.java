package anaxxes.com.weatherFlow.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.models.AQIGasModel;
import anaxxes.com.weatherFlow.models.TodayForecastModel;

public class AQIGasAdapter extends RecyclerView.Adapter<AQIGasAdapter.AQIViewHolder> {

    private Context context;
    private ArrayList<AQIGasModel> list = new ArrayList<>();

    public AQIGasAdapter(Context context) {
        this.context = context;
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

        public void setData(AQIGasModel model) {

            tvHeading.setText(model.getHeading());
            tvValue.setText(model.getValue());
        }
    }

    public void updateData(ArrayList<AQIGasModel> newList) {
        list = newList;
        notifyDataSetChanged();
    }
}
