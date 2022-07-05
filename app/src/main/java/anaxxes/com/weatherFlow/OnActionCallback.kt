package anaxxes.com.weatherFlow

interface OnActionCallback {
    fun callback(key: String?, vararg data: Any?)
}