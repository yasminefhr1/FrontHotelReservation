package ma.ensa.projet.ui

import AddReservationDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ma.ensa.projet.data.Chambre
import ma.ensa.projet.data.Client
import ma.ensa.projet.data.Reservation
import ma.ensa.projet.data.ReservationRequest

@Composable
fun ReservationsScreen(
    reservations: List<Reservation>,
    onAddClick: (ReservationRequest) -> Unit,
    onDeleteClick: (Reservation) -> Unit,
    onEditClick: (Reservation, ReservationRequest) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var reservationToEdit by remember { mutableStateOf<Reservation?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mes Réservations", fontWeight = FontWeight.Bold) },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                backgroundColor = MaterialTheme.colors.secondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter une réservation")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            items(reservations) { reservation ->
                ReservationCard(
                    reservation = reservation,
                    onDeleteClick = {
                        coroutineScope.launch {
                            onDeleteClick(reservation)
                            snackbarHostState.showSnackbar("Réservation supprimée : ${reservation.id}")
                        }
                    },
                    onEditClick = {
                        reservationToEdit = reservation // Set the reservation to edit
                    }
                )
            }
        }
    }

    if (showAddDialog) {
        AddReservationDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { reservationRequest ->
                coroutineScope.launch {
                    onAddClick(reservationRequest)
                    snackbarHostState.showSnackbar("Réservation ajoutée avec succès")
                }
                showAddDialog = false
            }
        )
    }

    reservationToEdit?.let { reservation ->
        EditReservationDialog(
            reservation = reservation,
            onDismiss = { reservationToEdit = null },
            onConfirm = { updatedRequest ->
                coroutineScope.launch {
                    onEditClick(reservation, updatedRequest)
                    snackbarHostState.showSnackbar("Réservation modifiée avec succès")
                }
                reservationToEdit = null
            }
        )
    }
}
