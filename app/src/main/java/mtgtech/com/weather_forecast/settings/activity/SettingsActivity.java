package mtgtech.com.weather_forecast.settings.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.settings.fragment.SettingsFragment;
import mtgtech.com.weather_forecast.utils.helpter.IntentHelper;

/**
 * Settings activity.
 * */

public class SettingsActivity extends GeoActivity
        implements Toolbar.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);
        initToolbar();
        SettingsFragment settingsFragment = new SettingsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_settings_container, settingsFragment)
                .commit();
    }

    @Override
    public View getSnackBarContainer() {
        return findViewById(R.id.activity_settings_container);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        // do nothing.
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.activity_settings_toolbar);
        toolbar.setTitle(getString(R.string.action_settings));
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
        toolbar.setNavigationOnClickListener(view -> finish());
        toolbar.inflateMenu(R.menu.activity_settings);
        toolbar.setOnMenuItemClickListener(this);
    }

    public void pushFragment(PreferenceFragmentCompat f, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, 0, android.R.anim.fade_in, 0)
                .replace(R.id.activity_settings_container, f)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_about:
                IntentHelper.startAboutActivity(this);
                break;
        }
        return true;
    }
}