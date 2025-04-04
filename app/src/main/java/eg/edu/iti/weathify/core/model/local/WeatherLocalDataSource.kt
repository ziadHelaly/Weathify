package eg.edu.iti.weathify.core.model.local

import eg.edu.iti.weathify.core.model.models.Alarm
import eg.edu.iti.weathify.core.model.models.FavouritePlace
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {

    suspend fun getAllFavourites(): Flow<List<FavouritePlace>>
    suspend fun addPlaceToFav(place: FavouritePlace): Long
    suspend fun removeFromFav(place: FavouritePlace): Int
    fun getFromSharedPref(key: String): Int
    fun saveInSharedPref(key: String, value: Int)
    suspend fun getAlarms(): Flow<List<Alarm>>
    suspend fun addAlarm(alarm: Alarm): Long
    suspend fun deleteAlarm(alarm: Alarm): Int
    suspend fun deleteAlarmById(id: String)
}