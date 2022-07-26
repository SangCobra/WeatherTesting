package mtgtech.com.weather_forecast.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.models.AirQualityModel;

public class AriQualityAdapter extends RecyclerView.Adapter<AriQualityAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AirQualityModel> list;

    public AriQualityAdapter(Context context, ArrayList<AirQualityModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(ArrayList<AirQualityModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_air_quality_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AirQualityModel airQualityModel = list.get(position);
        holder.setData(airQualityModel);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View colorView;
        private TextView adjustText, numberText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorView = itemView.findViewById(R.id.viewColor);
            adjustText = itemView.findViewById(R.id.adjustAqi);
            numberText = itemView.findViewById(R.id.numberRange);
        }

        @SuppressLint("ResourceAsColor")
        public void setData(AirQualityModel airQualityModel) {
            colorView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, airQualityModel.getColor())));
            adjustText.setText(airQualityModel.getAdjustAir());
            numberText.setText(airQualityModel.getNumberRange());
        }
    }
}
