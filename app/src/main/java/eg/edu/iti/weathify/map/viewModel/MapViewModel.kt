package eg.edu.iti.weathify.map.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.iti.weathify.core.model.models.FavouritePlace
import eg.edu.iti.weathify.core.model.models.SearchResponse
import eg.edu.iti.weathify.core.model.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MapViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _searchResults = MutableStateFlow<List<SearchResponse>>(emptyList())
    private val _selectedLocation =
        MutableStateFlow<Triple<Double, Double, String>>(Triple(30.0444, 31.2357, ""))

    val searchResults: StateFlow<List<SearchResponse>> = _searchResults.asStateFlow()
    val selectedLocation: StateFlow<Triple<Double, Double, String>> = _selectedLocation.asStateFlow()

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .filter { it.isNotEmpty() }
                .flatMapLatest { query ->
                    Log.d("`TAG`", "view model flow fire ")
                    repository.getCities(query)
                }
                .collect { results ->
                    if (results.size > 5) {
                        _searchResults.value = results.subList(0, 5)
                    } else {
                        _searchResults.value = results
                    }
                    Log.d("``TAG``", "viewmodel response : $results")
                }
        }
    }

    fun addToFav(lat:Double,lon: Double,name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res=repository.addPlaceToFav(FavouritePlace(lon.toString(),lat.toString(),name))
            if (res>0){
                Log.d("```TAG```", "addToFav: Success")
            }else{
                Log.d("```TAG```", "addToFav: Failed")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchQuery.emit(query)
        }
    }

    fun selectLocation(lat: Double, lon: Double,name:String) {
        _selectedLocation.value = Triple(lat, lon,name)
        _searchResults.value = emptyList()
    }
    fun saveInShPr(lon:String,lat:String){
        repository.saveCityInSHP(lon,lat)
    }
}