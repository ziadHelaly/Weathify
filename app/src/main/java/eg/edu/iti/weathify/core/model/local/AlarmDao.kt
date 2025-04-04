package eg.edu.iti.weathify.core.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eg.edu.iti.weathify.core.model.models.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("select * from alarm_table")
    fun getAlarms(): Flow<List<Alarm>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlarm(alarm: Alarm): Long

    @Delete
    suspend fun deleteAlarm(alarm: Alarm): Int

    @Query("delete from alarm_table where id = :alarmId")
    suspend fun deleteAlarmById(alarmId: String)

}