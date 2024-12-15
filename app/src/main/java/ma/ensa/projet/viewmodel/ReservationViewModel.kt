package ma.ensa.projet.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ma.ensa.projet.data.Reservation
import ma.ensa.projet.data.ReservationRequest
import ma.ensa.projet.data.ReservationService

class ReservationViewModel(context: Context) : ViewModel() {
    private val reservationService = ReservationService(context)

    private val _reservations = MutableLiveData<List<Reservation>>()
    val reservations: LiveData<List<Reservation>> = _reservations

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchReservations() {
        reservationService.getReservations(
            onSuccess = { fetchedReservations -> _reservations.postValue(fetchedReservations) },
            onError = { errorMsg -> _errorMessage.postValue(errorMsg) }
        )
    }

    fun createReservation(reservationRequest: ReservationRequest) {
        reservationService.addReservation(
            reservationRequest,
            onSuccess = { newReservation ->
                val currentList = _reservations.value?.toMutableList() ?: mutableListOf()
                currentList.add(newReservation)
                _reservations.postValue(currentList)
            },
            onError = { errorMsg -> _errorMessage.postValue(errorMsg) }
        )
    }

    fun deleteReservation(id: Long) {
        reservationService.deleteReservation(
            id,
            onSuccess = {
                val updatedList = _reservations.value?.filterNot { it.id == id }
                _reservations.postValue(updatedList)
            },
            onError = { errorMsg -> _errorMessage.postValue(errorMsg) }
        )
    }

    fun updateReservation(id: Long, reservationRequest: ReservationRequest) {
        reservationService.updateReservation(
            id,
            reservationRequest,
            onSuccess = { updatedReservation ->
                val updatedList = _reservations.value?.map {
                    if (it.id == id) updatedReservation else it
                }
                _reservations.postValue(updatedList)
            },
            onError = { errorMsg -> _errorMessage.postValue(errorMsg) }
        )
    }
}
