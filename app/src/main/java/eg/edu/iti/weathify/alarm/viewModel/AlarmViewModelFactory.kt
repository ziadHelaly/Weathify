package eg.edu.iti.weathify.alarm.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.edu.iti.weathify.core.model.repo.WeatherRepository

class AlarmViewModelFactory (private val repository: WeatherRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmViewModel(repository) as T
    }
}