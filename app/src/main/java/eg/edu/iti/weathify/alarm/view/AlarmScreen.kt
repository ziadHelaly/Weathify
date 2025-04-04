package eg.edu.iti.weathify.alarm.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import eg.edu.iti.weathify.R
import eg.edu.iti.weathify.alarm.viewModel.AlarmViewModel
import eg.edu.iti.weathify.core.model.models.Alarm
import eg.edu.iti.weathify.core.view.components.FAB
import eg.edu.iti.weathify.utils.Constants
import eg.edu.iti.weathify.utils.WorkMangerHelper.addWorker
import eg.edu.iti.weathify.utils.WorkMangerHelper.cancelWorker
import eg.edu.iti.weathify.worker.NotificationWorker
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlarmScreen(viewModel: AlarmViewModel, currentLongitude: String, currentLatitude: String) {
    var showDialog by remember { mutableStateOf(false) }
    val minutes by viewModel.timeDifferenceMinutes.collectAsStateWithLifecycle()
    val currentCity by viewModel.currentCity.collectAsStateWithLifecycle()
    val selectedCity by viewModel.selectedCity.collectAsStateWithLifecycle()
    val alarms by viewModel.alarms.collectAsStateWithLifecycle()
    val type by viewModel.alarmType.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = {
            FAB {
                showDialog = true
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.alarms), style = MaterialTheme.typography.titleLarge)
            if (alarms.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(alarms) { alarm ->
                        AlarmItem(
                            time = viewModel.formatTime(alarm.date),
                            city = alarm.city
                        ) {
                            viewModel.deleteAlarm(alarm)
                            cancelWorker(context, alarm.id)

                        }
                    }
                }

            }else{
                Text(stringResource(R.string.no_active_alarms), style = MaterialTheme.typography.bodyLarge)
            }
            if (showDialog) {
                AlarmDialog(
                    viewModel = viewModel,
                    onDismiss = { showDialog = false },
                    onConfirm = { selectedData ->
                        showDialog = false
                        if (minutes != null) {
                            val data = if (currentCity == "current location") {
                                Data.Builder()
                                    .putString(Constants.LONGITUDE_KEY, currentLongitude)
                                    .putString(Constants.LATITUDE_KEY, currentLatitude).build()
                            } else {
                                Data.Builder()
                                    .putString(Constants.LONGITUDE_KEY, selectedCity.longitude)
                                    .putString(Constants.LATITUDE_KEY, selectedCity.latitude)
                                    .build()
                            }
                            val alarm = if (type == R.string.notification) {
                                OneTimeWorkRequestBuilder<NotificationWorker>()
                                    .setInitialDelay(minutes!!, TimeUnit.MINUTES)
                                    .setConstraints(
                                        Constraints.Builder()
                                            .setRequiredNetworkType(NetworkType.CONNECTED).build()
                                    )
                                    .setInputData(data)
                                    .build()
                            } else{
                                OneTimeWorkRequestBuilder<NotificationWorker>()
                                    .setInitialDelay(minutes!!, TimeUnit.MINUTES)
                                    .setConstraints(
                                        Constraints.Builder()
                                            .setRequiredNetworkType(NetworkType.CONNECTED).build()
                                    )
                                    .setInputData(data)
                                    .build()
                            }
                            addWorker(worker = alarm, context = context)

                            viewModel.saveAlarm(
                                Alarm(
                                    alarm.id.toString(),
                                    selectedData.atZone(ZoneId.systemDefault()).toInstant()
                                        .toEpochMilli(),
                                    currentCity
                                )
                            )
                        }
                    }
                )
            }

        }
    }
}


@Composable
private fun AlarmItem(
    time: String,
    city: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.8f)
            ) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = city,
                    style = MaterialTheme.typography.bodyMedium

                )

            }
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                "",
                modifier = Modifier
                    .clickable { onClick() }
                    .padding(8.dp)
                    .weight(0.2f)
            )
        }

    }
}