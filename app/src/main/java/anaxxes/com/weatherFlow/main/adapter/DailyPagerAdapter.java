package anaxxes.com.weatherFlow.main.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import anaxxes.com.weatherFlow.basic.model.weather.Daily;
import anaxxes.com.weatherFlow.ui.fragment.DailyDetailsFragment;

public class DailyPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Daily> dailyWeatherList;
    public DailyPagerAdapter(@NonNull FragmentManager fm,ArrayList<Daily> dailyWeatherList) {
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
