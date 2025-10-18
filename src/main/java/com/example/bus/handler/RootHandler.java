package com.example.bus.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RootHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        byte[] response;
        try {
            InputStream resourceStream = getClass().getClassLoader()
                    .getResourceAsStream("static/index.html");
            response = resourceStream.readAllBytes();

            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.length);
        } catch (Exception e) {
            String notFoundResponse = "404 Not Found";
            exchange.sendResponseHeaders(404, notFoundResponse.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(notFoundResponse.getBytes());
            }
            return;
        }
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }
}
