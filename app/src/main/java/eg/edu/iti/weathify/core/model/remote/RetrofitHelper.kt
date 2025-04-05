package eg.edu.iti.weathify.core.model.remote

import android.content.Context
import eg.edu.iti.weathify.utils.Constants.Companion.BASE_URL
import eg.edu.iti.weathify.utils.Constants.Companion.SEARCH_URL
import eg.edu.iti.weathify.utils.NetworkUtil.isConnected
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object RetrofitHelper {

    private const val CACHE_SIZE = (5 * 1024 * 1024).toLong()

    private fun getCache(context: Context): Cache {
        val cacheDir = File(context.cacheDir, "http_cache")
        return Cache(cacheDir, CACHE_SIZE)
    }



    private fun getClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(getCache(context))
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (isConnected(context)) {
                    request.newBuilder()
                        .header("Cache-Control", "public, max-age=60")
                        .build()
                } else {
                    request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=86400")
                        .build()
                }
                chain.proceed(request)
            }
            .build()
    }

    fun createApis(context: Context) {
        val client = getClient(context)

        val weatherRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val nominatimRetrofit = Retrofit.Builder()
            .baseUrl(SEARCH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        serviceApi = weatherRetrofit.create(ServiceApi::class.java)
        searchApi = nominatimRetrofit.create(SearchApi::class.java)
    }

    lateinit var serviceApi: ServiceApi
    lateinit var searchApi: SearchApi
}