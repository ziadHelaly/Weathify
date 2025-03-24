package eg.edu.iti.weathify.core.model.remote

import eg.edu.iti.weathify.core.model.models.WeatherResponse

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(lat: String, lon: String): WeatherResponse
}