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
    private InterstitialAd interstitialAdDailyDetails;

    public InterstitialAd getInterstitialAdDailyDetails() {
        return interstitialAdDailyDetails;
    }

    public void setInterstitialAdDailyDetails(InterstitialAd interstitialAdDailyDetails) {
        this.interstitialAdDailyDetails = interstitialAdDailyDetails;
    }

    public InterstitialAd getInterstitialAd() {
        return interstitialAd;
    }

    public void setInterstitialAd(InterstitialAd interstitialAd) {
        this.interstitialAd = interstitialAd;
    }
}
