package com.example.bus.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeatTest {

    @Test
    void seatShouldBeUnreservedWhenCreated() {
        Seat seat = new Seat("1A");
        assertFalse(seat.isReserved());
        assertEquals("1A", seat.getSeatNumber());
    }

    @Test
    void seatShouldBeReservedAfterReservation() {
        Seat seat = new Seat("1B");
        seat.reserve();
        assertTrue(seat.isReserved());
    }

    @Test
    void seatShouldRemainReservedAfterMultipleReservations() {
        Seat seat = new Seat("1C");
        seat.reserve();
        seat.reserve();
        assertTrue(seat.isReserved());
    }
}
