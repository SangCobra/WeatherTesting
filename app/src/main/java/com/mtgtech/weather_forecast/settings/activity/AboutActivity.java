package com.mtgtech.weather_forecast.settings.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.settings.adapter.AboutAdapter;
import com.mtgtech.weather_forecast.weather_model.GeoActivity;

/**
 * About activity.
 */

public class AboutActivity extends GeoActivity
        implements View.OnClickListener {

    private CoordinatorLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
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

    private void initWidget() {
        this.container = findViewById(R.id.activity_about_container);

        Toolbar toolbar = findViewById(R.id.activity_about_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
        toolbar.setTitle(R.string.action_about);
        toolbar.setNavigationOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.activity_about_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AboutAdapter(this));
    }

    // interface.

    // on click listener.

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case -1:
                finish();
                break;
        }
    }
}
