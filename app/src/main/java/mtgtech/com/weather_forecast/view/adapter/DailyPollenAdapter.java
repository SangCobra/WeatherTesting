package mtgtech.com.weather_forecast.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.databinding.ItemPollenDailyBinding;
import mtgtech.com.weather_forecast.weather_model.model.option.unit.PollenUnit;
import mtgtech.com.weather_forecast.weather_model.model.weather.Daily;
import mtgtech.com.weather_forecast.weather_model.model.weather.Pollen;
import mtgtech.com.weather_forecast.weather_model.model.weather.Weather;

public class DailyPollenAdapter extends RecyclerView.Adapter<DailyPollenAdapter.ViewHolder> {

    private Weather weather;
    private PollenUnit unit;

    public DailyPollenAdapter(Weather weather) {
        this.weather = weather;
        this.unit = PollenUnit.PPCM;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemPollenDailyBinding.inflate(
                        LayoutInflater.from(parent.getContext())
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindView(weather.getDailyForecast().get(position));
    }

    @Override
    public int getItemCount() {
        return weather.getDailyForecast().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemPollenDailyBinding binding;

        ViewHolder(ItemPollenDailyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint({"SetTextI18n", "RestrictedApi"})
        void onBindView(Daily daily) {
            Context context = itemView.getContext();
            Pollen pollen = daily.getPollen();

            binding.title.setText(daily.getDate(context.getString(R.string.date_format_widget_long)));
            binding.subtitle.setText(daily.day().getWeatherText() + " / " + daily.night().getWeatherText());

            binding.grassIcon.setSupportImageTintList(ColorStateList.valueOf(
                    Pollen.getPollenColor(itemView.getContext(), pollen.getGrassLevel())
            ));
            binding.grassTitle.setText(context.getString(R.string.grass));
            binding.grassValue.setText(unit.getPollenText(context, pollen.getGrassIndex())
                    + " - " + pollen.getGrassDescription());

            binding.ragweedIcon.setSupportImageTintList(ColorStateList.valueOf(
                    Pollen.getPollenColor(itemView.getContext(), pollen.getRagweedLevel())
            ));
            binding.ragweedTitle.setText(context.getString(R.string.ragweed));
            binding.ragweedValue.setText(unit.getPollenText(context, pollen.getRagweedIndex())
                    + " - " + pollen.getRagweedDescription());

            binding.treeIcon.setSupportImageTintList(ColorStateList.valueOf(
                    Pollen.getPollenColor(itemView.getContext(), pollen.getTreeLevel())
            ));
            binding.treeTitle.setText(context.getString(R.string.tree));
            binding.treeValue.setText(unit.getPollenText(context, pollen.getTreeIndex())
                    + " - " + pollen.getTreeDescription());

            binding.moldIcon.setSupportImageTintList(ColorStateList.valueOf(
                    Pollen.getPollenColor(itemView.getContext(), pollen.getMoldLevel())
            ));
            binding.moldTitle.setText(context.getString(R.string.mold));
            binding.moldValue.setText(unit.getPollenText(context, pollen.getMoldIndex())
                    + " - " + pollen.getMoldDescription());
        }
    }
}
