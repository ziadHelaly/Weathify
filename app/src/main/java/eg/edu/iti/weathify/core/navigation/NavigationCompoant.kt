package eg.edu.iti.weathify.core.navigation

import MapScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import eg.edu.iti.weathify.core.model.local.WeatherDataBase
import eg.edu.iti.weathify.core.model.local.WeatherLocalDataSourceImpl
import eg.edu.iti.weathify.core.model.remote.RetrofitHelper
import eg.edu.iti.weathify.core.model.remote.WeatherRemoteDataSourceImpl
import eg.edu.iti.weathify.core.model.repo.WeatherRepositoryImpl
import eg.edu.iti.weathify.favourite.view.FavouriteScreen
import eg.edu.iti.weathify.favourite.viewModel.FavouriteViewModelFactory
import eg.edu.iti.weathify.home.view.HomeScreen
import eg.edu.iti.weathify.home.viewmodel.HomeViewModelFactory
import eg.edu.iti.weathify.map.viewModel.MapViewModelFactory

@Composable
fun NavComponent(
    navHostController: NavHostController,
    startDestination: ScreenRoutes,
    modifier: Modifier = Modifier,
    lat: MutableState<String>,
    lon: MutableState<String>
) {
    val context = LocalContext.current
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        val repo = WeatherRepositoryImpl.getInstance(
            WeatherRemoteDataSourceImpl(RetrofitHelper.serviceApi, RetrofitHelper.searchApi),
            WeatherLocalDataSourceImpl(WeatherDataBase.getInstance(context).getFavouriteDao())
        )
        composable<ScreenRoutes.HomeScreenRoute> { it ->
            val args: ScreenRoutes.HomeScreenRoute = it.toRoute()
            if (!args.lat.isNullOrEmpty() && !args.lon.isNullOrEmpty()) {
                HomeScreen(
                    long = mutableStateOf(args.lon),
                    lat = mutableStateOf(args.lat),
                    viewModel(factory = HomeViewModelFactory(repo))
                )
            } else {
                HomeScreen(long = lon, lat = lat, viewModel(factory = HomeViewModelFactory(repo)))
            }
        }
        composable<ScreenRoutes.FavScreenRoute> {
            FavouriteScreen(
                viewModel = viewModel(factory = FavouriteViewModelFactory(repo)),
                navToHome = { longitude, latitude ->
                    navHostController.navigate(ScreenRoutes.HomeScreenRoute(longitude, latitude))
                }) {
                navHostController.navigate(ScreenRoutes.MapScreenRoute)
            }
        }
        composable<ScreenRoutes.AlarmScreenRoute> {
//            HomeScreen(long = lon, lat = lat, viewModel(factory = HomeViewModelFactory(repo)))
            Text("alr")
        }
        composable<ScreenRoutes.SettingScreenRoute> {
//            HomeScreen(long = lon, lat = lat, viewModel(factory = HomeViewModelFactory(repo)))
            Text("set")
        }
        composable<ScreenRoutes.MapScreenRoute> {
            MapScreen(
                viewModel = viewModel(factory = MapViewModelFactory(repo)),
                modifier = modifier
            ) {
                navHostController.popBackStack()
            }

        }
    }
}