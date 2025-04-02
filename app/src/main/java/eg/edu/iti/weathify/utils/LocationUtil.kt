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

        fusedClient.lastLocation.addOnSuccessListener { location ->
            Log.d("`TAG`", "getLocation: before")
            if (location != null) {
                Log.d("``TAG", "getLastLocation: ${location.longitude}, ${location.latitude}")
                onLocationCallback(location.longitude.toString(), location.latitude.toString())
            } else {
                fusedClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                    .addOnSuccessListener { currentLocation ->
                        currentLocation?.let {
                            Log.d("``TAG", "getCurrentLocation (fallback): ${it.longitude}, ${it.latitude}")
                            onLocationCallback(it.longitude.toString(), it.latitude.toString())
                        }
                    }
            }
            Log.d("``TAG``", "getLocation: After")
        }
    }

}