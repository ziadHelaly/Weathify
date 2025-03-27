package eg.edu.iti.weathify.core.navigation

import androidx.compose.runtime.Composable
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
import eg.edu.iti.weathify.home.viewmodel.HomeViewModel
import eg.edu.iti.weathify.home.viewmodel.HomeViewModelFactory

@Composable
fun NavComponent(
    navHostController: NavHostController,
    startDestination: ScreenRoutes,
    modifier: Modifier = Modifier,
    lat: String,
    lon: String
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        val repo = WeatherRepositoryImpl.getInstance(
            WeatherRemoteDataSourceImpl(RetrofitHelper.serviceApi),
            WeatherLocalDataSourceImpl()
        )
        composable<ScreenRoutes.HomeScreenRoute> {
            HomeScreen(long = lon, lat = lat, viewModel(factory = HomeViewModelFactory(repo)))
        }
    }
}