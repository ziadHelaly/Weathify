package eg.edu.iti.weathify.core.model.remote

import eg.edu.iti.weathify.core.model.models.SearchResponse
import eg.edu.iti.weathify.core.model.models.WeatherResponse
import eg.edu.iti.weathify.utils.Result


interface WeatherRemoteDataSource {
    suspend fun searchCity(city: String): List<SearchResponse>
    suspend fun getAllWeatherData(lat: String, lon: String, lang: String): Result<WeatherResponse>
}