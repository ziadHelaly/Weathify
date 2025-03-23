package eg.edu.iti.weathify.core.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import eg.edu.iti.weathify.core.view.theme.WeathifyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var showSplash = true
    private lateinit var fusedClient: FusedLocationProviderClient
    private lateinit var locationCallBack: LocationCallback
    private var longitude = mutableStateOf("0.0")
    private var latitude = mutableStateOf("0.0")

    private val permissionRequestCode = 5005
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { showSplash }
        hideSplash()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeathifyTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Text(text = "dsfghjkl;kjkl", modifier = Modifier.padding(innerPadding))
                }

            }
        }
    }
    override fun onStart() {
        super.onStart()
        if (checkPermissions()) {
            Log.d("`TAG`", "onStart: true")
            if (isGPSOpened()){
                getLocation()
            }else{
                showDialogForGPS()
            }

        } else {
            Log.d("`TAG`", "onStart: false")
            requestPermissions()

        }
    }
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedClient = LocationServices.getFusedLocationProviderClient(this)

        var locationRequest = LocationRequest.Builder(0).apply {
            setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        }.build()

        locationCallBack = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                Log.d("``TAG``", "onLocationResult: ${p0.lastLocation?.longitude}")
                longitude.value = p0.lastLocation?.longitude.toString()
                latitude.value = p0.lastLocation?.latitude.toString()
            }
        }

        fusedClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.myLooper())
    }

    private fun hideSplash() {
        lifecycleScope.launch {
            delay(1000L)
            showSplash = false
        }
    }
    private fun showDialogForAskAgain() {
        AlertDialog.Builder(this).apply {
            this.setTitle("give me permissions")
            this.setMessage("location is needed to locate phone location")
            this.setCancelable(false)
//            this.setOnCancelListener{
//                this@MainActivity.finish()
//            }
            this.setPositiveButton("OK") { _, _ ->
                requestPermissions()
            }
        }.show()
    }
    private fun showDialogForGPS() {
        AlertDialog.Builder(this).apply {
            this.setTitle("Please open gps")
            this.setMessage("location is needed to locate phone location")
            this.setCancelable(false)
            this.setPositiveButton("OK") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }.show()
    }

    private fun showDialogGoToSettings() {
        AlertDialog.Builder(this).apply {
            this.setTitle("open permissions settings")
            this.setMessage("location is needed to locate phone location")
            this.setCancelable(false)
            this.setPositiveButton("OK") { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }.show()
    }

    private fun checkPermissions(): Boolean {
        var result = false
        if ((ContextCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            result = true
        }
        return result
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(
                ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION
            ),
            permissionRequestCode
        )
    }
    fun isGPSOpened(): Boolean {
        val locationManager: LocationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("``TAG``", "onRequestPermissionsResult: knock")
        if (requestCode == permissionRequestCode) {
            Log.d("``TAG``", "onRequestPermissionsResult: knock knock")
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("``TAG``", "onRequestPermissionsResult: true")
                getLocation()
            } else {
                Log.d("``TAG``", "onRequestPermissionsResult: false")
                if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                    Log.d("```TAG", "should rational true")
                    showDialogForAskAgain()

                } else {
                    Log.d("```TAG", "should rational true")
                    showDialogGoToSettings()
                }

            }
        }
    }


}

