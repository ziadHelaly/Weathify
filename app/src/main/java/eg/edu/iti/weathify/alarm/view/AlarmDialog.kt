package eg.edu.iti.weathify.alarm.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eg.edu.iti.weathify.R
import eg.edu.iti.weathify.alarm.viewModel.AlarmViewModel
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlarmDialog(
    viewModel: AlarmViewModel,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {

    val context = LocalContext.current

    val selectedDateTime by viewModel.selectedDateTime.collectAsState()
    val inputData by viewModel.inputData.collectAsState()
    val timeDifference by viewModel.timeDifferenceMinutes.collectAsState()
    val isTimeValid by viewModel.isTimeValid.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, day ->
                viewModel.updateDate(year, month, day)
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }

    if (showTimePicker) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, hour, minute ->
                viewModel.updateTime(hour, minute)
                showTimePicker = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.enter_details)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(onClick = { showDatePicker = true }) {
                    Text(
                        text = selectedDateTime?.format(
                            DateTimeFormatter.ofPattern(
                                "dd/MM/yyyy",
                                Locale.getDefault()
                            )
                        ) ?: stringResource(R.string.select_date)
                    )
                }

                Button(onClick = { showTimePicker = true }) {
                    Text(
                        text = selectedDateTime?.format(
                            DateTimeFormatter.ofPattern(
                                "HH:mm",
                                Locale.getDefault()
                            )
                        )
                            ?: stringResource(R.string.select_time)
                    )
                }

                if (timeDifference != null) {
                    Text(
                        text = if (isTimeValid) {
                            stringResource(R.string.ok)
                        } else {
                            stringResource(R.string.selected_time_is_in_the_past)
                        },
                        color = if (isTimeValid) Color.Green else Color.Red
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(viewModel.getResult())
                },
                enabled = isTimeValid
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}