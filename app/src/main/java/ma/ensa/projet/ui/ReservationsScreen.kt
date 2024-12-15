package ma.ensa.projet.ui

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
fun ReservationCard(
    reservation: Reservation,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { /* TODO: Handle reservation click if needed */ },
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "ID: ${reservation.id}", fontWeight = FontWeight.Bold)
            Text(text = "Client: ${reservation.client.id}")
            Text(text = "Début: ${reservation.checkInDate}")
            Text(text = "Fin: ${reservation.checkOutDate}")
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onEditClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Modifier", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Modifier", color = Color.White)
                }

                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Supprimer", color = Color.White)
                }
            }
        }
    }
}

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
        title = { Text("Modifier la Réservation") },
        text = {
            Column {
                TextField(
                    value = clientId,
                    onValueChange = { clientId = it },
                    label = { Text("Client ID") }
                )
                TextField(
                    value = dateDebut,
                    onValueChange = { dateDebut = it },
                    label = { Text("Date de Début (YYYY-MM-DD)") }
                )
                TextField(
                    value = dateFin,
                    onValueChange = { dateFin = it },
                    label = { Text("Date de Fin (YYYY-MM-DD)") }
                )
                TextField(
                    value = chambreIds,
                    onValueChange = { chambreIds = it },
                    label = { Text("Chambre IDs (séparés par des virgules)") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val parsedClientId = clientId.toLongOrNull()
                val parsedChambreIds = chambreIds.split(",")
                    .mapNotNull { it.trim().toLongOrNull() }
                    .map { Chambre(it) }

                if (parsedClientId != null && parsedChambreIds.isNotEmpty() &&
                    dateDebut.isNotEmpty() && dateFin.isNotEmpty()) {
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
            }) {
                Text("Modifier")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}


@Composable
fun AddReservationDialog(
    onDismiss: () -> Unit,
    onConfirm: (ReservationRequest) -> Unit
) {
    var dateDebut by remember { mutableStateOf("") }
    var dateFin by remember { mutableStateOf("") }
    var clientId by remember { mutableStateOf("") }
    var chambreIds by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajouter une Réservation") },
        text = {
            Column {
                TextField(
                    value = clientId,
                    onValueChange = { clientId = it },
                    label = { Text("Client ID") }
                )
                TextField(
                    value = dateDebut,
                    onValueChange = { dateDebut = it },
                    label = { Text("Date de Début (YYYY-MM-DD)") }
                )
                TextField(
                    value = dateFin,
                    onValueChange = { dateFin = it },
                    label = { Text("Date de Fin (YYYY-MM-DD)") }
                )
                TextField(
                    value = chambreIds,
                    onValueChange = { chambreIds = it },
                    label = { Text("Chambre IDs (séparés par des virgules)") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val parsedClientId = clientId.toLongOrNull()
                val parsedChambreIds = chambreIds.split(",")
                    .mapNotNull { it.trim().toLongOrNull() }
                    .map { Chambre(it) }

                if (parsedClientId != null && parsedChambreIds.isNotEmpty() &&
                    dateDebut.isNotEmpty() && dateFin.isNotEmpty()) {
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
            }) {
                Text("Ajouter")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}