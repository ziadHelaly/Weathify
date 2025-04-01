package eg.edu.iti.weathify.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenRoutes {
    @Serializable
    object MapScreenRoute :ScreenRoutes()

    @Serializable
    data class HomeScreenRoute(val lon:String?,val lat:String?) : ScreenRoutes()

    @Serializable
    object FavScreenRoute : ScreenRoutes()

    @Serializable
    object AlarmScreenRoute : ScreenRoutes()

    @Serializable
    object SettingScreenRoute : ScreenRoutes()
}