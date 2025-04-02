package eg.edu.iti.weathify.settings.view


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.iti.weathify.R
import eg.edu.iti.weathify.settings.viewModel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val temp by viewModel.currentTempUnit.collectAsStateWithLifecycle()
    val wind by viewModel.currentWindUnit.collectAsStateWithLifecycle()
    val location by viewModel.currentLocationSource.collectAsStateWithLifecycle()
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.titleLarge)
        SettingsSection(stringResource(R.string.language), language, viewModel.languagesOptions) {
            viewModel.updateLanguageSetting(it)
        }
        SettingsSection(stringResource(R.string.temp_unit), temp, viewModel.tempOptions) {
            viewModel.updateTempSetting(it)
        }
        SettingsSection(stringResource(R.string.wind_speed_unit), wind, viewModel.windOptions) {
            viewModel.updateWindSetting(it)
        }
        SettingsSection(stringResource(R.string.location_provider), location, viewModel.locationOptions) {
            viewModel.updateLocationSetting(it)
        }
    }
}

@Composable
private fun SettingsSection(
    label: String,
    defaultValue: Int,
    optionsList: List<Int>,
    onClick: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(0.25f))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(0.75f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .clickable { expanded = !expanded }
                    .background(MaterialTheme.colorScheme.secondaryContainer)

            ) {
                Text(stringResource(defaultValue), style = MaterialTheme.typography.labelMedium)
            }

            DropdownMenu(expanded, { expanded = false }) {
                optionsList.forEach { option ->
                    DropdownMenuItem(text = { Text(stringResource(option)) }, onClick = {
                        onClick(option)
                        expanded = !expanded
                    })
                }
            }
        }
    }
}