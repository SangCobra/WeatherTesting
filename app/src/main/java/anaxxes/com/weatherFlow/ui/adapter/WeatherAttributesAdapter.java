package anaxxes.com.weatherFlow.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import anaxxes.com.weatherFlow.R;

public class WeatherAttributesAdapter extends RecyclerView.Adapter<WeatherAttributesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> list;

    public WeatherAttributesAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_attribute_text, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = list.get(position);
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvAttributesWeather);
        }
    }
}
