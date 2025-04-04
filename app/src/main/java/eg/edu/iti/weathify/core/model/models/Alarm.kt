package eg.edu.iti.weathify.core.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("alarm_table")
data class Alarm(
    @PrimaryKey
    val id: String,
    val date: Long,
    val city: String,
)
