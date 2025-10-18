package com.example.bus.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void shouldCreateReservationUsingBuilder() {
        String reservationId = "123e4567-e89b-12d3-a456-426614174000";
        List<String> seats = Arrays.asList("1A", "1B");

        Reservation reservation = Reservation.builder()
                .reservationId(reservationId)
                .origin("A")
                .destination("B")
                .totalPrice(100.00)
                .seats(seats)
                .build();

        assertEquals(reservationId, reservation.getReservationId());
        assertEquals("A", reservation.getOrigin());
        assertEquals("B", reservation.getDestination());
        assertEquals(100.00, reservation.getTotalPrice());
        assertEquals(seats, reservation.getSeats());
    }

    @Test
    void shouldCreateReservationWithAllArgsConstructor() {
        String reservationId = "123e4567-e89b-12d3-a456-426614174000";
        List<String> seats = Arrays.asList("1C", "1D");

        Reservation reservation = new Reservation(reservationId, "B", "C", 50.00, seats);

        assertEquals(reservationId, reservation.getReservationId());
        assertEquals("B", reservation.getOrigin());
        assertEquals("C", reservation.getDestination());
        assertEquals(50.00, reservation.getTotalPrice());
        assertEquals(seats, reservation.getSeats());
    }

    @Test
    void shouldNotModifySeatsListAfterCreation() {
        List<String> seats = Arrays.asList("2A", "2B");
        Reservation reservation = Reservation.builder()
                .reservationId("test-id")
                .origin("A")
                .destination("D")
                .totalPrice(150.00)
                .seats(seats)
                .build();

        List<String> retrievedSeats = reservation.getSeats();
        assertThrows(UnsupportedOperationException.class, () ->
            retrievedSeats.add("2C")
        );
    }
}
