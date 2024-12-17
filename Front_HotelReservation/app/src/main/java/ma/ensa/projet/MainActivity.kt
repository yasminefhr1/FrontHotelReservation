package ma.ensa.projet

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import ma.ensa.projet.data.Reservation
import ma.ensa.projet.data.ReservationRequest
import ma.ensa.projet.data.ReservationService
import ma.ensa.projet.ui.MesReservationsTheme
import ma.ensa.projet.ui.ReservationsScreen

class MainActivity : ComponentActivity() {
    private var reservationsList by mutableStateOf<List<Reservation>>(emptyList())
    private var isLoading by mutableStateOf(true)
    private var errorMessage by mutableStateOf<String?>(null)
    private lateinit var reservationService: ReservationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reservationService = ReservationService(this)

        // Charger les réservations depuis le backend
        fetchReservations()

        setContent {
            MesReservationsTheme {
                ReservationsScreen(
                    reservations = reservationsList,
                    onAddClick = { reservationRequest ->
                        addReservation(reservationRequest)
                    },
                    onDeleteClick = { reservationToDelete ->
                        deleteReservation(reservationToDelete)
                    },
                    onEditClick = { reservationToEdit, updatedRequest ->
                        editReservation(reservationToEdit, updatedRequest)
                    }
                )
            }
        }
    }

    // Fonction pour récupérer les réservations
    private fun fetchReservations() {
        Log.d("MainActivity", "Fetching reservations")
        reservationService.getReservations(
            onSuccess = { reservations ->
                Log.d("MainActivity", "Reservations fetched: ${reservations.size}")
                reservationsList = reservations
                isLoading = false
                errorMessage = null
            },
            onError = { error ->
                Log.e("MainActivity", "Error fetching reservations: $error")
                reservationsList = emptyList()
                isLoading = false
                errorMessage = "Erreur lors de la récupération des réservations : $error"
            }
        )
    }

    // Fonction pour ajouter une réservation
    private fun addReservation(reservationRequest: ReservationRequest) {
        reservationService.addReservation(
            reservationRequest,
            onSuccess = { newReservation ->
                reservationsList = reservationsList + newReservation
            },
            onError = { error ->
                errorMessage = "Erreur lors de l'ajout : $error"
            }
        )
    }

    private fun deleteReservation(reservation: Reservation) {
        reservation.id?.let { id ->
            reservationService.deleteReservation(
                id,
                onSuccess = {
                    // Directly update the list by removing the deleted reservation
                    reservationsList = reservationsList.filter { it.id != id }
                    Log.d("MainActivity", "Reservation deleted: $id")
                },
                onError = { error ->
                    errorMessage = "Erreur lors de la suppression : $error"
                }
            )
        }
    }




    // Fonction pour modifier une réservation
    private fun editReservation(reservation: Reservation, updatedRequest: ReservationRequest) {
        reservation.id?.let { id ->
            reservationService.updateReservation(
                id = id,
                reservationRequest = updatedRequest,
                onSuccess = { updatedReservation ->
                    // Mettre à jour la liste avec la réservation modifiée
                    reservationsList = reservationsList.map {
                        if (it.id == updatedReservation.id) updatedReservation else it
                    }
                },
                onError = { error ->
                    errorMessage = "Erreur lors de la modification : $error"
                }
            )
        }
    }

}
