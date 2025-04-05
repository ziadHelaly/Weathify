package eg.edu.iti.weathify.core.model.repo

import eg.edu.iti.weathify.core.model.local.WeatherLocalDataSource
import eg.edu.iti.weathify.core.model.models.Alarm
import eg.edu.iti.weathify.core.model.models.FavouritePlace
import kotlinx.coroutines.flow.Flow

class FakeLocalDataSource(
    private val favsDataSource: MutableList<FavouritePlace>,
    private val alarmDataSource: MutableList<Alarm>
) :
    WeatherLocalDataSource {
    override suspend fun getAllFavourites(): Flow<List<FavouritePlace>> {
        TODO("Not yet implemented")
    }

    override suspend fun addPlaceToFav(place: FavouritePlace): Long {
        return if (place !in favsDataSource) {
            favsDataSource.add(place)
            1L
        } else {
            0L
        }
    }

    override suspend fun removeFromFav(place: FavouritePlace): Int {
        TODO("Not yet implemented")
    }

    override fun getFromSharedPref(key: String): Int {
        TODO("Not yet implemented")
    }

    override fun saveInSharedPref(key: String, value: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getAlarms(): Flow<List<Alarm>> {
        TODO("Not yet implemented")
    }

    override suspend fun addAlarm(alarm: Alarm): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlarm(alarm: Alarm): Int {
        return if (alarm in alarmDataSource) {
            alarmDataSource.remove(alarm)
            1
        } else {
            0
        }
    }

    override suspend fun deleteAlarmById(id: String): Int {
        TODO("Not yet implemented")
    }

    override fun saveCityInSharedPref(lon: String, lat: String) {
        TODO("Not yet implemented")
    }

    override fun getCityFromSharedPref(): Pair<String?, String?> {
        TODO("Not yet implemented")
    }
}