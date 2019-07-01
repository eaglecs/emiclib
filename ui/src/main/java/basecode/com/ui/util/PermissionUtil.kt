package basecode.com.ui.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

object PermissionUtil {
    fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        permissions.forEach { permission ->
            context?.let {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }
    val uploadImagePermissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val callPermission = arrayOf(
            Manifest.permission.CALL_PHONE
    )
}