package com.mtgtech.weather_forecast.main;

import static com.mtgtech.weather_forecast.main.MainActivity.isStartAgain;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.common.control.manager.AdmobManager;

import java.util.ArrayList;

import com.mtgtech.weather_forecast.BuildConfig;
import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.main.adapter.AriQualityAdapter;
import com.mtgtech.weather_forecast.models.AirQualityModel;
import com.mtgtech.weather_forecast.weather_model.GeoActivity;

public class AirQualityActivity extends GeoActivity {
    private RecyclerView recyclerView;
    private ImageView backHome;
    private AriQualityAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ari_quality);
        initView();
        AdmobManager.getInstance().loadBanner(this, BuildConfig.banner_aqi);
    }

    private void initView() {
        recyclerView = findViewById(R.id.rcvAirQuality);
        backHome = findViewById(R.id.backToHome);
        ArrayList<AirQualityModel> listAdapter = new ArrayList<>();
        int[] listColor = {
                R.color.colorLevel_1,
                R.color.colorLevel_2,
                R.color.colorLevel_3,
                R.color.colorLevel_4,
                R.color.colorLevel_5,
                R.color.colorLevel_6
        };
        String[] listAdjust = {
                "Good",
                "Moderate",
                "Unhealthy for Sensitive Groups",
                "Unhealthy",
                "Very Unhealthy",
                "Hazardous"
        };
        String[] listNumber = {
                "0-50",
                "51-100",
                "101-150",
                "151-200",
                "201-300",
                "301+"
        };
        for (int i = 0; i < 6; i++) {
            listAdapter.add(new AirQualityModel(listColor[i], listAdjust[i], listNumber[i]));
        }
        listAdapter.add(2, null);
        adapter = new AriQualityAdapter(this, listAdapter);
        backHome.setOnClickListener(v -> {
            isStartAgain = false;
            finish();

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isStartAgain = false;
    }

    @Override
    public View getSnackBarContainer() {
        return null;
    }
}
