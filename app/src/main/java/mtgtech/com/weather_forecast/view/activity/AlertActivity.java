package mtgtech.com.weather_forecast.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import java.util.List;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.weather_model.model.weather.Alert;
import mtgtech.com.weather_forecast.view.adapter.AlertAdapter;
import mtgtech.com.weather_forecast.view.decotarion.ListDecoration;

/**
 * Alert activity.
 * */

public class AlertActivity extends GeoActivity {

    private CoordinatorLayout container;

    private List<Alert> alarmList;
    public static final String KEY_ALERT_ACTIVITY_ALERT_LIST = "ALERT_ACTIVITY_ALERT_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        initData();
        initWidget();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // do nothing.
    }

    @Override
    public View getSnackBarContainer() {
        return container;
    }

    private void initData() {
        this.alarmList = getIntent().getParcelableArrayListExtra(KEY_ALERT_ACTIVITY_ALERT_LIST);
    }

    private void initWidget() {
        this.container = findViewById(R.id.activity_alert_container);

        Toolbar toolbar = findViewById(R.id.activity_alert_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.activity_alert_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new ListDecoration(this));
        recyclerView.setAdapter(new AlertAdapter(alarmList));
    }
}