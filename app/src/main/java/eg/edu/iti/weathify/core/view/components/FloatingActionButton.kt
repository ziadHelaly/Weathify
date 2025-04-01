package eg.edu.iti.weathify.core.view.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import eg.edu.iti.weathify.R

@Composable
fun FAB(modifier: Modifier = Modifier, action: () -> Unit) {
    FloatingActionButton(
        onClick = action,
    ) {
        Icon(painter = painterResource(R.drawable.ic_add), contentDescription = "")
    }
}