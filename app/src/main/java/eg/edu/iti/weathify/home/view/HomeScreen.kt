package eg.edu.iti.weathify.home.view


import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.iti.weathify.R
import eg.edu.iti.weathify.core.model.models.Current
import eg.edu.iti.weathify.core.model.models.WeatherResponse
import eg.edu.iti.weathify.home.viewmodel.HomeViewModel
import eg.edu.iti.weathify.utils.Result


@Composable
fun HomeScreen(
    long: String,
    lat: String,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentWeather by viewModel.currentWeather.collectAsStateWithLifecycle()
    val country = remember { mutableStateOf("") }

    LaunchedEffect(long != "0.0") {
        Log.d("``TAG``", "HomeScreen: $long , $lat")
        viewModel.getWeather(long, lat)
    }

    when (currentWeather) {
        is Result.Loading -> {
            CircularProgressIndicator()
        }

        is Result.Failure -> {
            Text((currentWeather as Result.Failure).message)
        }

        is Result.Success -> {
            Screen(
                country = getAddress(context,lat.toDouble(),long.toDouble()),
                current = (currentWeather as Result.Success).data
            )
        }
    }

}

fun getAddress(context: Context, lati: Double, long: Double) :String{
    val res=Geocoder(context).getFromLocation(lati, long, 1)
    if (!res.isNullOrEmpty()){
        return "${res[0].locality}, ${res[0].adminArea}"
    }
    return "Egypt"
}

@Composable
private fun Screen(modifier: Modifier = Modifier, country: String, current: WeatherResponse) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(Color(3, 155, 229, 255))
            .padding(start = 8.dp, end = 8.dp, top = 24.dp)
    ) {
        LocationSection(country = country)
        DegreeSection(current.current)
    }

}

@Composable
fun DegreeSection(current: Current, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0x80FFFFFF) // 50% transparent white
        ),
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(300.dp)

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Row {
                Text(current.temp.toInt().toString(), fontSize = 72.sp)
                Text("°c", fontSize = 28.sp)
            }
        }
    }
}

@Composable
fun LocationSection(country: String, modifier: Modifier = Modifier) {
    Row {
        Icon(painterResource(R.drawable.ic_location), contentDescription = "")
        Spacer(Modifier.width(4.dp))
        Text(country)
    }
}