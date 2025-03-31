package eg.edu.iti.weathify.core.model.remote

import eg.edu.iti.weathify.core.model.models.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("search")
    suspend fun searchCity(
        @Query("q") city: String,
        @Query("format") format: String = "json"
    ): List<SearchResponse>
}