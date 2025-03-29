package eg.edu.iti.weathify.core.navigation

import eg.edu.iti.weathify.R

data class NavigationItem(
    val title: String,
    val selected: Boolean = false,
    val icon: Int,
    val destination: ScreenRoutes
)

val navigationItems = listOf(
    NavigationItem("Home", true, R.drawable.ic_home, ScreenRoutes.HomeScreenRoute),
    NavigationItem("Favourite", false, R.drawable.ic_favorite, ScreenRoutes.FavScreenRoute),
    NavigationItem("Alarm", false, R.drawable.ic_notifications, ScreenRoutes.AlarmScreenRoute),
    NavigationItem("Settings", false, R.drawable.ic_settings, ScreenRoutes.SettingScreenRoute)
)