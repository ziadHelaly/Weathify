package eg.edu.iti.weathify.core.model.remote

import eg.edu.iti.weathify.core.model.models.WeatherResponse

class WeatherRemoteDataSourceImpl(private val serviceApi: ServiceApi) : WeatherRemoteDataSource {
    override suspend fun getCurrentWeather(lat: String, lon: String): WeatherResponse {
        return serviceApi.getCurrentWeather(lat, lon)
    }
}