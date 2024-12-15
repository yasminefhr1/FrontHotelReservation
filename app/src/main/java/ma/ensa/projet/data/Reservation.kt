package ma.ensa.projet.data

// Updated data classes
data class Client(val id: Long)
data class Chambre(val id: Long)

data class Reservation(
    val id: Long? = null,
    val checkInDate: String,
    val checkOutDate: String,
    val client: Client,
    val chambres: List<Chambre>? = null
)

data class ReservationRequest(
    val checkInDate: String,
    val checkOutDate: String,
    val client: Client,
    val chambres: List<Chambre>
)