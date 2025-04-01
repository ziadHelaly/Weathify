package eg.edu.iti.weathify.core.model.local

import eg.edu.iti.weathify.core.model.models.FavouritePlace
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {

    suspend fun getAllFavourites(): Flow<List<FavouritePlace>>
    suspend fun addPlaceToFav(place: FavouritePlace): Long
    suspend fun removeFromFav(place: FavouritePlace): Int
}