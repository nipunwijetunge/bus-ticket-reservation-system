package com.example.bus.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BaseResponseTest {

    @Test
    void shouldCreateBaseResponseUsingBuilder() {
        Object testData = "test data";
        BaseResponse response = BaseResponse.builder()
                .status(200)
                .message("Success")
                .data(testData)
                .build();

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
        assertEquals(testData, response.getData());
    }

    @Test
    void shouldCreateEmptyBaseResponse() {
        BaseResponse response = new BaseResponse();

        assertEquals(0, response.getStatus());
        assertNull(response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void shouldCreateBaseResponseWithAllArgsConstructor() {
        Object testData = "test data";
        BaseResponse response = new BaseResponse(404, "Not Found", testData);

        assertEquals(404, response.getStatus());
        assertEquals("Not Found", response.getMessage());
        assertEquals(testData, response.getData());
    }
}
