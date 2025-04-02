package eg.edu.iti.weathify.settings.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.edu.iti.weathify.core.model.repo.WeatherRepository

class SettingsViewModelFactory(val repository: WeatherRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(repository) as T
    }
}