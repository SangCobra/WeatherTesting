package com.mtgtech.weather_forecast.view.fragment;

import static com.mtgtech.weather_forecast.main.MainActivity.isStartAgain;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.common.control.manager.AdmobManager;

import java.util.List;

import com.mtgtech.weather_forecast.BuildConfig;
import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.db.DatabaseHelper;
import com.mtgtech.weather_forecast.main.MainActivity;
import com.mtgtech.weather_forecast.main.MainActivityViewModel;
import com.mtgtech.weather_forecast.main.MainListDecoration;
import com.mtgtech.weather_forecast.utils.helpter.IntentHelper;
import com.mtgtech.weather_forecast.utils.manager.ShortcutsManager;
import com.mtgtech.weather_forecast.view.adapter.location.LocationAdapter;
import com.mtgtech.weather_forecast.view.adapter.location.LocationTouchCallback;
import com.mtgtech.weather_forecast.weather_model.GeoActivity;
import com.mtgtech.weather_forecast.weather_model.model.location.Location;

public class LocationManageFragment extends Fragment
        implements LocationTouchCallback.OnLocationListChangedListener {

    private CardView cardView;
    private LinearLayout imgAddLocation;
    private ImageView imgBack;
    private AppCompatImageView searchIcon;
    private TextView searchTitle;
    private AppCompatImageButton currentLocationButton;
    private RecyclerView recyclerView;
    private List<Location> listLocationBefore;
    private MainActivity.LoadLocation loadLocation;
    private MainActivityViewModel mainActivityViewModel;
    private LocationAdapter adapter;
    private MainListDecoration decoration;
    private int searchRequestCode;
    private int providerSettingsRequestCode;
    private ValueAnimator colorAnimator;
    private boolean drawerMode = false;
    private FrameLayout frAd;
    private @Nullable
    LocationManageCallback locationListChangedListener;

    public void setLoadLocation(MainActivity.LoadLocation loadLocation) {
        this.loadLocation = loadLocation;
    }

    public LocationAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(LocationAdapter adapter) {
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_manage, container, false);
        listLocationBefore = readLocationList();
        mainActivityViewModel = new MainActivityViewModel();
        initWidget(view);
        return view;
    }

    public List<Location> readLocationList() {
        List<Location> locationList = DatabaseHelper.getInstance(requireActivity()).readLocationList();
        for (Location l : locationList) {
            l.setWeather(DatabaseHelper.getInstance(requireActivity()).readWeather(l));
        }
        return locationList;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        AdmobManager.getInstance().loadBanner(requireActivity(), AdIdUtils.idBanner);
        frAd = view.findViewById(R.id.native_ad);
        AdmobManager.getInstance().loadNative(requireContext(), BuildConfig.native_location, frAd, R.layout.custom_native_app);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initWidget(View view) {
        LinearLayout appBar = view.findViewById(R.id.fragment_location_manage_appBar);
        ViewCompat.setOnApplyWindowInsetsListener(appBar, (v, insets) -> {
            v.setPadding(
                    insets.getSystemWindowInsetLeft(),
                    insets.getSystemWindowInsetTop(),
                    drawerMode ? 0 : insets.getSystemWindowInsetRight(),
                    0
            );
            return insets;
        });

        this.cardView = view.findViewById(R.id.fragment_location_manage_searchBar);
        this.imgAddLocation = view.findViewById(R.id.imgAddLocation);
        this.imgBack = view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> {
            isStartAgain = true;
            if (listLocationBefore.size() < readLocationList().size()) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.KEY_MAIN_ACTIVITY_LOCATION_FORMATTED_ID, readLocationList().get(readLocationList().size() - 1).getFormattedId());
                intent.putExtra(MainActivity.KEY_LOCATION_INDEX, readLocationList().size() - 1);
                requireActivity().setResult(Activity.RESULT_OK, intent);
            }
            requireActivity().finish();
        });
        imgAddLocation.setOnClickListener(v -> {
                    if (listLocationBefore.size() < readLocationList().size()) {
                        mainActivityViewModel.init((GeoActivity) requireActivity(), readLocationList().get(readLocationList().size() - 1).getFormattedId());
                    }
                    IntentHelper.startSearchActivityForResult(requireActivity(), cardView, searchRequestCode);
                }
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardView.setTransitionName(getString(R.string.transition_activity_search_bar));
        }

        this.searchIcon = view.findViewById(R.id.fragment_location_manage_searchIcon);
        this.searchTitle = view.findViewById(R.id.fragment_location_manage_title);

        this.currentLocationButton = view.findViewById(R.id.fragment_location_manage_currentLocationButton);
        currentLocationButton.setOnClickListener(v -> {
            DatabaseHelper.getInstance(requireActivity()).writeLocation(Location.buildLocal());
            addLocation();
        });

        List<Location> locationList = readLocationList();
        List<Location> locationList1 = locationList;
        adapter = new LocationAdapter(
                requireActivity(),
                locationList1,
                (v, formattedId, index) -> {
                    if (locationListChangedListener != null) {
                        locationListChangedListener.onSelectedLocation(formattedId, index);
                    }
                },
                true
        );
        adapter.setListenerChange(this);
        this.recyclerView = view.findViewById(R.id.fragment_location_manage_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                requireActivity()));

        new ItemTouchHelper(
                new LocationTouchCallback(
                        (GeoActivity) requireActivity(),
                        adapter,
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.START | ItemTouchHelper.END,
                        this
                )
        ).attachToRecyclerView(recyclerView);

        onLocationListChanged(locationList, true, true);
    }


    public void setRequestCodes(int searchRequestCode, int providerSettingsRequestCode) {
        this.searchRequestCode = searchRequestCode;
        this.providerSettingsRequestCode = providerSettingsRequestCode;
    }

    public void updateView(List<Location> newList) {
//        if (newList.size() > 3){
//            newList.add(2, null);
//        }
//        else {
//            newList.add(null);
//        }
        adapter.update(newList);
//        iconWeatherStyle();
    }

    public void addLocation() {
        resetLocationList();
//        SnackbarUtils.showSnackbar(
//                (GeoActivity) requireActivity(), getString(R.string.feedback_collect_succeed));
    }

    public void resetLocationList() {
        List<Location> list = readLocationList();
//        if (list.size() > 3){
//            list.add(2, null);
//        }
//        else {
//            list.add(null);
//        }
        adapter.update(list);
        onLocationListChanged(list, true, true);
    }

    private void onLocationListChanged(List<Location> list, boolean updateShortcuts, boolean notifyOutside) {
        setCurrentLocationButtonEnabled(list);
        if (updateShortcuts && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutsManager.refreshShortcutsInNewThread(requireActivity(), list);
        }
        if (notifyOutside && locationListChangedListener != null) {
            locationListChangedListener.onLocationListChanged();
        }
    }

    private void setCurrentLocationButtonEnabled(List<Location> list) {
        boolean hasCurrentLocation = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCurrentPosition()) {
                hasCurrentLocation = true;
                break;
            }
        }
        currentLocationButton.setEnabled(!hasCurrentLocation);
        currentLocationButton.setAlpha(hasCurrentLocation ? 0.5f : 1);
    }

    public void setDrawerMode(boolean drawerMode) {
        this.drawerMode = drawerMode;
    }

    // interface.

    public void setOnLocationListChangedListener(LocationManageCallback l) {
        this.locationListChangedListener = l;
    }

    @Override
    public void onLocationSequenceChanged(List<Location> locationList) {
        DatabaseHelper.getInstance(requireActivity()).writeLocationList(locationList);
        onLocationListChanged(locationList, true, true);
    }

    // on location list changed listener.

    @Override
    public void onLocationInserted(List<Location> locationList, Location location) {
        DatabaseHelper.getInstance(requireActivity()).writeLocation(location);
        if (location.getWeather() != null) {
            DatabaseHelper.getInstance(requireActivity()).writeWeather(location, location.getWeather());
        }
        onLocationListChanged(locationList, true, true);
    }

    @Override
    public void onLocationRemoved(List<Location> locationList, Location location) {
        DatabaseHelper.getInstance(requireActivity()).deleteLocation(location);
        DatabaseHelper.getInstance(requireActivity()).deleteWeather(location);
        onLocationListChanged(locationList, true, true);
    }

    @Override
    public void onLocationChanged(List<Location> locationList, Location location) {
        DatabaseHelper.getInstance(requireActivity()).writeLocation(location);
        onLocationListChanged(locationList, true, true);
    }

    @Override
    public void onSelectProviderActivityStarted() {
        IntentHelper.startSelectProviderActivityForResult(
                requireActivity(), providerSettingsRequestCode);
    }

    public interface LocationManageCallback {
        void onSelectedLocation(@NonNull String formattedId, int index);

        void onLocationListChanged();
    }
}
