package eg.edu.iti.weathify.core.model.local

import android.content.SharedPreferences
import eg.edu.iti.weathify.core.model.models.Alarm
import eg.edu.iti.weathify.core.model.models.FavouritePlace
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl(
    private val favouritesDao: FavouritesDao,
    private val alarmDao: AlarmDao,
    private val sharedPreferences: SharedPreferences
) : WeatherLocalDataSource {

    override suspend fun getAllFavourites(): Flow<List<FavouritePlace>> {
        return favouritesDao.getAllFavourites()
    }

    override suspend fun addPlaceToFav(place: FavouritePlace): Long {
        return favouritesDao.addPlaceToFavourite(place)
    }

    override suspend fun removeFromFav(place: FavouritePlace): Int {
        return favouritesDao.removeFromFavourite(place)
    }

    override fun saveInSharedPref(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override fun getFromSharedPref(key: String): Int {
        return sharedPreferences.getInt(key, 2002)
    }

    override fun saveCityInSharedPref(lon: String, lat: String) {
        sharedPreferences.edit().putString("city_lon", lon).apply()
        sharedPreferences.edit().putString("city_lat", lat).apply()
    }

    override fun getCityFromSharedPref(): Pair<String?, String?> {
        return sharedPreferences.getString(
            "city_lon",
            null
        ) to sharedPreferences.getString("city_lat", null)
    }

    override suspend fun getAlarms(): Flow<List<Alarm>> {
        return alarmDao.getAlarms()
    }

    override suspend fun addAlarm(alarm: Alarm): Long {
        return alarmDao.insertAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm): Int {
        return alarmDao.deleteAlarm(alarm)
    }

    override suspend fun deleteAlarmById(id: String): Int {
        return alarmDao.deleteAlarmById(id)
    }

}