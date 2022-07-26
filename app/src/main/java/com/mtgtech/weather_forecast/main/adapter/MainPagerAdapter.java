package com.mtgtech.weather_forecast.main.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import com.mtgtech.weather_forecast.main.MainActivity;
import com.mtgtech.weather_forecast.view.fragment.HomeFragment;
import com.mtgtech.weather_forecast.weather_model.model.location.Location;

public class MainPagerAdapter extends FragmentStatePagerAdapter {


    private List<Location> list;
    private MainActivity.LoadLocation loadLocation;

    public MainPagerAdapter(@NonNull FragmentManager fm, List<Location> listOfLocations, MainActivity.LoadLocation loadLocation) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        list = listOfLocations;
        this.loadLocation = loadLocation;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setLoadLocation(loadLocation);
        Bundle bundle = new Bundle();
        bundle.putParcelable("location", list.get(position));
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
