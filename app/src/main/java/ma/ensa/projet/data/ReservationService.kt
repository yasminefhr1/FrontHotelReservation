
// Updated ReservationService
package ma.ensa.projet.data

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class ReservationService(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val gson = Gson()
    private val baseUrl = "http://10.0.2.2:8087/api/reservations"

    fun getReservations(
        onSuccess: (List<Reservation>) -> Unit,
        onError: (String) -> Unit
    ) {
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            baseUrl,
            null,
            { response ->
                try {
                    val reservations = (0 until response.length())
                        .map { response.getJSONObject(it) }
                        .map { gson.fromJson(it.toString(), Reservation::class.java) }
                    onSuccess(reservations)
                } catch (e: Exception) {
                    onError("Erreur de parsing: ${e.message}")
                }
            },
            { error -> onError("Erreur de récupération: ${error.message}") }
        )
        requestQueue.add(jsonArrayRequest)
    }

    fun addReservation(
        reservationRequest: ReservationRequest,
        onSuccess: (Reservation) -> Unit,
        onError: (String) -> Unit
    ) {
        val jsonBody = JSONObject().apply {
            put("reservation", JSONObject().apply {
                put("checkInDate", reservationRequest.checkInDate)
                put("checkOutDate", reservationRequest.checkOutDate)
                put("client", JSONObject().apply {
                    put("id", reservationRequest.client.id)
                })
            })
            put("chambreIds", JSONArray().apply {
                reservationRequest.chambres.forEach { chambre ->
                    put(chambre.id)  // Assurez-vous que vous envoyez juste les IDs et non les objets Chambre
                }
            })
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            baseUrl,
            jsonBody,
            { response ->
                try {
                    val reservation = gson.fromJson(response.toString(), Reservation::class.java)
                    onSuccess(reservation)
                } catch (e: Exception) {
                    onError("Erreur de parsing: ${e.message}")
                }
            },
            { error -> onError("Erreur d'ajout de réservation: ${error.message}") }
        )
        requestQueue.add(jsonObjectRequest)
    }


    fun deleteReservation(
        id: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE,
            "$baseUrl/$id",
            null,
            { onSuccess() },
            { error -> onError("Erreur de suppression: ${error.message}") }
        )
        requestQueue.add(jsonObjectRequest)
    }

    fun updateReservation(
        id: Long,
        reservationRequest: ReservationRequest,
        onSuccess: (Reservation) -> Unit,
        onError: (String) -> Unit
    ) {
        val jsonBody = JSONObject().apply {
            // Assurez-vous d'envoyer l'ID dans le corps de la requête pour la mise à jour
            put("reservation", JSONObject().apply {
                put("id", id) // L'ID de la réservation à mettre à jour
                put("checkInDate", reservationRequest.checkInDate)
                put("checkOutDate", reservationRequest.checkOutDate)
                put("client", JSONObject().apply {
                    put("id", reservationRequest.client.id)
                })
            })
            put("chambreIds", JSONArray().apply {
                reservationRequest.chambres.forEach { chambre ->
                    put(chambre.id)  // Utilisez les IDs des chambres uniquement
                }
            })
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT,
            "$baseUrl/$id",
            jsonBody,
            { response ->
                try {
                    val updatedReservation = gson.fromJson(response.toString(), Reservation::class.java)
                    onSuccess(updatedReservation)
                } catch (e: Exception) {
                    onError("Erreur de parsing: ${e.message}")
                }
            },
            { error -> onError("Erreur de mise à jour: ${error.message}") }
        )
        requestQueue.add(jsonObjectRequest)
    }


}
