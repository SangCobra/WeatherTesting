package anaxxes.com.weatherFlow.admob

import android.content.Context
import android.view.ViewGroup
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.UnifiedNativeAd
import anaxxes.com.weatherFlow.WeatherFlow.click
import anaxxes.com.weatherFlow.utils.PurchaseUtils
import android.annotation.SuppressLint
import com.google.android.gms.ads.interstitial.InterstitialAd

class AdmobUtil(private val context: Context) {
    private var mInterstitialAd: InterstitialAd? = null
    private var mInterstitialAdListener: OnInterstitialAdListener? = null
    private var isInterstitialAdLoaded = true


    @SuppressLint("MissingPermission")
    fun loadBannerAd(
            container: ViewGroup,
            adSize: AdSize?
    ) {
        val mAdView = AdView(context)
        mAdView.adSize = adSize
        mAdView.adUnitId = Config.bannerId
        container.addView(mAdView)
        val adRequest =
                AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    @SuppressLint("MissingPermission")
    fun loadNativeAd(listener: OnNativeAdListener) {
        val builder = AdLoader.Builder(context, Config.nativeId)
        val loader =
                builder.forUnifiedNativeAd { unifiedNativeAd: UnifiedNativeAd? ->

                    //TODO: Commentby abdul due to native template error
                    val templateView = listener.onTemplateView()
                    templateView.setNativeAd(unifiedNativeAd)
                }.build()
        loader.loadAd(AdRequest.Builder().build())
    }

    fun showInterstitialAd(listener: OnInterstitialAdListener?) {
        mInterstitialAdListener = listener

        if ((click++) % 5 == 0 && !PurchaseUtils.isPurchased(context)) {
//            if (mInterstitialAd!!.isLoaded) {
//                isInterstitialAdLoaded = true
//                mInterstitialAd!!.show()
//            } else {
//                mInterstitialAdListener?.onAdListener(false, Interstitial.ON_AD_CLOSED)
//            }
        }else{
            mInterstitialAdListener?.onAdListener(false, Interstitial.ON_AD_CLOSED)
        }

    }

    fun initInterstitialAd() {
//        mInterstitialAd = InterstitialAd(context)
//        mInterstitialAd!!.adUnitId = Config.interstitialId
//        mInterstitialAd!!.loadAd(AdRequest.Builder().build())
//        mInterstitialAd!!.adListener = object : AdListener() {
//            override fun onAdLoaded() {
//                super.onAdLoaded()
//                mInterstitialAdListener?.onAdListener(
//                        isInterstitialAdLoaded,
//                        Interstitial.ON_AD_LOADED
//                )
//            }
//
//            override fun onAdClicked() {
//                super.onAdClicked()
//                mInterstitialAdListener?.onAdListener(
//                        isInterstitialAdLoaded,
//                        Interstitial.ON_AD_CLICKED
//                )
//            }
//
//            override fun onAdFailedToLoad(i: Int) {
//                super.onAdFailedToLoad(i)
//                mInterstitialAdListener?.onAdListener(
//                        isInterstitialAdLoaded,
//                        Interstitial.ON_AD_FAILED_TO_LOAD
//                )
//            }
//
//            override fun onAdLeftApplication() {
//                super.onAdLeftApplication()
//                mInterstitialAdListener?.onAdListener(
//                        isInterstitialAdLoaded,
//                        Interstitial.ON_AD_LEFT_APP
//                )
//            }
//
//            override fun onAdOpened() {
//                super.onAdOpened()
//                mInterstitialAdListener?.onAdListener(
//                        isInterstitialAdLoaded,
//                        Interstitial.ON_AD_OPENED
//                )
//            }
//
//            override fun onAdClosed() {
//                super.onAdClosed()
//                mInterstitialAd!!.loadAd(AdRequest.Builder().build())
//                mInterstitialAdListener?.onAdListener(
//                        isInterstitialAdLoaded,
//                        Interstitial.ON_AD_CLOSED
//                )
//            }
//        }
    }


    init {
//        MobileAds.initialize(context, Config.appId)
    }
}