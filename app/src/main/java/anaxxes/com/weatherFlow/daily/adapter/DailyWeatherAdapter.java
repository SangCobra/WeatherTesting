package anaxxes.com.weatherFlow.daily.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.option.unit.DurationUnit;
import anaxxes.com.weatherFlow.basic.model.option.unit.PrecipitationUnit;
import anaxxes.com.weatherFlow.basic.model.option.unit.ProbabilityUnit;
import anaxxes.com.weatherFlow.basic.model.option.unit.TemperatureUnit;
import anaxxes.com.weatherFlow.basic.model.weather.Daily;
import anaxxes.com.weatherFlow.basic.model.weather.HalfDay;
import anaxxes.com.weatherFlow.basic.model.weather.Precipitation;
import anaxxes.com.weatherFlow.basic.model.weather.PrecipitationDuration;
import anaxxes.com.weatherFlow.basic.model.weather.PrecipitationProbability;
import anaxxes.com.weatherFlow.basic.model.weather.Temperature;
import anaxxes.com.weatherFlow.daily.adapter.holder.AirQualityHolder;
import anaxxes.com.weatherFlow.daily.adapter.holder.AstroHolder;
import anaxxes.com.weatherFlow.daily.adapter.holder.LargeTitleHolder;
import anaxxes.com.weatherFlow.daily.adapter.holder.LineHolder;
import anaxxes.com.weatherFlow.daily.adapter.holder.MarginHolder;
import anaxxes.com.weatherFlow.daily.adapter.holder.OverviewHolder;
import anaxxes.com.weatherFlow.daily.adapter.holder.PollenHolder;
import anaxxes.com.weatherFlow.daily.adapter.holder.TitleHolder;
import anaxxes.com.weatherFlow.daily.adapter.holder.UVHolder;
import anaxxes.com.weatherFlow.daily.adapter.holder.ValueHolder;
import anaxxes.com.weatherFlow.daily.adapter.holder.WindHolder;
import anaxxes.com.weatherFlow.daily.adapter.model.DailyAirQuality;
import anaxxes.com.weatherFlow.daily.adapter.model.DailyAstro;
import anaxxes.com.weatherFlow.daily.adapter.model.DailyPollen;
import anaxxes.com.weatherFlow.daily.adapter.model.DailyUV;
import anaxxes.com.weatherFlow.daily.adapter.model.DailyWind;
import anaxxes.com.weatherFlow.daily.adapter.model.LargeTitle;
import anaxxes.com.weatherFlow.daily.adapter.model.Line;
import anaxxes.com.weatherFlow.daily.adapter.model.Margin;
import anaxxes.com.weatherFlow.daily.adapter.model.Overview;
import anaxxes.com.weatherFlow.daily.adapter.model.Title;
import anaxxes.com.weatherFlow.daily.adapter.model.Value;
import anaxxes.com.weatherFlow.databinding.ItemWeatherDailyPollenBinding;
import anaxxes.com.weatherFlow.settings.SettingsOptionManager;

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder> {

    private List<ViewModel> modelList;
    private int spanCount;

    public interface ViewModel {
        int getCode();
    }

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void onBindView(ViewModel model, int position);
    }

    public GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            int viewType = getItemViewType(position);
            if (Value.isCode(viewType)) {
                return 1;
            } else {
                return spanCount;
            }
        }
    };

    public DailyWeatherAdapter(Context context, Daily daily, int spanCount) {
        this.modelList = new ArrayList<>();
        this.spanCount = spanCount;

        modelList.add(new LargeTitle(context.getString(R.string.daytime)));
        modelList.add(new Overview(daily.day(), true));
        modelList.add(new DailyWind(daily.day().getWind()));
        modelList.addAll(getHalfDayOptionalModelList(context, daily.day()));

        modelList.add(new Line());
        modelList.add(new LargeTitle(context.getString(R.string.nighttime)));
        modelList.add(new Overview(daily.night(), false));
        modelList.add(new DailyWind(daily.night().getWind()));
        modelList.addAll(getHalfDayOptionalModelList(context, daily.night()));

        modelList.add(new Line());
        modelList.add(new LargeTitle(context.getString(R.string.life_details)));
        modelList.add(new DailyAstro(daily.sun(), daily.moon(), daily.getMoonPhase()));
        if (daily.getAirQuality().isValid()) {
            modelList.add(new Title(R.drawable.weather_haze_mini_xml, context.getString(R.string.air_quality)));
            modelList.add(new DailyAirQuality(daily.getAirQuality()));
        }
        if (daily.getPollen().isValid()) {
            modelList.add(new Title(R.drawable.ic_flower, context.getString(R.string.allergen)));
            modelList.add(new DailyPollen(daily.getPollen()));
        }
        if (daily.getUV().isValid()) {
            modelList.add(new Title(R.drawable.ic_uv, context.getString(R.string.uv_index)));
            modelList.add(new DailyUV(daily.getUV()));
        }
        modelList.add(new Line());
        modelList.add(new Value(
                context.getString(R.string.hours_of_sun),
                DurationUnit.H.getDurationText(context, daily.getHoursOfSun())
        ));
        modelList.add(new Margin());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (LargeTitle.isCode(viewType)) {
            return new LargeTitleHolder(parent);
        } else if (Overview.isCode(viewType)) {
            return new OverviewHolder(parent);
        } else if (Line.isCode(viewType)) {
            return new LineHolder(parent);
        } else if (Margin.isCode(viewType)) {
            return new MarginHolder(parent);
        } else if (Value.isCode(viewType)) {
            return new ValueHolder(parent);
        } else if (Title.isCode(viewType)) {
            return new TitleHolder(parent);
        } else if (DailyAirQuality.isCode(viewType)) {
            return new AirQualityHolder(parent);
        } else if (DailyAstro.isCode(viewType)) {
            return new AstroHolder(parent);
        } else if (DailyPollen.isCode(viewType)) {
            return new PollenHolder(
                    ItemWeatherDailyPollenBinding.inflate(
                            LayoutInflater.from(parent.getContext())
                    )
            );
        } else if (DailyUV.isCode(viewType)) {
            return new UVHolder(parent);
        } else if (DailyWind.isCode(viewType)) {
            return new WindHolder(parent);
        }
        throw new RuntimeException("Invalid viewType.");
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindView(modelList.get(position), position);
    }

    @Override
    public int getItemViewType(int position) {
        return modelList.get(position).getCode();
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    private List<ViewModel> getHalfDayOptionalModelList(Context context, HalfDay halfDay) {
        List<ViewModel> list = new ArrayList<>();
        // temperature.
        Temperature temperature = halfDay.getTemperature();
        TemperatureUnit temperatureUnit = SettingsOptionManager.getInstance(context).getTemperatureUnit();
        if (temperature.isValid()) {
            TemperatureUnit unit = SettingsOptionManager.getInstance(context).getTemperatureUnit();
            int resId;
            if (unit == TemperatureUnit.C) {
                resId = R.drawable.ic_temperature_celsius;
            } else if (unit == TemperatureUnit.F) {
                resId = R.drawable.ic_temperature_fahrenheit;
            } else {
                resId = R.drawable.ic_temperature_kelvin;
            }
            list.add(new Title(resId, context.getString(R.string.temperature)));
            if (temperature.getRealFeelTemperature() != null) {
                list.add(new Value(
                        context.getString(R.string.real_feel_temperature),
                        temperatureUnit.getTemperatureText(context, temperature.getRealFeelTemperature())
                ));
            }
            if (temperature.getRealFeelShaderTemperature() != null) {
                list.add(new Value(
                        context.getString(R.string.real_feel_shader_temperature),
                        temperatureUnit.getTemperatureText(context, temperature.getRealFeelShaderTemperature())
                ));
            }
            if (temperature.getApparentTemperature() != null) {
                list.add(new Value(
                        context.getString(R.string.apparent_temperature),
                        temperatureUnit.getTemperatureText(context, temperature.getApparentTemperature())
                ));
            }
            if (temperature.getWindChillTemperature() != null) {
                list.add(new Value(
                        context.getString(R.string.wind_chill_temperature),
                        temperatureUnit.getTemperatureText(context, temperature.getWindChillTemperature())
                ));
            }
            if (temperature.getWetBulbTemperature() != null) {
                list.add(new Value(
                        context.getString(R.string.wet_bulb_temperature),
                        temperatureUnit.getTemperatureText(context, temperature.getWetBulbTemperature())
                ));
            }
            if (temperature.getDegreeDayTemperature() != null) {
                list.add(new Value(
                        context.getString(R.string.degree_day_temperature),
                        temperatureUnit.getTemperatureText(context, temperature.getDegreeDayTemperature())
                ));
            }
            list.add(new Margin());
        }

        // precipitation.
        Precipitation precipitation = halfDay.getPrecipitation();
        PrecipitationUnit precipitationUnit = SettingsOptionManager.getInstance(context).getPrecipitationUnit();
        if (precipitation.getTotal() != null && precipitation.getTotal() > 0) {
            list.add(new Title(R.drawable.ic_water, context.getString(R.string.precipitation)));
            list.add(new Value(
                    context.getString(R.string.total),
                    precipitationUnit.getPrecipitationText(context, precipitation.getTotal())
            ));
            if (precipitation.getRain() != null && precipitation.getRain() > 0) {
                list.add(new Value(
                        context.getString(R.string.rain),
                        precipitationUnit.getPrecipitationText(context, precipitation.getRain())
                ));
            }
            if (precipitation.getSnow() != null && precipitation.getSnow() > 0) {
                list.add(new Value(
                        context.getString(R.string.snow),
                        precipitationUnit.getPrecipitationText(context, precipitation.getSnow())
                ));
            }
            if (precipitation.getIce() != null && precipitation.getIce() > 0) {
                list.add(new Value(
                        context.getString(R.string.ice),
                        precipitationUnit.getPrecipitationText(context, precipitation.getIce())
                ));
            }
            if (precipitation.getThunderstorm() != null && precipitation.getThunderstorm() > 0) {
                list.add(new Value(
                        context.getString(R.string.thunderstorm),
                        precipitationUnit.getPrecipitationText(context, precipitation.getThunderstorm())
                ));
            }
            list.add(new Margin());
        }

        // precipitation probability.
        PrecipitationProbability probability = halfDay.getPrecipitationProbability();
        if (probability.getTotal() != null && probability.getTotal() > 0) {
            list.add(new Title(R.drawable.ic_water_percent, context.getString(R.string.precipitation_probability)));
            list.add(new Value(
                    context.getString(R.string.total),
                    ProbabilityUnit.PERCENT.getProbabilityText(context, probability.getTotal())
            ));
            if (probability.getRain() != null && probability.getRain() > 0) {
                list.add(new Value(
                        context.getString(R.string.rain),
                        ProbabilityUnit.PERCENT.getProbabilityText(context, probability.getRain())
                ));
            }
            if (probability.getSnow() != null && probability.getSnow() > 0) {
                list.add(new Value(
                        context.getString(R.string.snow),
                        ProbabilityUnit.PERCENT.getProbabilityText(context, probability.getSnow())
                ));
            }
            if (probability.getIce() != null && probability.getIce() > 0) {
                list.add(new Value(
                        context.getString(R.string.ice),
                        ProbabilityUnit.PERCENT.getProbabilityText(context, probability.getIce())
                ));
            }
            if (probability.getThunderstorm() != null && probability.getThunderstorm() > 0) {
                list.add(new Value(
                        context.getString(R.string.thunderstorm),
                        ProbabilityUnit.PERCENT.getProbabilityText(context, probability.getThunderstorm())
                ));
            }
            list.add(new Margin());
        }

        // precipitation duration.
        PrecipitationDuration duration = halfDay.getPrecipitationDuration();
        if (duration.getTotal() != null && duration.getTotal() > 0) {
            list.add(new Title(R.drawable.ic_time, context.getString(R.string.precipitation_duration)));
            list.add(new Value(
                    context.getString(R.string.total),
                    DurationUnit.H.getDurationText(context, duration.getTotal())
            ));
            if (duration.getRain() != null && duration.getRain() > 0) {
                list.add(new Value(
                        context.getString(R.string.rain),
                        DurationUnit.H.getDurationText(context, duration.getRain())
                ));
            }
            if (duration.getSnow() != null && duration.getSnow() > 0) {
                list.add(new Value(
                        context.getString(R.string.snow),
                        DurationUnit.H.getDurationText(context, duration.getSnow())
                ));
            }
            if (duration.getIce() != null && duration.getIce() > 0) {
                list.add(new Value(
                        context.getString(R.string.ice),
                        DurationUnit.H.getDurationText(context, duration.getIce())
                ));
            }
            if (duration.getThunderstorm() != null && duration.getThunderstorm() > 0) {
                list.add(new Value(
                        context.getString(R.string.thunderstorm),
                        DurationUnit.H.getDurationText(context, duration.getThunderstorm())
                ));
            }
            list.add(new Margin());
        }
        return list;
    }
}
