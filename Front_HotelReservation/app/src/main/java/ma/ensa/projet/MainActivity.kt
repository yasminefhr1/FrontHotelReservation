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

        reservationService.getReservationsWithTime(
            onSuccess = { reservations, elapsedTime ->
                Log.d("Performance", "GET Reservations: $elapsedTime ms")
                reservationsList = reservations
            },
            onError = { error ->
                Log.e("Performance", "GET Reservations failed: $error")
            }
        )

    }

    // Fonction pour ajouter une réservation
    private fun addReservation(reservationRequest: ReservationRequest) {

        reservationService.addReservationWithTime(
            reservationRequest,
            onSuccess = { newReservation, elapsedTime ->
                reservationsList = reservationsList + newReservation
                Log.d("Performance", "Ajout terminé en ${elapsedTime}ms")
            },
            onError = { error ->
                errorMessage = "Erreur lors de l'ajout : $error"
            }
        )

    }


    private fun deleteReservation(reservation: Reservation) {
        reservation.id?.let { id ->
            reservationService.deleteReservationWithTime(
                id,
                onSuccess = { elapsedTime ->
                    reservationsList = reservationsList.filter { it.id != id }
                    Log.d("MainActivity", "Reservation deleted: $id in $elapsedTime ms")
                },
                onError = { error ->
                    Log.e("MainActivity", "Delete Reservation Error Details: $error")
                    // Afficher un message d'erreur à l'utilisateur
                    errorMessage = "Impossible de supprimer la réservation. Veuillez réessayer."
                }
            )
        } ?: run {
            Log.e("MainActivity", "Reservation ID is null")
            errorMessage = "ID de réservation invalide"
        }
    }

    // Fonction pour modifier une réservation
    private fun editReservation(reservation: Reservation, updatedRequest: ReservationRequest) {
        reservation.id?.let { id ->
            reservationService.updateReservationWithTime(
                id = id,
                reservationRequest = updatedRequest,
                onSuccess = { updatedReservation, elapsedTime ->
                    // Mettre à jour la liste avec la réservation modifiée
                    reservationsList = reservationsList.map {
                        if (it.id == updatedReservation.id) updatedReservation else it
                    }
                    Log.d("Performance", "Mise à jour terminée en ${elapsedTime}ms")
                },
                onError = { error ->
                    errorMessage = "Erreur lors de la modification : $error"
                }
            )
        }
    }
}
