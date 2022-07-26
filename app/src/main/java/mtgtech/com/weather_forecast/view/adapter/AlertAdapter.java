package mtgtech.com.weather_forecast.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.List;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.model.weather.Alert;

/**
 * Alert adapter.
 */

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.ViewHolder> {

    private List<Alert> alertList;

    public AlertAdapter(List<Alert> list) {
        this.alertList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alert, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(alertList.get(position).getDescription());
        holder.subtitle.setText(
                DateFormat.getDateTimeInstance(
                        DateFormat.LONG, DateFormat.DEFAULT
                ).format(alertList.get(position).getDate())
        );
        holder.content.setText(alertList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // widget
        TextView title;
        TextView subtitle;
        TextView content;

        ViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.item_alert_title);
            this.subtitle = itemView.findViewById(R.id.item_alert_subtitle);
            this.content = itemView.findViewById(R.id.item_alert_content);
        }
    }
}

