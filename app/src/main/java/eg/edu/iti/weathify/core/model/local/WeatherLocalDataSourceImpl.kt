package eg.edu.iti.weathify.core.model.local

import eg.edu.iti.weathify.core.model.models.FavouritePlace
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl(
    private val favouritesDao: FavouritesDao
) : WeatherLocalDataSource {

    override suspend fun getAllFavourites(): Flow<List<FavouritePlace>> {
        return favouritesDao.getAllFavourites()
    }

    override suspend fun addPlaceToFav(place: FavouritePlace): Long {
        return favouritesDao.addPlaceToFavourite(place)
    }

    override suspend fun removeFromFav(place: FavouritePlace): Int {
        return favouritesDao.removeFromFavourite(place)
    }

}