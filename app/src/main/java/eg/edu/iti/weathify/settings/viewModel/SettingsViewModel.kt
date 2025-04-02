package eg.edu.iti.weathify.settings.viewModel

import androidx.lifecycle.ViewModel
import eg.edu.iti.weathify.R
import eg.edu.iti.weathify.core.model.repo.WeatherRepository
import eg.edu.iti.weathify.utils.Constants.Companion.LANGUAGE_KEY
import eg.edu.iti.weathify.utils.Constants.Companion.LOCATION_KEY
import eg.edu.iti.weathify.utils.Constants.Companion.TEMP_KEY
import eg.edu.iti.weathify.utils.Constants.Companion.WIND_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _currentTempUnit = MutableStateFlow(R.string.c)
    val currentTempUnit: StateFlow<Int> = _currentTempUnit

    private val _currentWindUnit = MutableStateFlow(R.string.kmh)
    val currentWindUnit: StateFlow<Int> = _currentWindUnit

    private val _currentLanguage = MutableStateFlow(R.string.device_default)
    val currentLanguage: StateFlow<Int> = _currentLanguage

    private val _currentLocationSource = MutableStateFlow(R.string.gps)
    val currentLocationSource: StateFlow<Int> = _currentLocationSource

    val locationOptions = listOf(
        R.string.gps,
        R.string.map
    )
    val tempOptions = listOf(
        R.string.c,
        R.string.k,
        R.string.f
    )
    val languagesOptions = listOf(
        R.string.device_default,
        R.string.english,
        R.string.arabic
    )
    val windOptions = listOf(
        R.string.ms,
        R.string.kmh,
        R.string.mh
    )

    init {
        loadCurrentSettings()
    }

    private fun loadCurrentSettings() {
        repository.getFromSharedPref(TEMP_KEY)?.let {
            if (it != "N/A") {
                _currentTempUnit.value = it.toInt()
            }
        }
        repository.getFromSharedPref(LOCATION_KEY)?.let {
            if (it != "N/A") {
                _currentLocationSource.value = it.toInt()
            }
        }
        repository.getFromSharedPref(LANGUAGE_KEY)?.let {
            if (it != "N/A") {
                _currentLanguage.value = it.toInt()
            }
        }
        repository.getFromSharedPref(WIND_KEY)?.let {
            if (it != "N/A") {
                _currentWindUnit.value = it.toInt()
            }
        }
    }

    fun updateTempSetting(value: Int) {
        _currentTempUnit.value = value
        repository.saveInSharedPref(TEMP_KEY,value.toString())
    }
    fun updateWindSetting(value: Int) {
        _currentWindUnit.value = value
        repository.saveInSharedPref(WIND_KEY,value.toString())
    }
    fun updateLocationSetting(value: Int) {
        _currentLocationSource.value = value
        repository.saveInSharedPref(LOCATION_KEY,value.toString())
    }
    fun updateLanguageSetting(value: Int) {
        _currentLanguage.value = value
        repository.saveInSharedPref(LANGUAGE_KEY,value.toString())
    }


}