package eg.edu.iti.weathify.core.model.models

import androidx.room.Entity

@Entity(primaryKeys = ["longitude","latitude"])
data class FavouritePlace(
    val longitude: String,
    val latitude: String,
    val name:String
)
