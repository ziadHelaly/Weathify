package eg.edu.iti.weathify.core.navigation

import eg.edu.iti.weathify.R

data class NavigationItem(
    val title: Int,
    val selected: Boolean = false,
    val icon: Int,
    val destination: ScreenRoutes
)

val navigationItems = listOf(
    NavigationItem(R.string.home, true, R.drawable.ic_home, ScreenRoutes.HomeScreenRoute(null,null,true)),
    NavigationItem(R.string.favourite, false, R.drawable.ic_favorite, ScreenRoutes.FavScreenRoute),
    NavigationItem(R.string.alarm, false, R.drawable.ic_notifications, ScreenRoutes.AlarmScreenRoute),
    NavigationItem(R.string.settings, false, R.drawable.ic_settings, ScreenRoutes.SettingScreenRoute)
)