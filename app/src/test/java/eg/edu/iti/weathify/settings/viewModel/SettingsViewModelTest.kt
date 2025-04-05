package eg.edu.iti.weathify.settings.viewModel

import eg.edu.iti.weathify.core.model.repo.WeatherRepository
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest{
    private val testDispatcher:TestDispatcher = UnconfinedTestDispatcher()
    private val repo:WeatherRepository= mockk(relaxed = true)
    private lateinit var viewModel:SettingsViewModel

    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
        viewModel =SettingsViewModel(repo)
    }
    @After
    fun after(){
        Dispatchers.resetMain()
    }
    @Test
    fun updateTempSettings()= runTest{
        //Given
        val input =1

        //when
        viewModel.updateTempSetting(input)
        advanceUntilIdle()
        //then
        assertThat(viewModel.currentTempUnit.value,`is`(1))
    }
    @Test
    fun updateLanguageSettings()= runTest{
        //Given
        val input =1

        //when
        viewModel.updateLanguageSetting(input)
        advanceUntilIdle()
        //then
        assertThat(viewModel.currentLanguage.value,`is`(1))
    }

}