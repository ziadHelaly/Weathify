package eg.edu.iti.weathify.core.model.remote

import eg.edu.iti.weathify.core.model.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {
    @GET("onecall")
    suspend fun getAllWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = "minutely,alerts",
        @Query("appid") appid: String = "",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ): Response<WeatherResponse>
}
