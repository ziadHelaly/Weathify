package eg.edu.iti.weathify.worker

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.PowerManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import eg.edu.iti.weathify.alert.AlertActivity
import eg.edu.iti.weathify.alert.AlertReceiver
import eg.edu.iti.weathify.core.model.local.WeatherDataBase
import eg.edu.iti.weathify.core.model.local.WeatherLocalDataSourceImpl
import eg.edu.iti.weathify.core.model.remote.RetrofitHelper
import eg.edu.iti.weathify.core.model.remote.WeatherRemoteDataSourceImpl
import eg.edu.iti.weathify.core.model.repo.WeatherRepository
import eg.edu.iti.weathify.core.model.repo.WeatherRepositoryImpl
import eg.edu.iti.weathify.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale

class AlertWorker(private val context: Context, private val workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

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

    @SuppressLint("ScheduleExactAlarm")
    override suspend fun doWork(): Result {
        val lon = inputData.getString(Constants.LONGITUDE_KEY) ?: return Result.failure()
        val lat = inputData.getString(Constants.LATITUDE_KEY) ?: return Result.failure()
        val id = workerParams.id.toString()
        return try {
            val response = repo.getCurrentWeather(lat, lon, "en")


            if (response is eg.edu.iti.weathify.utils.Result.Success) {
                val weatherData = response.data
                val description = weatherData.current.weather[0].description
                val temp = weatherData.current.temp.toInt()
                val city = getCityName(lat.toDouble(), lon.toDouble()) ?: "Unknown"
                val intent = Intent(applicationContext, AlertReceiver::class.java).apply {
                    putExtra("description", description)
                    putExtra("degree", temp)
                    putExtra("city", city)
                }


                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val triggerTime = Calendar.getInstance().timeInMillis + 1000

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
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



}