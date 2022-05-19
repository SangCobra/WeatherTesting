package anaxxes.com.weatherFlow.admob


object Config {
    /*
   * Google AdMob
   */

//    Testing ads
    const val appId: String = "ca-app-pub-3940256099942544~3347511713"
    const val bannerId = "ca-app-pub-3940256099942544/6300978111"
    const val interstitialId = "ca-app-pub-3940256099942544/1033173712"
    const val nativeId = "ca-app-pub-3940256099942544/2247696110"
    const val PRODUCT_ID = "YOUR PRODUCT ID FROM GOOGLE PLAY CONSOLE HERE"
    const val LICENCE_KEY_FROM_GOOGLE_PLAY_CONSOLE= "YOUR LICENSE KEY FROM GOOGLE PLAY CONSOLE HERE"


    const val DEFAULT_NUMBER_OF_ITEMS_PER_AD = 5

    fun isAdAvailable(position: Int): Boolean {
        if (position % DEFAULT_NUMBER_OF_ITEMS_PER_AD == 0 && position != 0) {
            return true
        }
        return false
    }





    const val KEY_TITLE = "TITLE"
    const val KEY_LINK = "LINK"
    const val PRIVACY_POLICY = "Privacy Policy"

}