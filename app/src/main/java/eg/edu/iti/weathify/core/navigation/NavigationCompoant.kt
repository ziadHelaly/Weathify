package eg.edu.iti.weathify.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import eg.edu.iti.weathify.core.model.local.WeatherLocalDataSourceImpl
import eg.edu.iti.weathify.core.model.remote.RetrofitHelper
import eg.edu.iti.weathify.core.model.remote.WeatherRemoteDataSourceImpl
import eg.edu.iti.weathify.core.model.repo.WeatherRepositoryImpl
import eg.edu.iti.weathify.home.view.HomeScreen
import eg.edu.iti.weathify.home.viewmodel.HomeViewModelFactory

@Composable
fun NavComponent(
    navHostController: NavHostController,
    startDestination: ScreenRoutes,
    modifier: Modifier = Modifier,
    lat: MutableState<String>,
    lon: MutableState<String>
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        val repo = WeatherRepositoryImpl.getInstance(
            WeatherRemoteDataSourceImpl(RetrofitHelper.serviceApi),
            WeatherLocalDataSourceImpl()
        )
        composable<ScreenRoutes.HomeScreenRoute> {
            HomeScreen(long = lon, lat = lat, viewModel(factory = HomeViewModelFactory(repo)))
        }
        composable<ScreenRoutes.FavScreenRoute> {
//            HomeScreen(long = lon, lat = lat, viewModel(factory = HomeViewModelFactory(repo)))
            Text("fav")
        }
        composable<ScreenRoutes.AlarmScreenRoute> {
//            HomeScreen(long = lon, lat = lat, viewModel(factory = HomeViewModelFactory(repo)))
            Text("alr")
        }
        composable<ScreenRoutes.SettingScreenRoute> {
//            HomeScreen(long = lon, lat = lat, viewModel(factory = HomeViewModelFactory(repo)))
            Text("set")
        }
    }
}