package eg.edu.iti.weathify.core.view

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import eg.edu.iti.weathify.core.navigation.NavComponent
import eg.edu.iti.weathify.core.navigation.ScreenRoutes
import eg.edu.iti.weathify.core.view.theme.WeathifyTheme
import eg.edu.iti.weathify.utils.Constants.Companion.permissionRequestCode
import eg.edu.iti.weathify.utils.LocationUtil.getLocation
import eg.edu.iti.weathify.utils.LocationUtil.isGPSOpened
import eg.edu.iti.weathify.utils.PermissionsUtil.checkPermissions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var showSplash = true
    private var longitude = mutableStateOf("0.0")
    private var latitude = mutableStateOf("0.0")

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
                    val navController = rememberNavController()
                    NavComponent(
                        navHostController = navController,
                        startDestination = ScreenRoutes.HomeScreenRoute,
                        lat = latitude.value,
                        lon = longitude.value,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if (checkPermissions(this)) {
            Log.d("`TAG`", "onStart: true")
            if (isGPSOpened(this)) {
                getLocation(this@MainActivity) { long, lat ->
                    longitude.value = long
                    latitude.value = lat
                }
            } else {
                showDialogForGPS(this@MainActivity)
            }

        } else {
            Log.d("`TAG`", "onStart: false")
            requestPermissions(this@MainActivity)

        }
    }


    private fun hideSplash() {
        lifecycleScope.launch {
            delay(1000L)
            showSplash = false
        }
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
                getLocation(this@MainActivity) { long, lat ->
                    longitude.value = long
                    latitude.value = lat
                }
            } else {
                Log.d("``TAG``", "onRequestPermissionsResult: false")
                if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                    Log.d("```TAG", "should rational true")
                    showDialogForAskAgain(this@MainActivity)

                } else {
                    Log.d("```TAG", "should rational true")
                    showDialogGoToSettings(this@MainActivity)
                }

            }
        }
    }


}

