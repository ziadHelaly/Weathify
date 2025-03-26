package eg.edu.iti.weathify.core.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import eg.edu.iti.weathify.utils.Constants.Companion.permissionRequestCode

fun showDialogForAskAgain(context: Context) {
    AlertDialog.Builder(context).apply {
        this.setTitle("give me permissions")
        this.setMessage("location is needed to locate phone location")
        this.setCancelable(false)
        this.setPositiveButton("OK") { _, _ ->
            requestPermissions(context as Activity)
        }
    }.show()
}

fun requestPermissions(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(
            ACCESS_COARSE_LOCATION,
            ACCESS_FINE_LOCATION
        ),
        permissionRequestCode
    )
}

fun showDialogForGPS(context: Context) {
    AlertDialog.Builder(context).apply {
        this.setTitle("Please open gps")
        this.setMessage("location is needed to locate phone location")
        this.setCancelable(false)
        this.setPositiveButton("OK") { _, _ ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        }
    }.show()
}

fun showDialogGoToSettings(context: Context) {
    AlertDialog.Builder(context).apply {
        this.setTitle("open permissions settings")
        this.setMessage("location is needed to locate phone location")
        this.setCancelable(false)
        this.setPositiveButton("OK") { _, _ ->
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }.show()
}