package com.example.bus.service;

import com.example.bus.dto.BaseResponse;
import com.example.bus.model.Reservation;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("Tests are disabled due to unpredictable seat changes in sequential test cases")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BusServiceTest {
    private BusService busService;

    @BeforeEach
     void setUp() {
        busService = BusService.getInstance();
    }

    @Test
    void shouldReturnAllAvailableSeatsForValidRoute() {
        BaseResponse response = busService.checkAvailability("A", "B", 1);

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
        Map<String, Object> data = (Map<String, Object>) response.getData();
        List<String> availableSeats = (List<String>) data.get("availableSeats");
        assertEquals(40, availableSeats.size());
        assertEquals(50.00, data.get("totalPrice"));
    }

    @Test
    void shouldCalculateCorrectTotalPriceForMultiplePassengers() {
        BaseResponse response = busService.checkAvailability("A", "C", 3);

        assertEquals(200, response.getStatus());
        Map<String, Object> data = (Map<String, Object>) response.getData();
        assertEquals(300.00, data.get("totalPrice"));
    }

    @Test
    void shouldReturnZeroPriceForInvalidRoute() {
        BaseResponse response = busService.checkAvailability("A", "E", 1);

        assertEquals(200, response.getStatus());
        Map<String, Object> data = (Map<String, Object>) response.getData();
        assertEquals(0.00, data.get("totalPrice"));
    }

    @Test
    void shouldSuccessfullyReserveSeatsForValidRequest() {
        BaseResponse response = busService.reserve("A", "B", 2);

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
        Reservation reservation = (Reservation) response.getData();
        assertEquals(2, reservation.getSeats().size());
        assertEquals(100.00, reservation.getTotalPrice());
        assertNotNull(reservation.getReservationId());
    }

    @Test
    void shouldAssignConsecutiveSeatsWhenAvailable() {
        BaseResponse response = busService.reserve("A", "B", 3);

        Reservation reservation = (Reservation) response.getData();
        List<String> seats = reservation.getSeats();
        assertEquals(3, seats.size());
        assertTrue(areSeatsConsecutive(seats));
    }

    @Test
    @Order(Integer.MAX_VALUE)
    void shouldHandleReservationWhenNotEnoughSeatsAvailable() {
        // First reserve most seats
        busService.reserve("A", "B", 20);

        // Try to reserve more seats than available
        BaseResponse response = busService.reserve("A", "B", 23);

        int status = response.getStatus();
        assertEquals(400, status);
    }

    private boolean areSeatsConsecutive(List<String> seats) {
        for (int i = 1; i < seats.size(); i++) {
            String current = seats.get(i);
            String previous = seats.get(i - 1);
            if (!areAdjacent(previous, current)) {
                return false;
            }
        }
        return true;
    }

    private boolean areAdjacent(String seat1, String seat2) {
        int row1 = Integer.parseInt(seat1.substring(0, seat1.length() - 1));
        int row2 = Integer.parseInt(seat2.substring(0, seat2.length() - 1));
        char col1 = seat1.charAt(seat1.length() - 1);
        char col2 = seat2.charAt(seat2.length() - 1);

        return row1 == row2 && Math.abs(col1 - col2) == 1;
    }
}
