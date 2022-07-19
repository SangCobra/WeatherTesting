package mtgtech.com.weather_forecast.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import androidx.fragment.app.FragmentTransaction;
import android.view.View;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.main.MainActivity;
import mtgtech.com.weather_forecast.view.fragment.LocationManageFragment;

/**
 * Manage activity.
 * */

public class ManageActivity extends GeoActivity {

    private CoordinatorLayout container;
    private LocationManageFragment manageFragment;

    public static final int SEARCH_ACTIVITY = 1;
    private int requestCode;
    public static final int SELECT_PROVIDER_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manage);

        container = findViewById(R.id.activity_manage_container);

        manageFragment = new LocationManageFragment();
        manageFragment.setDrawerMode(false);
        manageFragment.setRequestCodes(SEARCH_ACTIVITY, SELECT_PROVIDER_ACTIVITY);
        manageFragment.setOnLocationListChangedListener(new LocationManageFragment.LocationManageCallback() {
            @Override
            public void onSelectedLocation(@NonNull String formattedId,int index) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.KEY_MAIN_ACTIVITY_LOCATION_FORMATTED_ID, formattedId);
                intent.putExtra(MainActivity.KEY_LOCATION_INDEX, index);
                intent.putExtra(MainActivity.KEY_RELOAD_WEATHER, 1000);
                setResult(RESULT_OK,intent);
//                setResult(
//                        RESULT_OK,
//                        new Intent().putExtra(MainActivity.KEY_MAIN_ACTIVITY_LOCATION_FORMATTED_ID, formattedId)
//                );
                finish();
            }

            @Override
            public void onLocationListChanged() {
                setResult(RESULT_OK);
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.activity_manage_container, manageFragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SEARCH_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    manageFragment.addLocation();
                    this.requestCode = SEARCH_ACTIVITY;
                }
//                manageFragment.addLocation();
//                manageFragment.resetLocationList();
                break;

            case SELECT_PROVIDER_ACTIVITY:
                manageFragment.resetLocationList();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (requestCode == SEARCH_ACTIVITY){
            Intent intent = new Intent();
            intent.putExtra(MainActivity.KEY_MAIN_ACTIVITY_LOCATION_FORMATTED_ID,manageFragment.readLocationList().get(manageFragment.readLocationList().size()-1).getFormattedId());
            intent.putExtra(MainActivity.KEY_LOCATION_INDEX, manageFragment.readLocationList().size()-1);
            intent.putExtra(MainActivity.KEY_RELOAD_WEATHER, 1000);
            setResult(Activity.RESULT_OK, intent);
        }
        super.onBackPressed();
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
}
