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

}
