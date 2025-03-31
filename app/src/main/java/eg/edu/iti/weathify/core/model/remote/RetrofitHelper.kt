package eg.edu.iti.weathify.core.model.remote

import eg.edu.iti.weathify.utils.Constants.Companion.BASE_URL
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private val client = OkHttpClient.Builder()
        .build()

    val weatherRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/") // ✅ Correct base URL
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val nominatimRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://nominatim.openstreetmap.org/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val serviceApi: ServiceApi = weatherRetrofit.create(ServiceApi::class.java)
    val searchApi: SearchApi = nominatimRetrofit.create(SearchApi::class.java)
}