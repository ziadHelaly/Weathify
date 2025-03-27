package eg.edu.iti.weathify.core.model.repo

import android.location.Location
import eg.edu.iti.weathify.core.model.models.WeatherResponse
import eg.edu.iti.weathify.utils.Result

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: String, lon: String): Result<WeatherResponse>

}