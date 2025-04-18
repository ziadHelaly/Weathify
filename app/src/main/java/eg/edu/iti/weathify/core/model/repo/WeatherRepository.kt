package eg.edu.iti.weathify.core.model.repo

import eg.edu.iti.weathify.core.model.models.Alarm
import eg.edu.iti.weathify.core.model.models.FavouritePlace
import eg.edu.iti.weathify.core.model.models.SearchResponse
import eg.edu.iti.weathify.core.model.models.WeatherResponse
import eg.edu.iti.weathify.utils.Result
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: String, lon: String,lang:String): Result<WeatherResponse>

    suspend fun getFavourites(): Flow<List<FavouritePlace>>
    suspend fun addPlaceToFav(place: FavouritePlace): Long
    suspend fun removePlaceFromFav(place: FavouritePlace): Int

    fun getCities(query: String): Flow<List<SearchResponse>>

    fun getFromSharedPref(key: String): Int
    fun saveInSharedPref(key: String, value: Int)
    suspend fun getAlarms(): Flow<List<Alarm>>
    suspend fun addAlarm(alarm: Alarm): Long
    suspend fun deleteAlarm(alarm: Alarm): Int
    suspend fun deleteAlarmById(id: String): Int
    fun saveCityInSHP(lon: String, lat: String)
    fun getCityFromSHP(): Pair<String?, String?>
}