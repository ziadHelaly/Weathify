package eg.edu.iti.weathify.core.model.remote

import eg.edu.iti.weathify.BuildConfig
import eg.edu.iti.weathify.core.model.models.SearchResponse
import eg.edu.iti.weathify.core.model.models.WeatherResponse
import eg.edu.iti.weathify.utils.Result

class WeatherRemoteDataSourceImpl(
    private val serviceApi: ServiceApi,
    private val searchApi: SearchApi
) : WeatherRemoteDataSource {
    override suspend fun getAllWeatherData(lat: String, lon: String): Result<WeatherResponse> {

        return try {
            val result = serviceApi.getAllWeatherData(lat, lon, appid = BuildConfig.MY_API_KEY)
            if (result.isSuccessful) {
                result.body()?.let {
                    Result.Success(it)
                } ?: Result.Failure(result.message())

            } else {
                Result.Failure(result.message())
            }
        } catch (e: Exception) {
            Result.Failure(e.message.toString())
        }
    }

    override suspend fun searchCity(city: String): List<SearchResponse> {
        return searchApi.searchCity(city)
    }
}