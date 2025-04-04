package eg.edu.iti.weathify.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import eg.edu.iti.weathify.BuildConfig
import eg.edu.iti.weathify.R
import eg.edu.iti.weathify.core.model.local.WeatherDataBase
import eg.edu.iti.weathify.core.model.local.WeatherLocalDataSourceImpl
import eg.edu.iti.weathify.core.model.remote.RetrofitHelper
import eg.edu.iti.weathify.core.model.remote.WeatherRemoteDataSourceImpl
import eg.edu.iti.weathify.core.model.repo.WeatherRepository
import eg.edu.iti.weathify.core.model.repo.WeatherRepositoryImpl
import eg.edu.iti.weathify.utils.Constants
import eg.edu.iti.weathify.utils.LocaleHelper
import eg.edu.iti.weathify.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

class NotificationWorker(private val context: Context, private val workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "weather_channel"

    private val repo: WeatherRepository = WeatherRepositoryImpl.getInstance(
        remoteDataSource = WeatherRemoteDataSourceImpl(
            RetrofitHelper.serviceApi,
            RetrofitHelper.searchApi
        ),
        localDataSource = WeatherLocalDataSourceImpl(
            favouritesDao = WeatherDataBase.getInstance(
                context
            ).getFavouriteDao(),
            alarmDao = WeatherDataBase.getInstance(context).getAlarmDao(),
            sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        )
    )

    override suspend fun doWork(): Result {
        createNotificationChannel()

        val lon = inputData.getString(Constants.LONGITUDE_KEY) ?: return Result.failure()
        val lat = inputData.getString(Constants.LATITUDE_KEY) ?: return Result.failure()
//        val lang = LocaleHelper.getSavedLanguage(context)
        val id = workerParams.id.toString()
        return try {
            val response = repo.getCurrentWeather(lat, lon, "en")


            if (response is eg.edu.iti.weathify.utils.Result.Success) {
                val weatherData = response.data
                val description = weatherData.current.weather[0].description
                val temperature = weatherData.current.temp.toInt()
                val city = getCityName(lat.toDouble(), lon.toDouble()) ?: "Unknown"

                showNotification(description, temperature, city)
                repo.deleteAlarmById(id)
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private suspend fun getCityName(lat: Double, lon: Double): String? {
        return withContext(Dispatchers.IO) {
            try {
                val addresses = Geocoder(context, Locale.getDefault())
                    .getFromLocation(lat, lon, 1)
                addresses?.get(0)?.countryName
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Weathify Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Weather Alerts"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(description: String, temperature: Int, city: String) {
        val soundUri =
            Uri.parse("android.resource://${applicationContext.packageName}/raw/notification_sound")

        val notification: Notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_app_icon)
            .setContentTitle("Weather Update")
            .setContentText("Today's weather: $description, $temperature°C in $city")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(soundUri)
            .build()

        notificationManager.notify(1, notification)
    }
}
