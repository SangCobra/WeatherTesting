package mtgtech.com.weather_forecast;

import com.google.android.gms.ads.interstitial.InterstitialAd;

public class AdCache {
    private static AdCache instance;

    public static AdCache getInstance() {
        if (instance == null) {
            instance = new AdCache();
        }
        return instance;
    }

    private AdCache() {

    }
    private InterstitialAd interstitialAd;

    public InterstitialAd getInterstitialAd() {
        return interstitialAd;
    }

    public void setInterstitialAd(InterstitialAd interstitialAd) {
        this.interstitialAd = interstitialAd;
    }
}
