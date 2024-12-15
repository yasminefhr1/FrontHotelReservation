package ma.ensa.projet.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReservationApi {

    @GET("reservations")
    suspend fun getReservations(): Response<List<Reservation>>

    @POST("reservations")
    suspend fun addReservation(@Body reservationRequest: ReservationRequest): Response<Reservation>

    @DELETE("reservations/{id}")
    suspend fun deleteReservation(@Path("id") id: Long): Response<Void>
}
