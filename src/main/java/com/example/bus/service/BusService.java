package com.example.bus.service;

import com.example.bus.dto.BaseResponse;
import com.example.bus.model.Reservation;
import com.example.bus.model.Seat;

import java.util.*;

public class BusService {
    private final List<Seat> seats = new ArrayList<>();
    private final List<Seat> reservedSeats = new ArrayList<>();

    // Adds the seats to the bus on initialization
    private BusService() {
        for (int i = 1; i <= 10; i++) {
            for (char c = 'A'; c <= 'D'; c++) {
                seats.add(new Seat(i + "" + c));
            }
        }
    }

    private static BusService busService;

    public static BusService getInstance() {
        if (busService == null) {
            busService = new BusService();
        }

        return busService;
    }

    // A map of prices between different locations kept as pairs of destinations for easy access
    private static final Map<String, Double> PRICE_MAP = Map.ofEntries(
            Map.entry("A-B", 50.00), Map.entry("A-C", 100.00), Map.entry("A-D", 150.00),
            Map.entry("B-C", 50.00), Map.entry("B-D", 100.00), Map.entry("C-D", 50.00),
            Map.entry("B-A", 50.00), Map.entry("C-A", 100.00), Map.entry("D-A", 150.00),
            Map.entry("C-B", 50.00), Map.entry("D-B", 100.00), Map.entry("D-C", 50.00)
    );

    private static final List<String> LOCATIONS = List.of("A", "B", "C", "D");

    public synchronized BaseResponse checkAvailability(String origin, String destination, int passengers) {
        if (validatePayload(origin, destination, passengers) != null) {
            return validatePayload(origin, destination, passengers);
        }

        List<String> available = seats.stream()
                .filter(s -> !s.isReserved())
                .map(Seat::getSeatNumber)
                .toList();

        double price = PRICE_MAP.getOrDefault(origin + "-" + destination, 0.00);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("availableSeats", available);
        data.put("totalPrice", price * passengers);

        return BaseResponse.builder()
                .status(200)
                .message("Success")
                .data(data)
                .build();
    }

    public synchronized BaseResponse reserve(String origin, String destination, int passengers) {
        double price = PRICE_MAP.getOrDefault(origin + "-" + destination, 0.00);
        List<String> assigned = new ArrayList<>();

        if (validatePayload(origin, destination, passengers) != null) {
            return validatePayload(origin, destination, passengers);
        }

        seats.stream()
            .filter(s -> !s.isReserved())
            .limit(passengers)
            .forEach(s -> {
                s.reserve();
                assigned.add(s.getSeatNumber());
                reservedSeats.add(new Seat(s.getSeatNumber()));
            });

        Reservation reservation = Reservation.builder()
                .reservationId(UUID.randomUUID().toString())
                .origin(origin)
                .destination(destination)
                .totalPrice(price * passengers)
                .seats(assigned)
                .build();

        return BaseResponse.builder()
                .status(200)
                .message("Success")
                .data(reservation)
                .build();
    }

    private BaseResponse validatePayload(String origin, String destination, int passengers) {
        if (passengers > 40) {
            return BaseResponse.builder()
                    .status(400)
                    .message("Number of passengers exceeds available seats")
                    .data(null)
                    .build();
        }

        if (passengers <= 0) {
            return BaseResponse.builder()
                    .status(400)
                    .message("Number of passengers must be greater than zero")
                    .data(null)
                    .build();
        }

        if (passengers > (40 - reservedSeats.size())) {
            return BaseResponse.builder()
                    .status(400)
                    .message("Not enough available seats for the requested number of passengers")
                    .data(null)
                    .build();
        }

        if (origin.length() > 1 || destination.length() > 1 || !LOCATIONS.contains(origin) || !LOCATIONS.contains(destination)) {
            return BaseResponse.builder()
                    .status(400)
                    .message("Invalid origin or destination")
                    .data(null)
                    .build();
        }

        return null;
    }
}
