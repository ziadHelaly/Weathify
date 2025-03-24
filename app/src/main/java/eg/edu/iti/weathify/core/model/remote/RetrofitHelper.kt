package eg.edu.iti.weathify.core.model.remote

import eg.edu.iti.weathify.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    val retrofitInstance = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val serviceApi= retrofitInstance.create(ServiceApi::class.java)

}