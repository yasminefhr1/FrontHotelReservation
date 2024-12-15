package ma.ensa.projet.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.material.Typography
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import ma.ensa.projet.data.Reservation
import ma.ensa.projet.data.ReservationRequest

// ========== Couleurs du Thème ==========

private val LightColorPalette = lightColors(
    primary = Color(0xFFFF5C5C),
    primaryVariant = Color(0xFFCC4A4A),
    secondary = Color(0xFFFFC107),
    background = Color(0xFFFFEFEF),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val DarkColorPalette = darkColors(
    primary = Color(0xFFFF5C5C),
    primaryVariant = Color(0xFFCC4A4A),
    secondary = Color(0xFFFFC107),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

// ========== Thème personnalisé ==========

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

// ========== Utilisation du Thème ==========

@Composable
fun ReservationsApp(
    reservations: List<Reservation>,
    onAddClick: (ReservationRequest) -> Unit,
    onDeleteClick: (Reservation) -> Unit,
    onEditClick: (Reservation, ReservationRequest) -> Unit // Ajuster pour inclure Reservation et ReservationRequest
) {
    MyAppTheme {
        ReservationsScreen(
            reservations = reservations,
            onAddClick = onAddClick,
            onDeleteClick = onDeleteClick,
            onEditClick = onEditClick // Passer correctement onEditClick
        )
    }
}





// Définir la typographie
val Typography = Typography(
    defaultFontFamily = FontFamily.Default,
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    button = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)

// Définir les formes
val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
)

// Thème de l'application
@Composable
fun MesReservationsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
