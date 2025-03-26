package eg.edu.iti.weathify.utils

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionsUtil {
    fun checkPermissions(context: Context): Boolean {
        var result = false
        if ((ContextCompat.checkSelfPermission(
                context,
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(
                context,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            result = true
        }
        return result
    }

}