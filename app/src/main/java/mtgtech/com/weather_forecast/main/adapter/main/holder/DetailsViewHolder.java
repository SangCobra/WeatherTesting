package mtgtech.com.weather_forecast.main.adapter.main.holder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.main.adapter.DetailsAdapter;
import mtgtech.com.weather_forecast.resource.provider.ResourceProvider;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;

public class DetailsViewHolder extends AbstractMainCardViewHolder {

    private CardView card;

    private TextView title;
    private RecyclerView detailsRecyclerView;

    public DetailsViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.container_main_details, parent, false));

        this.card = itemView.findViewById(R.id.container_main_details);
        this.title = itemView.findViewById(R.id.container_main_details_title);
        this.detailsRecyclerView = itemView.findViewById(R.id.container_main_details_recyclerView);
    }

    @Override
    public void onBindView(GeoActivity activity, @NonNull Location location, @NonNull ResourceProvider provider,
                           boolean listAnimationEnabled, boolean itemAnimationEnabled, boolean firstCard) {
        super.onBindView(activity, location, provider,
                listAnimationEnabled, itemAnimationEnabled, firstCard);

        if (location.getWeather() != null) {
            card.setCardBackgroundColor(themeManager.getRootColor(context));

            title.setTextColor(themeManager.getWeatherThemeColors()[0]);

            detailsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            detailsRecyclerView.setAdapter(new DetailsAdapter(context, location.getWeather()));
        }
    }
}
