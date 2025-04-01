package eg.edu.iti.weathify.favourite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.edu.iti.weathify.core.model.repo.WeatherRepository

class FavouriteViewModelFactory(private val repository: WeatherRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavouriteViewModel(repository) as T
    }
}