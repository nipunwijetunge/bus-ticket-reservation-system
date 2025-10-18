package com.example.bus.util;

import com.example.bus.dto.BaseResponse;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JsonUtilTest {

    @Mock
    private HttpExchange exchange;

    private Headers headers;

    @BeforeEach
    void setUp() {
        headers = new Headers();
    }

    @Test
    void shouldReadJsonFromHttpExchange() throws IOException {
        String jsonInput = "{\"key\":\"value\",\"number\":42}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes());
        when(exchange.getRequestBody()).thenReturn(inputStream);

        Map<String, Object> result = JsonUtil.readJson(exchange, Map.class);

        assertEquals("value", result.get("key"));
        assertEquals(42.0, ((Number) result.get("number")).doubleValue());
    }

    @Test
    void shouldHandleComplexObjectDeserialization() throws IOException {
        String jsonInput = "{\"status\":200,\"message\":\"Success\",\"data\":{\"count\":1}}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes());
        when(exchange.getRequestBody()).thenReturn(inputStream);

        BaseResponse response = JsonUtil.readJson(exchange, BaseResponse.class);

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
    }

    @Test
    void shouldWriteJsonToHttpExchange() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(exchange.getResponseBody()).thenReturn(outputStream);
        when(exchange.getResponseHeaders()).thenReturn(headers);

        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");
        data.put("number", 42);

        JsonUtil.sendJson(exchange, data);

        String result = outputStream.toString();
        assertTrue(result.contains("\"key\":\"value\""));
        assertTrue(result.contains("\"number\":42"));
        assertEquals("application/json", headers.getFirst("Content-Type"));
        verify(exchange).sendResponseHeaders(eq(200), anyLong());
    }

    @Test
    void shouldHandleNullValues() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(exchange.getResponseBody()).thenReturn(outputStream);
        when(exchange.getResponseHeaders()).thenReturn(headers);

        Map<String, Object> data = new HashMap<>();
        data.put("nullValue", null);

        JsonUtil.sendJson(exchange, data);

        String result = outputStream.toString();
        assertTrue(result.contains("{}"));
    }

    @Test
    void shouldCloseStreamsAfterOperation() throws IOException {
        ByteArrayOutputStream outputStream = spy(new ByteArrayOutputStream());
        when(exchange.getResponseBody()).thenReturn(outputStream);
        when(exchange.getResponseHeaders()).thenReturn(headers);

        JsonUtil.sendJson(exchange, new HashMap<>());

        verify(outputStream, times(1)).close();
    }

    @Test
    void shouldHandleEmptyInput() throws IOException {
        String jsonInput = "{}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes());
        when(exchange.getRequestBody()).thenReturn(inputStream);

        Map<String, Object> result = JsonUtil.readJson(exchange, Map.class);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
