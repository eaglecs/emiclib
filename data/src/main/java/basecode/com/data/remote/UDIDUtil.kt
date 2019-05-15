package basecode.com.data.remote

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import java.util.*

object UDIDUtil {
    @SuppressLint("HardwareIds")
    fun getUDID(context: Context): String {
        var uniqueId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        if (uniqueId.isNullOrEmpty()) {
            uniqueId = getUniquePsuedoID()
        }
        return uniqueId
    }

    private fun getUniquePsuedoID(): String {
        val deviceIdShort = "35" + //we make this look like a valid IMEI

                Build.BOARD.length % 10 + Build.BRAND.length % 10 +
                Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 +
                Build.DISPLAY.length % 10 + Build.HOST.length % 10 +
                Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +
                Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +
                Build.TAGS.length % 10 + Build.TYPE.length % 10 +
                Build.USER.length % 10 //13 digits

        var serial = "serial"
        return try {
            serial = Build::class.java.getField("SERIAL").get(null).toString()
            UUID(deviceIdShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        } catch (e: Exception){
            UUID(deviceIdShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        }
    }
}