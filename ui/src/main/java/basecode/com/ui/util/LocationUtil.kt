package basecode.com.ui.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import basecode.com.domain.extention.number.roundUp
import com.google.android.gms.location.LocationServices

class LocationUtil(private val context: Context) {
    @SuppressLint("MissingPermission")
    fun getLatLongCurrent(onActionSuccess: (latitude: Double, longitude: Double) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
//                    ConstApp.lat = location.latitude
//                    ConstApp.lng = location.longitude
                    onActionSuccess.invoke(location.latitude, location.longitude)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun listenerLocationStatus() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                10f, object : LocationListener {
                    override fun onLocationChanged(location: Location) {
//                        ConstApp.lat = location.latitude
//                        ConstApp.lng = location.longitude
                    }

                    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
                    }

                    override fun onProviderEnabled(p0: String) {
                    }

                    override fun onProviderDisabled(p0: String) {
                    }

                })
        }

    }

    fun getDistance(fromLat: Double, fromLng: Double, toLat: Double, toLng: Double): String {
        val locationDepart = Location("Depart")
        val locationDestination = Location("Destination")
        locationDepart.latitude = fromLat
        locationDepart.longitude = fromLng
        locationDestination.latitude = toLat
        locationDestination.longitude = toLng
        val distance = locationDepart.distanceTo(locationDestination)
        return when {
            distance == 0f -> ""
            distance < 1000 -> "${(distance.toDouble() / 1000).roundUp(1)}km"
            distance > 1000 && distance < 10000 -> {
                val distanceKm = ((distance / 1000).toInt()).toDouble()
                val remainder = ((distance % 1000) / 1000).toDouble().roundUp(1)
                "${distanceKm + remainder}km"
            }
            else -> {
                var distanceKm = (distance / 1000).toInt()
                val remainder = distance % 1000
                if (remainder > 0) {
                    distanceKm += 1
                }
                "${distanceKm}km"
            }
        }
    }
}