package mtgtech.com.weather_forecast

interface OnActionCallback {
    fun callback(key: String?, vararg data: Any?)
}