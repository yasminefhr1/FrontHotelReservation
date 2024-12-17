package ma.ensa.full_backend.test;

import ma.ensa.full_backend.model.Chambre;
import ma.ensa.full_backend.model.Client;
import ma.ensa.full_backend.model.Reservation;
import ma.ensa.full_backend.model.TypeChambre;
import ma.ensa.full_backend.repository.ChambreRepository;
import ma.ensa.full_backend.repository.ClientRepository;
import ma.ensa.full_backend.repository.ReservationRepository;
import ma.ensa.full_backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private ReservationService reservationService;

    @Transactional
    public void createTestData() {
        // Create client with the exact name and details from the JSON
        Client client = createClient(
                "John ExtraNamePart ExtraNamePart ExtraNamePart ExtraNamePart ExtraNamePart ExtraNamePart ExtraNamePart ExtraNamePart ExtraNamePa",
                "Doe ExtraLastNamePart ExtraLastNamePart ExtraLastNamePart ExtraLastNamePart ExtraLastNamePart ExtraLastNamePart ExtraLastNamePart ExtraLastNamePart ExtraLastNamePart ExtraLastNamePart",
                "john.doe@example.com"
        );

        // Create chambers with specific types and prices
        List<Chambre> chambers = createSpecificChambers();

        // Create reservation with specific dates
        createReservationWithChambers(client, chambers,
                LocalDateTime.parse("2024-12-16T21:26:54.216809"),
                LocalDateTime.parse("2024-12-20T21:26:54.216809")
        );

        System.out.println("Test data creation completed successfully.");
    }

    private Client createClient(String firstName, String lastName, String email) {
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setPhoneNumber("123-456-7890");
        return clientRepository.save(client);
    }

    private List<Chambre> createSpecificChambers() {
        List<Chambre> chambers = new ArrayList<>();
        float[] prices = {100.0f, 120.0f, 130.0f, 140.0f, 150.0f, 160.0f, 170.0f, 180.0f, 190.0f, 200.0f, 210.0f};
        TypeChambre[] types = {
                TypeChambre.SINGLE, TypeChambre.DOUBLE, TypeChambre.SINGLE,
                TypeChambre.DOUBLE, TypeChambre.SINGLE, TypeChambre.DOUBLE,
                TypeChambre.SINGLE, TypeChambre.DOUBLE, TypeChambre.SINGLE,
                TypeChambre.DOUBLE, TypeChambre.SINGLE
        };

        for (int i = 0; i < prices.length; i++) {
            Chambre chambre = new Chambre();
            chambre.setTypeChambre(types[i]);
            chambre.setPrix(prices[i]);
            chambre.setDisponible(true);
            chambreRepository.save(chambre);
            chambers.add(chambre);
        }

        return chambers;
    }

    private Reservation createReservationWithChambers(Client client, List<Chambre> chambres,
                                                      LocalDateTime checkInDate,
                                                      LocalDateTime checkOutDate) {
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setCheckInDate(java.sql.Timestamp.valueOf(checkInDate));
        reservation.setCheckOutDate(java.sql.Timestamp.valueOf(checkOutDate));

        // Get chamber IDs
        List<Long> chambreIds = chambres.stream()
                .map(Chambre::getId)
                .collect(Collectors.toList());

        // Use reservation service to create reservation with chambers
        return reservationService.createReservation(reservation, chambreIds);
    }

    @Transactional
    public void printTestData() {
        // Print clients
        System.out.println("\n--- Clients ---");
        clientRepository.findAll().forEach(client ->
                System.out.println(client.getFirstName() + " " + client.getLastName())
        );

        // Print chambers
        System.out.println("\n--- Chambers ---");
        chambreRepository.findAll().forEach(chambre ->
                System.out.println("Type: " + chambre.getTypeChambre() +
                        ", Price: " + chambre.getPrix() +
                        ", Available: " + chambre.isDisponible())
        );

        // Print reservations
        System.out.println("\n--- Reservations ---");
        reservationRepository.findAll().forEach(reservation -> {
            System.out.println("Client: " + reservation.getClient().getFirstName() +
                    ", Check-in: " + reservation.getCheckInDate() +
                    ", Check-out: " + reservation.getCheckOutDate());
            System.out.println("Chambers:");
            reservation.getChambres().forEach(chambre ->
                    System.out.println("  - Type: " + chambre.getTypeChambre() +
                            ", Price: " + chambre.getPrix())
            );
        });
    }
}