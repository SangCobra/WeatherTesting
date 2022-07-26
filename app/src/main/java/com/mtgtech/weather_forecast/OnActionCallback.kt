package com.mtgtech.weather_forecast

interface OnActionCallback {
    fun callback(key: String?, vararg data: Any?)
}