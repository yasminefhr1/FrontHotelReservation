package ma.ensa.projet.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ma.ensa.projet.data.Reservation

@Composable
fun ReservationCard(
    reservation: Reservation,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isExpanded) 1.05f else 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                shadowElevation = if (isExpanded) 20f else 10f
            }
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            ) {
                isExpanded = !isExpanded
            },
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6A11CB),   // Vibrant Purple
                            Color(0xFF2575FC)    // Bright Blue
                        )
                    )
                )
                .padding(16.dp)
        ) {
            // Reservation Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reservation #${reservation.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )

                IconButton(
                    onClick = { /* Optional action */ },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = "Reservation Details",
                        tint = Color(0xFFFFC107), // Amber
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Reservation Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(12.dp)
            ) {
                ReservationDetailItem(
                    icon = Icons.Default.Person,
                    label = "Client",
                    value = reservation.client.id.toString(),
                    iconColor = Color(0xFF2196F3)
                )
                ReservationDetailItem(
                    icon = Icons.Default.DateRange,
                    label = "Arrival",
                    value = reservation.checkInDate.toString(),
                    iconColor = Color(0xFF4CAF50)
                )
                ReservationDetailItem(
                    icon = Icons.Default.ExitToApp,
                    label = "Departure",
                    value = reservation.checkOutDate.toString(),
                    iconColor = Color(0xFFFF5722)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActionButton(
                    onClick = onEditClick,
                    icon = Icons.Default.Edit,
                    text = "Edit",
                    backgroundColor = Color(0xFF2196F3)
                )

                ActionButton(
                    onClick = onDeleteClick,
                    icon = Icons.Default.Delete,
                    text = "Delete",
                    backgroundColor = Color(0xFFFF5252)
                )
            }
        }
    }
}

@Composable
fun ReservationDetailItem(
    icon: ImageVector,
    label: String,
    value: String,
    iconColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier
                .size(30.dp)
                .background(
                    color = iconColor.copy(alpha = 0.1f),
                    shape = CircleShape
                )
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f),
                fontStyle = FontStyle.Italic
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun ActionButton(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    backgroundColor: Color
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f)

    Button(
        onClick = {
            isPressed = true
            onClick()
            isPressed = false
        },
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .scale(scale),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        interactionSource = remember { MutableInteractionSource() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
