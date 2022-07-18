package mtgtech.com.weather_forecast.view.activity;

import static mtgtech.com.weather_forecast.main.MainActivity.isShowAds;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.common.control.interfaces.AdCallback;
import com.common.control.manager.AdmobManager;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import mtgtech.com.weather_forecast.BuildConfig;
import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.main.MainActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadIntersAd();
        isShowAds = false;

    }

    private void startMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void loadIntersAd() {
        AdmobManager.getInstance().loadInterAds(this, BuildConfig.inter_splash, new AdCallback() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                startMain();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError i) {
                super.onAdFailedToLoad(i);
                startMain();

            }


            @Override
            public void onResultInterstitialAd(InterstitialAd interstitialAd) {
                super.onResultInterstitialAd(interstitialAd);
                AdmobManager.getInstance().showInterstitial(SplashActivity.this, interstitialAd, this);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(LoadAdError errAd) {
                super.onAdFailedToShowFullScreenContent(errAd);
                startMain();
            }
        });
    }
}
