package mtgtech.com.weather_forecast.main.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import mtgtech.com.weather_forecast.weather_model.model.location.Location;
import mtgtech.com.weather_forecast.view.fragment.HomeFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {


    private List<Location> list;

    public MainPagerAdapter(@NonNull FragmentManager fm, List<Location> listOfLocations) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        list = listOfLocations;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("location",list.get(position));
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
