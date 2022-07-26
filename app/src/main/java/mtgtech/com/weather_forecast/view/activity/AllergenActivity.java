package mtgtech.com.weather_forecast.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.db.DatabaseHelper;
import mtgtech.com.weather_forecast.view.adapter.DailyPollenAdapter;
import mtgtech.com.weather_forecast.view.decotarion.ListDecoration;
import mtgtech.com.weather_forecast.view.weather_widget.insets.FitBottomSystemBarRecyclerView;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;

public class AllergenActivity extends GeoActivity {

    public static final String KEY_ALLERGEN_ACTIVITY_LOCATION_FORMATTED_ID = "ALLERGEN_ACTIVITY_LOCATION_FORMATTED_ID";
    private CoordinatorLayout container;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergen);
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
        String formattedId = getIntent().getStringExtra(KEY_ALLERGEN_ACTIVITY_LOCATION_FORMATTED_ID);
        if (!TextUtils.isEmpty(formattedId)) {
            location = DatabaseHelper.getInstance(this).readLocation(formattedId);
        }
        if (location == null) {
            location = DatabaseHelper.getInstance(this).readLocationList().get(0);
        }
        location.setWeather(DatabaseHelper.getInstance(this).readWeather(location));
    }

    private void initWidget() {
        this.container = findViewById(R.id.activity_allergen_container);

        Toolbar toolbar = findViewById(R.id.activity_allergen_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        if (location.getWeather() != null) {
            FitBottomSystemBarRecyclerView recyclerView = findViewById(R.id.activity_allergen_recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new ListDecoration(this));
            recyclerView.setAdapter(new DailyPollenAdapter(location.getWeather()));
        } else {
            finish();
        }
    }
}
