package com.example.bus.handler;

import com.example.bus.service.BusService;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityHandlerTest {

    @Mock
    private HttpExchange exchange;

    @Mock
    private BusService busService;

    private AvailabilityHandler handler;

    @BeforeEach
    void setUp() {
        handler = new AvailabilityHandler();
    }

    @Test
    void shouldHandleValidPostRequest() throws IOException {
        String requestBody = "{\"origin\":\"A\",\"destination\":\"B\",\"passengers\":2}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(requestBody.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Headers headers = new Headers();

        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestBody()).thenReturn(inputStream);
        when(exchange.getResponseBody()).thenReturn(outputStream);
        when(exchange.getResponseHeaders()).thenReturn(headers);

        handler.handle(exchange);

        verify(exchange).sendResponseHeaders(eq(200), anyLong());
        verify(exchange, times(1)).getRequestMethod();
    }

    @Test
    void shouldRejectNonPostRequests() throws IOException {
        when(exchange.getRequestMethod()).thenReturn("GET");

        handler.handle(exchange);

        verify(exchange).sendResponseHeaders(405, -1);
        verify(busService, never()).checkAvailability(any(), any(), anyInt());
    }

    @Test
    void shouldHandleInvalidJsonRequest() throws IOException {
        String invalidJson = "{invalid json}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidJson.getBytes());

        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestBody()).thenReturn(inputStream);

        handler.handle(exchange);

        verify(exchange).sendResponseHeaders(eq(400), anyLong());
    }

    @Test
    void shouldHandleMissingFields() throws IOException {
        String incompleteRequest = "{\"origin\":\"A\"}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(incompleteRequest.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Headers headers = new Headers();

        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestBody()).thenReturn(inputStream);
        when(exchange.getResponseBody()).thenReturn(outputStream);
        when(exchange.getResponseHeaders()).thenReturn(headers);

        handler.handle(exchange);

        verify(exchange).sendResponseHeaders(eq(400), anyLong());
        verify(busService, never()).checkAvailability(any(), any(), anyInt());
    }
}
