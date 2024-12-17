package ma.ensa.projet.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ma.ensa.projet.data.Chambre
import ma.ensa.projet.data.Client
import ma.ensa.projet.data.Reservation
import ma.ensa.projet.data.ReservationRequest

@Composable
fun EditReservationDialog(
    reservation: Reservation,
    onDismiss: () -> Unit,
    onConfirm: (ReservationRequest) -> Unit
) {
    var dateDebut by remember { mutableStateOf(reservation.checkInDate) }
    var dateFin by remember { mutableStateOf(reservation.checkOutDate) }
    var clientId by remember { mutableStateOf(reservation.client.id.toString()) }
    var chambreIds by remember { mutableStateOf(reservation.chambres?.joinToString(",") { it.id.toString() } ?: "") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Reservation",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Modifier la Réservation",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
        },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                StyledTextField(
                    value = clientId,
                    onValueChange = { clientId = it },
                    label = "Client ID",
                    icon = Icons.Default.Person
                )
                Spacer(modifier = Modifier.height(8.dp))
                StyledTextField(
                    value = dateDebut,
                    onValueChange = { dateDebut = it },
                    label = "Date de Début (YYYY-MM-DD)",
                    icon = Icons.Default.DateRange
                )
                Spacer(modifier = Modifier.height(8.dp))
                StyledTextField(
                    value = dateFin,
                    onValueChange = { dateFin = it },
                    label = "Date de Fin (YYYY-MM-DD)",
                    icon = Icons.Default.Event
                )
                Spacer(modifier = Modifier.height(8.dp))
                StyledTextField(
                    value = chambreIds,
                    onValueChange = { chambreIds = it },
                    label = "Chambre IDs (séparés par des virgules)",
                    icon = Icons.Default.Hotel
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedClientId = clientId.toLongOrNull()
                    val parsedChambreIds = chambreIds.split(",")
                        .mapNotNull { it.trim().toLongOrNull() }
                        .map { Chambre(it) }

                    if (parsedClientId != null && parsedChambreIds.isNotEmpty() &&
                        dateDebut.isNotEmpty() && dateFin.isNotEmpty()
                    ) {
                        val reservationRequest = ReservationRequest(
                            checkInDate = dateDebut,
                            checkOutDate = dateFin,
                            client = Client(parsedClientId),
                            chambres = parsedChambreIds
                        )
                        onConfirm(reservationRequest)
                        onDismiss()
                    } else {
                        Toast.makeText(
                            context,
                            "Veuillez remplir tous les champs correctement.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50)),
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Confirm",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Modifier", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF5252)),
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Annuler", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF4CAF50),
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = Color(0xFF4CAF50).copy(alpha = 0.1f),
                    shape = CircleShape
                )
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF4CAF50),
                unfocusedIndicatorColor = Color.Gray,
                textColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
