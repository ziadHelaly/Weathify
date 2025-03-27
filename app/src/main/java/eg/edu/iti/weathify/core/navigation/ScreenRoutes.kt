package eg.edu.iti.weathify.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenRoutes {
    @Serializable
    object HomeScreenRoute : ScreenRoutes()
}