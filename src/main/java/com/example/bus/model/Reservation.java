package com.example.bus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class Reservation {
    private final String reservationId;
    private final String origin;
    private final String destination;
    private final double totalPrice;
    private final List<String> seats;
}
