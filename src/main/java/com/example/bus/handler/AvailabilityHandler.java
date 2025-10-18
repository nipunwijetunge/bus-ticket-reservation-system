package com.example.bus.handler;

import com.example.bus.dto.BaseResponse;
import com.example.bus.service.BusService;
import com.example.bus.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Map;

@Log4j2
public class AvailabilityHandler implements HttpHandler {
    private final BusService service;

    public AvailabilityHandler() {
        this.service = BusService.getInstance();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("POST")) {
            Map<String, ?> body = JsonUtil.readJson(exchange, Map.class);

            if (body == null) {
                exchange.sendResponseHeaders(400, 0);
                log.info("Bad Request: Body is null");
                return;
            }

            String origin = body.get("origin") != null ? body.get("origin").toString() : "";
            String destination = body.get("destination") != null ? body.get("destination").toString() : "";
            int passengers = body.get("passengers") != null ? ((Double) body.get("passengers")).intValue() : 0;

            if (origin == null || destination == null) {
                exchange.sendResponseHeaders(400, -1);
                log.info("Bad Request: Required fields are missing or invalid");
                return;
            }

            log.debug("Origin: {}", origin);
            log.debug("Destination: {}", destination);
            log.debug("Passengers: {}", passengers);

            BaseResponse response = service.checkAvailability(origin, destination, passengers);
            JsonUtil.sendJson(exchange, response);
        } else {
            exchange.sendResponseHeaders(405, -1);
            log.info("Method Not Allowed: {}", exchange.getRequestMethod());
        }
    }
}
