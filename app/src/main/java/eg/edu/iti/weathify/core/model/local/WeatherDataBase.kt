package eg.edu.iti.weathify.core.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eg.edu.iti.weathify.core.model.models.Alarm
import eg.edu.iti.weathify.core.model.models.FavouritePlace

@Database(entities = [FavouritePlace::class, Alarm::class], version = 2)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getFavouriteDao(): FavouritesDao
    abstract fun getAlarmDao(): AlarmDao

    companion object {
        @Volatile
        private var instance: WeatherDataBase? = null
        fun getInstance(context: Context): WeatherDataBase {
            return instance ?: synchronized(this) {
                val INSTANCE =
                    Room.databaseBuilder(context, WeatherDataBase::class.java, "weather_db")
                        .fallbackToDestructiveMigration()
                        .build()
                instance = INSTANCE
                INSTANCE
            }
        }
    }

}