package eg.edu.iti.weathify.map.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.edu.iti.weathify.core.model.repo.WeatherRepository
import eg.edu.iti.weathify.home.viewmodel.HomeViewModel

class MapViewModelFactory(private val repository: WeatherRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(repository) as T
    }
}