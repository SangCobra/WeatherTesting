package mtgtech.com.weather_forecast.main.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import mtgtech.com.weather_forecast.weather_model.model.weather.Daily;

public class DailyPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Daily> dailyWeatherList;

    public DailyPagerAdapter(@NonNull FragmentManager fm, ArrayList<Daily> dailyWeatherList) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.dailyWeatherList = dailyWeatherList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return null;
    }

    @Override
    public int getCount() {
        return dailyWeatherList.size();
    }
}
