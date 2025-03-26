package eg.edu.iti.weathify.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.LOCATION_SERVICE
import android.content.Context
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

object LocationUtil {
    fun isGPSOpened(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun getLocation(activity: Activity, onLocationCallback: (String, String) -> Unit) {
        val fusedClient = LocationServices.getFusedLocationProviderClient(activity)

        val locationRequest = LocationRequest.Builder(0).apply {
            setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        }.build()

        val locationCallBack = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                Log.d("``TAG``", "onLocationResult: ${p0.lastLocation?.longitude}")
                onLocationCallback(
                    p0.lastLocation?.longitude.toString(),
                    p0.lastLocation?.latitude.toString()
                )
            }
        }

        fusedClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.myLooper())
    }
}