package eg.edu.iti.weathify.core.model.repo


import eg.edu.iti.weathify.core.model.local.WeatherLocalDataSource
import eg.edu.iti.weathify.core.model.models.WeatherResponse
import eg.edu.iti.weathify.core.model.remote.WeatherRemoteDataSource
import eg.edu.iti.weathify.utils.Result

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

    override suspend fun getCurrentWeather(lat: String, lon: String): Result<WeatherResponse> {
        return remoteDataSource.getAllWeatherData(lat, lon)
    }



}