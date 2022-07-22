package mtgtech.com.weather_forecast.main.adapter.main;

import android.animation.Animator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.android.ads.nativetemplates.TemplateView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import mtgtech.com.weather_forecast.WeatherFlow;
import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;
import mtgtech.com.weather_forecast.weather_model.model.option.appearance.CardDisplay;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;
import mtgtech.com.weather_forecast.main.adapter.main.holder.AbstractMainCardViewHolder;
import mtgtech.com.weather_forecast.main.adapter.main.holder.AbstractMainViewHolder;
import mtgtech.com.weather_forecast.main.adapter.main.holder.AirQualityViewHolder;
import mtgtech.com.weather_forecast.main.adapter.main.holder.AllergenViewHolder;
import mtgtech.com.weather_forecast.main.adapter.main.holder.AstroViewHolder;
import mtgtech.com.weather_forecast.main.adapter.main.holder.DailyViewHolder;
import mtgtech.com.weather_forecast.main.adapter.main.holder.DetailsViewHolder;
import mtgtech.com.weather_forecast.main.adapter.main.holder.FooterViewHolder;
import mtgtech.com.weather_forecast.main.adapter.main.holder.HeaderViewHolder;
import mtgtech.com.weather_forecast.main.adapter.main.holder.HourlyViewHolder;
import mtgtech.com.weather_forecast.resource.provider.ResourceProvider;
import mtgtech.com.weather_forecast.settings.SettingsOptionManager;
import mtgtech.com.weather_forecast.utils.PurchaseUtils;

public class MainAdapter extends RecyclerView.Adapter<AbstractMainViewHolder> {

    private @NonNull GeoActivity activity;
    private @NonNull Location location;
    private @NonNull ResourceProvider provider;

    private @NonNull List<Integer> viewTypeList;
    private @Nullable Integer firstCardPosition;
    private @NonNull List<Animator> pendingAnimatorList;
    private int headerCurrentTemperatureTextHeight;
    private boolean listAnimationEnabled;
    private boolean itemAnimationEnabled;

    public MainAdapter(@NonNull GeoActivity activity, @NonNull Location location,
                       @NonNull ResourceProvider provider,
                       boolean listAnimationEnabled, boolean itemAnimationEnabled) {
        reset(activity, location, provider, listAnimationEnabled, itemAnimationEnabled);
    }

    public void reset(@NonNull GeoActivity activity, @NonNull Location location,
                      @NonNull ResourceProvider provider,
                      boolean listAnimationEnabled, boolean itemAnimationEnabled) {
        this.activity = activity;
        this.location = location;
        this.provider = provider;

        this.viewTypeList = new ArrayList<>();
        this.firstCardPosition = null;
        this.pendingAnimatorList = new ArrayList<>();
        this.headerCurrentTemperatureTextHeight = -1;
        this.listAnimationEnabled = listAnimationEnabled;
        this.itemAnimationEnabled = itemAnimationEnabled;

        if (location.getWeather() != null) {
            Weather weather = location.getWeather();
            List<CardDisplay> cardDisplayList = SettingsOptionManager.getInstance(activity).getCardDisplayList();
            viewTypeList.add(ViewType.HEADER);

            for (CardDisplay c : cardDisplayList) {
                if (c == CardDisplay.CARD_AIR_QUALITY
                        && !weather.getCurrent().getAirQuality().isValid()) {
                    continue;
                }
                if (c == CardDisplay.CARD_ALLERGEN
                        && !weather.getDailyForecast().get(0).getPollen().isValid()) {
                    continue;
                }
                if (c == CardDisplay.CARD_SUNRISE_SUNSET
                        && (weather.getDailyForecast().size() == 0
                        || !weather.getDailyForecast().get(0).sun().isValid())) {
                    continue;
                }
                viewTypeList.add(getViewType(c));
            }
            if(!PurchaseUtils.isPurchased(activity)) {
                viewTypeList.add(2, ViewType.AD);
                viewTypeList.add(ViewType.AD);
            }
            viewTypeList.add(ViewType.FOOTER);

            ensureFirstCard();
        }
    }

    public void setNullWeather() {
        this.viewTypeList = new ArrayList<>();
        ensureFirstCard();
    }

    @NonNull
    @Override
    public AbstractMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ViewType.HEADER:
                return new HeaderViewHolder(parent);

            case ViewType.DAILY:
                return new DailyViewHolder(parent);

            case ViewType.HOURLY:
                return new HourlyViewHolder(parent);

            case ViewType.AIR_QUALITY:
                return new AirQualityViewHolder(parent);

            case ViewType.ALLERGEN:
                return new AllergenViewHolder(parent);

            case ViewType.ASTRO:
                return new AstroViewHolder(parent);

            case ViewType.DETAILS:
                return new DetailsViewHolder(parent);

            default: // FOOTER.
                return new FooterViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractMainViewHolder holder, int position) {
        if (holder instanceof AbstractMainCardViewHolder) {
            ((AbstractMainCardViewHolder) holder).onBindView(
                    activity,
                    location,
                    provider,
                    listAnimationEnabled,
                    itemAnimationEnabled,
                    firstCardPosition != null && firstCardPosition == position
            );
        }
        else {
            holder.onBindView(activity, location, provider, listAnimationEnabled, itemAnimationEnabled);
        }
    }

//     class AdViewHolder extends AbstractMainViewHolder {
//         public AdViewHolder(ViewGroup parent) {
//             super(LayoutInflater.from(parent.getContext()).inflate(
//                     R.layout.item_adview, parent, false));
//
//
//         }
//
//         @Override
//         public void onBindView(Context context, @NonNull Location location, @NonNull ResourceProvider provider, boolean listAnimationEnabled, boolean itemAnimationEnabled) {
//             super.onBindView(context, location, provider, listAnimationEnabled, itemAnimationEnabled);
//         }
//
////         TemplateView adTemplateView  = itemView.findViewById(R.id.nativeTemplate);
//    }

    @Override
    public void onViewRecycled(@NonNull AbstractMainViewHolder holder) {
        holder.onRecycleView();
    }

    @Override
    public int getItemCount() {
        return viewTypeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewTypeList.get(position);
    }

    private void ensureFirstCard() {
        firstCardPosition = null;
        for (int i = 0; i < getItemCount(); i ++) {
            int type = getItemViewType(i);
            if (type == ViewType.DAILY
                    || type == ViewType.HOURLY
                    || type == ViewType.AIR_QUALITY
                    || type == ViewType.ALLERGEN
                    || type == ViewType.ASTRO
                    || type == ViewType.DETAILS) {
                firstCardPosition = i;
                return;
            }
        }
    }

    public int getCurrentTemperatureTextHeight(RecyclerView recyclerView) {
        if (headerCurrentTemperatureTextHeight <= 0 && getItemCount() > 0) {
            AbstractMainViewHolder holder = (AbstractMainViewHolder) recyclerView.findViewHolderForAdapterPosition(0);
            if (holder instanceof HeaderViewHolder) {
                headerCurrentTemperatureTextHeight
                        = ((HeaderViewHolder) holder).getCurrentTemperatureHeight();
            }
        }
        return headerCurrentTemperatureTextHeight;
    }

    public void onScroll(RecyclerView recyclerView) {
        AbstractMainViewHolder holder;
        for (int i = 0; i < getItemCount(); i ++) {
            holder = (AbstractMainViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (holder != null && holder.getTop() < recyclerView.getMeasuredHeight()) {
                holder.enterScreen(pendingAnimatorList, listAnimationEnabled);
            }
        }
    }

    private static int getViewType(CardDisplay cardDisplay) {
        switch (cardDisplay) {
            case CARD_DAILY_OVERVIEW:
                return ViewType.DAILY;

            case CARD_HOURLY_OVERVIEW:
                return ViewType.HOURLY;

            case CARD_AIR_QUALITY:
                return ViewType.AIR_QUALITY;

            case CARD_ALLERGEN:
                return ViewType.ALLERGEN;

            case CARD_SUNRISE_SUNSET:
                return ViewType.ASTRO;

            case AD_TYPE:
                return ViewType.AD;

            default: // CARD_LIFE_DETAILS.
                return ViewType.DETAILS;
        }
    }
}
