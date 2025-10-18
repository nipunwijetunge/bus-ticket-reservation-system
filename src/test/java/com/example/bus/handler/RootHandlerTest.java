package com.example.bus.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RootHandlerTest {

    @Mock
    private HttpExchange exchange;

    private RootHandler handler;
    private Headers headers;

    @BeforeEach
    void setUp() {
        handler = new RootHandler();
        headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);
    }

    @Test
    void shouldServeIndexHtmlFile() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(exchange.getResponseBody()).thenReturn(outputStream);

        handler.handle(exchange);

        verify(exchange).sendResponseHeaders(200, outputStream.size());
        assertEquals("text/html; charset=UTF-8",
            exchange.getResponseHeaders().getFirst("Content-Type"));

        byte[] expectedContent = Files.readAllBytes(
            Paths.get("src/main/resources/static/index.html"));
        assertArrayEquals(expectedContent, outputStream.toByteArray());
    }

    @Test
    void shouldSetCorrectContentTypeHeader() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(exchange.getResponseBody()).thenReturn(outputStream);

        handler.handle(exchange);

        Headers responseHeaders = exchange.getResponseHeaders();
        assertTrue(responseHeaders.containsKey("Content-Type"));
        assertEquals("text/html; charset=UTF-8",
            responseHeaders.getFirst("Content-Type"));
    }

    @Test
    void shouldCloseOutputStreamAfterWriting() throws IOException {
        ByteArrayOutputStream outputStream = spy(new ByteArrayOutputStream());
        when(exchange.getResponseBody()).thenReturn(outputStream);

        handler.handle(exchange);

        verify(outputStream, times(1)).close();
    }
}
