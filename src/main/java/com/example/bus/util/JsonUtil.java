package com.example.bus.util;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Log4j2
public class JsonUtil {
    private static final Gson gson = new Gson();

    public static <T> T readJson(HttpExchange exchange, Class<T> clazz) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);

            return gson.fromJson(reader, clazz);
        } catch (Exception e) {
            log.error("Invalid JSON input: {}", e.getMessage());
        }

        return null;
    }

    public static void sendJson(HttpExchange exchange, Object data) throws IOException {
        String json = "";
        try {
            json = gson.toJson(data);
        } catch (Exception e) {
            log.error("Invalid JSON output: {}", e.getMessage());
        }
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}