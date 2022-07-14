package anaxxes.com.weatherFlow.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.common.control.interfaces.AdCallback;
import com.common.control.manager.AdmobManager;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.main.MainActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private InterstitialAd interstitialAd;

    public InterstitialAd getInterstitialAd() {
        return interstitialAd;
    }

    public void setInterstitialAd(InterstitialAd interstitialAd) {
        this.interstitialAd = interstitialAd;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadIntersAd();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SplashActivity.this.interstitialAd != null){
                    AdmobManager.getInstance().showInterstitial(SplashActivity.this, getInterstitialAd(), new AdCallback() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();

                        }
                    });
                }
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 5000);
    }
    private void loadIntersAd(){
        AdmobManager.getInstance().loadInterAds(this, "ca-app-pub-3940256099942544/8691691433", new AdCallback() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void interCallback(InterstitialAd interstitialAd) {
                super.interCallback(interstitialAd);
            }

            @Override
            public void onResultInterstitialAd(InterstitialAd interstitialAd) {
                super.onResultInterstitialAd(interstitialAd);
                setInterstitialAd(interstitialAd);
            }

            @Override
            public void onNativeAds(NativeAd nativeAd) {
                super.onNativeAds(nativeAd);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(LoadAdError errAd) {
                super.onAdFailedToShowFullScreenContent(errAd);
            }
        });
    }
}
