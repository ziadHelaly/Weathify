package eg.edu.iti.weathify.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.iti.weathify.R
import eg.edu.iti.weathify.core.model.models.WeatherResponse
import eg.edu.iti.weathify.core.model.repo.WeatherRepository
import eg.edu.iti.weathify.utils.Constants.Companion.LANGUAGE_KEY
import eg.edu.iti.weathify.utils.Constants.Companion.TEMP_KEY
import eg.edu.iti.weathify.utils.Constants.Companion.WIND_KEY
import eg.edu.iti.weathify.utils.ConvertUnitsUtil.toF
import eg.edu.iti.weathify.utils.ConvertUnitsUtil.toK
import eg.edu.iti.weathify.utils.ConvertUnitsUtil.toKmH
import eg.edu.iti.weathify.utils.ConvertUnitsUtil.toMH
import eg.edu.iti.weathify.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class HomeViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _currentWeather = MutableStateFlow<Result<WeatherResponse>>(Result.Loading)
    val currentWeather: StateFlow<Result<WeatherResponse>> = _currentWeather

    private val _tempUnit = MutableStateFlow(R.string.c)
    val tempUnit: StateFlow<Int> = _tempUnit

    private val _windUnit = MutableStateFlow(R.string.kmh)
    val windUnit: StateFlow<Int> = _windUnit

    init {
        loadSettings()
    }

    fun getWeather(long: String, lat: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val lang = repository.getFromSharedPref(LANGUAGE_KEY)
            val l = when (lang) {
                R.string.arabic -> "ar"
                R.string.english -> "en"
                else -> "en"
            }
            val result = repository.getCurrentWeather(lat, long, l)
            if (result is Result.Success) {
                _currentWeather.value = Result.Success(result.data)
            }
        }
    }

    private fun loadSettings() {
        repository.getFromSharedPref(TEMP_KEY).let {
            if (it != 2002) {
                _tempUnit.value = it
            }
        }
        repository.getFromSharedPref(WIND_KEY).let {
            if (it != 2002) {
                _windUnit.value = it
            }
        }
    }

    fun convertWindSpeedUnits(original: Double): String {
        return when (_windUnit.value) {
            R.string.kmh -> String.format(locale = Locale.getDefault(), "%.2f", toKmH(original))
            R.string.mh -> String.format(locale = Locale.getDefault(), "%.2f", toMH(original))
            else -> original.toString()
        }
    }

    fun convertTempUnits(original: Double): Double {
        return when (_tempUnit.value) {
            R.string.k -> toK(original)
            R.string.f -> toF(original)
            else -> original
        }
    }

    fun formatTime(timestamp: Long, formatType: FormatTypes, timeZone: String): String {
        val dt = Date(timestamp * 1000)
        val formatter = SimpleDateFormat(
            when (formatType) {
                FormatTypes.date -> "EEE, MMM yyyy"
                FormatTypes.hour -> "hh:mm a"
                FormatTypes.day -> "EEE"
                FormatTypes.hour24 -> "HH"
                FormatTypes.All -> "EEE, MMM yyyy , HH:mm"
            }, Locale.getDefault()
        )
        formatter.timeZone = TimeZone.getTimeZone(timeZone)

        return formatter.format(dt)
    }
}
