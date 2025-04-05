package eg.edu.iti.weathify.core.model.local

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import eg.edu.iti.weathify.core.model.models.Alarm
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.Test


@MediumTest
@RunWith(AndroidJUnit4::class)
class WeatherLocalDataSourceTest {
    private lateinit var weatherLocalDataSource: WeatherLocalDataSource
    private lateinit var dataBase: WeatherDataBase
    private lateinit var favDao: FavouritesDao
    private lateinit var alarmDao: AlarmDao
    @Before
    fun setup() {
        val context:Context =ApplicationProvider.getApplicationContext()
        dataBase = Room.inMemoryDatabaseBuilder(
            context,
            WeatherDataBase::class.java
        ).build()
        favDao = dataBase.getFavouriteDao()
        alarmDao = dataBase.getAlarmDao()

        weatherLocalDataSource = WeatherLocalDataSourceImpl(
            favDao,
            alarmDao,
            context.getSharedPreferences("test_sh",Context.MODE_PRIVATE)
        )

    }

    @After
    fun closeDB() {
        dataBase.close()
    }

    @Test
    fun saveAlarm_retrieve_it() = runTest {
        // Given
        val alarm = Alarm("", 10000L, "damietta")
        //when
        weatherLocalDataSource.addAlarm(alarm)
        val result = weatherLocalDataSource.getAlarms().first().first()
        //then
        assertThat(result.id,`is`(alarm.id))
        assertThat(result.date,`is`(alarm.date))
        assertThat(result.city,`is`(alarm.city))
    }
    @Test
    fun saveAlarm_thenDeleteItById_retrieve_is_deleted() = runTest {
        // Given
        val alarm = Alarm("123", 10000L, "damietta")
        //when
        weatherLocalDataSource.addAlarm(alarm)
        val result = weatherLocalDataSource.deleteAlarmById(alarm.id)
        //then
        assertThat(result,`is`(1))
    }

}