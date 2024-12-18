
// Updated ReservationService
package ma.ensa.projet.data

import android.content.Context
import android.util.Log
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import kotlin.system.measureTimeMillis

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
    fun getReservationsWithTime(
        onSuccess: (List<Reservation>, Long) -> Unit,
        onError: (String) -> Unit
    ) {
        val startTime = System.currentTimeMillis()

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            baseUrl,
            null,
            { response ->
                val elapsedTime = System.currentTimeMillis() - startTime
                try {
                    val reservations = (0 until response.length())
                        .map { response.getJSONObject(it) }
                        .map { gson.fromJson(it.toString(), Reservation::class.java) }
                    onSuccess(reservations, elapsedTime)
                } catch (e: Exception) {
                    onError("Erreur de parsing: ${e.message}")
                }
            },
            { error -> onError("Erreur de récupération: ${error.message}") }
        )
        requestQueue.add(jsonArrayRequest)
    }

    fun addReservationWithTime(
        reservationRequest: ReservationRequest,
        onSuccess: (Reservation, Long) -> Unit,
        onError: (String) -> Unit
    ) {
        val startTime = System.currentTimeMillis()

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
                    put(chambre.id)
                }
            })
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            baseUrl,
            jsonBody,
            { response ->
                val elapsedTime = System.currentTimeMillis() - startTime
                try {
                    val reservation = gson.fromJson(response.toString(), Reservation::class.java)
                    Log.d("Performance", "POST Reservation: ${elapsedTime}ms")
                    onSuccess(reservation, elapsedTime)
                } catch (e: Exception) {
                    onError("Erreur de parsing: ${e.message}")
                }
            },
            { error ->
                val elapsedTime = System.currentTimeMillis() - startTime
                Log.e("Performance", "POST Reservation failed: ${elapsedTime}ms")
                onError("Erreur d'ajout de réservation: ${error.message}")
            }
        )
        requestQueue.add(jsonObjectRequest)
    }
    fun updateReservationWithTime(
        id: Long,
        reservationRequest: ReservationRequest,
        onSuccess: (Reservation, Long) -> Unit,
        onError: (String) -> Unit
    ) {
        val startTime = System.currentTimeMillis() // Démarrer le chronomètre

        val jsonBody = JSONObject().apply {
            put("reservation", JSONObject().apply {
                put("id", id)
                put("checkInDate", reservationRequest.checkInDate)
                put("checkOutDate", reservationRequest.checkOutDate)
                put("client", JSONObject().apply {
                    put("id", reservationRequest.client.id)
                })
            })
            put("chambreIds", JSONArray().apply {
                reservationRequest.chambres.forEach { chambre ->
                    put(chambre.id) // Ajouter les IDs des chambres
                }
            })
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT,
            "$baseUrl/$id",
            jsonBody,
            { response ->
                val elapsedTime = System.currentTimeMillis() - startTime // Temps écoulé
                try {
                    val updatedReservation = gson.fromJson(response.toString(), Reservation::class.java)
                    Log.d("Performance", "PUT Reservation: $elapsedTime ms")
                    onSuccess(updatedReservation, elapsedTime)
                } catch (e: Exception) {
                    onError("Erreur de parsing: ${e.message}")
                }
            },
            { error ->
                val elapsedTime = System.currentTimeMillis() - startTime // Temps écoulé
                Log.e("Performance", "PUT Reservation failed: $elapsedTime ms")
                onError("Erreur de mise à jour: ${error.message}")
            }
        )
        requestQueue.add(jsonObjectRequest)
    }
    fun deleteReservationWithTime(
        id: Long,
        onSuccess: (Long) -> Unit,
        onError: (String) -> Unit
    ) {
        val startTime = System.currentTimeMillis()

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.DELETE,
            "$baseUrl/$id",
            null,
            { response ->
                val elapsedTime = System.currentTimeMillis() - startTime
                Log.d("Performance", "DELETE Reservation: $elapsedTime ms")
                onSuccess(elapsedTime)
            },
            { error ->
                val elapsedTime = System.currentTimeMillis() - startTime
                // Vérifier spécifiquement le code de statut
                val statusCode = error.networkResponse?.statusCode
                Log.e("Performance", "DELETE Reservation failed: $elapsedTime ms, Status: $statusCode")

                // Si le code de statut est 204 (No Content), considérez comme un succès
                if (statusCode == 204) {
                    onSuccess(elapsedTime)
                } else {
                    onError("Erreur de suppression: ${error.message}")
                }
            }
        ) {
            // Override pour accepter une réponse vide
            override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                return if (response?.data?.isNotEmpty() == true) {
                    super.parseNetworkResponse(response)
                } else {
                    Response.success(null, HttpHeaderParser.parseCacheHeaders(response))
                }
            }
        }

        requestQueue.add(jsonObjectRequest)
    }

}
