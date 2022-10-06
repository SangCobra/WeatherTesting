package com.mtgtech.weather_forecast.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.common.control.manager.AdmobManager;
import com.mtgtech.weather_forecast.BuildConfig;
import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.daily_weather.adapter.holder.DailyListAdapter;
import com.mtgtech.weather_forecast.models.AirQualityModel;

public class AriQualityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_AD = 16;
    private static final int TYPE_ITEM = 11;
    private Context context;
    private ArrayList<AirQualityModel> list;

    public AriQualityAdapter(Context context, ArrayList<AirQualityModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null)
            return TYPE_AD;
        return TYPE_ITEM;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(ArrayList<AirQualityModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_AD){
            return new AdViewHolder(LayoutInflater.from(context).inflate(R.layout.item_native, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_air_quality_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            AirQualityModel airQualityModel = list.get(position);
            ((ViewHolder) holder).setData(airQualityModel);
        }

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
    public class AdViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout frAd;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            frAd = itemView.findViewById(R.id.fr_ad_native);
            frAd.setBackgroundColor(Color.parseColor("#3B3B3B"));
            AdmobManager.getInstance().loadNative(context, BuildConfig.native_daily_weather, frAd, R.layout.custom_native_1);
        }
    }
}
