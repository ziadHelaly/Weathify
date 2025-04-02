package eg.edu.iti.weathify.core.view.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import eg.edu.iti.weathify.core.navigation.navigationItems

@Composable
fun NavigationBottomBar(navController: NavController, modifier: Modifier = Modifier) {
    val selected = rememberSaveable { mutableIntStateOf(0) }
    NavigationBar(
        containerColor = Color.White.copy(alpha = 0.0f)
    ) {
        navigationItems.forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                selected = selected.intValue == index,
                onClick = {
                    selected.intValue = index
                    navController.navigate(navigationItem.destination)
                },
                icon = {
                    Icon(
                        painterResource(navigationItem.icon),
                        "",
//                        tint = if (selected.intValue == index) Color.Black else Color.White
                    )
                },
                alwaysShowLabel = false,
                label = {
                    Text(stringResource( navigationItem.title))
                }
            )
        }
    }
}