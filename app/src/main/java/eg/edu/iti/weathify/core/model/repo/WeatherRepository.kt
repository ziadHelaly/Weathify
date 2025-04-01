package eg.edu.iti.weathify.core.model.repo

import android.location.Location
import eg.edu.iti.weathify.core.model.models.FavouritePlace
import eg.edu.iti.weathify.core.model.models.SearchResponse
import eg.edu.iti.weathify.core.model.models.WeatherResponse
import eg.edu.iti.weathify.utils.Result
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: String, lon: String): Result<WeatherResponse>

    suspend fun getFavourites(): Flow<List<FavouritePlace>>
    suspend fun addPlaceToFav(place: FavouritePlace): Long
    suspend fun removePlaceFromFav(place: FavouritePlace): Int

    fun getCities(query: String): Flow<List<SearchResponse>>

    fun getFromSharedPref(key: String): String?
    fun saveInSharedPref(key: String, value: String)
}