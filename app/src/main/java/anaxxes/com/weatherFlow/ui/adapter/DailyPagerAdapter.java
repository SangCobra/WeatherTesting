package anaxxes.com.weatherFlow.ui.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.ui.fragment.DailyDetailsFragment;

public class DailyPagerAdapter extends FragmentStatePagerAdapter {

    private Location location;
    public DailyPagerAdapter(@NonNull FragmentManager fm, Location location) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.location = location;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putParcelable("location",location);
        DailyDetailsFragment fragment = new DailyDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return location.getWeather().getDailyForecast().size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return location.getWeather().getDailyForecast().get(position).getDate("EEE\nd/M");
    }
}
