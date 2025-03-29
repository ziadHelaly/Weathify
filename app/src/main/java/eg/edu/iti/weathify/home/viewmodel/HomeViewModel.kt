package eg.edu.iti.weathify.home.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.edu.iti.weathify.core.model.models.Current
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
    fun formatDate(timestamp: Long): String {
        val dt = Date(timestamp * 1000)
        val formatter = SimpleDateFormat("EEE, MMM yyyy", Locale.getDefault())
        return formatter.format(dt)
    }
    fun formatHour(timestamp: Long): String {
        val dt = Date(timestamp * 1000)
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return formatter.format(dt)
    }
    fun formatDay(timestamp: Long): String {
        val dt = Date(timestamp * 1000)
        val formatter = SimpleDateFormat("EEE", Locale.getDefault())
        return formatter.format(dt)
    }

}
