package mtgtech.com.weather_forecast.main;

import static mtgtech.com.weather_forecast.main.MainActivity.isStartAgain;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import mtgtech.com.weather_forecast.databinding.ActivityRadarBinding;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;

public class RadarActivity extends GeoActivity {

    private Float latitude;
    private Float longitude;

    private ActivityRadarBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityRadarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        latitude = getIntent().getFloatExtra("latitude", 0);
        longitude = getIntent().getFloatExtra("longitude", 0);


        Log.d("lkasdfsdf", "width: " + latitude + "height: " + longitude);


        String html = "<html><head><style>" +

                "iframe { " +
                "position: absolute; " +
                "top:0; " +
                "left: 0; " +
                "width: 100%; " +
                "height: 100%; " +
                "}" +
                "</style></head><body  style=\"padding: 0; margin: 0;\">" +
                "<div ><iframe src=\"https://embed.windy.com/embed2.html?" +
                "lat=" + latitude +
                "&lon=" + longitude +
                "&detailLat=" + latitude + "" +
                "&detailLon=" + longitude +
                "&zoom=8&level=surface&overlay=wind&product=ecmwf&menu=&message=true&marker=&calendar=now&pressure=&type=map&location=coordinates&detail=&metricWind=default&metricTemp=default&radarRange=-1\"" +
                " frameborder=\"0\"></iframe></div></body></html>";


//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)DisplayUtils.dpToPx(this,300),(int)DisplayUtils.dpToPx(this,100));
//            binding.background.cardWebView.setLayoutParams(layoutParams);
        binding.radarWebView.getSettings().setJavaScriptEnabled(true);
        binding.radarWebView.loadData(html, "text/html; charset=UTF-8", null);

    }

    @Override
    public View getSnackBarContainer() {
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isStartAgain = false;
    }
}