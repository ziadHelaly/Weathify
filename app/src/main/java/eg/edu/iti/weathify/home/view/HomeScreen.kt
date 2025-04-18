package eg.edu.iti.weathify.home.view


import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import eg.edu.iti.weathify.R
import eg.edu.iti.weathify.core.model.models.Current
import eg.edu.iti.weathify.core.model.models.Daily
import eg.edu.iti.weathify.core.model.models.Hourly
import eg.edu.iti.weathify.core.model.models.WeatherResponse
import eg.edu.iti.weathify.home.viewmodel.FormatTypes
import eg.edu.iti.weathify.home.viewmodel.HomeViewModel
import eg.edu.iti.weathify.utils.Constants.Companion.imageLink
import eg.edu.iti.weathify.utils.LocaleHelper.Companion.localized
import eg.edu.iti.weathify.utils.NetworkMonitor
import eg.edu.iti.weathify.utils.NetworkUtil
import eg.edu.iti.weathify.utils.Result
import java.util.Locale


@Composable
fun HomeScreen(
    long: MutableState<String>,
    lat: MutableState<String>,
    viewModel: HomeViewModel,
    isHome: Boolean,
) {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    val currentWeather by viewModel.currentWeather.collectAsStateWithLifecycle()
    val address by viewModel.address.collectAsStateWithLifecycle()
    var isNetworkBarVisible by remember { mutableStateOf(false) }
    val networkMonitor = remember {
        NetworkMonitor(
            onNetworkAvailable = {
                isNetworkBarVisible = false
            },
            onNetworkLost = {
                isNetworkBarVisible = true
            }
        )
    }

    DisposableEffect(owner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    networkMonitor.registerNetworkCallbacks(context)
                    isNetworkBarVisible = !NetworkUtil.isConnected(context)
                }

                Lifecycle.Event.ON_PAUSE -> {
                    networkMonitor.unregisterNetworkCallbacks(context)
                }

                Lifecycle.Event.ON_DESTROY -> {
                    networkMonitor.unregisterNetworkCallbacks(context)
                }

                else -> {}
            }
        }

        owner.lifecycle.addObserver(observer)
        onDispose {
            owner.lifecycle.removeObserver(observer)
        }
    }
    LaunchedEffect(long.value) {
        Log.d("``TAG``", "HomeScreen: ${long.value} , ${lat.value}")
        if (isHome) {
            if (long.value != "0.0") {
                viewModel.getWeatherForHome(long.value, lat.value)
            }
        } else {
            if (long.value != "0.0") {
                viewModel.getWeather(long.value, lat.value)
            }
        }
        viewModel.getAddress(
            Geocoder(context, Locale.getDefault()),
            lat.value.toDouble(),
            long.value.toDouble(),
            isHome
        )

    }

    when (currentWeather) {
        is Result.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Result.Failure -> {
            Text((currentWeather as Result.Failure).message)
        }

        is Result.Success -> {
            Screen(
                country = address,
                current = (currentWeather as Result.Success).data,
                viewModel = viewModel,
                isConnected = !isNetworkBarVisible
            )
        }
    }

}

@Composable
private fun Screen(
    viewModel: HomeViewModel,
    country: String,
    current: WeatherResponse,
    isConnected: Boolean
) {
    val tempUnit by viewModel.tempUnit.collectAsStateWithLifecycle()
    val windUnit by viewModel.windUnit.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter =
            if (viewModel.formatTime(
                    current.current.dt.toLong(),
                    FormatTypes.hour24,
                    current.timezone
                )
                    .toInt() in 6..17
            ) {
                painterResource(R.drawable.ic_day)
            } else painterResource(R.drawable.ic_background),
            "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.8f)

        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 8.dp, end = 8.dp, top = 24.dp)
        ) {

            LocationSection(country = country)
            DegreeSection(
                current.current,
                viewModel = viewModel,
                tempUnit = tempUnit,
                date = viewModel.formatTime(
                    current.current.dt.toLong(),
                    FormatTypes.date,
                    current.timezone
                ),
            )
            ForeCastSection(current.current, viewModel, windUnit)
            HourlySection(current.hourly, viewModel, current, tempUnit = tempUnit)
            DailySection(viewModel, current.daily, current, tempUnit = tempUnit)
        }
        if (!isConnected) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    stringResource(R.string.no_internet_connect_to_get_fresh_data),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun DegreeSection(
    current: Current,
    date: String,
    viewModel: HomeViewModel,
    tempUnit: Int
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0x80FFFFFF)
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
            Text(date, fontSize = 24.sp)
            GlideImage(
                model = imageLink(current.weather[0].icon),
                "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp)
            )
            Row {
                Text(viewModel.convertTempUnits(current.temp).toInt().localized(), fontSize = 72.sp)
                Text(stringResource(tempUnit), fontSize = 28.sp)
            }
            Text(current.weather[0].description, fontSize = 24.sp)
        }
    }
}

@Composable
private fun ForeCastSection(
    current: Current,
    viewModel: HomeViewModel,
    windUnit: Int,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ForeCastItem(
            painterResource(R.drawable.ic_wind),
            stringResource(R.string.wind_speed),
            viewModel.convertWindSpeedUnits(current.wind_speed),
            stringResource(windUnit)
        )

        ForeCastItem(
            painterResource(R.drawable.ic_hum),
            stringResource(R.string.humidity),
            current.humidity.localized(),
            "%"
        )
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ForeCastItem(
            painterResource(R.drawable.ic_cloud),
            stringResource(R.string.clouds),
            current.clouds.localized(),
            "%"
        )

        ForeCastItem(
            painterResource(R.drawable.ic_pressure),
            stringResource(R.string.air_pressure),
            current.pressure.localized(),
            stringResource(R.string.hpa)
        )

    }
}


@Composable
private fun LocationSection(country: String) {
    Row {
        Icon(painterResource(R.drawable.ic_location), contentDescription = "")
        Spacer(Modifier.width(4.dp))
        Text(country)
    }
}

@Composable
private fun HourlySection(
    hours: List<Hourly>,
    viewModel: HomeViewModel,
    current: WeatherResponse,
    tempUnit: Int
) {
    Text(stringResource(R.string.next_24_hours), fontSize = 16.sp, fontWeight = FontWeight.Bold)
    LazyRow {
        items(hours.subList(0, 25)) { hour ->
            HourCard(
                time = viewModel.formatTime(hour.dt.toLong(), FormatTypes.hour, current.timezone),
                icon = hour.weather[0].icon,
                temp = viewModel.convertTempUnits(hour.temp).toInt().localized(),
                tempUnit = tempUnit
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun HourCard(
    time: String,
    icon: String,
    temp: String,
    tempUnit: Int
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.25f)

        ),
        modifier = Modifier
            .padding(8.dp)
            .width(75.dp)
            .height(100.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(time, fontSize = 12.sp)
            GlideImage(
                model = imageLink(icon),
                "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(50.dp)
            )
            Row {
                Text(temp, fontSize = 12.sp)
                Text(stringResource(tempUnit), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun DailySection(
    viewModel: HomeViewModel,
    days: List<Daily>,
    current: WeatherResponse,
    tempUnit: Int
) {
    Text(stringResource(R.string.next_7_days), fontSize = 16.sp, fontWeight = FontWeight.Bold)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            itemsIndexed(days.subList(0, 7)) { i, day ->
                DailyCard(
                    dayTitle = when (i) {
                        0 -> stringResource(R.string.today)
                        1 -> stringResource(R.string.tomorrow)
                        else -> viewModel.formatTime(
                            day.dt.toLong(),
                            FormatTypes.day,
                            current.timezone
                        )
                    },
                    description = day.weather[0].description,
                    icon = day.weather[0].icon,
                    min = viewModel.convertTempUnits(day.temp.min).toInt().localized(),
                    max = viewModel.convertTempUnits(day.temp.max).toInt().localized(),
                    unit = stringResource(tempUnit),
                )
            }
        }

    }


}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun DailyCard(
    dayTitle: String,
    description: String,
    min: String,
    max: String,
    unit: String,
    icon: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.25f)

        ),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                dayTitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideImage(
                    model = imageLink(icon),
                    "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(50.dp)
                )
                Text(description, fontSize = 16.sp)
            }
            Text("$min/$max$unit", fontSize = 16.sp)
        }
    }
}

@Composable
private fun ForeCastItem(
    icon: Painter,
    type: String,
    value: String,
    unit: String,
) {
    Row {
        Icon(
            icon, "", Modifier
                .size(20.dp)
                .padding(4.dp)
        )
        Text(text = "$type: ", fontSize = 16.sp)
        Text(text = "$value ", fontSize = 16.sp)
        Text(text = unit, fontSize = 12.sp)
    }
}

