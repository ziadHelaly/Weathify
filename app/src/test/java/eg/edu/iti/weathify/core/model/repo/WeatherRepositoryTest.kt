package eg.edu.iti.weathify.core.model.repo

import eg.edu.iti.weathify.core.model.local.WeatherLocalDataSource
import eg.edu.iti.weathify.core.model.models.Alarm
import eg.edu.iti.weathify.core.model.models.FavouritePlace
import eg.edu.iti.weathify.core.model.remote.WeatherRemoteDataSource
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test


class WeatherRepositoryTest {
    private lateinit var fakeLocalDataSource: WeatherLocalDataSource
    private lateinit var fakeRemoteDataSource: WeatherRemoteDataSource
    lateinit var repository: WeatherRepository

    private val favsData = mutableListOf(
        FavouritePlace("1.05", "15.0", "p1"),
        FavouritePlace("12.0", "16.0", "p2"),
        FavouritePlace("13.05", "5.0", "p3"),
        FavouritePlace("14.05", "16.0", "p4"),
    )
    private val alarmsData = mutableListOf(
        Alarm("1", 100L, "egypt"),
        Alarm("2", 100L, "usa"),
        Alarm("3", 100L, "ksa"),
        Alarm("4", 100L, "dutch"),
    )

    @Before
    fun setup() {
        fakeLocalDataSource = FakeLocalDataSource(favsData, alarmsData)
        fakeRemoteDataSource = mockk(relaxed = true)
        repository = WeatherRepositoryImpl.getInstance(fakeRemoteDataSource, fakeLocalDataSource)
    }

    @Test
    fun addPlaceToFav_notAlreadyAdd() = runTest {
        //Given
        val place = FavouritePlace("11", "12", "damietta")

        //When
        val result = repository.addPlaceToFav(place)

        //Then
        assertThat(result,`is`(1L))


    }
    @Test
    fun addPlaceToFav_AlreadyAdd() = runTest {
        //Given
        val place = FavouritePlace("1.05", "15.0", "p1")

        //When
        val result = repository.addPlaceToFav(place)

        //Then
        assertThat(result,`is`(0L))
    }
    @Test
    fun deleteAlarm_exits()= runTest{
        //Given
        val alarm = Alarm("1", 100L, "egypt")
        //When
        val result= repository.deleteAlarm(alarm)
        //Then
        assertThat(result , `is`(1))
    }
    @Test
    fun deleteAlarm_notExits()= runTest{
        //Given
        val alarm = Alarm("11", 1200L, "spain")
        //When
        val result= repository.deleteAlarm(alarm)
        //Then
        assertThat(result , `is`(0))
    }
}