package com.example.bus;

import com.example.bus.handler.AvailabilityHandler;
import com.example.bus.handler.ReservationHandler;
import com.example.bus.handler.RootHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.log4j.Log4j2;

import java.net.InetSocketAddress;

@Log4j2
public class MainServer {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
            server.createContext("/", new RootHandler());
            server.createContext("/api/availability", new AvailabilityHandler());
            server.createContext("/api/reserve", new ReservationHandler());

            server.setExecutor(null);
            log.info("Bus Reservation API running at http://localhost:8080");
            server.start();
        } catch (Exception e) {
            log.error("Failed to start server: {}", e.getMessage());
        }
    }
}
