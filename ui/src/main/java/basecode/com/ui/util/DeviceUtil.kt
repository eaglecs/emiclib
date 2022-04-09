package basecode.com.ui.util

import android.content.Context
import basecode.com.ui.R

class DeviceUtil(private val context: Context) {
    fun isPhoneDevice(): Boolean {
        return context.resources.getBoolean(R.bool.isPhone)
    }

    fun getWidthScreen(): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getHeightScreen(): Int {
        return context.resources.displayMetrics.heightPixels
    }

    enum class DisplayMetric(val value: Float) {
        LDPI(0.75f),
        MDPI(1.0f),
        HDPI(1.5f),
        XHDPI(2.0f),
        XXHDPI(3.0f),
        XXXHDPI(4.0f)
    }
}