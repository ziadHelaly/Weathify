package eg.edu.iti.weathify.alarm.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import eg.edu.iti.weathify.R
import eg.edu.iti.weathify.alarm.viewModel.AlarmViewModel
import eg.edu.iti.weathify.core.view.components.FAB
import eg.edu.iti.weathify.worker.NotificationWorker
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlarmScreen(viewModel: AlarmViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf("") }
    val minutes by viewModel.timeDifferenceMinutes.collectAsStateWithLifecycle()
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
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showDialog) {
                AlarmDialog(
                    viewModel = viewModel,
                    onDismiss = { showDialog = false },
                    onConfirm = { selectedData ->
                        result = selectedData
                        showDialog = false
                        if (minutes != null){
                            val workManger = WorkManager.getInstance(context)
                            val alarm= OneTimeWorkRequestBuilder<NotificationWorker>()
                                .setInitialDelay(minutes!!,TimeUnit.MINUTES)
                                .build()
                            workManger.enqueue(alarm)
                        }
                    }
                )
            }
        }
    }
}


@Composable
private fun FavouriteItem(
    modifier: Modifier = Modifier,
    name: String,
    onDisplayClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .clickable { onDisplayClick() }
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.8f)
            )
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