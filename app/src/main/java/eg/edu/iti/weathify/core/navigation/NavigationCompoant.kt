package eg.edu.iti.weathify.core.navigation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import eg.edu.iti.weathify.map.view.MapScreen
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
import eg.edu.iti.weathify.alarm.view.AlarmScreen
import eg.edu.iti.weathify.alarm.viewModel.AlarmViewModelFactory
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
import eg.edu.iti.weathify.settings.view.SettingsScreen
import eg.edu.iti.weathify.settings.viewModel.SettingsViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
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
            WeatherLocalDataSourceImpl(
                WeatherDataBase.getInstance(context).getFavouriteDao(),
                WeatherDataBase.getInstance(context).getAlarmDao(),
                context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            )
        )
        composable<ScreenRoutes.HomeScreenRoute> {
            val args: ScreenRoutes.HomeScreenRoute = it.toRoute()
            val isHome = args.isHome
            if (!args.lat.isNullOrEmpty() && !args.lon.isNullOrEmpty()) {
                HomeScreen(
                    long = mutableStateOf(args.lon),
                    lat = mutableStateOf(args.lat),
                    viewModel(factory = HomeViewModelFactory(repo)),
                    isHome
                )
            } else {
                HomeScreen(
                    long = lon,
                    lat = lat,
                    viewModel(factory = HomeViewModelFactory(repo)),
                    isHome
                )
            }
        }
        composable<ScreenRoutes.FavScreenRoute> {
            FavouriteScreen(
                viewModel = viewModel(factory = FavouriteViewModelFactory(repo)),
                navToHome = { longitude, latitude ->
                    navHostController.navigate(
                        ScreenRoutes.HomeScreenRoute(
                            longitude,
                            latitude,
                            false
                        )
                    )
                }) {
                navHostController.navigate(ScreenRoutes.MapScreenRoute(false))
            }
        }
        composable<ScreenRoutes.AlarmScreenRoute> {
            AlarmScreen(
                viewModel = viewModel(factory = AlarmViewModelFactory(repo)),
                lon.value,
                lat.value
            )
        }
        composable<ScreenRoutes.SettingScreenRoute> {

            SettingsScreen(viewModel = viewModel(factory = SettingsViewModelFactory(repo))) {
                navHostController.navigate(ScreenRoutes.MapScreenRoute(true))
            }
        }
        composable<ScreenRoutes.MapScreenRoute> {
            val args: ScreenRoutes.MapScreenRoute = it.toRoute()
            val isSettingsMode = args.settingsMode
            MapScreen(
                viewModel = viewModel(factory = MapViewModelFactory(repo)),
                modifier = modifier,
                isSettingsMode
            ) {
                navHostController.popBackStack()
            }

        }
    }
}