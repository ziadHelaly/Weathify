package eg.edu.iti.weathify.core.view.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val White = Color(0xFFFFFFFF)
val screenBG = Brush.linearGradient(
    colors = listOf(
        Color(0xFF020024),
        Color(0xFF090979)
    ),
    start = Offset(0f, 0f),
    end = Offset(0f, Float.POSITIVE_INFINITY)
)