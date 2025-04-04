package eg.edu.iti.weathify.favourite.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.iti.weathify.core.model.models.FavouritePlace
import eg.edu.iti.weathify.core.model.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavouriteViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _allFavourite = MutableStateFlow<List<FavouritePlace>>(arrayListOf())
    val allFavourites: StateFlow<List<FavouritePlace>> = _allFavourite

    init {
        getFavourites()
    }

    private fun getFavourites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavourites().collect {
                _allFavourite.value = it
            }
        }
    }
    fun addToFav(place: FavouritePlace){
        viewModelScope.launch (Dispatchers.IO){
            val result=repository.addPlaceToFav(place)
//            if (result>0)
        }
    }
    fun removeFromFavs(place: FavouritePlace){
        viewModelScope.launch(Dispatchers.IO) {
            val result=repository.removePlaceFromFav(place)
            if (result>0){
                Log.d("`TAG`", "removeFromFavs: Done")
            }
        }
    }
}