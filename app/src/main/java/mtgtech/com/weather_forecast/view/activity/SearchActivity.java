package mtgtech.com.weather_forecast.view.activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
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

import com.turingtechnologies.materialscrollbar.CustomIndicator;

import java.util.ArrayList;
import java.util.List;

import mtgtech.com.weather_forecast.BuildConfig;
import mtgtech.com.weather_forecast.R;
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
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listSearch = new ArrayList<>();

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
                    this.pos = pos;
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
//                    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                    if (manager != null) {
//                        manager.hideSoftInputFromWindow(binding.editText.getWindowToken(), 0);
//                    }
//                    binding.clearBtn.setVisibility(View.VISIBLE);
                    accuWeatherService.getApi().searchPlace(BuildConfig.ACCU_WEATHER_KEY, "vi", s.toString(), true)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(searches -> {
                                listSearch = (ArrayList<Search>) searches;
                                SearchActivity.this.adapter.setList(listSearch);
//                                setState(STATE_LOADING);
                            });


                    binding.clearBtn.setVisibility(View.VISIBLE);
                }
                else binding.clearBtn.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        new Handler().post(() -> {
//            binding.editText.requestFocus();
//            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (inputManager != null) {
//                inputManager.showSoftInput(binding.editText, 0);
//            }
//        });
// 19037245202018
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false);


//        ArrayList<LocationModel> listModel = new ArrayList<>();
//        locationList.forEach(location -> {
//            listModel.add(new LocationModel(this, location, SettingsOptionManager.getInstance(this).getTemperatureUnit(), SettingsOptionManager.getInstance(this).getWeatherSource()
//            , ThemeManager.getInstance(this).isLightTheme(), location.getFormattedId().equals(null)));
//        });

        binding.recyclerView.setLayoutManager(layoutManager);
//        binding.recyclerView.addItemDecoration(new ListDecoration(this));
        binding.recyclerView.setAdapter(adapter);

//        binding.scrollBar.setIndicator(
//                new WeatherSourceIndicator(this).setTextSize(16), true);
//
//        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @ColorInt int sourceColor = Color.TRANSPARENT;
//            @ColorInt int color;
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                color = adapter.getItemSourceColor(layoutManager.findFirstVisibleItemPosition());
//                if (color != sourceColor) {
//                    binding.scrollBar.setHandleColor(color);
//                    binding.scrollBar.setHandleOffColor(color);
//                }
//            }
//        });

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

        }

    }

    @Override
    public void requestLocationFailed(String query) {
    }
}