package eg.edu.iti.weathify.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.iti.weathify.core.model.models.WeatherResponse
import eg.edu.iti.weathify.core.model.repo.WeatherRepository
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


    fun getWeather(long: String, lat: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getCurrentWeather(lat, long)
            if (result is Result.Success) {
                _currentWeather.value = Result.Success(result.data)
            }
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
