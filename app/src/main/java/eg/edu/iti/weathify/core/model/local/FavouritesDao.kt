package eg.edu.iti.weathify.core.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eg.edu.iti.weathify.core.model.models.FavouritePlace
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDao {
    @Query("select * from favouriteplace")
    fun getAllFavourites(): Flow<List<FavouritePlace>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlaceToFavourite(place: FavouritePlace):Long

    @Delete
    suspend fun removeFromFavourite(place: FavouritePlace):Int
}