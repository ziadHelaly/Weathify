package eg.edu.iti.weathify.core.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import eg.edu.iti.weathify.core.view.theme.WeathifyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var showSplash = true
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
                    Text(text = "", modifier = Modifier.padding(innerPadding))
                }

            }
        }
    }

    private fun hideSplash() {
        lifecycleScope.launch {
            delay(1000L)
            showSplash = false
        }
    }
}

