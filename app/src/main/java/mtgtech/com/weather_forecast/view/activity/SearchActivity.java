package mtgtech.com.weather_forecast.view.activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Toast;

import com.turingtechnologies.materialscrollbar.CustomIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mtgtech.com.weather_forecast.BuildConfig;
import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.main.MainActivityViewModel;
import mtgtech.com.weather_forecast.utils.LanguageUtils;
import mtgtech.com.weather_forecast.view.adapter.location.LocationAdapter;
import mtgtech.com.weather_forecast.view.adapter.location.LocationTouchCallback;
import mtgtech.com.weather_forecast.view.fragment.LocationManageFragment;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.weather_model.model.location.Location;
import mtgtech.com.weather_forecast.databinding.ActivitySearchBinding;
import mtgtech.com.weather_forecast.view.adapter.location.SearchLocationAdapter;
import mtgtech.com.weather_forecast.utils.DisplayUtils;
import mtgtech.com.weather_forecast.db.DatabaseHelper;
import mtgtech.com.weather_forecast.weather_forecast.WeatherHelper;
import mtgtech.com.weather_forecast.weather_forecast.json.accu.search.Search;
import mtgtech.com.weather_forecast.weather_forecast.service.AccuWeatherService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Search activity.
 * */

public class SearchActivity extends GeoActivity
        implements WeatherHelper.OnRequestLocationListener {

    private ActivitySearchBinding binding;

    private SearchLocationAdapter adapter;
    private WeatherHelper weatherHelper;
    private MainActivityViewModel mainActivityViewModel;

    private List<Location> currentList;
    private List<Location> locationList;
    private String query = "";
    private ArrayList<Search> listSearch;

    private int state = STATE_SHOWING;
    private static final int STATE_SHOWING = 1;
    private static final int STATE_LOADING = 2;
    private AccuWeatherService accuWeatherService = new AccuWeatherService();

    int pos = 0;

    private static class ShowAnimation extends Animation {
        // widget
        private View v;

        ShowAnimation(View v) {
            this.v = v;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            v.setAlpha(interpolatedTime);
        }
    }

    private static class HideAnimation extends Animation {
        // widget
        private View v;

        HideAnimation(View v) {
            this.v = v;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            v.setAlpha(1 - interpolatedTime);
        }
    }

    private static class WeatherSourceIndicator extends CustomIndicator {

        public WeatherSourceIndicator(Context context) {
            super(context);
        }

        @Override
        protected int getIndicatorHeight() {
            return 40;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listSearch = new ArrayList<>();
        mainActivityViewModel = new MainActivityViewModel();
        boolean lightTheme = !DisplayUtils.isDarkMode(this);
        DisplayUtils.setSystemBarStyle(this, getWindow(),
                true, lightTheme, true, lightTheme);

        initData();
        initWidget();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // do nothing.
    }

    @Override
    public void onBackPressed() {
        if (getWindow().getAttributes().softInputMode
                != WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
            finishSelf(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public View getSnackBarContainer() {
        return binding.container;
    }

    // init.

    private void initData() {
        this.currentList = DatabaseHelper.getInstance(this).readLocationList();
        this.locationList = new ArrayList<>();

        this.weatherHelper = new WeatherHelper();
    }

    @SuppressLint("NewApi")
    private void initWidget() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.searchBar.setTransitionName(getString(R.string.transition_activity_search_bar));
        }
        if (!TextUtils.isEmpty(binding.editText.getText())){
            binding.clearBtn.setVisibility(View.VISIBLE);
        }else {
            binding.clearBtn.setVisibility(View.GONE);
        }

        binding.clearBtn.setOnClickListener(v -> binding.editText.setText(""));
        binding.editText.setOnClickListener(v -> {
            binding.editText.setCursorVisible(true);
        });
        this.adapter = new SearchLocationAdapter(
                this,
                (l, pos)->{
                    query = l;
                    weatherHelper.requestLocation(SearchActivity.this, query, SearchActivity.this);
                    finishSelf(true);
                }
                );
        adapter.setList(listSearch);

        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("CheckResult")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    if (isNetworkConnected()){
                        accuWeatherService.getApi().searchPlace(BuildConfig.ACCU_WEATHER_KEY, LanguageUtils.getCurrentLocale(SearchActivity.this).getLanguage(), s.toString(), true)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread()).subscribe(searches -> {
                                    listSearch = (ArrayList<Search>) searches;
                                    SearchActivity.this.adapter.setList(listSearch);
//                                setState(STATE_LOADING);
                                });

                    }
                    else {
                        Toast.makeText(SearchActivity.this, getString(R.string.not_have_internet), Toast.LENGTH_LONG).show();
                        return;
                    }


                    binding.clearBtn.setVisibility(View.VISIBLE);
                }
                else binding.clearBtn.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false);



        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);


        binding.progress.setAlpha(0);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            binding.searchContainer.setAlpha(1f);
        } else {
            AnimatorSet animationSet = (AnimatorSet) AnimatorInflater.loadAnimator(
                    this, R.animator.search_container_in);
            animationSet.setStartDelay(350);
            animationSet.setTarget(binding.searchContainer);
            animationSet.start();
        }
    }

    // control.

    private void finishSelf(boolean selected) {
        setResult(selected ? RESULT_OK : RESULT_CANCELED, null);
        binding.searchContainer.setAlpha(0);
        ActivityCompat.finishAfterTransition(this);
    }

    private void setState(int stateTo) {
        if(state == stateTo) {
            return;
        }

        binding.recyclerView.clearAnimation();
        binding.progress.clearAnimation();

        switch (stateTo) {
            case STATE_SHOWING:
                if (state == STATE_LOADING) {
                    binding.recyclerView.setVisibility(View.VISIBLE);

                    ShowAnimation show = new ShowAnimation(binding.recyclerView);
                    show.setDuration(150);
                    binding.recyclerView.startAnimation(show);

                    HideAnimation hide = new HideAnimation(binding.progress);
                    hide.setDuration(150);
                    binding.progress.startAnimation(hide);
                }
                break;

            case STATE_LOADING:
                if (state == STATE_SHOWING) {
                    binding.recyclerView.setAlpha(0);
                    binding.progress.setAlpha(1);
                    binding.recyclerView.setVisibility(View.GONE);
                }
                break;
        }
        state = stateTo;
    }

    // interface.

    // on editor action listener.
    // on request weather location listener.

    @Override
    public void requestLocationSuccess(String query, List<Location> locationList) {
        if (locationList.get(0) != null){
            DatabaseHelper.getInstance(SearchActivity.this).writeLocation(locationList.get(0));
            Intent intent = new Intent();
            intent.putExtra("location", (Serializable) locationList.get(0));
            setResult(RESULT_OK, intent);
            mainActivityViewModel.init(SearchActivity.this, locationList.get(0).getFormattedId());
        }
    }

    @Override
    public void requestLocationFailed(String query) {
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}