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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.models.TodayForecastModel;

public class TodayForecastAdapter extends RecyclerView.Adapter<TodayForecastAdapter.TodayForecastViewHolder> {

    private Context context;
    private ArrayList<TodayForecastModel> list = new ArrayList<>();

    public TodayForecastAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TodayForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TodayForecastViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_today_forcast, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TodayForecastViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TodayForecastViewHolder extends RecyclerView.ViewHolder {

        private ImageView todayImage;
        private TextView tvHeading, tvValue;

        public TodayForecastViewHolder(@NonNull View itemView) {
            super(itemView);


        }

        public void setData(TodayForecastModel model) {

            todayImage = itemView.findViewById(R.id.imgToday);
            tvHeading = itemView.findViewById(R.id.tvTodayHeading);
            tvValue = itemView.findViewById(R.id.tvTodayValue);
            todayImage.setImageDrawable(ContextCompat.getDrawable(context,model.getTodayForecastImage()));
            tvHeading.setText(model.getTodayForecastHeading());
            tvValue.setText(model.getTodayForecastValue());
        }
    }

    public void updateData(ArrayList<TodayForecastModel> newList) {
        list = newList;
        notifyDataSetChanged();
    }
}
