package anaxxes.com.weatherFlow.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.GeoActivity;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.db.DatabaseHelper;
import anaxxes.com.weatherFlow.ui.adapter.DailyPollenAdapter;
import anaxxes.com.weatherFlow.ui.decotarion.ListDecoration;
import anaxxes.com.weatherFlow.ui.widget.insets.FitBottomSystemBarRecyclerView;

public class AllergenActivity extends GeoActivity {
    
    private CoordinatorLayout container;

    private Location location;
    public static final String KEY_ALLERGEN_ACTIVITY_LOCATION_FORMATTED_ID = "ALLERGEN_ACTIVITY_LOCATION_FORMATTED_ID";

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
