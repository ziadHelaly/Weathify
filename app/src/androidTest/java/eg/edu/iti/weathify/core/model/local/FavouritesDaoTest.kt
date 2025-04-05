package eg.edu.iti.weathify.core.model.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import eg.edu.iti.weathify.core.model.models.FavouritePlace
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@SmallTest
@RunWith(AndroidJUnit4::class)
class FavouritesDaoTest {
    private lateinit var dao: FavouritesDao
    private lateinit var dataBase: WeatherDataBase

    @Before
    fun setup() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).build()
        dao = dataBase.getFavouriteDao()
    }

    @After
    fun closeDB() {
        dataBase.close()
    }

    @Test
    fun save_place_and_retrieve_it() = runTest {
        //Given
        val favouritePlace = FavouritePlace("1.0", "2.0", "cairo")

        //when
        dao.addPlaceToFavourite(favouritePlace)
        val saved = dao.getPlaceByName("cairo")

        //then
        assertThat(saved.name, `is`(favouritePlace.name))
        assertThat(saved.latitude, `is`(favouritePlace.latitude))
        assertThat(saved.longitude, `is`(favouritePlace.longitude))
    }
    @Test
    fun delete_place_and_retrieve_all() = runTest {
        //Given
        val favouritePlace1 = FavouritePlace("1.0", "2.0", "cairo")
        val favouritePlace2 = FavouritePlace("4.0", "3.0", "paris")
        val favouritePlace3 = FavouritePlace("5.0", "6.0", "usa")

        //when
        dao.addPlaceToFavourite(favouritePlace1)
        dao.addPlaceToFavourite(favouritePlace2)
        dao.addPlaceToFavourite(favouritePlace3)
        dao.removeFromFavourite(favouritePlace1)
        val res= dao.getAllFavourites().first()

        //then
        assertThat(res.size,`is`(2))
    }

}