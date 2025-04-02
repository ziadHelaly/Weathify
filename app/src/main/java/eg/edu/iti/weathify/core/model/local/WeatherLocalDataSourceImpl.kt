package eg.edu.iti.weathify.core.model.local

import android.content.SharedPreferences
import eg.edu.iti.weathify.core.model.models.FavouritePlace
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl(
    private val favouritesDao: FavouritesDao,
    private val sharedPreferences: SharedPreferences
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

    override fun saveInSharedPref(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override fun getFromSharedPref(key: String): Int {
        return sharedPreferences.getInt(key,2002)
    }


}