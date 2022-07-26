package mtgtech.com.weather_forecast.view.adapter.location;

import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mtgtech.com.weather_forecast.R;

public class AdHolder extends RecyclerView.ViewHolder {
    private FrameLayout frAd;

    public AdHolder(@NonNull View itemView) {
        super(itemView);
        frAd = itemView.findViewById(R.id.fr_ad_native);
    }
}
