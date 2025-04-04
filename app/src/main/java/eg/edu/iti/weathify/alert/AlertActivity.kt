package eg.edu.iti.weathify.alert

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eg.edu.iti.weathify.R
import eg.edu.iti.weathify.ui.theme.WeathifyTheme
import eg.edu.iti.weathify.ui.theme.screenBG

class AlertActivity : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var description: String
    private lateinit var temp: String
    private lateinit var city: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        description = intent.getStringExtra("description") ?: "Very Nice"
        temp = intent.getIntExtra("degree", 16).toString()
        city = intent.getStringExtra("city") ?: "Egypt"
        enableEdgeToEdge()
        setContent {
            WeathifyTheme {
                Scaffold(
                    modifier = Modifier.background(screenBG).fillMaxSize()
                ) { innerPadding ->
                    AlertScreen(
                        description = description,
                        temp = temp,
                        city = city,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        finish()
                    }
                }
            }
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.notification_sound)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

@Composable
private fun AlertScreen(
    description: String,
    temp: String,
    city: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text("Weathify Alert", style = MaterialTheme.typography.titleLarge)
        Text("Weather is $description in $city", style = MaterialTheme.typography.titleLarge)
        Text("$temp °C", style = MaterialTheme.typography.displayLarge)
        Text("Have a nice day!", style = MaterialTheme.typography.bodyLarge)
        Button(
            onClick = onClick
        ) {
            Text("Dismiss")
        }
    }
}
