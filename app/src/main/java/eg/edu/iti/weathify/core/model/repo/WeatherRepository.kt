package eg.edu.iti.weathify.core.model.repo

import eg.edu.iti.weathify.core.model.models.WeatherResponse

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: String, lon: String): WeatherResponse
}