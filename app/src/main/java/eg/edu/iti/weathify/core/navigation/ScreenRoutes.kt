package eg.edu.iti.weathify.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenRoutes {
    @Serializable
    data class MapScreenRoute(val settingsMode:Boolean) :ScreenRoutes()

    @Serializable
    data class HomeScreenRoute(val lon:String?,val lat:String?,val isHome:Boolean) : ScreenRoutes()

    @Serializable
    object FavScreenRoute : ScreenRoutes()

    @Serializable
    object AlarmScreenRoute : ScreenRoutes()

    @Serializable
    object SettingScreenRoute : ScreenRoutes()
}