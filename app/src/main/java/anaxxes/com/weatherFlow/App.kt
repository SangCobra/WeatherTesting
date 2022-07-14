package anaxxes.com.weatherFlow

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
        return false
    }

    override fun getOpenAppAdId(): String {
        return ""
    }
}