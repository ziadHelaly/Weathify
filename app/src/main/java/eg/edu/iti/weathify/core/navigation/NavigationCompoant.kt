package eg.edu.iti.weathify.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun NavComponent(navHostController: NavHostController, startDestination: ScreenRoutes, modifier: Modifier = Modifier) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {

    }
}