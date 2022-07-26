package mtgtech.com.weather_forecast

import com.common.control.MyApplication

class App : MyApplication() {
    override fun onApplicationCreate() {
    }

    override fun hasAds(): Boolean {
        return true
    }

    override fun isShowDialogLoadingAd(): Boolean {
        return false
    }

    override fun isShowAdsTest(): Boolean {
        return true
    }

    override fun enableAdsResume(): Boolean {
        return true
    }

    override fun getOpenAppAdId(): String {
        return BuildConfig.open_app
    }
}