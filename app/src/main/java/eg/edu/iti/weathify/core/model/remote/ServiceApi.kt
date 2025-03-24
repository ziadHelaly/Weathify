package eg.edu.iti.weathify.core.model.remote

import android.os.Build
import eg.edu.iti.weathify.core.model.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {
    @GET("onecall")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = "minutely,alerts,daily,hourly",
        @Query("appid") appid: String="",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ):WeatherResponse
}
