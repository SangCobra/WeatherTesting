package anaxxes.com.weatherFlow.ui.fragment;

import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.GeoActivity;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.db.DatabaseHelper;
import anaxxes.com.weatherFlow.main.MainListDecoration;
import anaxxes.com.weatherFlow.ui.adapter.location.LocationAdapter;
import anaxxes.com.weatherFlow.ui.adapter.location.LocationTouchCallback;
import anaxxes.com.weatherFlow.utils.SnackbarUtils;
import anaxxes.com.weatherFlow.utils.helpter.IntentHelper;
import anaxxes.com.weatherFlow.utils.manager.AdIdUtils;
import anaxxes.com.weatherFlow.utils.manager.ShortcutsManager;

public class LocationManageFragment extends Fragment
        implements LocationTouchCallback.OnLocationListChangedListener {

    private CardView cardView;
    private LinearLayout imgAddLocation;
    private ImageView imgBack;
    private AppCompatImageView searchIcon;
    private TextView searchTitle;
    private AppCompatImageButton currentLocationButton;
    private RecyclerView recyclerView;

    private LocationAdapter adapter;
    private MainListDecoration decoration;
    private int searchRequestCode;
    private int providerSettingsRequestCode;

    private ValueAnimator colorAnimator;

    private boolean drawerMode = false;

    private @Nullable
    LocationManageCallback locationListChangedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_manage, container, false);
        initWidget(view);
        return view;
    }

    private List<Location> readLocationList() {
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
        AdmobManager.getInstance().loadNative(requireContext(), AdIdUtils.idNative, view.findViewById(R.id.native_ad),R.layout.custom_native_app);
    }

    @Override
    public void onResume() {
        super.onResume();
        initWidget(requireView());
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
            requireActivity().finish();
        });
        imgAddLocation.setOnClickListener(v ->
                IntentHelper.startSearchActivityForResult(requireActivity(), cardView, searchRequestCode));
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
                (v, formattedId,index) -> {
                    if (locationListChangedListener != null) {
                        locationListChangedListener.onSelectedLocation(formattedId,index);
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

        onLocationListChanged(locationList, false, false);
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
        SnackbarUtils.showSnackbar(
                (GeoActivity) requireActivity(), getString(R.string.feedback_collect_succeed));
    }

    public void resetLocationList() {
        List<Location> list = readLocationList();
//        if (list.size() > 3){
//            list.add(2, null);
//        }
//        else {
//            list.add(null);
//        }
//        adapter.update(list);
        onLocationListChanged(list, false, true);
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

    public interface LocationManageCallback {
        void onSelectedLocation(@NonNull String formattedId,int index);

        void onLocationListChanged();
    }

    public void setOnLocationListChangedListener(LocationManageCallback l) {
        this.locationListChangedListener = l;
    }

    // on location list changed listener.

    @Override
    public void onLocationSequenceChanged(List<Location> locationList) {
        DatabaseHelper.getInstance(requireActivity()).writeLocationList(locationList);
        onLocationListChanged(locationList, true, true);
    }

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
}
