package eg.edu.iti.weathify.core.model.repo


import android.util.Log
import eg.edu.iti.weathify.core.model.local.WeatherLocalDataSource
import eg.edu.iti.weathify.core.model.models.Alarm
import eg.edu.iti.weathify.core.model.models.FavouritePlace
import eg.edu.iti.weathify.core.model.models.SearchResponse
import eg.edu.iti.weathify.core.model.models.WeatherResponse
import eg.edu.iti.weathify.core.model.remote.WeatherRemoteDataSource
import eg.edu.iti.weathify.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl private constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource
) : WeatherRepository {
    companion object {
        private var INSTANCE: WeatherRepositoryImpl? = null
        fun getInstance(
            remoteDataSource: WeatherRemoteDataSource,
            localDataSource: WeatherLocalDataSource
        ): WeatherRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                val temp = WeatherRepositoryImpl(
                    remoteDataSource,
                    localDataSource
                )
                INSTANCE = temp
                temp
            }
        }
    }

    override suspend fun getCurrentWeather(lat: String, lon: String,lang:String): Result<WeatherResponse> {
        Log.d("`TAG`", "getCurrentWeather: call fired")
        return remoteDataSource.getAllWeatherData(lat, lon,lang)
    }

    override suspend fun getFavourites(): Flow<List<FavouritePlace>> {
        return localDataSource.getAllFavourites()
    }

    override suspend fun addPlaceToFav(place: FavouritePlace): Long {
        return localDataSource.addPlaceToFav(place)
    }

    override suspend fun removePlaceFromFav(place: FavouritePlace): Int {
        return localDataSource.removeFromFav(place)
    }


    override fun getCities(query: String): Flow<List<SearchResponse>> = flow {
        try {
            emit(remoteDataSource.searchCity(query))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    override fun getFromSharedPref(key: String): Int {
        return localDataSource.getFromSharedPref(key)
    }

    override fun saveInSharedPref(key: String, value: Int) {
        localDataSource.saveInSharedPref(key, value)
    }
    override suspend fun getAlarms(): Flow<List<Alarm>> {
        return localDataSource.getAlarms()
    }

    override suspend fun addAlarm(alarm: Alarm): Long {
        return localDataSource.addAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm): Int {
        return localDataSource.deleteAlarm(alarm)
    }

    override suspend fun deleteAlarmById(id: String) {
        return localDataSource.deleteAlarmById(id)
    }

}
